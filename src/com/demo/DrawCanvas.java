package com.demo;

import com.demo.listeners.CanvasListener;
import com.demo.models.Point2D;
import com.demo.models.Point3D;
import com.demo.models.Vector2D;
import com.demo.motions.MotionManager;
import com.demo.shape.Polygon;
import com.demo.shape.Rectangle;
import com.demo.shape.*;
import com.demo.shapes3D.Cone;
import com.demo.shapes3D.Cylinder;
import com.demo.shapes3D.Rectangular;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Create by VinhIT
 * On 27/03/2021
 */

public class DrawCanvas extends Canvas {

    public static final int canvasWidth = 1005, canvasHeight = 605;
    public static final int rowSize = canvasWidth / 5, colSize = canvasHeight / 5;
    public static final int pixelSize = 5;          // Kích thước 1 đơn vị
    public static int currentColor = 0x0000ff;      // Màu vẽ đang chọn hiện tại
    public static int currentFillColor = 0xffffff;  // Màu vẽ đang chọn hiện tại

    private final int gridColor = 0xEEE9E9;         // Màu của lưới

    private boolean is2DCoordinates = true;         // Chọn hệ trục tọa độ 3D hay 2D
    private boolean isShowMotions = false;          // Show Animation

    private DrawMode drawMode;                      // Chế độ đường vẽ (nét liền, đứt, gạch nối,...)
    private Mode mode;                              // Chế độ hình vẽ (hình chữ nhật, tròn, elip,...)

    private final int[][] board = new int[rowSize][colSize];      // Bảng màu canvas chính
    private final int[][] tempBoard = new int[rowSize][colSize];  // Bảng màu phụ cho việc preview hình, sau khi `merge()` thì board và tempBoard sẽ hợp lại thành 1

    private final CanvasListener listener;          // Sự kiện cập nhập tọa độ con chuột
    private Geometry geometry;                      // Hình vẽ hiện tại

    private List listShapes = new ArrayList<>();    // Danh sách các shape đang làm việc trên màn hình
    private final List<int[][]> boardStates = new ArrayList<>();
    private final List<List> shapesStates = new ArrayList<>();

    private int curState = 0;                       // Trạng thái hiện tại (sử dụng với undo/redo)

    private boolean isFillColor = false;            // Chế độ tô màu shape

    private boolean isShowAxis = true;              // Show trục tọa độ 2D hoặc 3D tùy theo `is2DCoordinates`
    private boolean isShowGrid = true;              // Show lưới tọa độ
    private boolean isShowPointCoord = false;       // Show các điểm thuộc mỗi shape

    private final int MAX_UNDO = 10;                // Số lượt undo/redo tối đa

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

        // Thiết lập trạng thái ban đầu
        boardStates.add(getCurrentBoard());     // Ban đầu bảng vẽ là màu trắng
        shapesStates.add(new ArrayList());      // Chưa có hình vẽ nào

        // Chế độ đường vẽ mặc định là: Vẽ nét liền - DEFAULT
        drawMode = DrawMode.DEFAULT;

        // Chế độ hình vẽ là vẽ đường thẳng
        setMode(Mode.LINE);

