import java.util.concurrent.*;

public class MatrixTask {
	//static ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	static ExecutorService exec = Executors.newCachedThreadPool();
	static Matrix multiply(Matrix a, Matrix b)
	
	throws InterruptedException, ExecutionException {
		int n = a.getDim();
		Matrix c = new Matrix(n);
		Future<?> future = exec.submit(new MultiplyTask(a, b, c));
		future.get();
		return c;
	}
	


}