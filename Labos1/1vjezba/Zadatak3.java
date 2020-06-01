import java.util.Scanner;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class Zadatak3 {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter A coordinates:");
		double Ax = sc.nextInt();
		double Ay = sc.nextInt();
		double Az = sc.nextInt();
		
		System.out.println("Enter B coordinates:");
		double Bx = sc.nextInt();
		double By = sc.nextInt();
		double Bz = sc.nextInt();
		
		System.out.println("Enter C coordinates:");
		double Cx = sc.nextInt();
		double Cy = sc.nextInt();
		double Cz = sc.nextInt();
		
		System.out.println("Enter T coordinates:");
		double Tx = sc.nextInt();
		double Ty = sc.nextInt();
		double Tz = sc.nextInt();
		
		sc.close();
		
		double [][] matrix1 = {
				{Ax, Bx, Cx},
				{Ay, By, Cy},
				{Az, Bz, Cz}};
		
		RealMatrix M = new Array2DRowRealMatrix(matrix1);
		
		double [][] matrix2 = {
				{Tx},
				{Ty},
				{Tz}};
		
		RealMatrix T = new Array2DRowRealMatrix(matrix1);
		 //matrica je nesingularna
		if (new LUDecomposition(M).getSolver().isNonSingular()) {
			RealMatrix result = new LUDecomposition(M).getSolver().getInverse().multiply(T);
			System.out.println(result);
			System.out.println("[t1 t2 t3] = [" + result.getEntry(0, 0)
			+ " " + result.getEntry(1, 0) + " " + result.getEntry(2, 0) + "]");
			return ;

		}else {
			Vector3D v1 = new Vector3D(Ax, Ay, Az);
			Vector3D v2 = new Vector3D(Bx, By, Bz);
			Vector3D v3 = new Vector3D(Cx, Cy, Cz);
			Vector3D v4 = new Vector3D(Tx, Ty, Tz);
			
			double pov = v2.subtract(v1).crossProduct(v3.subtract(v1)).getNorm() /2;
			double povA = v2.subtract(v4).crossProduct(v3.subtract(v4)).getNorm() /2;
			double povB = v1.subtract(v4).crossProduct(v3.subtract(v1)).getNorm() /2;
			double povC = v1.subtract(v4).crossProduct(v2.subtract(v1)).getNorm() /2;
			
			System.out.println("[t1 t2 t3] = [" + povA/pov
			+ " " + povB/pov + " " + povC/pov + "]");
			
		}
		
		
		
	}

}
