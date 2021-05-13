package com.demo;

import com.demo.listeners.CanvasListener;
import com.demo.models.Point2D;
import com.demo.models.Vector2D;
import com.demo.shape.Rectangle;
import com.demo.shape.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.Queue;
import java.util.*;

/**
 * Create by VinhIT
 * On 27/03/2021
 */

public class DrawCanvas extends Canvas {

    public static final int canvasWidth = 1005, canvasHeight = 605;
    public static final int rowSize = canvasWidth / 5, colSize = canvasHeight / 5;
    public static final int pixelSize = 5;  // Kích thước 1 đơn vị
    public static int currentColor = 0xff0000;  // Màu vẽ đang chọn hiện tại

    private DrawMode drawMode;
    private Mode mode; // Chế độ hình vẽ

    private int[][] board = new int[rowSize][colSize];      // Bảng màu canvas chính
    private int[][] tempBoard = new int[rowSize][colSize];  // Bảng màu phụ cho việc preview hình, sau khi `merge()` thì board và tempBoard sẽ hợp lại thành 1

    private CanvasListener listener; // Sự kiện cập nhập tọa độ con chuột
    private Geometry geometry;  // Hình vẽ

    private Set<Point2D> coordinatePoints = new HashSet<>();
    private List listShapes = new ArrayList<>();
    private List<int[][]> boardStates = new ArrayList<>();
    private List<List> shapesStates = new ArrayList<>();

    private int curState = 0;

    private boolean isShowAxis = true;
    private boolean isShowGrid = true;
    private boolean isShowPointCoord = false;

    private final int MAX_UNDO = 10;

    int[] spillX = new int[]{0, 1, 0, -1};
    int[] spillY = new int[]{-1, 0, 1, 0};

    //------------------------------------------------------------------------//

    public DrawCanvas(CanvasListener listener) {
        this.listener = listener;

        setPreferredSize(new Dimension(canvasWidth, canvasHeight));

        // mặc định nền màu trắng
        setBackground(new Color(0xFFFFFF));

        this.addMouseListener(new MyMouseAdapter());
        this.addMouseMotionListener(new MyMouseMotionAdapter());

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                tempBoard[i][j] = board[i][j] = 0xffffff;
            }
        }


        boardStates.add(getCurrentBoard());
        shapesStates.add(new ArrayList());

        // Mode mặc định là vẽ PEN
        drawMode = DrawMode.DEFAULT;
        setMode(mode.LINE);
