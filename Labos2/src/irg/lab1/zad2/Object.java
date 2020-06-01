package irg.lab1.zad2;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.util.ArrayList;
import java.util.List;

public class Object implements GLEventListener {

    private List<Coefficients> coeffs;
    private List<ArrayList<PointDouble>> vertices;
    private List<PointDouble> points;

    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    private double zmin;
    private double zmax;

    public Object(List<ArrayList<PointDouble>> list, List<PointDouble> points)  {
        this.coeffs = new ArrayList<>();
        this.vertices = list;
        this.points = points;
        setCoeffs(list);
        setMinMax();
    }

    private void setCoeffs (List<ArrayList<PointDouble>> list){
        for (List<PointDouble> l : list) {
            PointDouble p1 = l.get(0);
            PointDouble p2 = l.get(1);
            PointDouble p3 = l.get(2);

            double A = (p2.getY() - p1.getY()) * (p3.getZ() - p1.getZ()) - (p2.getZ() - p1.getZ()) * (p3.getY() - p1.getY());
            double B = -(p2.getX() - p1.getX()) * (p3.getZ() - p1.getZ()) + (p2.getZ() - p1.getZ()) * (p3.getX() - p1.getX());
            double C = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
            double D = -p1.getX() * A - p1.getY() * B - p1.getZ() * C;

            Coefficients c = new Coefficients(A, B, C, D);
            this.coeffs.add(c);

        }
    }

    private void setMinMax () {
        PointDouble p = points.get(0);
        xmin = p.getX(); xmax = p.getX();
        ymin = p.getY(); ymax = p.getY();
        zmin = p.getZ(); zmax = p.getZ();

        for(int i= 1; i< points.size(); i++) {
            double x = points.get(i).getX();
            double y = points.get(i).getY();
            double z = points.get(i).getZ();

            if(x < xmin) xmin = x;
            if(x > xmax) xmax = x;
            if(y < ymin) ymin = y;
            if(y > ymax) ymax = y;
            if(z < zmin) zmin = z;
            if(z > zmax) zmax = z;
        }

    }

    public void checkIfObjectContainsDot(PointDouble p) {
        boolean flag = true;
        for (Coefficients c : coeffs) {
            if((c.getA() * p.getX() + c.getB() * p.getY() + c.getC() * p.getZ() + c.getD()) > 0) {
                flag = false;
                break;
            }
        }

        System.out.println(flag == true  ? "Point lies inside the boundary of an object." : "Point lies outside the boundary of an object.");
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
        gl2.glTranslated(100, 100, 0);
        gl2.glScaled(200, 200, 1);

        for (int i = 0; i < this.vertices.size(); i++) {

            ArrayList<PointDouble> v = this.vertices.get(i);

            PointDouble v1 = v.get(0);
            PointDouble v2 = v.get(1);
            PointDouble v3 = v.get(2);

            draw(gl2, v1.getX(), v1.getY(), v2.getX(), v2.getY());
            draw(gl2, v1.getX(), v1.getY(), v3.getX(), v3.getY());
            draw(gl2, v2.getX(), v2.getY(), v3.getX(), v3.getY());

        }
        //gl2.glTranslated(300,300,0);
        //gl2.glScaled(200,200,0);

    }
    public void draw(GL2 gl2, double x1, double y1, double x2, double y2) {
        gl2.glPointSize(1.0f);
        gl2.glColor3f(0.0f, 0.0f, 1.0f);
        gl2.glBegin(GL.GL_LINES);
        gl2.glVertex2d(x1, y1);
        gl2.glVertex2d(x2, y2);
        gl2.glEnd();
        gl2.glFlush();
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int i, int i1, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0)  height = 1;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, width, height, 0,0, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glDisable(GL.GL_DEPTH_TEST);
    }

}
