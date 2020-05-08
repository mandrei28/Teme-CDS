import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Matrix {
	int dim;
	int[][] data;
	int rowDisplace, colDisplace;
	
	Matrix(int d) {
		dim = d;
		rowDisplace = colDisplace = 0;
		data = new int[d][d];
	}
	
	Matrix(int[][] matrix, int x, int y, int d) {
		data = matrix;
		rowDisplace = x; colDisplace = y;
		dim = d;
	}
	
	double get(int row, int col) {
		return data[row + rowDisplace][col + colDisplace];
	}
	
	void set(int row, int col, int value) {
		data[row + rowDisplace][col + colDisplace] = value;
	}
	
	void updateCell(int row, int col, double value) {
		data[row + rowDisplace][col + colDisplace] += value;  
	}
	
	int getDim() { return dim; }
	
	Matrix[][] split() {
		Matrix[][] result = new Matrix[2][2];
		int newDim = dim / 2;
		// we split the matrix in 4 equal matrices
		result[0][0] = new Matrix(data, rowDisplace			, colDisplace		  , newDim);
		result[0][1] = new Matrix(data, rowDisplace			, colDisplace + newDim, newDim);
		result[1][0] = new Matrix(data, rowDisplace + newDim, colDisplace		  , newDim);
		result[1][1] = new Matrix(data, rowDisplace + newDim, colDisplace + newDim, newDim);
		return result;
	}
	
	void printMatrix(FileWriter output) throws IOException { // prints the matrix
		
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				output.write(data[i][j] + " ");
			}
			output.write("\n");
		}
		output.write("\n");
	}
}