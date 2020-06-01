package zadatak7;

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
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Point p = new Point();
        boolean flag = false;
        try{

            System.out.println("Enter 'c' for constant shading or 'g' for gouraud shading");
            if(br.readLine().trim().equalsIgnoreCase("c")) flag = true;
            System.out.println("Enter coordinates of a source point... ");
            System.out.println("x: ");
            Double x = Double.valueOf(br.readLine());
            System.out.println("y: ");
            Double y = Double.valueOf(br.readLine());
            System.out.println("z: ");
            Double z = Double.valueOf(br.readLine());

            p = new Point(x, y, z);
            br.close();
        }catch(IOException ex) {

        }

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        int height = 800;
        int width = 800;
        glcanvas.setSize(width, height);
        Parser parser = new Parser();
        try {
            parser.parse(args[0]);
        } catch (Exception ex) {

        }

        Transformation transformation = new Transformation(parser.getViewPoint(), parser.getDirection(), parser.getVertices(), parser.getPolygons(),p, flag);

        glcanvas.addGLEventListener(transformation);
        final JFrame frame = new JFrame("Transformation");
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
