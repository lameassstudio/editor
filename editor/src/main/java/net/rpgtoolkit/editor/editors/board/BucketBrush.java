/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.editor.editors.board;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Stack;
import net.rpgtoolkit.common.assets.Tile;

/**
 *
 *
 * @author Joshua Michael Daly
 */
public class BucketBrush extends AbstractBrush {

  /**
   *
   */
  protected Tile pourTile;

  /**
   *
   */
  protected Tile oldTile;

  /**
   *
   */
  public BucketBrush() {

  }

  /**
   *
   *
   * @return
   */
  @Override
  public Shape getShape() {
    return getBounds();
  }

  /**
   *
   *
   * @return
   */
  @Override
  public Rectangle getBounds() {
    return new Rectangle(0, 0, 1, 1);
  }

  /**
   *
   *
   * @return
   */
  public Tile getPourTile() {
    return pourTile;
  }

  /**
   *
   *
   * @param tile
   */
  public void setPourTile(Tile tile) {
    pourTile = tile;
  }

  /**
   *
   *
   * @return
   */
  public Tile getOldTile() {
    return oldTile;
  }

  /**
   *
   *
   * @param tile
   */
  public void setOldTile(Tile tile) {
    oldTile = tile;
  }

  /**
   *
   *
   * @param g2d
   * @param view
   */
  @Override
  public void drawPreview(Graphics2D g2d, AbstractBoardView view) {

  }

  /**
   *
   *
   * @param brush
   * @return
   */
  @Override
  public boolean equals(Brush brush) {
    return brush instanceof BucketBrush;
  }

  /**
   *
   *
   * @param x
   * @param y
   * @param selection
   * @return
   */
  @Override
  public Rectangle doPaint(int x, int y, Rectangle selection) {
    BoardLayerView layer = affectedContainer.getLayer(initialLayer);

    if (layer == null) {
      return null;
    }

    oldTile = layer.getLayer().getTileAt(x, y);

    if (oldTile == pourTile) {
      return null;
    }

    if (selection == null) {
      Rectangle area = new Rectangle(new Point(x, y));
      Stack<Point> stack = new Stack<>();

      stack.push(new Point(x, y));

      while (!stack.empty()) {
        // Remove the next tile from the stack.
        Point point = stack.pop();

        if (layer.getLayer().contains(point.x, point.y)
                && layer.getLayer().getTileAt(point.x, point.y) == oldTile) {
          layer.getLayer().setTileAt(point.x, point.y, pourTile);
          area.add(point);

          stack.push(new Point(point.x, point.y - 1));
          stack.push(new Point(point.x, point.y + 1));
          stack.push(new Point(point.x + 1, point.y));
          stack.push(new Point(point.x - 1, point.y));
        }
      }

      return new Rectangle(area.x, area.y, area.width + 1, area.height + 1);
    } else {
      if (selection.contains(x, y)) {
        for (int y2 = selection.y; y2 < selection.height + selection.y; y2++) {
          for (int x2 = selection.x; x2 < selection.width + selection.x; x2++) {
            layer.getLayer().setTileAt(x2, y2, pourTile);
          }
        }
      }

      return selection;
    }
  }
}
