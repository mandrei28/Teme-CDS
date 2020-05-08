import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MathMultiplication { // classic math multiplication for checking if we got the right answer
	int data[][];
	MathMultiplication(int a[][], int b[][])
	{
		data = new int[a.length][a.length];
		mathMultiplication(a, b);
	}
	
	void mathMultiplication(int a[][], int b[][]) {
	   int dim = a.length;
       for (int i = 0; i < dim; i++) {
           for (int j = 0; j < dim; j++) {
               for (int k = 0; k < dim; k++) {
                   data[i][j] = data[i][j] + a[i][k] * b[k][j];
               }
           }
       }
	}
	
	void printMatrix(FileWriter output) throws IOException {
		for (int i = 0; i < data.length; ++i) {
			for (int j = 0; j < data.length; ++j) {
				output.write(data[i][j] + " ");
			}
			output.write("\n");
		}
		output.write("\n");
	}
}
