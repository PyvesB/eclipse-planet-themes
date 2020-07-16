/**
 * Copyright (c) 2014 - 2020 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 *   Pierre-Yves B. - switch to Fira Code and use ILog instead of LogService
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Collections.emptyMap;
import static java.util.Collections.list;
import static org.eclipse.core.runtime.FileLocator.find;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import static java.nio.file.Files.copy;

class FontLoader {

  static final String FONT_FACE = "Fira Code";
  static final String FONTS_DIRECTORY = "/fonts";

  private final String fontDirectory;

  FontLoader( String fontDirectory ) {
    this.fontDirectory = fontDirectory;
  }

  void load( Bundle bundle, Display display ) {
    try {
      doLoad( bundle, display );
    } catch( RuntimeException rte ) {
      getLogger( bundle ).error( "Unable to load fonts.", rte );
    }
  }

  private void doLoad( Bundle bundle, Display display ) {
    list( getFontPaths( bundle, fontDirectory ) )
      .forEach( fontPath -> loadFont( bundle, fontPath, display ) );
  }

  private static ILog getLogger( Bundle bundle ) {
     return Platform.getLog( bundle );
  }

  private static Enumeration<String> getFontPaths( Bundle bundle, String fontDirectory ) {
    return bundle.getEntryPaths( fontDirectory );
  }

  private static void loadFont( Bundle bundle, String fontPath, Display display ) {
    if( fontPath.endsWith( ".ttf" ) ) {
      URL url = computeFontUrl( find( bundle, new Path( fontPath ), emptyMap() ) );
      File diskLocation = getDiskLocation( fontPath );
      copyToDisk( url, diskLocation );
      display.asyncExec( () -> display.loadFont( diskLocation.toString() ) );
    }
  }

  private static File getDiskLocation( String fontPath ) {
    try {
      return Platform.getStateLocation(FrameworkUtil.getBundle(FontLoader.class)).append( fontPath ).toFile().getCanonicalFile();
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static URL computeFontUrl( URL url ) {
    try {
      return FileLocator.toFileURL( url );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static void copyToDisk( URL url, File diskLocation ) {
    try( InputStream input = url.openStream() ) {
      ensureDirectoryExists( diskLocation.toPath() );
      copy( input, diskLocation.toPath(), REPLACE_EXISTING );
    } catch (IOException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }

  private static void ensureDirectoryExists( java.nio.file.Path path ) throws IOException {
    if( !Files.exists( path.getParent() ) ) {
      Files.createDirectories( path.getParent() );
    }
  }
}