//        setShowPointCoord(false);
        Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(c);

    }

    private void fillColor(Point2D startPoint) {

        Queue<Point2D> queue = new ArrayDeque<>();
        Queue<Point2D> tempQueue = new ArrayDeque<>();
        Queue<Point2D> tempQueue2 = new ArrayDeque<>();

        int colorBg = board[startPoint.getComputerX()][startPoint.getComputerY()];
        int colorFill = DrawCanvas.currentColor;

        tempQueue.add(startPoint);
        while (!tempQueue.isEmpty()) {
            Point2D point = tempQueue.remove();

            for (int i = 0; i < spillX.length; i++) {
                Point2D p = new Point2D(point.getX() + spillX[i], point.getY() + spillY[i], point.getColor());
                if (p.getComputerX() < 0 || p.getComputerY() < 0 || p.getComputerX() >= DrawCanvas.rowSize || p.getComputerY() >= DrawCanvas.colSize)
                    continue;
                if (board[p.getComputerX()][p.getComputerY()] == colorFill) continue;
                if (board[p.getComputerX()][p.getComputerY()] == colorBg) {
                    board[p.getComputerX()][p.getComputerY()] = tempBoard[p.getComputerX()][p.getComputerY()] = colorFill;
                    tempQueue2.add(new Point2D(p));
                    queue.add(new Point2D(p));
                }

            }
            if (tempQueue.isEmpty()) {
                while (!tempQueue2.isEmpty()) {
                    tempQueue.add(new Point2D(tempQueue2.remove()));
                }
            }
        }

        int cnt = 0;
        while (!queue.isEmpty()) {
            Point2D p = queue.remove();
            putPixel(p);
            cnt++;
        }

        System.out.println("Done fill color: " + cnt);
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
        if (geometry != null) {
            geometry.setDrawMode(drawMode);
        }
    }

    /*
     * Cài đặt hình muốn vẽ
     */
    public void setMode(Mode MODE) {
        this.mode = MODE;
        listener.notifyShapeModeChanged(MODE);
        if (MODE == MODE.NONE) return;

        switch (MODE) {
            case POINT -> {
                geometry = new SinglePoint(this);
            }
            case PEN -> {
                geometry = new Pen(this);
            }
            case LINE -> {
                geometry = new Line(this, drawMode);
            }
            case RECTANGLE -> {
                geometry = new Rectangle(this, drawMode);
            }
            case CIRCLE -> {
                geometry = new Circle(this, drawMode);
            }
            case ELLIPSE -> {
                geometry = new Ellipse(this, drawMode);
            }
            case ELLIPSE_DASH -> {
                geometry = new EllipseDash(this);
            }
            case TRIANGLE -> {
                geometry = new Triangle(this, drawMode);
            }
        }
    }

    public void setShowAxis(boolean showAxis) {
        isShowAxis = showAxis;
        if (isShowAxis) drawAxis();
        else clearAxis();
    }

    public void setShowGrid(boolean showGrid) {
        isShowGrid = showGrid;
        if (isShowGrid) drawGrid();
        else clearGrid();

        if (isShowPointCoord) drawAllPoints();
    }

    public void setShowPointCoord(boolean showPointCoord) {
        isShowPointCoord = showPointCoord;
        if (isShowPointCoord) {
            drawAllPoints();
            repaint();
        } else {
            System.out.println("no show");

            Point2D p;
            for (int i = 0; i < rowSize; i++) {
                for (int j = 0; j < colSize; j++) {
                    p = Point2D.fromComputerCoordinate(i, j);

                    p.setColor(board[i][j]);
                    putPixel(p);
                }
            }

            if (isShowGrid) drawGrid();
        }
    }


    public void addPointsToDrawCoord(Point2D p) {
        coordinatePoints.add(p);
    }

    public void drawPointsCoordinate(Point2D p) {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.drawString(String.format("(%d, %d)", p.getX(), p.getY()), p.getComputerX() * 5 - 5, p.getComputerY() * 5 - 5);
        g.dispose();
    }

    public void drawAllPoints() {
        if (!isShowPointCoord) return;

        for (Point2D p : coordinatePoints) {
            drawPointsCoordinate(p);
        }
    }

    /*
     * Xóa những điểm đã cũ mà không thuộc hình vẽ preview
     * Bằng cách lấy màu nền vẽ đè lên
     */
    public void clearDraw(List<Point2D> point2DS) {
        for (Point2D p : point2DS) {
            if (p.getComputerX() < 0 || p.getComputerY() < 0 || p.getComputerX() >= rowSize || p.getComputerY() >= colSize)
                continue;

            if (p.getColor() != board[p.getComputerX()][p.getComputerY()]) {
                p.setColor(board[p.getComputerX()][p.getComputerY()]);
                tempBoard[p.getComputerX()][p.getComputerY()] = board[p.getComputerX()][p.getComputerY()];

                putPixel(p);
                if (isShowAxis) drawAxis();
            }
        }
    }

    /*
     * Vẽ bản preview của hình
     */
    public void applyDraw(List<Point2D> point2DList) {
        for (Point2D p : point2DList) {
            if (p.getComputerX() < 0 || p.getComputerY() < 0 || p.getComputerX() >= rowSize || p.getComputerY() >= colSize)
                continue;

            if (p.getColor() != board[p.getComputerX()][p.getComputerY()]) {
                tempBoard[p.getComputerX()][p.getComputerY()] = p.getColor();
                putPixel(p);

                if (isShowAxis) drawAxis();
            }
        }

        drawAllPoints();
    }

    public void applyBoard(int[][] newBoard) {
        Point2D p;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (newBoard[i][j] != board[i][j]) {
                    board[i][j] = tempBoard[i][j] = newBoard[i][j];
                    p = Point2D.fromComputerCoordinate(i, j);
                    p.setColor(board[i][j]);
                    putPixel(p);
                }
            }
        }
    }

    /*
     * Hợp nhất nét vẽ preview của hình lên canvas
     */
    public void merge() {
        // Thêm hình vừa vẽ vào danh sách undo

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j] != tempBoard[i][j]) {
                    board[i][j] = tempBoard[i][j];
                }
            }
        }

        if (curState < boardStates.size() - 1) {
            clearStatesFrom(curState + 1);
        }

        listShapes.add(geometry);
        listener.notifyDataSetChanged(listShapes);

        saveStates();

        if (isShowPointCoord) drawAllPoints();
    }

    int[][] getCurrentBoard() {
        int[][] a = new int[rowSize][colSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) a[i][j] = board[i][j];
        }
        return a;
    }

    List getCurrentShapes() {
        List list = new ArrayList();
        for (int i = 0; i < listShapes.size(); i++) {
            Geometry geo = (Geometry) listShapes.get(i);
            list.add(geo.copy());
        }
        System.out.println("Saved list size: "+list.size());
        return list;
    }

    List getListShapesAt(int state) {
        List list = new ArrayList();
        for (int i = 0; i < shapesStates.get(state).size(); i++) {
            Geometry geo = (Geometry) shapesStates.get(state).get(i);
            list.add(geo.copy());
        }
        return list;
    }

    private void clearStatesFrom(int startIndex) {
        int i = boardStates.size() - 1;
        while (i >= startIndex) {
            boardStates.remove(i);
            shapesStates.remove(i);
            i--;
        }

        listShapes = getListShapesAt(curState);
    }

    private void saveStates() {

        boardStates.add(getCurrentBoard());
        shapesStates.add(getCurrentShapes());

        curState++;

        if (curState > MAX_UNDO) {
            boardStates.remove(0);
            shapesStates.remove(0);
            curState--;
        }

//        listShapes = getListShapesAt(curState);
//        listener.notifyDataSetChanged(listShapes);

        System.out.println("saved state: " + curState);

    }


    private void applyState(int state) {
        listShapes = getListShapesAt(state);

        listener.notifyDataSetChanged(listShapes);

        int[][] stateBoard = boardStates.get(state);
        Point2D p;

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (stateBoard[i][j] != board[i][j]) {
                    board[i][j] = tempBoard[i][j] = stateBoard[i][j];

                    p = Point2D.fromComputerCoordinate(i, j);
                    p.setColor(board[i][j]);
                    putPixel(p);
                }
            }
        }

        if (isShowAxis) drawAxis();

        System.out.println("apply state " + state + " done: " + listShapes.size());
    }


    public void undo() {
        if (curState > 0) applyState(--curState);
    }

    public void redo() {
        if (curState < boardStates.size() - 1) applyState(++curState);
    }

    /*
     * Vẽ trục tọa độ
     */
    private void drawAxis() {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight);
        g.drawLine(canvasWidth / 2 + 1, 0, canvasWidth / 2 + 1, canvasHeight);
        g.drawLine(0, canvasHeight / 2, canvasWidth, canvasHeight / 2);
        g.drawLine(0, canvasHeight / 2 + 1, canvasWidth, canvasHeight / 2 + 1);

