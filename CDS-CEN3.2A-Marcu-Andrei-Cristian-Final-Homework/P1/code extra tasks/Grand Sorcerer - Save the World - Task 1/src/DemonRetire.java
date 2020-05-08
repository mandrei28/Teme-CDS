/**
 * Class that manages the demon retire semaphore which releases a permit each 50ms so a random demon can retire
 * It's a thread class
 * @author marcu
 */
public class DemonRetire extends Thread{
	GrandSorcererHelper grandSorcererHelper;
	
	DemonRetire(GrandSorcererHelper grandSorcererHelper)
	{
		this.grandSorcererHelper = grandSorcererHelper;
	}
	/**
	 * Thread specific class
	 * Releases a permit of demonRetireSemaphore each 50ms. The semaphore is common for each demon since we have only an instance of grandSorcererHelper
	 */
	public void run() {
		while(this.grandSorcererHelper.grandSorcerer.gameEnded == false)
		{
			//we release a permit and a random demon can pick it
			this.grandSorcererHelper.demonRetireSemaphore.release();
			
			//Thread goes to sleep for 50 ms
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}