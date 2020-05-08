import java.util.concurrent.Executor;

public class PCExecutor implements Executor{
	public void execute(Runnable command) {

		Thread thread = new Thread(command);
		thread.start();
		
	}
}
