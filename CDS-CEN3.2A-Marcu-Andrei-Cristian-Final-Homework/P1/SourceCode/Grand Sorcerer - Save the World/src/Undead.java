import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Class that represents the undead
 * It's a thread class
 * @author marcu
 */
public class Undead extends Thread{
	static int undeadNo; // the random number of undeads(it is defined in GrandSorcerer class)
	Coven covens[]; // all the covens(defined in GrandSorcerer). We need them so the undead can pick a random coven to haunt
	int covensNo; // the number of covens
	int undeadID; // unique identifier of undead
	int coven; // the random picked coven
	int retiredDemons; // the random number between 5 and 10 of demons we will retire when the undead will haunt a coven and the coven isn't defended by a witch
	Boolean isDefeated = false; // the undead was defeated by a witch
	Random rand;
	
	Undead(int undeadID, Coven covens[])
	{
		this.undeadID = undeadID;
		this.rand = new Random();
		this.covens = covens;
		this.covensNo = covens.length;
	}
	
	/**
	 * Thread specific function
	 * Sends the undead to the coven and checks if there is no witch defending the coven
	 * If there is no witch the undead resets the ingredients in coven
	 * After 500-1000 ms the undead leaves the coven and picks another one
	 */
	public void run()
	{
		while(isDefeated == false) // while the undead is not defeated by a witch
		{
			coven = pickACoven(); // the undead picks a random coven
			covens[coven].undeadsInCoven.add(this); // we add the undead in coven
			if(covens[coven].witchesInCoven.isEmpty()) // if there is no witch
			{
				retiredDemons = rand.nextInt(10 - 4) + 5; // it scared a random number of demons
				scareDemons(retiredDemons); // it retires the scared demons
				loseAllIngredients(); // all ingredients in the coven are lost
			}
			
			
			int milis = rand.nextInt(1000 - 499) + 500; // the undead goes to sleep for 500-1000ms
			try {
				Thread.sleep(milis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			covens[coven].undeadsInCoven.remove(this); // when the undead is ready to haunt another coven he will leave the current coven
		}
		Undead.undeadNo--; // if the undead is defeated we decrease the number so we can check when all of them are defeated
	}
	
	/**
	 * Method that picks a random coven
	 * @return the coven the undead is going to hunt
	 */
	public int pickACoven() {
		return rand.nextInt(covensNo); // picks a random coven from [0, covensNo);
	}
	
	/**
	 * Function that retires a random number of demons(5-10) when there is no witch to defend the coven
	 * @param numberOfRetiredDemons the number of retired demons
	 */
	public void scareDemons(int numberOfRetiredDemons) {
		ArrayList<Demon> demonsToRetire = new ArrayList<Demon>(); // the auxiliary arraylist in which we will keep the demons we are going to retire
		if(!covens[coven].demons.isEmpty()) { // if we have demons we can retire
			covens[coven].demonsLock.lock(); // we lock the demonsLock because we are going to modify the demons array
			for(Demon demon: covens[coven].demons) {
				demonsToRetire.add(demon); // we add demons in the auxiliary arraylist until we hit the random number of demons we are going to retire
				if(demonsToRetire.size() == numberOfRetiredDemons)
					break;
			}
			covens[coven].demonsLock.unlock(); // we unlock the demons lock
			for(Demon demon: demonsToRetire)
			{
				demon.retireDemon(); // we iterate through the demons we should retire and retire each one of them
			}
			System.out.println("Undead " + this.undeadID + " has scared " + numberOfRetiredDemons + " demons and reseted all ingredients in coven " + covens[coven].covenID);
		}

	}
	
	/**
	 * Function that resets all the ingredients in coven
	 */
	public void loseAllIngredients() {
		this.covens[coven].ingredientsLock.lock(); // we lock the ingredientsLock while we modify the ingredientsInCoven map
		
		this.covens[coven].ingredientsInCoven = new ConcurrentHashMap<String, Integer>(covens[coven].covenHelper.ingredientsInCoven); // all ingredients go back to the initial value(0)
		
		this.covens[coven].ingredientsLock.unlock(); // we unlock the ingredientsLock
	}
}
