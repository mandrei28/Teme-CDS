import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Class that resets the ingredients in coven every 10 seconds
 * It's a thread class
 * @author marcu
 */
public class CovenReset extends Thread{
	Coven coven;
	
	CovenReset(Coven coven){
		this.coven = coven;
	}
	/**
	 * Every 10s we reset the ingredients and scare the demons making them sleep for 1 second
	 * This function represents the thread
	 */
	public void run() {
		while(coven.grandSorcerer.gameEnded == false) // if all undeads were defeated we stop all threads
		{
			try {
				Thread.sleep(10000); // we wait 10 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.coven.ingredientsLock.lock(); // we lock the ingredientsLock so we can modify it without any thread overwriting our data
			//System.out.println("The demons were scared. Ingredients in coven " + coven.covenID + " have been reseted");
			this.coven.ingredientsInCoven = new ConcurrentHashMap<String, Integer>(coven.covenHelper.ingredientsInCoven); // the ingredientsInCoven goes back to the default map created in CovenHelper with value 0
			for(Demon demon: this.coven.demons) {
				demon.isScared = true; // all demons are scared ( they stop for 1 second )
			}
			this.coven.ingredientsLock.unlock(); // we unlock the ingredientsLock
		}
	}

}
