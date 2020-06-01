package zadatak5;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );

        // The canvas
        final GLCanvas glcanvas = new GLCanvas( capabilities );
        int height = 800;
        int width = 800;
        glcanvas.setSize( width, height );
       Parser parser = new Parser();
       try{
           parser.parse(args[0]);
       }catch (Exception ex) {

       }

        Transformation transformation = new Transformation(parser.getViewPoint(), parser.getDirection(), parser.getVertices(), parser.getPolygons(),parser.getOrder());

        glcanvas.addGLEventListener(transformation);
        KeyInput ki = new KeyInput(parser.getViewPoint(),parser.getDirection(),transformation, glcanvas );
        glcanvas.addKeyListener(ki);
        final JFrame frame = new JFrame ( "Transformation" );
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

        glcanvas.requestFocusInWindow();

    }
}
