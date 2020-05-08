import java.util.ArrayList;
import java.util.Random;
/**
 * Class that transfers the potion from the witch to the sorcerer and vice versa
 * @author marcu
 */
public class PotionTransfer {
	GrandSorcerer grandSorcerer;
	Random rand;
	
	PotionTransfer(GrandSorcerer grandSorcerer){
		this.grandSorcerer = grandSorcerer;
		this.rand = new Random();
	}
	
	/**
	 * Method that gives the potion to the sorcerer if he has less than 20 potions
	 * @param potion the potion we are going to give
	 */
	public void givePotion(String potion)
	{
		grandSorcerer.potionsLock.lock(); // we lock the potions lock so we can add a potion safely
		
		if(grandSorcerer.potions.size() < 20)// if the number of potions the grand witcher has is smaller than 20 we add the potion
			grandSorcerer.potions.add(potion);
		
		grandSorcerer.potionsLock.unlock(); // we unlock the potions lock
	}
	
	
	/**
	 * Method that gives the witch a random number of potion between 2 and 5 if there sorcerer has enough
	 * @return 0 if the sorcerer doesn't have enough potions
	 * @return other value(the number of potions the witch took)
	 */
	public int takePotion()
	{
		grandSorcerer.potionsLock.lock(); //we lock the potions lock so we can add remove potions safely
		
		int noRemovedPotions = rand.nextInt(5 - 1) + 2; // the random number of potions the witch is going to take(between 2 and 5)
		ArrayList<String> toBeRemoved = new ArrayList<String>(); // the potions we are going to remove from the potions arraylist
		if(grandSorcerer.potions.size() < noRemovedPotions) { // if the number of potions the grand witcher has is smaller than the number of potions we need we stop and we unlock the potionslock
			grandSorcerer.potionsLock.unlock();
			return 0;
		}
		for(String potion: grandSorcerer.potions) // we add potions in the auxiliary arraylist(toBeRemoved) until we have enough potions(equal to the random value we defined before)
		{
			toBeRemoved.add(potion);
			if(toBeRemoved.size() == noRemovedPotions)
				break;
		}
		grandSorcerer.potions.removeAll(toBeRemoved); // we remove all the potions added before
		
		grandSorcerer.potionsLock.unlock(); // we unlock the potionsLock
		return noRemovedPotions; // we return the number of potions the witch took
	}
}
