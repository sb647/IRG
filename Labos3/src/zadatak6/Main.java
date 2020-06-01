package zadatak6;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        GLCapabilities capabilities = new GLCapabilities( profile );

        // The canvas
        final GLCanvas glcanvas = new GLCanvas( capabilities );
        int height = 800;
        int width = 800;
        glcanvas.setSize( width, height );
        Parser p = new Parser();
        List<Point> points = new ArrayList<>();
        try{
            points = p.parse(args[0]);
            p.parsePolygon(args[1]);
        }catch(Exception e) {

        }
        Bernstein bern = new Bernstein(points);
        glcanvas.addGLEventListener(bern);
        Transformation transformation = new Transformation(bern.getCurvePoints().get(0), p.getVertices(), p.getPolygons(), p.getOrder());
        glcanvas.addGLEventListener(transformation);
        //glcanvas.display();
        final JFrame frame = new JFrame ( "Zadatak6" );
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

        for(int i = 1; i < bern.getCurvePoints().size(); i++) {
           Thread.sleep(200);
           transformation.setViewPoint(bern.getCurvePoints().get(i));

           glcanvas.display();
       }


    }
}
