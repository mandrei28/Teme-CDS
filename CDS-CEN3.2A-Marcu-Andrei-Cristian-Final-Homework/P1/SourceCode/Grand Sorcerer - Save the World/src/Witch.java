import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Class that represents the Witch
 * It's a thread class
 * @author marcu
 */
public class Witch extends Thread{
	int witchID; // unique identifier of witch
	Coven covens[]; // all the covens so we can pick a random one
	int covensNo; // number of covens
	int coven; // the coven witch will defend
	int potionsTook; // the random number of potions the witch will take in fight
	Random rand;
	WitchHelper witchHelper; // the witchHelper which contains the potion recipes
	PotionTransfer potionTransfer; // used to take/give potions to the grand sorcerer
	ReentrantLock witchLock; // used so 2 witches aren't going to fight the same undead
	
	public Witch(int witchID, Coven covens[], WitchHelper witchHelper, PotionTransfer potionTransfer)
	{
		this.witchLock = new ReentrantLock();
		this.witchID = witchID;
		this.covens = covens;
		this.covensNo = covens.length;
		this.rand = new Random();
		this.witchHelper = witchHelper;
		this.potionTransfer = potionTransfer;
	}
	
	/**
	 * The thread specific function
	 * The witch creates a potion with each coven visit if there are ingredients
	 * The witch picks a coven to defend. If an undead visits the coven while the witch is there, they will fight
	 * If the witch has enough potions she will kill the demon
	 * Otherwise the coven will lose 10% of all ingredients
	 */
	public void run() {
		while(covens[0].grandSorcerer.gameEnded == false)
		{
			this.witchLock.lock(); // we let the witch fight the undead alone, no other witch is going to intervene in the fight
			coven = pickACoven(); // we pick a coven
			createPotion(coven); // we create and send the potion to the grand witcher if there are enough ingredients
			covens[coven].witchesInCoven.add(this); // we add the witch in the coven
			covens[coven].undeadsInCovenLock.lock(); // we lock the undeadsInCovenLock because we might defeat an undead and remove it from the undeadsInCoven array
			if(covens[coven].undeadsInCoven.size() > 0) // we check if there is any undead in the same coven as the witch
			{
				if(covens[coven].undeadsInCoven.get(0).isDefeated == false) // if the undeadsInCoven[0] undead is not defeated already
				{
					potionsTook = this.takePotions(); // the witch tries to get the potion and saves the number of potion she took.
					// if the number is 0 it means that the grand sorcerer didn't had enough potions 
					if(potionsTook > 0 && covens[coven].undeadsInCoven.get(0).isDefeated == false) { // if we took enough potions from the grand sorcerer and the undead isn't defeated
						covens[coven].undeadsInCoven.get(0).isDefeated = true; // the undead is now defeated
						System.out.println("Witch " + this.witchID + " has took " + potionsTook + " potions and defeated undead " + covens[coven].undeadsInCoven.get(0).undeadID + " in coven " + covens[coven].covenID);
					}
					else
					{// if the grand sorcerer couldn't give us enough potions we will fight the undead and lose 10% of each ingredient in coven
						covens[coven].lose10PercentIngredients();
						//System.out.println("lost10PercentIngredients");
					}
				}
				covens[coven].undeadsInCovenLock.unlock(); // we unlock the undeadsInCoven lock
			}
			else
				covens[coven].undeadsInCovenLock.unlock(); // we unlock the undeadsInCoven lock in case we don't get on the if statement
			int millis = rand.nextInt(30 - 9) + 10; // witch sleeps between 10-30ms
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			covens[coven].witchesInCoven.remove(this); // the witch retires from the coven and goes to defend another coven
			this.witchLock.unlock(); // the witch lock is unlocked and another witch can begin the fight
		}
	}
	
	/**
	 * Method that random picks a coven
	 * @return the random picked coven
	 */
	public int pickACoven() {
		return rand.nextInt(covensNo); // we pick a random coven
	}
	
	/**
	 * Creates a potion if there are ingredients
	 * @param covenNo the coven we are taking ingredients from
	 */
	public void createPotion(int covenNo)
	{
		covens[covenNo].ingredientsLock.lock(); // we lock the ingredientsInCoven lock because we will modify the values when we take ingredients for potion
		if(potionTransfer.grandSorcerer.potions.size() < 20) // if we have less than 20 potions we can createmore
		{
			int priority = 8; // we prioritize the potions that require more ingredients, and decrease the priority by one if we don't find any
			int prev;
			Boolean potionCreated = false; // when a potion is created we stop
			Boolean covenHasIngredients = true; // we assume that the coven has enough ingredients
			while(potionCreated == false && priority >= 4 && potionTransfer.grandSorcerer.potions.size() < 20) // while we didn't create potion
			{
				for(Map.Entry<String, ArrayList<String>> entry: witchHelper.potions.entrySet()) // we iterate through potion recipes
				{
					if(entry.getValue().size() == priority) // we check if the number of ingredients the potion requires is the maximum possible
					{
						covenHasIngredients = true; // we assume that the coven has ingredients
						for(String ingredient: entry.getValue())
						{
							if(covens[covenNo].ingredientsInCoven.get(ingredient) == 0) {
								covenHasIngredients = false;// if an ingredient is missing we stop because this coven can't supply us enough ingredients for the potion
								break;
							}
						}
						if(covenHasIngredients == true) // if we found all the ingredients
						{
							for(String ingredient: entry.getValue())
							{
								prev = covens[covenNo].ingredientsInCoven.get(ingredient); // we update each ingredient used to make the potion with previous value - 1
								covens[covenNo].ingredientsInCoven.put(ingredient, prev - 1);
							}
							
							//System.out.println("Witch " + this.witchID + " has created " + entry.getKey());
							potionCreated = true; // we have created a potion
							sendPotion(entry.getKey());// we send the potion to the witcher
						}
					}
				}
				priority--; // if we didn't find any potion we can make with the current priority we decrease it and search again. the minimum of ingredients is 4
			}
		}
		covens[covenNo].ingredientsLock.unlock();// we unlock the lock
	}
	
	/**
	 * Method that send the potion to the potion transfer instance which will send it to the sorcerer
	 * @param potion the potion we are sending
	 */
	public void sendPotion(String potion) {
		System.out.println("Witch " + this.witchID + " has sent " + potion + " to the grand sorcerer");
		potionTransfer.givePotion(potion);// potion is sent to the sorcerer
	}
	
	/**
	 * Method that tries to take potions from the sorcerer in order to fight an undead
	 * @return 0 if the sorcerer couldn't give us potions
	 * @return the number of potions the witch took
	 */
	public int takePotions() {
		int result = potionTransfer.takePotion(); // result will contain the number of potions the sorcerer can give to the witch for the fight 
		if(result > 0) { // if the sorcerer sent us potions we return the number of potions
			return result;
		}
		return 0;// the sorcerer couldn't give us potions for the fight
	}
}
