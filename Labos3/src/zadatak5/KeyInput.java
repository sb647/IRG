package zadatak5;

import com.jogamp.opengl.awt.GLCanvas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class KeyInput implements KeyListener {
    private Point viewPoint;
    private Point pointOfView;
    private Transformation transformation;
    Scanner sc = new Scanner(System.in);
    GLCanvas canvas;

    public KeyInput(Point viewPoint, Point pointOfView, Transformation transformation, GLCanvas canvas) {
        this.viewPoint = viewPoint;
        this.pointOfView = pointOfView;
        this.transformation = transformation;
        this.canvas = canvas;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()){
            case 'g' :{
                readInput('g');
                break;
            }
            case 'o': {
                readInput('o');
                break;
            }
        }
    }

    private void readInput(char c) {
        System.out.println("Enter coordinate:");
        String coordinate = sc.next();
        System.out.println("Press '+' to increase value/ '-' to decrease value:");
        String value = sc.next();
        sc.close();

        if(c == 'g') {
            switch(coordinate) {
                case "x":
                    double x = this.pointOfView.getX();
                    this.pointOfView.setX(value.equals("+") ? (x+1): (x-1));
                    break;
                case "y":
                    double y = this.pointOfView.getY();
                    this.pointOfView.setY(value.equals("+") ? (y+1): (y-1));
                    break;
                case "z":
                    double z = this.pointOfView.getZ();
                    this.pointOfView.setZ(value.equals("+") ? (z+1): (z-1));
                    break;
            }
            this.transformation.setPointOfView(this.pointOfView);
            canvas.display();
        }else {

            switch(coordinate) {
                case "x":
                    double x = this.viewPoint.getX();
                    this.viewPoint.setX(value.equals("+") ? (x+1): (x-1));
                    break;
                case "y":
                    double y = this.viewPoint.getY();
                    this.viewPoint.setY(value.equals("+") ? (y+1): (y-1));
                    break;
                case "z":
                    double z = this.viewPoint.getZ();
                    this.viewPoint.setZ(value.equals("+") ? (z+1): (z-1));
                    break;
            }
            this.transformation.setViewPoint(this.viewPoint);
            canvas.display();

        }

    }



    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
