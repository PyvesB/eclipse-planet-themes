/*******************************************************************************
 * Copyright (c) 2010, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fabio Zadrozny - Bug 465711
 *     Simon Scholz <simon.scholz@vogella.com> - Bug 497586
 *     Simon Scholz <simon.scholz@vogella.com> - Bug 506540
 *     Mike Marchand <mmarchand@cranksoftware.com> - Bug 538740
 *     Pierre-Yves B. <pyvesdev@gmail.com> - Revert breaking changes to tab outline behaviour.
 *     Pierre-Yves B. <pyvesdev@gmail.com> - Fix active tab logic.
 *     Pierre-Yves B. <pyvesdev@gmail.com> - Simplify implementation, remove dead code and drop support for unused CSS properties.
 *******************************************************************************/
package io.github.pyvesb.eclipse_planet_themes;

import org.eclipse.e4.ui.internal.css.swt.ICTabRendering;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

@SuppressWarnings("restriction")
public class CTabFolderPlanetsRenderer extends CTabFolderRenderer implements ICTabRendering {

	// keylines
	static final int OUTER_KEYLINE = 1;
	static final int INNER_KEYLINE = 0;
	static final int TOP_KEYLINE = 0;

	// The tab has an outline, it contributes to the trim. See Bug 562183.
	static final int TAB_OUTLINE = 1;

	// Item Constants
	static final int ITEM_TOP_MARGIN = 2;
	static final int ITEM_BOTTOM_MARGIN = 6;
	static final int ITEM_LEFT_MARGIN = 4;
	static final int ITEM_RIGHT_MARGIN = 4;

	Rectangle rectShape;

	Color outerKeyline, selectedTabFillColor, tabOutlineColor, selectedTabHighlightColor;

	int paddingLeft = 0, paddingRight = 0, paddingTop = 0, paddingBottom = 0;

	private boolean drawTabHighlightOnTop = true;

	public CTabFolderPlanetsRenderer(CTabFolder parent) {
		super(parent);
	}

