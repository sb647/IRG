import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.Scanner;

public class Zadatak2 {
	
	static double determinantOfMatrix(RealMatrix matrix) { 
	   
	   return ( matrix.getEntry(0, 0) * (matrix.getEntry(1, 1) * matrix.getEntry(2, 2) - matrix.getEntry(2, 1) * matrix.getEntry(1, 2)) 
	        - matrix.getEntry(0, 1) * (matrix.getEntry(1, 0) * matrix.getEntry(2, 2) - matrix.getEntry(1, 2) * matrix.getEntry(2, 0)) 
	        + matrix.getEntry(0, 2) * (matrix.getEntry(1, 0) * matrix.getEntry(2, 1) - matrix.getEntry(1, 1) * matrix.getEntry(2, 0))); 
	    
	}
	
	public static void main(String[] args) {
		System.out.println("Enter values:");
		Scanner sc = new Scanner(System.in);
		double a1 = sc.nextDouble();
		double b1 = sc.nextDouble();
		double c1= sc.nextDouble();
		double d1 = sc.nextDouble();
		
		double a2 = sc.nextDouble();
		double b2= sc.nextDouble();
		double c2= sc.nextDouble();
		double d2 = sc.nextDouble();
		
		double a3 = sc.nextDouble();
		double b3 = sc.nextDouble();
		double c3= sc.nextDouble();
		double d3= sc.nextDouble();
		
		sc.close();
		
		double [][] matrix1 = {
				{a1, b1, c1},
				{a2, b2, c2},
				{a3, b3, c3}
		};
		
		RealMatrix m1 = new Array2DRowRealMatrix(matrix1);
		
		double [][] matrix2 = {
				{d1, b1, c1},
				{d2, b2, c2},
				{d3, b3, c3}
		};
		
		RealMatrix m2 = new Array2DRowRealMatrix(matrix2);
		
		double [][] matrix3 = {
				{a1, d1, c1},
				{a2, d2, c2},
				{a3, d3, c3}
		};
		
		RealMatrix m3 = new Array2DRowRealMatrix(matrix3);
		
		double [][] matrix4 = {
				{a1, b1, d1},
				{a2, b2, d2},
				{a3, b3, d3}
		};
		
		RealMatrix m4 = new Array2DRowRealMatrix(matrix4);
		
		double D = determinantOfMatrix(m1);
		double D1 = determinantOfMatrix(m2);
		double D2 = determinantOfMatrix(m3);
		double D3 = determinantOfMatrix(m4);
		
		if(D != 0) {
			double x = D1 / D;
			double y = D2 / D;
			double z = D3 / D;
			
			System.out.println("[x y z] = [" + x +" "+ y + " " + z + "]");
			
		}
		
	
	}

}
