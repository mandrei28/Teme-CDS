import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
			
			Scanner sc = new Scanner(System.in); 
			
			File file = new File("result.txt");
			
			FileWriter output = new FileWriter(file);
			
			System.out.println("Size of matrix: ");
			int N = sc.nextInt();
			
			MatrixGenerator randomMatrix1 = new MatrixGenerator(N);
			MatrixGenerator randomMatrix2 = new MatrixGenerator(N);
			
			Matrix a = new Matrix(randomMatrix1.data, 0, 0, N);
			Matrix b = new Matrix(randomMatrix2.data, 0, 0, N);
			
			a.printMatrix(output);
			b.printMatrix(output);
			
			output.write("Math multiplication: \n");
			long startTime = System.currentTimeMillis() / 1000;
			MathMultiplication d = new MathMultiplication(a.data, b.data);
			long endTime = System.currentTimeMillis() / 1000;
			d.printMatrix(output);
			long duration = (endTime - startTime);
			output.write("Math multiplication duration in seconds : " + duration);
			
			output.write("\n\nDivide et impera with threads multipliction \n");
			startTime = System.currentTimeMillis() / 1000;
			Matrix e = MatrixTask.multiply(a, b);
			endTime = System.currentTimeMillis() / 1000;
			e.printMatrix(output);
			duration = (endTime - startTime);
			output.write("Divite et impera multiplication duration in seconds : " + duration);
			
			System.out.println("Check the output file for the results");
			
			MatrixTask.exec.shutdownNow();
			
			sc.close();
			output.close();
		
	}

}
