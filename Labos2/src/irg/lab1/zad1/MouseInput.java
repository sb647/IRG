package irg.lab1.zad1;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseInput extends MouseAdapter {

    private int n;
    private int counter;
    private int height;
    private int width;
    private List<Point> list ;
    private Polygon polygon;

    public MouseInput (int n, int h, int w, Polygon polygon) {
        this.n = n;
        this.height = h;
        this.width = w;
        counter = 0;
        list = new ArrayList<>();
        this.polygon = polygon;


    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if(counter < n) {
            Point point = new Point(e.getX(), height-e.getY(), 0);
            list.add(point);
            System.out.println(e.getX() + " , " + (height - e.getY()));

            if(++counter == n) {
                this.polygon.setPoints(list);
                this.polygon.setXmin(width);
                this.polygon.setYmin(height);
                this.polygon.setMinMax();
                this.polygon.setCoefs();


            }
        }else {
            System.out.println("Check if point lies in polygon:");
            System.out.println(e.getX() +" , " + (height-e.getY()));
            Point point = new Point(e.getX(), height-e.getY(), 0);
            this.polygon.checkIfPolygonContainsPoint(point);
        }

    }
}
