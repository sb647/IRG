package zadatak7;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.ArrayList;
import java.util.List;

public class Transformation implements GLEventListener {
    private Point viewPoint;
    private Point pointOfView;
    private Point src;
    private List<Point> vertices = new ArrayList<>();
    private List<ArrayList<Point>> polygons = new ArrayList<>();
    private List<Point> originalVertices = new ArrayList<>();
    private List<ArrayList<Point>> visiblePolygons = new ArrayList<>();
    private boolean flag;


    public Transformation(Point viewPoint, Point pointOfView, List<Point> vertices, List<ArrayList<Point>> polygons, Point src, boolean flag) {
        this.viewPoint = viewPoint;
        this.pointOfView = pointOfView;
        this.vertices = vertices;
        this.src = src;
        setOriginalVertices();
        this.polygons = polygons;
        this.flag = flag;
        scale();
        transformAndProject();
    }

    private void scale() {

        Point p = vertices.get(0);
        double xmin = p.getX();
        double xmax = p.getX();
        double ymin = p.getY();
        double ymax = p.getY();
        double zmin = p.getZ();
        double zmax = p.getZ();

        for(int i= 1; i < vertices.size(); i++) {
            double x = vertices.get(i).getX();
            double y = vertices.get(i).getY();
            double z = vertices.get(i).getZ();

            if(x < xmin) xmin = x;
            if(x > xmax) xmax = x;
            if(y < ymin) ymin = y;
            if(y > ymax) ymax = y;
            if(z < zmin) zmin = z;
            if(z > zmax) zmax = z;
        }

        double x = (xmin + xmax)/2.0;
        double y = (ymin + ymax)/2.0;
        double z = (zmin + zmax)/2.0;

        double s =  2.0 / Math.max(Math.max((xmax-xmin),(ymax-ymin)),(zmax-zmin));

        for(Point v : vertices) {
            v.setX((v.getX()-x) * s);
            v.setY((v.getY()-y) * s);
            v.setZ((v.getZ()-z) * s);
        }

    }


    private void setOriginalVertices() {
        for(Point v : vertices) {
            this.originalVertices.add(v.copy());
        }
    }

    private RealMatrix calculateTransformationMatrix () {
        double [][] matrix1 = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {-viewPoint.getX(), -viewPoint.getY(), -viewPoint.getZ(), 1}
        };
        RealMatrix T1 = new Array2DRowRealMatrix(matrix1);

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