        // Thiết lập con trỏ chuột mặc định
        Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(c);
    }


    Thread motionThread;        // Thread sử dụng để trình chiếu hoạt ảnh: Animation

    public void setShowMotions(boolean showMotions) {
        isShowMotions = showMotions;

        if (isShowMotions) {
            if (motionThread == null) {
                clearScreen();
                motionThread = new MotionManager(this);
                motionThread.start();
            }
        } else {
            motionThread.stop();
            motionThread = null;
            clearScreen();
        }
    }

    public boolean isShowMotions() {
        return isShowMotions;
    }

    public boolean isIs2DCoordinates() {
        return is2DCoordinates;
    }

    /*
     * Chọn chế độ vẽ 3D hoặc 2D
     */
    public void setIs2DCoordinates(boolean is2DCoordinates) {
        if (is2DCoordinates != this.is2DCoordinates) {
            resetStates();

            if (isShowAxis) clearAxis();
            this.is2DCoordinates = is2DCoordinates;
            if (isShowAxis) drawAxis();

            clearScreen();

        }
    }

    /*
     * Thiết lập chế đọ vẽ hình
     */
    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
        if (geometry != null) {
            geometry.setDrawMode(drawMode);
        }
    }

    /*
     * Khởi tạo đối tượng hình muốn vẽ
     */
    public void setMode(Mode MODE) {
        this.mode = MODE;
        listener.notifyShapeModeChanged(MODE);
        if (MODE == Mode.NONE) return;

        switch (MODE) {
            case POINT -> {
                geometry = new SinglePoint(this);
            }
            case PEN -> {
                geometry = new Pen(this);
            }
            case LINE -> {
                geometry = new Line(this, drawMode, isFillColor);
            }
            case RECTANGLE -> {
                geometry = new Rectangle(this, drawMode, isFillColor);
            }
            case CIRCLE -> {
                geometry = new Circle(this, drawMode, isFillColor);
            }
            case ELLIPSE -> {
                geometry = new Ellipse(this, drawMode, isFillColor);
            }
            case ELLIPSE_DASH -> {
                geometry = new EllipseDash(this);
            }
            case TRIANGLE -> {
                geometry = new Triangle(this, drawMode, isFillColor);
            }
            case RECTANGULAR -> {
                geometry = new Rectangular(this);
            }
            case CYLINDER -> {
                geometry = new Cylinder(this);
            }
            case CONE -> {
                geometry = new Cone(this);
            }
        }
    }


    /*
     * Thiết lập chế độ tô màu cho hình vẽ
     */
    public void setFillColor(boolean fillColor) {
        isFillColor = fillColor;
        geometry.setFillColor(fillColor);
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
    }

    /*
     * Hiển thị tọa độ các điểm thuộc mỗi hình vẽ khác nhau
     */
    public void setShowPointCoord(boolean showPointCoord) {
        isShowPointCoord = showPointCoord;
        if (isShowPointCoord) {
            for (int i = 0; i < listShapes.size(); i++) {
                Geometry g = (Geometry) listShapes.get(i);
                g.showPointsCoordinate();
            }
        } else {
            for (int i = 0; i < listShapes.size(); i++) {
                Geometry g = (Geometry) listShapes.get(i);
                g.clearPointsCoordinate();
            }
            if (isShowAxis) drawAxis();
        }
    }

    /*
     * Vẽ tọa độ của điểm 2D lên màn hình
     * @param   p   điểm muốn hiển thị tọa độ
     */
    public void drawPointsCoordinate(Point2D p) {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.drawString(String.format("(%d, %d)", p.getX(), p.getY()), p.getComputerX() * 5 - 5, p.getComputerY() * 5 - 5);
        g.dispose();
    }

    /*
     * Vẽ tọa độ của điểm 3D lên màn hình
     * @param   p   điểm muốn hiển thị tọa độ
     */
    public void drawPointsCoordinate(Point3D p) {
        if (p == null) return;
        Point2D p2 = p.to2DPoint();
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.drawString(p.toString(), p2.getComputerX() * 5 - 5, p2.getComputerY() * 5 - 5);
        g.dispose();
    }


    /*
     * Xóa những điểm đã cũ mà không thuộc hình vẽ preview
     * Bằng cách lấy màu nền vẽ đè lên
     * @param   point2Ds    danh sách những điểm cần xóa
     */
    public void clearDraw(List<Point2D> point2DS) {
        for (Point2D p : point2DS) {
            if (p.insideScreen()) {
                if (p.getColor() != board[p.getComputerX()][p.getComputerY()]) {
                    p.setColor(board[p.getComputerX()][p.getComputerY()]);
                    tempBoard[p.getComputerX()][p.getComputerY()] = board[p.getComputerX()][p.getComputerY()];
                    putPixel(p);
                }
            }
        }
    }

    /*
     * Vẽ bản preview của hình
     * @param   point2DS    danh sách những điểm cần vẽ
     */
    public void applyDraw(List<Point2D> point2DS) {
        for (Point2D p : point2DS) {
            if (p.insideScreen()) {
                if (p.getColor() != board[p.getComputerX()][p.getComputerY()]) {
                    tempBoard[p.getComputerX()][p.getComputerY()] = p.getColor();
                    putPixel(p);
                }
            }

        }

        // Hiển thị tọa độ các điểm cơ bản của các hình vẽ khác
        // Vì khi putpixel thì chữ sẽ bị mất đi
        if (isShowPointCoord) showFixedShapesCoordinate();

    }

    /*
     * Vẽ lại 1 vùng hình chữ nhật
     * Dùng để vẽ lại vùng hiển thị tọa độ các điểm
     * @param startX, startY, endX, endY    đều là tọa độ máy tính
     */
    public void reDrawPoints(int startX, int startY, int endX, int endY) {
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (i < 0 || i >= rowSize || j < 0 || j >= colSize) continue;

                Point2D p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);
            }
        }

        if (isShowGrid) {
            // Vẽ lại lưới pixel tại vùng bị thay đổi
            // Không vẽ lại toàn bộ màn hình vì tốn nhiều thời gian xử lý

            Graphics g = getGraphics();
            g.setColor(new Color(gridColor));

            for (int i = startX; i <= endX; i++) {
                g.drawLine(i * pixelSize, startY * pixelSize, i * pixelSize, endY * pixelSize);
            }
            for (int i = startY; i <= endY; i++) {
                g.drawLine(startX * pixelSize, i * pixelSize, endX * pixelSize, i * pixelSize);
            }
            g.dispose();
        }
    }

    /*
     * Ngay lập tức vẽ lên màn hình bảng màu mới
     * @param   newBoard    bảng màu mới
     */
    public void applyBoard(int[][] newBoard) {
        Point2D p;
        int i;
        for (int u = 0; u <= rowSize / 2; u++) {
            for (int j = 0; j < colSize; j++) {

                // Vẽ từ giữa ra 2 bên màn hình

                i = rowSize / 2 + u;
                if (newBoard[i][j] != board[i][j]) {
                    board[i][j] = tempBoard[i][j] = newBoard[i][j];
                    p = Point2D.fromComputerCoordinate(i, j);
                    p.setColor(board[i][j]);
                    putPixel(p);
                }

                i = rowSize / 2 - u;
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
     * Hợp nhất nét vẽ preview của hình lên bảng màu chính
     * Sau khi hoàn thành, hình vẽ được cố định trên màn hình
     */
    public void merge() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j] != tempBoard[i][j]) {
                    board[i][j] = tempBoard[i][j];
                }
            }
        }

        // Xóa những trạng thái phía sau
        // tức sau khi thêm hình vẽ mới thì đây là trạng thái mới nhất
        // không thể REDO đến các trạng thái khác
        if (curState < boardStates.size() - 1) {
            clearStatesFrom(curState + 1);
        }

        // Thêm hình vừa vẽ vào danh sách
        listShapes.add(geometry);
        // Cập nhật lại danh sách các hình được vẽ lên bảng thông báo bên phải màn hình
        listener.notifyDataSetChanged(listShapes);

        // Lưu lại trạng thái hiện tại
        saveStates();

        // Hiển thị tọa độ các điểm của hình vừa vẽ
        if (isShowPointCoord) geometry.showPointsCoordinate();
    }

    /*
     * @return bản sao bảng màu hiện tại
     */
    int[][] getCurrentBoard() {
        int[][] a = new int[rowSize][colSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) a[i][j] = board[i][j];
        }
        return a;
    }

    /*
     * @return bản sao danh sách các hình vẽ hiện tại
     */
    List getCurrentShapes() {
        List list = new ArrayList();
        for (int i = 0; i < listShapes.size(); i++) {
            Geometry geo = (Geometry) listShapes.get(i);
            list.add(geo.copy());
        }
        System.out.println("Saved list size: " + list.size());
        return list;
    }

    /*
     * Lấy danh sách các hình vẽ thuộc 1 trạng thái nhất định
     * @param   state   trạng thái muốn lấy
     * @return      List copy các hình thuộc trạng thái đó
     */
    List getListShapesAt(int state) {
        List list = new ArrayList();
        for (int i = 0; i < shapesStates.get(state).size(); i++) {
            Geometry geo = (Geometry) shapesStates.get(state).get(i);
            list.add(geo.copy());
        }
        return list;
    }

    /*
     * Xóa các trạng thái từ `startIndex` trở về sau
     */
    private void clearStatesFrom(int startIndex) {
        int i = boardStates.size() - 1;
        while (i >= startIndex) {
            boardStates.remove(i);
            shapesStates.remove(i);
            i--;
        }

        listShapes = getListShapesAt(curState);
    }

    /*
     * Lưu lại trạng thái hiện tại
     */
    private void saveStates() {
        boardStates.add(getCurrentBoard());
        shapesStates.add(getCurrentShapes());

        curState++;

        if (curState > MAX_UNDO) {
            boardStates.remove(0);
            shapesStates.remove(0);
            curState--;
        }

        listener.onUndoState(curState != 0);
        listener.onRedoState(curState != boardStates.size() - 1);

        System.out.println("saved state: " + curState);
    }


    /*
     * Áp dụng trạng thái xác định lên màn hình
     * @param   state   trạng thái muốn áp dụng
     */
    private void applyState(int state) {
        listShapes = getListShapesAt(state);
        listener.notifyDataSetChanged(listShapes);

        int[][] stateBoard = boardStates.get(state);
        applyBoard(stateBoard);

        System.out.println("apply state " + state + " done: " + listShapes.size());

        listener.onUndoState(curState != 0);
        listener.onRedoState(curState != boardStates.size() - 1);
    }


    public void undo() {
        if (curState > 0) applyState(--curState);
    }

    public void redo() {
        if (curState < boardStates.size() - 1) applyState(++curState);
    }

    /*
     * Vẽ trục tọa độ 2D hoặc 3D
     * Dựa vào giá trị `is2DCoordinates`
     */
    private void drawAxis() {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);

        if (is2DCoordinates) {
            g.drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight);
            g.drawLine(canvasWidth / 2 + 1, 0, canvasWidth / 2 + 1, canvasHeight);
            g.drawLine(0, canvasHeight / 2, canvasWidth, canvasHeight / 2);
            g.drawLine(0, canvasHeight / 2 + 1, canvasWidth, canvasHeight / 2 + 1);
        } else {
            g.drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight / 2);
            g.drawLine(canvasWidth / 2 + 1, 0, canvasWidth / 2 + 1, canvasHeight / 2);
            g.drawLine(canvasWidth / 2, canvasHeight / 2, canvasWidth, canvasHeight / 2);
            g.drawLine(canvasWidth / 2, canvasHeight / 2 + 1, canvasWidth, canvasHeight / 2 + 1);

            Point2D point = new Point2D(0, 0);
            for (int z = 0; ; z--) {
                point.set(z, z);
                if (point.getComputerY() > rowSize) break;
                drawAxisAt(point, g);
            }
        }
        g.dispose();

        if (isShowPointCoord) showFixedShapesCoordinate();
    }

    /*
     * Xóa 2 trục tọa độ
     */
    private void clearAxis() {
        Point2D p;

        if (is2DCoordinates) {
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
        } else {
            int j = colSize / 2;
            int i = rowSize / 2;
            for (; i < rowSize; i++) {
                p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);
            }

            i = rowSize / 2;
            j = colSize / 2;
            for (; j >= 0; j--) {
                p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);
            }

            i = rowSize / 2;
            j = colSize / 2;
            for (; j < colSize; j++) {
                p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);
                p = Point2D.fromComputerCoordinate(i + 1, j);
                p.setColor(board[i + 1][j]);
                putPixel(p);
                i--;
            }
        }

        if (isShowGrid) {
            Graphics g = getGraphics();
            g.setColor(new Color(gridColor));

            for (int i = 0; i <= rowSize; i++) {
                g.drawLine(i * pixelSize, 0, i * pixelSize, canvasHeight);
            }
            for (int i = 0; i <= colSize; i++) {
                g.drawLine(0, i * pixelSize, canvasWidth, i * pixelSize);
            }
            g.dispose();
        }

        if (isShowPointCoord) showFixedShapesCoordinate();

    }

    /*
     * Vẽ lưới hệ tọa độ
     */
    private void drawGrid() {

        Graphics g = getGraphics();
        g.setColor(new Color(gridColor));

        for (int i = 0; i <= rowSize; i++) {
            g.drawLine(i * pixelSize, 0, i * pixelSize, canvasHeight);
        }
        for (int i = 0; i <= colSize; i++) {
            g.drawLine(0, i * pixelSize, canvasWidth, i * pixelSize);
        }
        g.dispose();

        if (isShowAxis) drawAxis();
        if (isShowPointCoord) showFixedShapesCoordinate();
    }

    /*
     * Xóa lưới tọa độ
     */
    private void clearGrid() {
        Point2D p;

        int i;
        for (int u = 0; u <= rowSize / 2; u++) {
            for (int j = 0; j < colSize; j++) {
                i = rowSize / 2 + u;
                p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);

                i = rowSize / 2 - u;
                p = Point2D.fromComputerCoordinate(i, j);
                p.setColor(board[i][j]);
                putPixel(p);
            }
        }

        if (isShowAxis) drawAxis();
        if (isShowPointCoord) showFixedShapesCoordinate();
    }

    /*
     * Paint Super Pha-ke
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (isShowAxis) drawAxis();
        if (isShowGrid) drawGrid(); // Vẽ ô lưới

        // Cần đoạn code này vì khi app bị ẩn xuống
        // Khi mở lại thì hàm paint sẽ được gọi lại
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

//        demoOpenImageFile();
    }

    public void openFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String sname = file.getAbsolutePath(); //THIS WAS THE PROBLEM
            System.out.println(sname);
            demoOpenImageFile(file);
        }
    }

    private void demoOpenImageFile(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            int[][] b = newDefaultBoard();
            for (int i = 2; i < image.getWidth(); i += 5) {
                if (i >= canvasWidth) break;
                for (int j = 2; j < image.getHeight(); j += 5) {
                    if (j >= canvasHeight) break;
                    b[i / 5][j / 5] = image.getRGB(i, j);
                }
            }
            applyBoard(b);
            saveStates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Vẽ màu cho từng điểm
     * @param   point   điểm cần vẽ, bao gồm cả tọa độ và màu vẽ
     */
    public void putPixel(Point2D point) {
        Graphics g = getGraphics();
        g.setColor(new Color(point.getColor()));

        if (isShowGrid)
            g.fillRect(point.getComputerX() * pixelSize + 1, point.getComputerY() * pixelSize + 1, pixelSize - 1, pixelSize - 1);
        else
            g.fillRect(point.getComputerX() * pixelSize, point.getComputerY() * pixelSize, pixelSize, pixelSize);

        // Vẽ lại axis tại từng điểm cho tối ưu
        drawAxisAt(point, g);

        g.dispose();
    }

    /*
     * Vẽ đường trục tọa độ tại 1 điểm xác định
     * Dùng phương thức này trong trường hợp 1 vài điểm thuộc trục tọa độ bị thay đổi nhằm tối ưu thời gian vẽ
     */
    private void drawAxisAt(Point2D point, Graphics g) {
        if (isShowAxis) {
            g.setColor(Color.BLACK);
            if (isIs2DCoordinates()) {
                if (point.getX() == 0) {
                    g.drawLine(canvasWidth / 2, point.getComputerY() * 5, canvasWidth / 2, (point.getComputerY() + 1) * 5);
                    g.drawLine(canvasWidth / 2 + 1, point.getComputerY() * 5, canvasWidth / 2 + 1, (point.getComputerY() + 1) * 5);
                }
                if (point.getY() == 0) {
                    g.drawLine(point.getComputerX() * 5, canvasHeight / 2, (point.getComputerX() + 1) * 5, canvasHeight / 2);
                    g.drawLine(point.getComputerX() * 5, canvasHeight / 2 + 1, (point.getComputerX() + 1) * 5, canvasHeight / 2 + 1);
                }
            } else {
                if (point.getX() == 0 && point.getY() >= 0) {
                    if (point.getY() == 0) {
                        g.drawLine(canvasWidth / 2, point.getComputerY() * 5, canvasWidth / 2, point.getComputerY() * 5 + 3);
                        g.drawLine(canvasWidth / 2 + 1, point.getComputerY() * 5, canvasWidth / 2 + 1, point.getComputerY() * 5 + 3);
                    } else {
                        g.drawLine(canvasWidth / 2, point.getComputerY() * 5, canvasWidth / 2, (point.getComputerY() + 1) * 5);
                        g.drawLine(canvasWidth / 2 + 1, point.getComputerY() * 5, canvasWidth / 2 + 1, (point.getComputerY() + 1) * 5);
                    }
                }
                if (point.getY() == 0 && point.getX() >= 0) {
                    if (point.getX() == 0) {
                        g.drawLine(point.getComputerX() * 5 + 2, canvasHeight / 2, (point.getComputerX() + 1) * 5, canvasHeight / 2);
                        g.drawLine(point.getComputerX() * 5 + 2, canvasHeight / 2 + 1, (point.getComputerX() + 1) * 5, canvasHeight / 2 + 1);
                    } else {
                        g.drawLine(point.getComputerX() * 5, canvasHeight / 2, (point.getComputerX() + 1) * 5, canvasHeight / 2);
                        g.drawLine(point.getComputerX() * 5, canvasHeight / 2 + 1, (point.getComputerX() + 1) * 5, canvasHeight / 2 + 1);
                    }
                }
                if (point.getX() <= 0 && point.getX() == point.getY()) {
                    if (point.getX() == 0) {
                        g.drawLine(point.getComputerX() * 5 + 3, point.getComputerY() * 5 + 2, point.getComputerX() * 5, point.getComputerY() * 5 + 5);
                        g.drawLine(point.getComputerX() * 5 + 4, point.getComputerY() * 5 + 2, point.getComputerX() * 5 + 1, point.getComputerY() * 5 + 5);
                    } else {
                        g.drawLine(point.getComputerX() * 5 + 5, point.getComputerY() * 5, point.getComputerX() * 5, point.getComputerY() * 5 + 5);
                        g.drawLine(point.getComputerX() * 5 + 5, point.getComputerY() * 5 + 1, point.getComputerX() * 5 + 1, point.getComputerY() * 5 + 5);
                    }
                }
            }
        }
    }

    /*
     * Xóa toàn bộ màn hình, mặc định màn hình sẽ quay về màu trắng
     */
    public void clearScreen() {
        System.out.println("Clear screen");

        geometry.clearAll();
        listShapes.clear();
        listener.clear();

        setMode(Mode.NONE);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        Graphics g = getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);    // vẽ như này cho nhanh

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                tempBoard[i][j] = board[i][j] = 0xffffff;
            }
        }

        saveStates();

        if (isShowAxis) drawAxis();
        if (isShowGrid) drawGrid(); // Xóa xong thì vẽ lại lưới tọa độ
    }

    private void resetStates() {
        System.out.println("reset state");

        listShapes.clear();
        listener.clear();

        boardStates.clear();
        shapesStates.clear();

        curState = -1;
    }

    /*
     * @return 1 bảng màu mới có màu trắng
     */
    public static int[][] newDefaultBoard() {
        int[][] a = new int[rowSize][colSize];
        for (int i = 0; i < rowSize; i++)
            for (int j = 0; j < colSize; j++)
                a[i][j] = 0xffffff;
        return a;
    }

    /*
     * Xóa các hình được chỉ định khỏi màn hình
     * @param   removeIndex     các index hình vẽ muốn xóa
     */
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
            if (isShowPointCoord) {
                ((Geometry) listShapes.get(removeIndex[i])).clearPointsCoordinate();
            }
            listShapes.remove(removeIndex[i]);
        }

        listener.notifyDataSetChanged(listShapes);
        applyBoard(temp);

        saveStates();
    }


    /*
     * tính năng di chuyển hình vẽ
     * @param   indexShapesSelected     index các hình vẽ muốn di chuyển
     */
    public void move(int[] indexShapesSelected) {
        listShapesSelected.clear();
        for (int i : indexShapesSelected) listShapesSelected.add(i);

        if (indexShapesSelected.length > 0) {
            setMode(Mode.MOVE);
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
            mapPoints.clear();
        } else {
            setMode(Mode.NONE);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /*
     * Quay hình vẽ quanh 1 tâm xác định
     * @param   indexShapesSelected     index các hình vẽ muốn quay
     * @param   root    tâm quay
     */
    public void rotate(int[] indexShapesSelected, Point2D root) {
        this.rootPoint = root;
        listShapesSelected.clear();
        for (int i : indexShapesSelected) listShapesSelected.add(i);

        if (indexShapesSelected.length > 0) {
            setMode(Mode.ROTATE);
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
            mapPoints.clear();
        } else {
            setMode(Mode.MOVE);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /*
     * Lấy đối xứng hình vẽ qua 1 điểm hoặc 1 đường thẳng
     * @param   indexShapesSelected     index các hình vẽ
     * @param   root    điểm thứ nhất
     * @param   root2   điểm thứ 2
     * Nếu root2 là null thì là phép đỗi xứng qua điểm, ngược lại là đỗi xứng qua đường thẳng đi qua 2 điểm root, root2
     */
    public void reflect(int[] indexShapesSelected, Point2D root, Point2D root2) {
        this.rootPoint = root;
        this.rootPoint2 = root2;

        listShapesSelected.clear();
        for (int i : indexShapesSelected) listShapesSelected.add(i);

        if (indexShapesSelected.length > 0) {
            reflectShapes(rootPoint2 == null);
        }
        setMode(Mode.NONE);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /*
     * Thu phóng hình vẽ theo tỉ lệ
     * @param   indexShapesSelected     index các hình vẽ muốn quay
     * @param   root    điểm tâm thu phóng
     * @param   scaleX  tỉ lệ thu phóng theo chiều X
     * @param   scaleY  tỉ lệ thu phóng theo chiều Y
     */
    public void scale(int[] indexShapesSelected, Point2D root, double scaleX, double scaleY) {
        listShapesSelected.clear();
        for (int i : indexShapesSelected) listShapesSelected.add(i);

        if (indexShapesSelected.length > 0) {
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

            if (isShowMotions) return;

            // TODO: Them ve diem
            super.mouseClicked(e);

            // Set màu cho điểm vẽ là màu đang chọn
//            Point2D point = Point2D.fromComputerCoordinate(e.getX() / DrawCanvas.pixelSize, e.getY() / DrawCanvas.pixelSize);
//            point.setColor(DrawCanvas.currentColor);
//
////            if (geometry instanceof SinglePoint) {
////                geometry.setStartPoint(point);
////                geometry.setupDraw();
////
////                mouseReleased(e);
////            }
//
//            if (mode == Mode.FILL_COLOR) {
//                fillColor(point);
//                saveStates();
//            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isShowMotions) return;

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

    /*
     * Sau khi các hình vẽ thực hiện cấc phép biến đổi như tịnh tiến, quay,..
     * Thì sẽ hợp nhất nó với bảng vẽ (cố định hình vẽ)
     */
    public void applyShapesChange() {
        setMode(Mode.NONE);
        startMove = null;
        endMove = null;

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j] != tempBoard[i][j]) {
                    board[i][j] = tempBoard[i][j];
                }
            }
        }

        for (int index : mapNewPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] points = g.getPoints();
            for (int i = 0; i < points.length; i++) {
                points[i] = mapNewPoints.get(index).get(i);
            }
            g.setPoints(points);

            listShapes.set(index, g);

            if (isShowPointCoord) g.showPointsCoordinate();
        }

        mapPoints.clear();
        mapNewPoints.clear();

        if (isShowAxis) drawAxis();

        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        listener.notifyDeselectedAllItems();

        saveStates();
    }


    Point2D rootPoint = null, rootPoint2 = null;
    Point2D startMove = null, endMove = null;
    List<Integer> listShapesSelected = new ArrayList<>();

    HashMap<Integer, ArrayList<Point2D>> mapPoints = new HashMap<>();       // Tạo độ các điểm ban đầu của hình vẽ
    HashMap<Integer, ArrayList<Point2D>> mapNewPoints = new HashMap<>();    // Tạo độ các điểm mới, khi đang áp dụng các phép biến đổi của hình vẽ

    public class MyMouseMotionAdapter extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isShowMotions) return;

            // Nhấn giữ chuột và kéo

            super.mouseDragged(e);
            if (e.getX() >= canvasWidth || e.getY() >= canvasHeight || e.getX() <= 0 || e.getY() <= 0) return;

            Point2D point = Point2D.fromComputerCoordinate(e.getX() / DrawCanvas.pixelSize, e.getY() / DrawCanvas.pixelSize);
            geometry.setColor(DrawCanvas.currentColor);
            geometry.setColorFill(DrawCanvas.currentFillColor);

            listener.mouseCoordinate(point.getX(), point.getY());

            if (mode == Mode.NONE || mode == Mode.FILL_COLOR) return;
            if (geometry instanceof SinglePoint) return;


            // --------- Move ---------
            if (mode == Mode.MOVE && listShapesSelected.size() > 0) {

                moveShapes(point);

            } else if (mode == Mode.ROTATE && listShapesSelected.size() > 0) {

                rotateShapes(point);

            } else if (mode == Mode.REFLECT && listShapesSelected.size() > 0) {


            } else {

                if (geometry.getStartPoint() == null) {
                    geometry.setStartPoint(point);
                } else {
                    geometry.setEndPoint(point);
                    geometry.draw(); // Xem trước nét vẽ
                }
            }


        }


        @Override
        public void mouseMoved(MouseEvent e) {
            if (isShowMotions) return;

            // Chuột di chuyển bình thưởng, KHÔNG nhấn giữ

            super.mouseMoved(e);

            // Sử dụng kĩ thuật Callback, cập nhật tọa độ hiển thị lên JLable ở góc dưới màn hình
            Point2D point = Point2D.fromComputerCoordinate(e.getX() / DrawCanvas.pixelSize, e.getY() / DrawCanvas.pixelSize);
            listener.mouseCoordinate(point.getX(), point.getY());
        }

    }

    /*
     * Xử lý thu phóng hình vẽ
     */
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

            g.draw();
            listener.notifyShapeChanged(index, g.toString());
        }

        applyShapesChange();
    }

    /*
     * Xử lý đối xứng hình vẽ
     * @param   isReflectByPoint   true: đối xứng qua điểm
     *                              false: đối xứng qua đoạn thẳng
     */
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


            g.draw();
            listener.notifyShapeChanged(index, g.toString());
        }

        applyShapesChange();
    }

    /*
     * Chuyển các hình vẽ được chọn thuộc `listShapesSelected` sang chế độ preview - tức là nó không còn cố định trên màn hình vẽ nữa
     * Mà có thể áp dụng các phép dịch chuyển
     */
    public void convertShapeToPreview() {
        if (mapPoints.size() == 0) {
            mapNewPoints.clear();

            for (int index : listShapesSelected) {
                Geometry g = (Geometry) listShapes.get(index);

                g.clearPointsCoordinate();

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
                for (int j : listShapesSelected) {
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

    /*
     * Di chuyển các hình vẽ
     * @param   point   vị trí con chuột hiện tại
     */
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

            g.draw();
            listener.notifyShapeChanged(index, g.toString());
        }


    }

    /*
     * Hiển thị tọa độ các điểm thuộc mỗi hình vẽ
     */
    private void showFixedShapesCoordinate() {
        for (int i = 0; i < listShapes.size(); i++) {
            boolean inSelectedIndex = false;
            for (int index : listShapesSelected) {
                if (index == i) {
                    inSelectedIndex = true;
                    break;
                }
            }
            if (!inSelectedIndex) {
                Geometry geometry = (Geometry) listShapes.get(i);
                geometry.showPointsCoordinate();
            }
        }
    }

    /*
     * Quay hình các hình vẽ
     * @param   point   vị trí con chuột hiện tại
     */
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

        if (Double.isNaN(angle)) return;

        int cur = pointLine(startMove, rootPoint, point);

        if (cur > 0) {
            angle = -angle;
        }

        for (int index : mapPoints.keySet()) {
            Geometry g = (Geometry) listShapes.get(index);

            Point2D[] points = g.getPoints();

            if (g instanceof Circle) {
                // hình tròn chỉ xoay tâm
                // Bán kính giữ nguyên để tránh sai số
                points[0] = mapPoints.get(index).get(0).rotate(rootPoint, angle);
                points[1] = new Point2D(points[0].getX() + ((Circle) g).getRadius(), points[0].getY());

                mapNewPoints.get(index).set(0, new Point2D(points[0]));
                mapNewPoints.get(index).set(1, new Point2D(points[1]));
            } else {
                for (int i = 0; i < points.length; i++) {

                    points[i] = mapPoints.get(index).get(i).rotate(rootPoint, angle);

                    mapNewPoints.get(index).set(i, new Point2D(points[i]));
                }
            }

            g.setPoints(points);
            g.draw();

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


    /*
     * Tính năng copy các hình vẽ có sẵn
     */
    public void copyShapes(int[] indexCopy) {
        for (int index : indexCopy) {
            // Tạo bản sao
            Geometry g = ((Geometry) listShapes.get(index)).copy();

            listShapes.add(g);
            g.draw();

            listener.notifyShapeInserted(g.toString());
        }
        saveStates();
    }

    /*
     * Vẽ hình hộp chữ nhật trong 3D
     */
    public void drawRectangular(Point3D root, int length, int width, int height) {
        geometry = new Rectangular(this);
        ((Rectangular) geometry).set(root, length, width, height);
        geometry.draw();
        merge();
    }

    /*
     * Vẽ hình trụ trong 3D
     */
    public void drawCylinder(Point3D root, int a, int b, int h) {
        geometry = new Cylinder(this);
        ((Cylinder) geometry).set(root, a, b, h);
        geometry.draw();
        merge();
    }

    /*
     * Vẽ hình nón trong 3D
     */
    public void drawCone(Point3D root, int a, int b, int h) {
        geometry = new Cone(this);
        ((Cone) geometry).set(root, a, b, h);
        geometry.draw();
        merge();
    }

    /*
     * Vẽ hình đa giác trong 2D
     */
    public void drawPolygon(Point2D[] points) {
        geometry = new Polygon(this);
        geometry.setPoints(points);
        geometry.draw();
        merge();
    }
}
