package zadatak8;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import org.apache.commons.math3.complex.Complex;

public class Mandelbrot implements GLEventListener {

    private Integer eps;
    private Integer m;
    private Double umin;
    private Double umax;
    private Double vmin;
    private Double vmax;
    private Integer xmax;
    private Integer ymax;
    private boolean flag;
    private Double cRe;
    private Double cIm;

    public Mandelbrot(Integer eps, Integer m, Double umin, Double umax, Double vmin, Double vmax, Integer xmax, Integer ymax, boolean flag, Double cRe, Double cIm) {
        this.eps = eps;
        this.m = m;
        this.umin = umin;
        this.umax = umax;
        this.vmin = vmin;
        this.vmax = vmax;
        this.xmax = xmax;
        this.ymax = ymax;
        this.flag = flag;
        this.cRe = cRe;
        this.cIm = cIm;
    }

    public Mandelbrot() {

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

        if (flag == true) mandelbrot(gl2);
        else julia(gl2, cRe, cIm);

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

    private void mandelbrot (GL2 gl2) {

        gl2.glBegin(GL2.GL_POINTS);
        for(int x = 0; x < xmax ; x++) {
            for(int y = 0; y < ymax; y++) {
                double u0 = (umax - umin) / xmax * x + umin;
                double v0 = (vmax - vmin) / ymax * y + vmin;

                int k = -1;
                double cRe = u0;
                double cIm = v0;
                Complex z0 = new Complex(0,0);

                double r;
                do {
                    k = k + 1;
                    double Re = z0.getReal() * z0.getReal() - z0.getImaginary() * z0.getImaginary() + cRe;
                    double Im = 2 * z0.getReal() * z0.getImaginary() + cIm;
                    z0 = new Complex(Re, Im);
                    r = Math.sqrt(Re * Re + Im * Im);

                } while( r < eps && k < m);

                if(k == -1)  gl2.glColor3d(0,0,0);
                else gl2.glColor3d(k/m, 1.0 - (double)k/m/2.0, 0.8-(double)k/m/3.0);
                gl2.glVertex2i(x, y);
            }
        }
        gl2.glEnd();
    }

    private void julia (GL2 gl2, double cRe, double cIm) {

        gl2.glBegin(GL2.GL_POINTS);
        for(int x = 0; x < xmax ; x++) {
            for(int y = 0; y < ymax; y++) {
                double u0 = (umax - umin) / xmax * x + umin;
                double v0 = (vmax - vmin) / ymax * y + vmin;

                int k = -1;
                Complex z0 = new Complex(u0,v0);

                double r;
                do {
                    k = k + 1;
                    double Re = z0.getReal() * z0.getReal() - z0.getImaginary() * z0.getImaginary() + cRe;
                    double Im = 2 * z0.getReal() * z0.getImaginary() + cIm;
                    z0 = new Complex(Re, Im);
                    r = Math.sqrt(Re * Re + Im * Im);

                } while( r < eps && k < m);

                if(k == -1)  gl2.glColor3d(0,0,0);
                else gl2.glColor3d(k/m, 1.0 - (double)k/m/2.0, 0.8-(double)k/m/3.0);
                gl2.glVertex2i(x, y);
            }
        }
        gl2.glEnd();

    }
}