        double [][] matrix4 = {
                {0,-1, 0, 0},
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        RealMatrix T4 = new Array2DRowRealMatrix(matrix4);

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
        double h = 1.0 / Math.sqrt(x*x + y*y + z*z);

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
            // double h = M2.getEntry(0,3);
           // if(h != 1 && h !=0 ) {
            //    double m = 1.0 / h;
             //   M2 = M2.scalarMultiply(m);
            //    M2.setEntry(0,3, 1);
           // }

            RealMatrix M3 = M2.multiply(P);
            double h = M3.getEntry(0,3);
            if(h != 1 && h !=0 ) {
                double m = 1.0/h;
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

        //GLU glu = GLU.createGLU();
        //gl2.glMatrixMode(GL2.GL_PROJECTION);
        //gl2.glLoadIdentity();
        //glu.gluPerspective(45.0, 1.0, 0.5, 8.0);
        //glu.gluLookAt (viewPoint.getX(), viewPoint.getY(), viewPoint.getZ(), 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        //gl2.glMatrixMode(GL2.GL_MODELVIEW);

        gl2.glTranslated(400, 400, 0);
        gl2.glScaled(50, 50, 50);

        if(flag == true) constantShading(gl2);
        else gouraudShading(gl2);


        for (int i = 0; i < this.visiblePolygons.size(); i++) {

            ArrayList<Point> v = this.visiblePolygons.get(i);

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
        gl.glOrtho(0, width, height, 0, 0, 1);
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


    public void constantShading(GL2 gl2) {
        for (ArrayList<Point> l : polygons) {
            Point p1 = l.get(0);
            Point p2 = l.get(1);
            Point p3 = l.get(2);

            double A = (p2.getY() - p1.getY()) * (p3.getZ() - p1.getZ()) - (p2.getZ() - p1.getZ()) * (p3.getY() - p1.getY());
            double B = -(p2.getX() - p1.getX()) * (p3.getZ() - p1.getZ()) + (p2.getZ() - p1.getZ()) * (p3.getX() - p1.getX());
            double C = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
            double D = -p1.getX() * A - p1.getY() * B - p1.getZ() * C;

            double tX = (p1.getX() + p2.getX() + p3.getX()) / 3;
            double tY = (p1.getY() + p2.getY() + p3.getY()) / 3;
            double tZ = (p1.getZ() + p2.getZ() + p3.getZ()) / 3;

            Vector3D n = new Vector3D(A,B,C).normalize();
           // Vector3D T = new Vector3D(viewPoint.getX()-tX, viewPoint.getY()-tY, viewPoint.getZ()-tZ ).normalize();

            if((A * viewPoint.getX() + B * viewPoint.getY() + C * viewPoint.getZ() + D) > 0) {
              this.visiblePolygons.add(l);
                Vector3D L = new Vector3D(src.getX()-tX, src.getY()-tY, src.getZ()-tZ).normalize();
                double Id = 100* 0.75;
                if((A * src.getX() + B * src.getY() + C * src.getZ() + D) > 0){
                    Id += 255 * 0.75 * n.dotProduct(L);
                }

                gl2.glColor3ub((byte)Id, (byte)(Id/2), (byte)Id);
                gl2.glBegin(GL.GL_TRIANGLES);
                gl2.glVertex3d(l.get(0).getX(), l.get(0).getY(), l.get(0).getZ());
                gl2.glVertex3d(l.get(1).getX(), l.get(1).getY(), l.get(1).getZ());
                gl2.glVertex3d(l.get(2).getX(), l.get(2).getY(), l.get(2).getZ());
                gl2.glEnd();
            }
        }
    }

    public void gouraudShading(GL2 gl2) {

        for(Point v : vertices ) {
            List<Vector3D> normals = new ArrayList<>();

            for(ArrayList<Point> l : polygons) {
                Point p1 = l.get(0);
                Point p2 = l.get(1);
                Point p3 = l.get(2);

                double A = (p2.getY() - p1.getY()) * (p3.getZ() - p1.getZ()) - (p2.getZ() - p1.getZ()) * (p3.getY() - p1.getY());
                double B = -(p2.getX() - p1.getX()) * (p3.getZ() - p1.getZ()) + (p2.getZ() - p1.getZ()) * (p3.getX() - p1.getX());
                double C = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
                double D = -p1.getX() * A - p1.getY() * B - p1.getZ() * C;

                if((A * viewPoint.getX() + B * viewPoint.getY() + C * viewPoint.getZ() + D) > 0) {
                    Vector3D n1 = calculateNormal(p1);
                    Vector3D L1 = new Vector3D(src.getX()-p1.getX(), src.getY()-p1.getY(), src.getZ()-p1.getZ()).normalize();
                    double I1 = 100* 0.75;
                    if((A * src.getX() + B * src.getY() + C * src.getZ() + D) > 0){
                        I1 += 255 * 0.75 * n1.dotProduct(L1);
                    }

                   // double I1 = 155 * 0.35 + 255* 0.75 * n1.dotProduct(L1);

                    Vector3D n2 = calculateNormal(p2);
                    Vector3D L2 = new Vector3D(src.getX()-p2.getX(), src.getY()-p2.getY(), src.getZ()-p2.getZ()).normalize();

                    double I2 = 100* 0.75;
                    if((A * src.getX() + B * src.getY() + C * src.getZ() + D) > 0){
                        I2 += 255 * 0.75 * n2.dotProduct(L2);
                    }
                    //double I2 = 155 * 0.35 + 255* 0.75 * n2.dotProduct(L2);

                    Vector3D n3 = calculateNormal(p3);
                    Vector3D L3 = new Vector3D(src.getX()-p3.getX(), src.getY()-p3.getY(), src.getZ()-p3.getZ()).normalize();
                    double I3 = 100* 0.75;
                    if((A * src.getX() + B * src.getY() + C * src.getZ() + D) > 0){
                        I3 += 255 * 0.75 * n3.dotProduct(L3);
                    }
                    //double I3 = 155 * 0.35 + 255* 0.75 * n3.dotProduct(L3);

                    gl2.glBegin(GL.GL_TRIANGLES);
                    gl2.glColor3ub((byte)I1, (byte)(I1/2), (byte)I1);
                    gl2.glVertex3d(l.get(0).getX(), l.get(0).getY(), l.get(0).getZ());
                    gl2.glColor3ub((byte)I2, (byte)(I2/2), (byte)I2);
                    gl2.glVertex3d(l.get(1).getX(), l.get(1).getY(), l.get(1).getZ());
                    gl2.glColor3ub((byte)I3, (byte)(I3/2), (byte)I3);
                    gl2.glVertex3d(l.get(2).getX(), l.get(2).getY(), l.get(2).getZ());
                    gl2.glEnd();

                }
            }
        }
    }

    private Vector3D calculateNormal (Point p) {
        List<Vector3D> normals = new ArrayList<>();
        for(ArrayList<Point> l : polygons) {
            if(l.contains(p)) {
                Point p1 = l.get(0);
                Point p2 = l.get(1);
                Point p3 = l.get(2);

                double A = (p2.getY() - p1.getY()) * (p3.getZ() - p1.getZ()) - (p2.getZ() - p1.getZ()) * (p3.getY() - p1.getY());
                double B = -(p2.getX() - p1.getX()) * (p3.getZ() - p1.getZ()) + (p2.getZ() - p1.getZ()) * (p3.getX() - p1.getX());
                double C = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
                double D = -p1.getX() * A - p1.getY() * B - p1.getZ() * C;
                Vector3D vec = new Vector3D(A,B,C);
                if (vec.getNorm() == 0){
                    normals.add(vec);
                    continue;
                }
                else normals.add(vec.normalize());
            }
        }
        double x = 0;
        double y = 0;
        double z = 0;

        for(Vector3D n : normals) {
            x += n.getX();
            y += n.getY();
            z += n.getZ();
        }
        x = x / normals.size();
        y = y / normals.size();
        z = z / normals.size();

        Vector3D result = new Vector3D(x,y,z);

        if(result.getNorm() == 0) return result;
        return result.normalize();

    }
}