//        if(isShowGrid) drawGrid();
    }

    /*
     * Xóa 2 trục tọa độ
     */
    private void clearAxis() {
        Point2D p;

        int j = colSize / 2;
        int i = 0;
        for (; i < rowSize; i++) {
            p = Point2D.fromComputerCoordinate(i, j);
            p.setColor(board[i][j]);
            putPixel(p);
        }

        i = rowSize / 2;
        j = 0;
        for (; j < colSize; j++) {
            p = Point2D.fromComputerCoordinate(i, j);
            p.setColor(board[i][j]);
            putPixel(p);
        }
        if (isShowGrid) {
            Graphics g = getGraphics();
            g.setColor(new Color(0xFFD9C7C7));

            for (i = 0; i <= rowSize; i++) {
                g.drawLine(i * pixelSize, 0, i * pixelSize, canvasHeight);
            }
            for (i = 0; i <= colSize; i++) {
                g.drawLine(0, i * pixelSize, canvasWidth, i * pixelSize);
            }
            g.dispose();
        }

    }

    /*
     * Vẽ lưới hệ tọa độ
     */
    private void drawGrid() {

        Graphics g = getGraphics();
        g.setColor(new Color(0xFFD9C7C7));

        for (int i = 0; i <= rowSize; i++) {
            g.drawLine(i * pixelSize, 0, i * pixelSize, canvasHeight);
        }
        for (int i = 0; i <= colSize; i++) {
            g.drawLine(0, i * pixelSize, canvasWidth, i * pixelSize);
        }
        g.dispose();
        if (isShowAxis) drawAxis();
    }

    /*
     * Xóa lưới tọa độ
     */
    private void clearGrid() {
        Point2D p;

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);
            }
        }

        if (isShowAxis) drawAxis();
    }

    /*
     * Paint Super Pha-ke
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (isShowAxis) drawAxis();
        if (isShowGrid) drawGrid(); // Vẽ ô lưới

        Point2D p;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j] != 0xffffff) {
                    p = Point2D.fromComputerCoordinate(i, j);
                    p.setColor(board[i][j]);
                    putPixel(p);
                }
            }
        }

        drawAllPoints();

    }

    private void putPixel(Point2D point) {
        Graphics g = getGraphics();
        g.setColor(new Color(point.getColor()));

        if (isShowGrid)
            g.fillRect(point.getComputerX() * pixelSize + 1, point.getComputerY() * pixelSize + 1, pixelSize - 1, pixelSize - 1);
        else
            g.fillRect(point.getComputerX() * pixelSize, point.getComputerY() * pixelSize, pixelSize, pixelSize);

        g.dispose();
    }


    /*
     * Xóa toàn bộ màn hình, mặc định màn hình sẽ quay về màu trắng
     */
    public void clearScreen() {
        System.out.println("Clear screen");

        coordinatePoints.clear();

        geometry.clearAll();

        Graphics g = getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);    // vẽ như này cho nhanh

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                tempBoard[i][j] = board[i][j] = 0xffffff;
            }
        }

        if (isShowAxis) drawAxis();

        if (isShowGrid) drawGrid(); // Xóa xong thì vẽ lại lưới tọa độ

