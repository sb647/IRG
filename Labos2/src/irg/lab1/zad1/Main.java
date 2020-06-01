package irg.lab1.zad1;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.*;
import com.jogamp.opengl.util.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter n: ");
        final int n = Integer.parseInt(br.readLine());
        br.close();
        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );

        // The canvas
        final GLCanvas glcanvas = new GLCanvas( capabilities );
        int height = 800;
        int width = 800;
        glcanvas.setSize( width, height );
        Polygon polygon = new Polygon(width,height);
        glcanvas.addGLEventListener(polygon);
        MouseInput mi = new MouseInput(n, height, width, polygon);
        glcanvas.addMouseListener(mi);
        final JFrame frame = new JFrame ( "Polygon" );
        frame.getContentPane().add( glcanvas, BorderLayout.CENTER);
        frame.setSize( frame.getContentPane().getPreferredSize() );
        frame.setVisible( true );
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closing window");
                e.getWindow().dispose();
                System.exit(0);
            }
        });

        final FPSAnimator animator = new FPSAnimator(glcanvas, 300, false);
        animator.start();
    }
}
