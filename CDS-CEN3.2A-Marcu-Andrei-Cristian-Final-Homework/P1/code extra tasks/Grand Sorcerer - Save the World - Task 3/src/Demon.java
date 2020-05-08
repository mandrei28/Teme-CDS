import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
/**
 * Class that represents the demons
 * It's a thread class
 * @author marcu
 */
public class Demon extends Thread {
	public int demonID; // unique identifier for demon
	public int X; // position in matrix
	public int Y; // position in matrix
	private Coven coven; // coven that consists the demon
	int penaltyRounds = 0; // rounds in which demon can't make ingredients
	int socialSkill = 0; // demon's social skill
	int wallsHit = 0; // total walls hit by demon
	Boolean isRetired = false; // if the demon is scared by an undead it becomes retired, so the thread will be stoped by this condition
	Boolean isScared = false; // if the demon is scared by the 10s ingredients reset from the coven it becomes scared and waits 1 second
	Boolean canUp = true, canDown = true, canLeft = true, canRight = true; // conditions used for the situation when demon hit a wall and tries to go back in the same direction(the wall direction)
	// this conditions will prevent it from doing this
	
	public Demon(int demonID, int X, int Y, Coven coven) {
		this.demonID = demonID;
		this.coven = coven;
		this.X = X;
		this.Y = Y;
	}
	/**
	 * Function that represents the thread. This function administrates the demon movement, the moment when the demon is scared and the social skill decrease when he hits 10 walls
	 */
	public void run() {
		while(isRetired == false && coven.grandSorcerer.gameEnded == false) // while demon is not retired and undeads aren't defeated
		{
			if(Math.abs(X - Y) <= 1) { // if we are on the main diagonal
				System.out.println("Demon " + demonID + " is sleeping");
				
				try {
					coven.demonsBarrier.await(); // we await the cyclic barrier. the barries is N/2 for each coven. the barrier is defined in coven
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				
			}
			
			coven.moveDemon(this); // the demon moves
			if(this.wallsHit == 10) // for each 10 walls hit we decrease it's socialskill
			{
				this.socialSkill -= 100;
				if(this.socialSkill < 0)
					this.socialSkill = 0;
				this.wallsHit = 0;
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(this.isScared == true) // if the demon is scared by the coven reset(10s) he takes a break for 1 second;
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.isScared = false; // after 1s the demon is no longer scared
			}
			
		}
	}
	
	/**
	 * Function that prints demon's current position
	 */
	public void currentPosition() 
	{
		//System.out.println("Demon " + demonID + " is curently at position (" + X + "," + Y + ") in coven " + coven.covenID); // prints demon's current position
	}
	
	/**
	 * Function that updates the position of the demon
	 * @param newX the new X position
	 * @param newY the new Y position
	 */
	public void moveDemon(int newX, int newY)
	{
		this.X = newX;
		this.Y = newY;
	}
	
	/**
	 * Function that puts the demon to sleep for 10-50ms when it is surrounded
	 */
	public void demonSurrounded() 
	{
		// if the demon can't move in any direction(it is surrounded) he waits 10-50ms
		Random rand = new Random();

		long milis = rand.nextInt(50 - 9) + 10;

		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function that retires a demon, making the thread stop, eliminating it from matrix and demons array
	 */
	public void retireDemon() {
		coven.covenLock.lock(); // we lock the covenLock because we are going to modify the coven matrix
		coven.demonsLock.lock(); // we lock the demonsLock because we are going to modify the demons array
		
		coven.coven[X][Y] = 0; // we mark demon's current position with 0(we remove it from matrix)
		coven.demons.remove(this); // we remove the demon from the demons array
		this.isRetired = true; // and the demon is finally retired and the thread will be stoped by the while condition
		
		coven.covenLock.unlock();
		coven.demonsLock.unlock();
	}
}
