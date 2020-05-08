import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Class that represents the demon spawner
 * It's a thread class
 * @author marcu
 */
public class DemonSpawner extends Thread{
	private Coven coven; // coven in which the demons are spawned
	private static int demonsSpawned; // the number of demonsSpawned. we will use this for the demonID
	ReentrantLock demonsSpawnedLock = new ReentrantLock(); //lock used each time we increase the demonsSpawned value
	
	public DemonSpawner(Coven coven) {
		this.coven = coven;
	}
	/**
	 * The thread function
	 * Spawns a demon every 500-1000 ms
	 */
	public void run() {
		while(coven.grandSorcerer.gameEnded == false) { // while undeads are not defeated
			Random rand = new Random();
			long milis = rand.nextInt(1000 - 499) + 500; // we sleep for 500-1000 ms


			// Sleeping a random time between 500 and 1000 milliseconds
			try {
				Thread.sleep(milis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Spawning a demon
			spawnADemon(); // we spawn a demon
		}
	}
	
	/**
	 * Function that spawns a demon with a random position and keeps the number of demons updated
	 */
	private void spawnADemon() {

		Random rand = new Random();
		
		//while a new demon is spawned the other demons can't move
		coven.covenLock.lock();

		if(coven.demons.size() != coven.covenHelper.N / 2) { // the maximum number of demons in a coven is N/2 where N is the size of coven matrix

			int X = rand.nextInt(coven.covenHelper.N); // random position for X from (0,N)
			int Y = rand.nextInt(coven.covenHelper.N); // random position for Y from (0,N)

			// Creating a new demon
			Demon demon = new Demon(DemonSpawner.demonsSpawned, X, Y, coven); // we create a new demon

			// Try inserting the demon in the coven
			if(coven.addDemon(demon)) { // we add the demon
				// No other thread can access the number of demons in the coven since we will modify it
				demonsSpawnedLock.lock();
				DemonSpawner.demonsSpawned++;
				demonsSpawnedLock.unlock(); // Unlock the demons counter lock
			}		
		}
		// Unlock the coven
		coven.covenLock.unlock();
	}
}
