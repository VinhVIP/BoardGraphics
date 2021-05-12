package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Vector2D;

import java.util.List;

/**
 * Create by VinhIT
 * On 25/03/2021
 */

public class Line extends Geometry {

    private int totalPoints = 2;

    public Line(DrawCanvas canvas, Point2D startPoint2D, Point2D endPoint2D) {
        super(canvas, startPoint2D, endPoint2D);
        initSizePoints(totalPoints);
    }

    public Line(DrawCanvas canvas) {
        super(canvas);
        initSizePoints(totalPoints);
    }

    public Line(DrawCanvas canvas, DrawMode drawMode) {
        super(canvas, drawMode);
        initSizePoints(totalPoints);
    }

    @Override
    public Geometry copy() {
        Line g = new Line(canvas);
        g.setStartPoint(new Point2D(startPoint));
        g.setEndPoint(new Point2D(endPoint));
        g.setDrawMode(drawMode);

        g.points = new Point2D[totalPoints];
        for(int i=0; i<totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    @Override
    public void setupDraw() {
        if (startPoint != null && endPoint != null) {
            // Đổ listDraw cho listClear
            swapList();

            // Xác định những điểm thuộc đường thẳng MỚI cần vẽ
            // Những điểm cần vẽ sẽ được thêm vào listDraw
            drawLine();

            // Xóa những điểm thuộc listClear
            clearOldPoints();

            // Vẽ những điểm thuộc listDraw
            drawNewPoints();

            showPointsCoordinate();
        }
    }


    /*
     * Hiển thị tọa độ 2 điểm start và end
     */
    @Override
    public void showPointsCoordinate() {
//        canvas.drawPointsCoordinate(startPoint);
//        canvas.drawPointsCoordinate(endPoint);
//        Graphics g = canvas.getGraphics();
//        g.setColor(Color.BLACK);
//
//        if (startPoint != null)
//            g.drawString(String.format("(%d, %d)", startPoint.getX(), startPoint.getY()), startPoint.getComputerX() * 5 + 5, startPoint.getComputerY() * 5 - 5);
//        if (endPoint != null)
//            g.drawString(String.format("(%d, %d)", endPoint.getX(), endPoint.getY()), endPoint.getComputerX() * 5 + 5, endPoint.getComputerY() * 5 - 5);

//        g.dispose();
    }

    public void drawLine() {
        lineBresenham(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());

        if (drawMode == DrawMode.ARROW) {
            Point2D pA = startPoint.translate(-startPoint.getX(), -startPoint.getY());
            Point2D pB = endPoint.translate(-startPoint.getX(), -startPoint.getY());

            Vector2D vAB = new Vector2D(pA, pB);
            double angle = vAB.angleRadian(Vector2D.oX);

            Point2D pC = pB.rotate(pB.getY() < pA.getY() ? angle : -angle);

            Point2D pU = new Point2D(pC.getX() - 4, pC.getY() + 4);
            Point2D pV = new Point2D(pC.getX() - 4, pC.getY() - 4);

            pU = pU.rotate(pB.getY() < pA.getY() ? -angle : angle);
            pV = pV.rotate(pB.getY() < pA.getY() ? -angle : angle);

            pU = pU.translate(startPoint.getX(), startPoint.getY());
            pV = pV.translate(startPoint.getX(), startPoint.getY());

            lineBresenham(endPoint.getX(), endPoint.getY(), pU.getX(), pU.getY());
            lineBresenham(endPoint.getX(), endPoint.getY(), pV.getX(), pV.getY());
        }
    }

    private void lineBresenham(int x1, int y1, int x2, int y2) {
        int x, y, Dx, Dy, p;
        Dx = Math.abs(x2 - x1);
        Dy = Math.abs(y2 - y1);
        x = x1;
        y = y1;

        int xUnit = 1, yUnit = 1;

        if (x2 - x1 < 0)
            xUnit = -xUnit;
        if (y2 - y1 < 0)
            yUnit = -yUnit;

        Point2D pt = new Point2D(x, y, color);
        // listDraw là List chứa danh sách những điểm sẽ vẽ lên màn hình
        listDraw.add(pt);

        // biến này đếm vị trí của điểm vẽ để xét xem nó có được hiển thị cho phù hợp với nét vẽ hay không
        int cnt = 1;

        if (Dx >= Dy) {
            // Trường hợp này ta chạy vòng lặp while theo biến x

            p = 2 * Dy - Dx;

            while (x != x2) {
                if (p < 0) p += 2 * Dy;
                else {
                    p += 2 * (Dy - Dx);
                    y += yUnit;
                }
                x += xUnit;

                // Kiểm tra xem điểm đó được hiển thị hay không, nếu được thì thêm vào list
                if (isShowPoint(cnt)) {
                    pt = new Point2D(x, y, color);
                    listDraw.add(pt);
                }
                // Tăng giá trị biến đếm lên
                cnt++;

            }
        } else {
            // Trường hợp này ta chạy vòng lặp while theo biến y

            p = 2 * Dx - Dy;

            while (y != y2) {
                if (p < 0) p += 2 * Dx;
                else {
                    p += 2 * (Dx - Dy);
                    x += xUnit;
                }
                y += yUnit;

                if (isShowPoint(cnt)) {
                    pt = new Point2D(x, y, color);
                    listDraw.add(pt);
                }
                cnt++;

            }
        }

    }


    @Override
    public String toString() {
        try {
            return String.format("Line: (%d, %d) -> (%d, %d)", startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void setPoints(Point2D[] points) {
        super.setPoints(points);
        startPoint = points[0];
        endPoint = points[1];
    }

    @Override
    public void setStartPoint(Point2D startPoint) {
        super.setStartPoint(startPoint);
        points[0] = startPoint;
    }

    @Override
    public void setEndPoint(Point2D endPoint) {
        super.setEndPoint(endPoint);
        points[1] = endPoint;
    }
}
