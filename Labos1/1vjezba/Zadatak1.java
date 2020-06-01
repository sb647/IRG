import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.AnyMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;


public class Zadatak1 {
	
	public static void main(String[] args) {
		
		
		Vector3D k1 = new Vector3D(2,3,-4);
		Vector3D k2 = new Vector3D(-1,4,-1);
		Vector3D v1 = k1.add(k2);
		System.out.println(v1);
		System.out.println(v1.dotProduct(k2));

		Vector3D k3 = new Vector3D(2,2,4);
		Vector3D v2 = v1.crossProduct(k3);
		System.out.println(v2);

		Vector3D v3 = v2.normalize();
		System.out.println(v3)

		Vector3D v4 = v2.negate();
		System.out.println(v4);
		
		double [][] matrix1 = {
				{1, 2, 3},
				{2, 1, 3},
				{4, 5, 1}
		};
		
		RealMatrix m1 = new Array2DRowRealMatrix(matrix1);
		
		double [][] matrix2 = {
				{-1, 2, -3},
				{5, -2, 7},
				{-4, -1, 3}
		};
		
		RealMatrix m2 = new Array2DRowRealMatrix(matrix2);
		
		RealMatrix M1 = m1.add(m2);
		System.out.println(M1);
		
		RealMatrix M2 = m1.multiply(m2.transpose());
		System.out.println(M2);
		
		RealMatrix M3 = m1.multiply(new LUDecomposition(m2).getSolver().getInverse());
		System.out.println(M3);
	
			
	}
	
}
