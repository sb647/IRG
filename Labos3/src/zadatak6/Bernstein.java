package zadatak6;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.util.ArrayList;
import java.util.List;

public class Bernstein implements GLEventListener {
    private List<Point> points = new ArrayList<>();
    private List<Point> curvePoints = new ArrayList<>();
    private List<Integer> factorials = new ArrayList<>();
    private int n;

    public Bernstein(List<Point> pointList) {
        this.points = pointList;
        this.n = pointList.size() -1;
        calculateFactorials();
        calculateBezier();
    }

    private void calculateFactorials() {
        factorials.add(1);
        factorials.add(1);
        int factor = 1;
        for(int i=2; i <= n; i++) {
            factor = factor * i;
            factorials.add(factor);
        }
    }


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl2.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
        gl2.glLoadIdentity();

        gl2.glEnable(GL.GL_DEPTH_TEST);

        gl2.glTranslated(400, 200, 0);
        gl2.glScaled(70, 70, 70);

        for (int i = 0; i < points.size() - 1; i++) {
            draw(gl2, points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
        }
        drawBezierCurve(gl2);

    }

    private void drawBezierCurve(GL2 gl2) {
        gl2.glPointSize(1.0f);
        gl2.glColor3f(0.0f, 0.0f, 255.0f);
        gl2.glBegin(GL.GL_POINTS);

        for(Point p : this.curvePoints) {
            gl2.glVertex2d(p.getX(), p.getY());
        }
        gl2.glEnd();
    }

    private void calculateBezier () {
        for (double t = 0; t <= 1; t += 0.01) {
            double x = 0;
            double y = 0;
            double z = 0;
            double b = 0;
            for (int i = 0; i <= n; i++) {
                b = ((double) factorials.get(n) / (double) (factorials.get(i) * factorials.get(n - i))) * Math.pow(t, i) * Math.pow(1 - t, n - i);
                x += b * points.get(i).getX();
                y += b * points.get(i).getY();
                z += b * points.get(i).getZ();
            }
            Point p = new Point(x, y, z);
            this.curvePoints.add(p);
        }
    }

    public void draw(GL2 gl2, double x1, double y1, double x2, double y2) {
        gl2.glPointSize(1.0f);
        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        gl2.glBegin(GL.GL_LINES);
        gl2.glVertex2d(x1, y1);
        gl2.glVertex2d(x2, y2);
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int i, int i1, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, width , 0, height, 0, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public List<Point> getCurvePoints() {
        return curvePoints;
    }


}
