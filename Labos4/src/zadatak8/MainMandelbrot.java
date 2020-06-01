package zadatak8;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainMandelbrot {

    public static void main(String[] args) {
        int height = 600;
        int width = 600;
        Mandelbrot mandelbrot = new Mandelbrot();
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            boolean flag = false;
            System.out.println("Press 'm' for Mandelbrot or 'j' for Julia... ");
            if(br.readLine().trim().equalsIgnoreCase("m"))flag = true;

            System.out.println("Enter eps value: ");
            Integer eps = Integer.valueOf(br.readLine().trim());
            System.out.println("Enter maximum number of iterations: ");
            Integer max = Integer.valueOf(br.readLine().trim());
            System.out.println("Enter u_min: ");
            Double umin = Double.valueOf(br.readLine().trim());
            System.out.println("Enter u_max: ");
            Double umax = Double.valueOf(br.readLine().trim());
            System.out.println("Enter v_min: ");
            Double vmin = Double.valueOf(br.readLine().trim());
            System.out.println("Enter v_max: ");
            Double vmax = Double.valueOf(br.readLine().trim());
            Double cRe = null;
            Double cIm = null;
            if(flag == false) {
                System.out.println("Enter cRe: ");
                cRe = Double.valueOf(br.readLine().trim());
                System.out.println("Enter cIm: ");
                cIm = Double.valueOf(br.readLine().trim());
            }

            mandelbrot = new Mandelbrot(eps, max, umin, umax, vmin, vmax , width, height, flag, cRe, cIm);

            br.close();
        }catch(IOException ex) {

        }

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.setSize(width, height);
        glcanvas.addGLEventListener(mandelbrot);
        final JFrame frame = new JFrame("Mandelbrot");
        frame.getContentPane().add(glcanvas, BorderLayout.CENTER);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing window");
                e.getWindow().dispose();
                System.exit(0);
            }
        });

        glcanvas.requestFocusInWindow();


    }
}
