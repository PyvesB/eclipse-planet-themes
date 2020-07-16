/**
 * Copyright (c) 2014 - 2020 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 *   Pierre-Yves B. - remove isPlatformSupported check
 */
package com.codeaffine.eclipse.ui.swt.theme;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class FontOnStartupLoader implements IStartup {

  private final FontRegistryUpdater fontRegistryUpdater;
  private final FontLoader fontLoader;

  public FontOnStartupLoader() {
    fontLoader = new FontLoader( FontLoader.FONTS_DIRECTORY );
    fontRegistryUpdater = new FontRegistryUpdater();
  }

  @Override
  public void earlyStartup() {
    waitTillWorkbenchWindowExists();
    loadFont( getShell() );
  }

  private static void waitTillWorkbenchWindowExists() {
    long timeout = System.currentTimeMillis() + 1000;
    while( PlatformUI.getWorkbench().getWorkbenchWindows().length == 0 && timeout > System.currentTimeMillis() ) {
      try {
        Thread.sleep( 50 );
      } catch( InterruptedException shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    }
  }

  private static Shell getShell() {
    return PlatformUI.getWorkbench().getWorkbenchWindows()[ 0 ].getShell();
  }

  private void loadFont( Shell shell ) {
    Bundle bundle = FrameworkUtil.getBundle( FontOnStartupLoader.class );
    fontLoader.load( bundle, shell.getDisplay() );
    fontRegistryUpdater.update( shell );
  }
}
