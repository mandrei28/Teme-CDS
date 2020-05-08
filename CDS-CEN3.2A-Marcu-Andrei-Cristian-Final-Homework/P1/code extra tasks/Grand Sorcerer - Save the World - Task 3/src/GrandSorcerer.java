import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 * The class that creates most of the classes in the program
 * It's a thread class
 * @author marcu
 */
public class GrandSorcerer extends Thread{
	ArrayList<String> potions; // potions the GrandSorcerer has
	ReentrantLock potionsLock; // used each time the sorcerer gets/sends potions
	Coven covens[]; // all the covens
	Witch witches[]; // all the witches
	Undead undead[]; // all the undeads
	int covensNo; // the number of covens
	int witchesNo; // the number of witches
	Random rand;
	WitchHelper witchHelper; // witch helper which contains potion recipes
	PotionTransfer potionTransfer; //used to transfer potions from witch to sorcerer and from sorcerer to witch
	GrandSorcererHelper grandSorcererHelper; //the grand sorcerer helper that creates the demon spawners
	Boolean gameEnded = false; // condition that stops all the threads when the undeads are defeated	
	/**
	 * We create the covens, witches, undeads with random number. We create the grandSorcererHelper
	 */
	GrandSorcerer()
	{
		rand = new Random();
		covensNo = rand.nextInt(20 - 2) + 3; // random number of convers from 3 to 20
		covens = new Coven[covensNo]; 
		witchesNo = rand.nextInt(10) + 1; //random witches number from 1 to 10
		witches = new Witch[witchesNo];
		Undead.undeadNo = rand.nextInt(50 - 19) + 20; //random number of undeads between 20 and 50
		undead = new Undead[Undead.undeadNo];
		potionTransfer = new PotionTransfer(this); // the potion transfer between witches and sorcerer
		witchHelper = new WitchHelper(); // we initialize the witchHelper
		potions = new ArrayList<String>(); // we initialize the potions array which is empty in the beggining
		potionsLock = new ReentrantLock(); // we initialize the potions lock
		
		for(int i = 0 ; i < covensNo; i++) {
			covens[i] = new Coven(i, this);
			covens[i].start(); //we start covens
		}
		grandSorcererHelper = new GrandSorcererHelper(this); // we create GrandSorcererHelper which will create demonSpawners
		for(int i = 0; i < witchesNo; i++)
		{
			witches[i] = new Witch(i, covens, witchHelper, potionTransfer);
			witches[i].start();//we start witches
		}
		for(int i = 0 ; i < Undead.undeadNo ; i++)
		{
			undead[i] = new Undead(i, covens);
			undead[i].start();//we start undeads
		}
	}
	
	/**
	 * We print the number of potions the sorcerer has and we check every 2 seconds if all undead were defeated in order to stop the program
	 */
	public void run() {
		while(gameEnded == false) { // while the demons aren't defeated
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			potionsLock.lock(); // we lock the potionsLock so the number of potions won't be modified while we print it
			//System.out.println("The grand sorcerer has " + potions.size() + " potions"); // we print the number of potions
			potionsLock.unlock(); // we unlock it
			if(Undead.undeadNo <= 0) { // if the number of undeads is 0 it means that all of them were defeated so the application should stop
				gameEnded = true;// the game has ended
				System.out.println("All undeads were defeated. Game will end soon");
			}
		}
	}	
}
