package irg.lab1.zad2;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainObject {

    public static void main(String[] args) throws FileNotFoundException {
        String str = args[0];
        Parser parser = new Parser();
        parser.parse(args[0]);

        Object obj = new Object(parser.getPolygons(), parser.getVertices());
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(obj);
        int height = 600;
        int width = 600;
        glcanvas.setSize(width, height);
        final JFrame frame = new JFrame("Objects");
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
        frame.setResizable(false);
        glcanvas.requestFocusInWindow();
        //Animator animator = new Animator(glcanvas);
        //animator.start();


        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter coordinates of a point... ");
            System.out.println("x: ");
            Double x = Double.valueOf(br.readLine());
            System.out.println("y: ");
            Double y = Double.valueOf(br.readLine());
            System.out.println("z: ");
            Double z = Double.valueOf(br.readLine());

            PointDouble p = new PointDouble(x, y, z);
            obj.checkIfObjectContainsDot(p);
            br.close();
        }catch(IOException ex) {

        }

    }
}
