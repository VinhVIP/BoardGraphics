package com.demo.shape;

import com.demo.DrawCanvas;
import com.demo.DrawMode;
import com.demo.models.Point2D;
import com.demo.models.Vector2D;

/**
 * Create by VinhIT
 * On 25/03/2021
 */

public class Line extends Geometry {

    private int totalPoints = 2;

    public Line(DrawCanvas canvas, Point2D startPoint, Point2D endPoint, int color, DrawMode drawMode) {
        super(canvas, startPoint, endPoint, color, drawMode);
        initSizePoints(totalPoints);
        points[0] = startPoint;
        points[1] = endPoint;
    }

    public Line(DrawCanvas canvas, int color, DrawMode drawMode) {
        super(canvas, color, drawMode);
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
    public void setupDraw() {
        processDraw();
        clearOldPoints();
        drawNewPoints();
    }

    @Override
    public Geometry copy() {
        Line g = new Line(canvas, new Point2D(startPoint), new Point2D(endPoint), color, drawMode);

        for (int i = 0; i < totalPoints; i++)
            g.points[i] = new Point2D(points[i]);

        for (Point2D p : listDraw) {
            g.listDraw.add(new Point2D(p));
        }

        return g;
    }

    @Override
    public void processDraw() {
        if (startPoint != null && endPoint != null) {
            swapList();
            drawLine();
        }
    }

    public void drawLine() {
        lineBresenham(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY());

        if (drawMode == DrawMode.ARROW) {
            Point2D pA = points[0].translate(-points[0].getX(), -points[0].getY());
            Point2D pB = points[1].translate(-points[0].getX(), -points[0].getY());

            Vector2D vAB = new Vector2D(pA, pB);
            double angle = vAB.angleRadian(Vector2D.oX);

            Point2D pC = pB.rotate(pB.getY() < pA.getY() ? angle : -angle);

            Point2D pU = new Point2D(pC.getX() - 4, pC.getY() + 4);
            Point2D pV = new Point2D(pC.getX() - 4, pC.getY() - 4);

            pU = pU.rotate(pB.getY() < pA.getY() ? -angle : angle);
            pV = pV.rotate(pB.getY() < pA.getY() ? -angle : angle);

            pU = pU.translate(points[0].getX(), points[0].getY());
            pV = pV.translate(points[0].getX(), points[0].getY());

            lineBresenham(points[1].getX(), points[1].getY(), pU.getX(), pU.getY());
            lineBresenham(points[1].getX(), points[1].getY(), pV.getX(), pV.getY());
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

    @Override
    public Point2D getCenterPoint() {
        return new Point2D((points[0].getX() + points[1].getX()) / 2, (points[0].getY() + points[1].getY()) / 2, points[0].getColor());
    }
}
