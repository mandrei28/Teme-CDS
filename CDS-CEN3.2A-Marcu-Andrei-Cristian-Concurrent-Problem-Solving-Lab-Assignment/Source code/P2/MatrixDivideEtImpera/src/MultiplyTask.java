import java.util.concurrent.Future;

public class MultiplyTask implements Runnable {
	Matrix a, b, c;
	
	public MultiplyTask(Matrix a, Matrix b, Matrix c) {
		this.a = a; 
		this.b = b; 
		this.c = c;
	}
	
	public void run() {
		try {
			int n = a.getDim();
			if (n == 1) {
				c.updateCell(0, 0, (a.get(0,0) * b.get(0,0)));
			} else if (n == 2) {
				
				for (int i = 0; i < 2; ++i) {
					for (int j = 0; j < 2; ++j) {
						// paralel solving for the sum
						c.updateCell(i, j, a.get(i, 0) * b.get(0, j) + a.get(i, 1) * b.get(1, j));
					}
				}	
				
			} else {
				Matrix[][] aa = a.split(), bb = b.split(), cc = c.split();
				Future<?>[][] future1 = (Future<?>[][]) new Future[2][2];
				Future<?>[][] future2 = (Future<?>[][]) new Future[2][2];
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						// concurrent solving for the 8xmultiplications
							future1[i][j] = MatrixTask.exec.submit(new MultiplyTask(aa[i][0], bb[0][j], cc[i][j]));
							future2[i][j] = MatrixTask.exec.submit(new MultiplyTask(aa[i][1], bb[1][j], cc[i][j]));
							future1[i][j].get();
							future2[i][j].get();
					}
				}
			}
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		}
	}
}
