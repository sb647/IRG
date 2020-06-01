package irg.lab1.zad1;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU.*;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements GLEventListener {

    private List<Point> points = new ArrayList<>();
    private int xmin;
    private int xmax=0;
    private int ymin;
    private int ymax=0;
    private int height;
    private int width;
    private List<Integer> a = new ArrayList<>();
    private List<Integer> b = new ArrayList<>();
    private List<Integer> c = new ArrayList<>();


    public Polygon(int width, int height) {
        this.width = width;
        this.height = height;

    }

    public void setXmin(int xmin) {
        this.xmin = xmin;
    }

    public void setYmin(int ymin) {
        this.ymin = ymin;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setCoefs() {
        for (int i = 0; i < (points.size()-1) ; i++ ){
            a.add(this.points.get(i).getY() - this.points.get(i + 1).getY());
            b.add(-this.points.get(i).getX() + this.points.get(i + 1).getX());
            c.add(this.points.get(i).getX() * this.points.get(i + 1).getY() - this.points.get(i + 1).getX() * this.points.get(i).getY());
        }
    }

    public void setMinMax() {
        for(Point p : points) {
            if(p.getX() < xmin) xmin = p.getX();
            if(p.getX()> xmax) xmax = p.getX();
            if(p.getY() < ymin) ymin = p.getY();
            if(p.getY() > ymax) ymax = p.getY();
        }
    }

    public void checkIfPolygonContainsPoint(Point p){
       boolean flag = true;
       for(int i = 0; i < a.size(); i++) {
           if((this.a.get(i) * p.getX() + this.b.get(i) * p.getY() + c.get(i)) > 0) {
               flag = false;
               break;
           }
       }

       System.out.println(flag == true  ? "Point lies inside of a polygon." : "Point lies outside of a polygon");
    }
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(1, 1, 1, 1);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        for (int i = 0; i < points.size() - 1; i++) {
            draw(gl2, points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY());
        }

        for (int y = ymin; y <= ymax; y++) {
            int L = xmin;
            int D = xmax;

            for (int i = 0; i < a.size(); i++) {
           //vodoravan brid, y koordinate jednake
                if (a.get(i) == 0) {

                    if (points.get(i).getY() == y) {
                        if (points.get(i).getX() < points.get(i + 1).getX()) {
                            L = points.get(i).getX();
                            D = points.get(i + 1).getX();
                        } else {
                            L = points.get(i + 1).getX();
                            D = points.get(i).getX();
                        }
                        break;
                    }
                    //regularan brid
                } else {

                    final int x1 = (-b.get(i) * y - c.get(i)) / a.get(i);
                    if (points.get(i).getY() < points.get(i + 1).getY()) { //lijevi brid
                        if (x1 > L) {
                            L = x1;
                        }
                    } else {  //desni
                        if (x1 < D) {
                            D = x1;
                        }
                    }
                }

            }
            if (L < D) {
                draw(gl2, L, y, D, y);
            }
        }

    }

    public void draw(GL2 gl2, int x1, int y1, int x2, int y2) {
        gl2.glPointSize(1.0f);
        gl2.glColor3f(0.0f, 0.0f, 1.0f);

        gl2.glBegin(GL.GL_LINES);
        gl2.glVertex2i(x1, y1);
        gl2.glVertex2i(x2, y2);
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        this.height = height;
        this.width = width;
        if (height <= 0)  height = 1;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, width , 0, height, 0, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glDisable(GL.GL_DEPTH_TEST);
    }
}
