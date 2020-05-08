import java.util.Random;

public class MatrixGenerator {
	int data[][];
	MatrixGenerator(int n) // generates random matrix
	{
		data = new int[n][n];
		generateMatrix(n);
	}
	public void generateMatrix(int n)
	{
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				data[i][j] = new Random().nextInt(10);
			}
		}
	}
}
