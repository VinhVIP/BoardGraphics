package com.demo;

import java.util.List;

/**
 * Create by VinhIT
 * On 29/03/2021
 */

public interface CanvasListener {
    void mouseCoordinate(int x, int y);
    void notifyShapeInserted(String shapeTitle);
    void notifyDataSetChanged(List listShape);
    void notifyShapeChanged(int position, String newTitle);
    void notifyDeselectedAllItems();
    void clear();
}