	@Override
	protected Rectangle computeTrim(int part, int state, int x, int y, int width, int height) {
		boolean onBottom = parent.getTabPosition() == SWT.BOTTOM;
		int borderTop = onBottom ? INNER_KEYLINE + OUTER_KEYLINE : TOP_KEYLINE + OUTER_KEYLINE;
		int borderBottom = onBottom ? TOP_KEYLINE + OUTER_KEYLINE : INNER_KEYLINE + OUTER_KEYLINE;
		int marginWidth = parent.marginWidth;
		int marginHeight = parent.marginHeight;

		// Trim is not affected by the corner size.
		switch (part) {
		case PART_BODY:
			if (state == SWT.FILL) {
				x = -1 - paddingLeft;
				int tabHeight = parent.getTabHeight() + 1;
				y = onBottom ? y - paddingTop - marginHeight - borderTop - TAB_OUTLINE
						: y - paddingTop - marginHeight - tabHeight - borderTop - TAB_OUTLINE;
				width = 2 + paddingLeft + paddingRight;
				height += paddingTop + paddingBottom + TAB_OUTLINE;
				height += tabHeight + borderBottom + borderTop;
			} else {
				x = x - marginWidth - OUTER_KEYLINE - INNER_KEYLINE;
				width = width + 2 * OUTER_KEYLINE + 2 * INNER_KEYLINE + 2 * marginWidth;
				int tabHeight = parent.getTabHeight() + 1;
				if (parent.getMinimized()) {
					y = onBottom ? y - borderTop - 5 : y - tabHeight - borderTop - 5;
					height = borderTop + borderBottom + tabHeight;
				} else {
					y = onBottom ? y - marginHeight - borderTop
							: y - marginHeight - tabHeight - borderTop - TAB_OUTLINE;
					height = height + borderBottom + borderTop + 2 * marginHeight + tabHeight + TAB_OUTLINE;
				}
			}
			break;
		case PART_HEADER:
			x = x - (INNER_KEYLINE + OUTER_KEYLINE);
			width = width + 2 * (INNER_KEYLINE + OUTER_KEYLINE);
			break;
		case PART_BORDER:
			x = x - INNER_KEYLINE - OUTER_KEYLINE - ITEM_LEFT_MARGIN;
			width = width + 2 * (INNER_KEYLINE + OUTER_KEYLINE) + ITEM_RIGHT_MARGIN;
			height += borderTop + borderBottom;
			y -= borderTop;

			break;
		default:
			if (0 <= part && part < parent.getItemCount()) {
				x -= ITEM_LEFT_MARGIN;
				width += ITEM_LEFT_MARGIN + ITEM_RIGHT_MARGIN + 1;
				y -= ITEM_TOP_MARGIN;
				height += ITEM_TOP_MARGIN + ITEM_BOTTOM_MARGIN;
			}
			break;
		}
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected Point computeSize(int part, int state, GC gc, int wHint, int hHint) {
		wHint += paddingLeft + paddingRight;
		hHint += paddingTop + paddingBottom;
		if (0 <= part && part < parent.getItemCount()) {
			gc.setAdvanced(true);
		}
		return super.computeSize(part, state, gc, wHint, hHint);
	}

	@Override
	protected void draw(int part, int state, Rectangle bounds, GC gc) {
		switch (part) {
		case PART_BACKGROUND:
			this.drawCustomBackground(gc, bounds, state);
			return;
		case PART_BODY:
			this.drawTabBody(gc, bounds);
			return;
		case PART_HEADER:
			this.drawTabHeader(gc, bounds, state);
			return;
		default:
			if (0 <= part && part < parent.getItemCount()) {
				// Sometimes the clipping is incorrect, see Bug 428697 and Bug 563345
				// Resetting it before draw the tabs prevents draw issues.
				gc.setClipping((Rectangle) null);
				gc.setAdvanced(true);
				if (bounds.width == 0 || bounds.height == 0)
					return;
				if ((state & SWT.SELECTED) != 0) {
					drawSelectedTab(part, gc, bounds);
					state &= ~SWT.BACKGROUND;
					super.draw(part, state, bounds, gc);
				} else if ((state & SWT.HOT) == 0 && !parent.isFocusControl()) {
					gc.setAlpha(0xcf);
					state &= ~SWT.BACKGROUND;
					super.draw(part, state, bounds, gc);
					gc.setAlpha(0xff);
				} else {
					state &= ~SWT.BACKGROUND;
					super.draw(part, state, bounds, gc);
				}
				return;
			}
		}
		super.draw(part, state, bounds, gc);
	}

	void drawTabHeader(GC gc, Rectangle bounds, int state) {
		boolean onBottom = parent.getTabPosition() == SWT.BOTTOM;
		Rectangle trim = computeTrim(PART_HEADER, state, 0, 0, 0, 0);
		trim.width = bounds.width - trim.width;

		// XXX: The magic numbers need to be cleaned up. See
		// https://bugs.eclipse.org/425777 for details.
		trim.height = (parent.getTabHeight() + (onBottom ? 7 : 4)) - trim.height;

		trim.x = -trim.x;
		trim.y = onBottom ? bounds.height - parent.getTabHeight() - 2 : -trim.y;
		draw(PART_BACKGROUND, SWT.NONE, trim, gc);

		gc.setForeground(outerKeyline);

		gc.drawRectangle(rectShape);
	}

	void drawTabBody(GC gc, Rectangle bounds) {
		int marginWidth = parent.marginWidth;
		int marginHeight = parent.marginHeight;
		int delta = INNER_KEYLINE + OUTER_KEYLINE + 2 * marginWidth;
		int width = bounds.width - delta;
		int height = Math.max(
				parent.getTabHeight() + INNER_KEYLINE + OUTER_KEYLINE,
				bounds.height - INNER_KEYLINE - OUTER_KEYLINE - 2 * marginHeight);

		// Remember for use in header drawing
		Rectangle rect = new Rectangle(bounds.x, bounds.y, width, height);
		gc.fillRectangle(rect);
		rectShape = rect;
	}

	private int[] computeSquareTabOutline(int itemIndex, boolean onBottom, int startX, int endX, int bottomY,
			Rectangle bounds, Point parentSize) {
		int index = 0;
		int outlineY = onBottom ? bottomY + bounds.height : bottomY - bounds.height - 1;
		int[] points = new int[12];

		if (itemIndex != 0 || bounds.x != -computeTrim(CTabFolderRenderer.PART_HEADER, SWT.NONE, 0, 0, 0, 0).x) {
			points[index++] = INNER_KEYLINE + OUTER_KEYLINE;
			points[index++] = bottomY;
		}
		points[index++] = startX;
		points[index++] = bottomY;

		points[index++] = startX;
		points[index++] = outlineY;

		points[index++] = endX;
		points[index++] = outlineY;

		points[index++] = endX;
		points[index++] = bottomY;

		points[index++] = parentSize.x - INNER_KEYLINE - OUTER_KEYLINE;
		points[index++] = bottomY;

		int[] tmpPoints = new int[index];
		System.arraycopy(points, 0, tmpPoints, 0, index);

		return tmpPoints;
	}

	void drawSelectedTab(int itemIndex, GC gc, Rectangle bounds) {
		if (parent.getSingle() && parent.getItem(itemIndex).isShowing())
			return;

		boolean onBottom = parent.getTabPosition() == SWT.BOTTOM;
		int bottomY = onBottom ? bounds.y : bounds.y + bounds.height;
		int selectionX1, selectionY1, selectionX2, selectionY2;
		int startX, endX;
		Point parentSize = parent.getSize();

		gc.setClipping(0, onBottom ? bounds.y : bounds.y, parentSize.x - INNER_KEYLINE - OUTER_KEYLINE,
				bounds.y + bounds.height);// bounds.height

		gc.setBackground(selectedTabFillColor);
		gc.setForeground(selectedTabFillColor);

		startX = bounds.x - 1;
		endX = bounds.x + bounds.width;
		selectionX1 = startX + 1;
		selectionY1 = bottomY;
		selectionX2 = endX - 1;
		selectionY2 = bottomY;

		int[] tmpPoints = computeSquareTabOutline(itemIndex, onBottom, startX, endX, bottomY, bounds, parentSize);
		bounds.height++; // increase area to fill by outline thickness
		gc.fillRectangle(bounds);

		gc.drawLine(selectionX1, selectionY1, selectionX2, selectionY2);

		gc.setForeground(tabOutlineColor);

		gc.drawPolyline(tmpPoints);

		gc.setClipping((Rectangle) null);

		gc.setForeground(outerKeyline);
		gc.drawRectangle(rectShape);

		if (selectedTabHighlightColor != null) {
			gc.setBackground(selectedTabHighlightColor);
			boolean highlightOnTop = drawTabHighlightOnTop;
			if (onBottom) {
				highlightOnTop = !highlightOnTop;
			}
			int verticalOffset = highlightOnTop ? 0 : bounds.height - 2;
			int horizontalOffset = itemIndex == 0 ? 0 : 1;
			int widthAdjustment = 0;
			gc.fillRectangle(bounds.x + horizontalOffset, bounds.y + verticalOffset, bounds.width - widthAdjustment, 3);
		}
	}

	private void drawCustomBackground(GC gc, Rectangle bounds, int state) {
		Rectangle partHeaderBounds = computeTrim(PART_HEADER, state, bounds.x, bounds.y, bounds.width, bounds.height);

		boolean onBottom = parent.getTabPosition() == SWT.BOTTOM;
		int borderTop = onBottom ? INNER_KEYLINE + OUTER_KEYLINE : TOP_KEYLINE + OUTER_KEYLINE;
		Rectangle parentBounds = parent.getBounds();
		int y = (onBottom) ? 0 : partHeaderBounds.y + partHeaderBounds.height - 1;
		int height = (onBottom) ? parentBounds.height - partHeaderBounds.height + 2 * paddingTop + 2 * borderTop
				: parentBounds.height - partHeaderBounds.height;

		gc.setBackground(selectedTabFillColor);
		gc.fillRectangle(partHeaderBounds.x, y, partHeaderBounds.width, height);
	}

	public Rectangle getPadding() {
		return new Rectangle(paddingTop, paddingRight, paddingBottom, paddingLeft);
	}

	public void setPadding(int paddingLeft, int paddingRight, int paddingTop, int paddingBottom) {
		this.paddingLeft = paddingLeft;
		this.paddingRight = paddingRight;
		this.paddingTop = paddingTop;
		this.paddingBottom = paddingBottom;
		parent.redraw();
	}

	@Override
	public void setOuterKeyline(Color color) {
		this.outerKeyline = color;
		parent.redraw();
	}

	@Override
	public void setSelectedTabHighlight(Color color) {
		this.selectedTabHighlightColor = color;
		parent.redraw();
	}

	@Override
	public void setSelectedTabFill(Color color) {
		selectedTabFillColor = color;
		parent.redraw();
	}

	@Override
	public void setTabOutline(Color color) {
		this.tabOutlineColor = color;
		parent.redraw();
	}

	@Override
	public void setSelectedTabHighlightTop(boolean drawTabHiglightOnTop) {
		this.drawTabHighlightOnTop = drawTabHiglightOnTop;
		parent.redraw();
	}

	@Override
	public void setSelectedTabFill(Color[] colors, int[] percents) {
		setSelectedTabFill(colors[0]);
	}

	@Override
	public void setUnselectedHotTabsColorBackground(Color color) {}

	@Override
	public void setUnselectedTabsColor(Color color) {}

	@Override
	public void setUnselectedTabsColor(Color[] colors, int[] percents) {}

	@Override
	public void setCornerRadius(int radius) {}

	@Override
	public void setShadowVisible(boolean visible) {}

	@Override
	public void setShadowColor(Color color) {}

	@Override
	public void setInnerKeyline(Color color) {}

	@Override
	public void setDrawCustomTabContentBackground(boolean drawCustomTabContentBackground) {}

}
