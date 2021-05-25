package com.demo.listeners;

import com.demo.Mode;

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

    void notifyShapeModeChanged(Mode MODE);

    void clear();

    void onUndoState(boolean isEnable);

    void onRedoState(boolean isEnable);
}
