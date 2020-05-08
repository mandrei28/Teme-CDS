import java.util.concurrent.locks.ReentrantLock;
/**
 * Self made CyclicBarrier class
 * @author marcu
 *
 */
public class CyclicBarrier {

	private ReentrantLock sleepingDemonsCounterLock = new ReentrantLock();
	private int sleepingDemonsCounter = 0;
	private int parties;
	/**
	 * @param parties the number of parties. in our case this will be N/2
	 */
	public CyclicBarrier(int parties) {
		this.parties = parties; // the number of parties. in our case this will be N/2
	}
	
	/**
	 * The CyclicBarrier await method. We increase the counter for each sleeping demon
	 * After we increase the counter we wait until all the demons are sleeping
	 * After all the demons are sleeping the demon can wake up again and start moving
	 * @throws InterruptedException if the thread can't sleep
	 */
	public void await() throws InterruptedException
	{
		this.sleepingDemonsCounterLock.lock(); // we lock while we modify the sleepingDemonsCounter value
		
		this.sleepingDemonsCounter ++; // we increase the number of sleeping demons
		
		this.sleepingDemonsCounterLock.unlock();
		
		while(sleepingDemonsCounter < parties)
		{
			// demon is sleeping, we wait until all demons from the coven are on the main diagonal
			Thread.sleep(50);
		}
	}
}
