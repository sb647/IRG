package zadatak5;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

public class Transformation implements GLEventListener {
    private Point viewPoint;
    private Point pointOfView;
    private List<Point> vertices = new ArrayList<>();
    private List<ArrayList<Point>> polygons = new ArrayList<>();
    private List<Point> originalVertices = new ArrayList<>();
    private List<List<Integer>> order = new ArrayList<>();

    public Transformation(Point viewPoint, Point pointOfView, List<Point> vertices, List<ArrayList<Point>> polygons, List<List<Integer>> order)

    {
        this.viewPoint = viewPoint;
        this.pointOfView = pointOfView;
        this.vertices = vertices;
        setOriginalVertices();
        this.polygons = polygons;
        this.order = order;
        transformAndProject();
    }

    private void setOriginalVertices() {
        for(Point v : vertices) {
            this.originalVertices.add(v.copy());
        }
    }

    private RealMatrix calculateTransformationMatrix () {
        //glediste u ishodiste ref. sustava
        double [][] matrix1 = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {-viewPoint.getX(), -viewPoint.getY(), -viewPoint.getZ(), 1}
        };
        RealMatrix T1 = new Array2DRowRealMatrix(matrix1);

        //rotacija za kut A u xy ravnini, dovucemo z' u xz ravninu
        double xg1 = pointOfView.getX() - viewPoint.getX();
        double yg1 = pointOfView.getY() - viewPoint.getY();
        double sina = yg1/ Math.sqrt(xg1* xg1 + yg1 * yg1);
        double cosa = xg1/ Math.sqrt(xg1* xg1 + yg1 * yg1);
        double [][] matrix2 = {
                {cosa, -sina, 0, 0},
                {sina, cosa, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        RealMatrix T2 = new Array2DRowRealMatrix(matrix2);

        //Rotacija za kut B u xz ravnini tako da poklopimo z osi
        double xg2 = Math.sqrt(xg1*xg1 + yg1*yg1);
        double zg2 = pointOfView.getZ() - viewPoint.getZ();
        double sinB = xg2 /Math.sqrt(xg2* xg2 + zg2 * zg2);
        double cosB = zg2/ Math.sqrt(xg2* xg2 + zg2 * zg2);

        double [][] matrix3 = {
                {cosB, 0, sinB, 0},
                {0, 1, 0, 0},
                {-sinB, 0, cosB, 0},
                {0, 0, 0, 1}
        };
        RealMatrix T3 = new Array2DRowRealMatrix(matrix3);

        //rotacija oko z za 90° tako da se poklope y osi
        double [][] matrix4 = {
                {0,-1, 0, 0},
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        RealMatrix T4 = new Array2DRowRealMatrix(matrix4);
        //x i x' su kolinearni, ali suprotni pa mnozimo x sa -1
        double [][] matrix5 = {
                {-1,0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        RealMatrix T5 = new Array2DRowRealMatrix(matrix5);

        RealMatrix matrix = T1.multiply(T2).multiply(T3).multiply(T4).multiply(T5);
        return matrix;
    }

    private RealMatrix calculateProjectionMatrix() {
        double x = viewPoint.getX() - pointOfView.getX();
        double y = viewPoint.getY() - pointOfView.getY();
        double z = viewPoint.getZ() - pointOfView.getZ();
        double h = 1 / Math.sqrt(x*x + y*y + z*z);

        double [][] matrix = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, h},
                {0, 0, 0, 0}
        };
        RealMatrix P = new Array2DRowRealMatrix(matrix);
        return P;
    }

    public void transformAndProject () {
        RealMatrix T = calculateTransformationMatrix();
        RealMatrix P = calculateProjectionMatrix();

        for(Point v : vertices) {
            double [][] matrix = {{v.getX(), v.getY(), v.getZ(), 1}};
            RealMatrix M1 = new Array2DRowRealMatrix(matrix);
            RealMatrix M2 = M1.multiply(T);
            RealMatrix M3 = M2.multiply(P);

            double h = M3.getEntry(0,3);
            if(h != 1 && h != 0) {
                double m = 1/h;
               M3 = M3.scalarMultiply(m);
            }

            v.setX(M3.getEntry(0,0));
            v.setY(M3.getEntry(0,1));
            v.setZ(M3.getEntry(0,2));
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
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl2.glTranslated(400, 200, 0);
        gl2.glScaled(10, 10, 10);
        for (int i = 0; i < this.polygons.size(); i++) {

            ArrayList<Point> v = this.polygons.get(i);

            Point v1 = v.get(0);
            Point v2 = v.get(1);
            Point v3 = v.get(2);

            draw(gl2, v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
            draw(gl2, v1.getX(), v1.getY(), v1.getZ(), v3.getX(), v3.getY(), v2.getZ());
            draw(gl2, v2.getX(), v2.getY(), v1.getZ(), v3.getX(), v3.getY(), v3.getZ());

        }
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

    public void draw(GL2 gl2, double x1, double y1, double z1, double x2, double y2, double z2) {
        gl2.glPointSize(1.0f);
        gl2.glColor3f(0.0f, 0.0f, 1.0f);
        gl2.glBegin(GL.GL_LINES);
        gl2.glVertex3d(x1, y1, z1);
        gl2.glVertex3d(x2, y2, z2);
        gl2.glEnd();
    }

    public void setViewPoint(Point viewPoint) {
        this.viewPoint = viewPoint;
        this.vertices.clear();
        this.polygons.clear();
        for(Point o : originalVertices) {
            this.vertices.add(o.copy());
        }

        for(List<Integer> o : order) {
            ArrayList<Point> l = new ArrayList<>();
            l.add(vertices.get(o.get(0)-1));
            l.add(vertices.get(o.get(1)-1));
            l.add(vertices.get(o.get(2)-1));
            polygons.add(l);
        }

        transformAndProject();
    }

    public void setPointOfView(Point pointOfView) {
        this.pointOfView = pointOfView;
        this.vertices.clear();
        this.polygons.clear();
        for(Point o : originalVertices) {
            this.vertices.add(o.copy());
        }

        for(List<Integer> o : order) {
            ArrayList<Point> l = new ArrayList<>();
            l.add(vertices.get(o.get(0)-1));
            l.add(vertices.get(o.get(1)-1));
            l.add(vertices.get(o.get(2)-1));
            polygons.add(l);
        }

        transformAndProject();


    }
}