//        resetStates();
        setMode(Mode.NONE);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        listShapes.clear();
        listener.clear();
        saveStates();
    }

    private void resetStates() {
        boardStates.clear();
        boardStates.add(getCurrentBoard());
        curState = 0;
    }

    private int[][] newDefaultBoard() {
        int[][] a = new int[rowSize][colSize];
        for (int i = 0; i < rowSize; i++)
            for (int j = 0; j < colSize; j++)
                a[i][j] = 0xffffff;
        return a;
    }

    public void clearShapes(int[] removeIndex) {
        int[][] temp = newDefaultBoard();
        for (int i = 0; i < listShapes.size(); i++) {
            boolean skip = false;
            for (int index : removeIndex) {
                if (index == i) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                for (Point2D p : ((Geometry) listShapes.get(i)).getListDraw()) {
                    if (p.getComputerX() < 0 || p.getComputerX() >= rowSize || p.getComputerY() < 0 || p.getComputerY() >= colSize)
                        continue;
                    temp[p.getComputerX()][p.getComputerY()] = p.getColor();
                }
            }
        }
        Arrays.sort(removeIndex);
        for (int i = removeIndex.length - 1; i >= 0; i--) {
            listShapes.remove(removeIndex[i]);
        }

        listener.notifyDataSetChanged(listShapes);

        applyBoard(temp);

        saveStates();
    }

    public void move(int[] indexMove) {
        listIndexMove.clear();
        for (int i : indexMove) listIndexMove.add(i);

        if (indexMove.length > 0) {
            setMode(Mode.MOVE);
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
            mapPoints.clear();
        } else {
            setMode(Mode.NONE);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }

    public void rotate(int[] indexMove, Point2D root) {
        this.rootPoint = root;
        listIndexMove.clear();
        for (int i : indexMove) listIndexMove.add(i);

        if (indexMove.length > 0) {
            setMode(Mode.ROTATE);
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
            mapPoints.clear();
        } else {
            setMode(Mode.MOVE);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }

    public void reflect(int[] indexMove, Point2D root, Point2D root2) {
        this.rootPoint = root;
        this.rootPoint2 = root2;

        listIndexMove.clear();
        for (int i : indexMove) listIndexMove.add(i);

        if (indexMove.length > 0) {
            reflectShapes(rootPoint2 == null);
        }
        setMode(Mode.NONE);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


    }

    public void scale(int[] indexMove, Point2D root, double scaleX, double scaleY) {
        listIndexMove.clear();
        for (int i : indexMove) listIndexMove.add(i);

        if (indexMove.length > 0) {
            scaleShapes(root, scaleX, scaleY);
        }

        setMode(Mode.NONE);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    // -------------- Lớp cài đặt sự kiện nhấn chuột ----------------
    public class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO: Them ve diem
            super.mouseClicked(e);

            // Set màu cho điểm vẽ là màu đang chọn
            Point2D point = Point2D.fromComputerCoordinate(e.getX() / DrawCanvas.pixelSize, e.getY() / DrawCanvas.pixelSize);
            point.setColor(DrawCanvas.currentColor);

//            if (geometry instanceof SinglePoint) {
//                geometry.setStartPoint(point);
//                geometry.setupDraw();
//
//                mouseReleased(e);
//            }

            if (mode == Mode.FILL_COLOR) {
                fillColor(point);
                saveStates();
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            startMove = endMove = null;

            if (mode == Mode.MOVE || mode == Mode.ROTATE) {
                // Hoàn thành moving | rotating
                System.out.println("Done move or rotate");
                applyShapesChange();

            } else if (mode == Mode.FILL_COLOR) {

            } else {
                if (geometry.getEndPoint() != null)
                    merge();

                setMode(mode);
            }
        }
    }

    public void applyShapesChange() {
        setMode(Mode.NONE);
        startMove = null;
        endMove = null;

        for (int index : mapNewPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] pointsRect = g.getPoints();
            for (int i = 0; i < pointsRect.length; i++) {
                pointsRect[i] = mapNewPoints.get(index).get(i);
            }
            g.setPoints(pointsRect);
            if (g instanceof Ellipse) {
                ((Ellipse) g).setRotate(null, 0);
            }

            listShapes.set(index, g);
        }

        mapPoints.clear();
        mapNewPoints.clear();

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j] != tempBoard[i][j]) {
                    board[i][j] = tempBoard[i][j];
                }
            }
        }

        if (isShowAxis) drawAxis();

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        listener.notifyDeselectedAllItems();

        saveStates();
    }


    Point2D rootPoint = null, rootPoint2 = null;
    Point2D startMove = null, endMove = null;
    List<Integer> listIndexMove = new ArrayList<>();

    HashMap<Integer, ArrayList<Point2D>> mapPoints = new HashMap<>();
    HashMap<Integer, ArrayList<Point2D>> mapNewPoints = new HashMap<>();

    public class MyMouseMotionAdapter extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            // Nhấn giữ chuột và kéo

            super.mouseDragged(e);
            if (e.getX() >= canvasWidth || e.getY() >= canvasHeight || e.getX() <= 0 || e.getY() <= 0) return;

            Point2D point = Point2D.fromComputerCoordinate(e.getX() / DrawCanvas.pixelSize, e.getY() / DrawCanvas.pixelSize);
            geometry.setColor(DrawCanvas.currentColor);
            listener.mouseCoordinate(point.getX(), point.getY());

            if (mode == Mode.NONE || mode == Mode.FILL_COLOR) return;
            if (geometry instanceof SinglePoint) return;


            // --------- Move ---------
            if (mode == Mode.MOVE && listIndexMove.size() > 0) {

                moveShapes(point);

            } else if (mode == Mode.ROTATE && listIndexMove.size() > 0) {

                rotateShapes(point);

            } else if (mode == Mode.REFLECT && listIndexMove.size() > 0) {


            } else {

                if (geometry.getStartPoint() == null) {
                    geometry.setStartPoint(point);
                } else {
                    geometry.setEndPoint(point);
                    geometry.setupDraw(); // Xem trước nét vẽ
                }
            }


        }


        @Override
        public void mouseMoved(MouseEvent e) {
            // Chuột di chuyển bình thưởng, KHÔNG nhấn giữ

            super.mouseMoved(e);

            // Sử dụng kĩ thuật Callback, cập nhật tọa độ hiển thị lên JLable ở góc dưới màn hình
            Point2D point = Point2D.fromComputerCoordinate(e.getX() / DrawCanvas.pixelSize, e.getY() / DrawCanvas.pixelSize);
            listener.mouseCoordinate(point.getX(), point.getY());
        }

    }

    public void scaleShapes(Point2D root, double scaleX, double scaleY) {
        System.out.println("Scale");

        convertShapeToPreview();

        for (int index : mapPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] points = g.getPoints();
            for (int i = 0; i < points.length; i++) {
                points[i] = points[i].scale(root, scaleX, scaleY);
                mapNewPoints.get(index).set(i, points[i]);
            }
            g.setPoints(points);

            g.setupDraw();
            listener.notifyShapeChanged(index, g.toString());
        }

        applyShapesChange();
    }

    public void reflectShapes(boolean isReflectByPoint) {
        System.out.println("Reflect");

        convertShapeToPreview();

        for (int index : mapPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] points = g.getPoints();
            for (int i = 0; i < points.length; i++) {
                if (isReflectByPoint) {
                    points[i] = points[i].reflect(rootPoint);
                } else {
                    points[i] = points[i].reflect(new Point2D(rootPoint), new Point2D(rootPoint2));
                }
                mapNewPoints.get(index).set(i, points[i]);
            }
            g.setPoints(points);


            g.setupDraw();
            listener.notifyShapeChanged(index, g.toString());
        }

        applyShapesChange();
    }

    public void convertShapeToPreview() {
        if (mapPoints.size() == 0) {
            mapNewPoints.clear();

            for (int index : listIndexMove) {
                Geometry g = (Geometry) listShapes.get(index);

                mapPoints.put(index, new ArrayList<>());
                mapNewPoints.put(index, new ArrayList<>());
                Point2D[] points = g.getPoints();
                for (int j = 0; j < points.length; j++) {
                    mapPoints.get(index).add(new Point2D(points[j]));
                    mapNewPoints.get(index).add(new Point2D(points[j]));
                }

                g.clearListDraw();
            }

            int[][] temp = newDefaultBoard();

            for (int i = 0; i < listShapes.size(); i++) {
                boolean skip = false;
                for (int j : listIndexMove) {
                    if (i == j) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    Geometry geo = (Geometry) listShapes.get(i);
                    for (Point2D p : geo.getListDraw()) {
                        if (p.getComputerX() < 0 || p.getComputerX() >= rowSize || p.getComputerY() < 0 || p.getComputerY() >= colSize)
                            continue;
                        temp[p.getComputerX()][p.getComputerY()] = p.getColor();
                    }
                }
            }

            applyBoard(temp);
        }

    }

    public void moveShapes(Point2D point) {

        convertShapeToPreview();

        if (startMove == null) {
            System.out.println("new start move");
            startMove = point;
            return;
        } else
            endMove = point;

        int xMove = endMove.getX() - startMove.getX();
        int yMove = endMove.getY() - startMove.getY();

        for (int index : mapPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] points = g.getPoints();
            for (int i = 0; i < points.length; i++) {
                points[i].set(mapPoints.get(index).get(i).getX() + xMove, mapPoints.get(index).get(i).getY() + yMove);
            }

            g.setPoints(points);

            for (int i = 0; i < points.length; i++) {
                mapNewPoints.get(index).set(i, points[i]);
            }

            g.setupDraw();
            listener.notifyShapeChanged(index, g.toString());
        }

    }


    public void rotateShapes(Point2D point) {
        if (rootPoint == null) {
            System.out.println("root point null");
            return;
        }

        convertShapeToPreview();

        if (startMove == null) {
            System.out.println("new start rotate");
            startMove = point;
            return;
        } else
            endMove = point;


        Vector2D v = new Vector2D(startMove, rootPoint);
        Vector2D v2 = new Vector2D(endMove, rootPoint);
        double angle = v.angleRadian(v2);

        int cur = pointLine(startMove, rootPoint, point);

        if (cur > 0) {
            angle = -angle;
        }

        for (int index : mapPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] points = g.getPoints();
            for (int i = 0; i < points.length; i++) {
                points[i] = mapPoints.get(index).get(i).rotate(rootPoint, angle);
                mapNewPoints.get(index).set(i, new Point2D(points[i]));
            }
            g.setPoints(points);

            if (g instanceof Ellipse) {
                ((Ellipse) g).setRotate(rootPoint, angle);
            }
            g.setupDraw();

            listener.notifyShapeChanged(index, g.toString());
        }
    }

    /*
     * Xác định vị trí tương đối của điểm p
     * So với đường thẳng đi qua 2 điểm p1 và p2
     */
    public int pointLine(Point2D p1, Point2D p2, Point2D p) {
        return (p1.getY() - p2.getY()) * p.getX() - (p1.getY() - p2.getY()) * p1.getX()
                + (p2.getX() - p1.getX()) * p.getY() - (p2.getX() - p1.getX()) * p1.getY();
    }

    public void copyShapes(int[] indexCopy) {
        for (int index : indexCopy) {
            // Tạo bản sao
            Geometry g = ((Geometry) listShapes.get(index)).copy();

            listShapes.add(g);
            g.setupDraw();

            listener.notifyShapeInserted(g.toString());
        }
        saveStates();
    }

}
