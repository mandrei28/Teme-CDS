import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Class that represents the coven. It covers most of the program functionability
 * It's a Thread class
 * @author marcu
 */
public class Coven extends Thread{
	public ConcurrentHashMap<String, Integer> ingredientsInCoven = new ConcurrentHashMap<String, Integer>();
	CovenHelper covenHelper; // contains additional coven information, like the ingredients and default value of the ingredientsInCoven map
	Integer[][] coven; // coven matrix
	int covenID; //coven unique identifier
	ArrayList<Demon> demons; // contains all the demons present in coven
	ArrayList<Witch> witchesInCoven; // contains all the witches present in coven
	ArrayList<Undead> undeadsInCoven; // contains all the undeads present in coven
	CovenReset covenReset; // another thread that resets the ingredients every 10 seconds
	GrandSorcerer grandSorcerer; // grandSorcerer used to acces the gameFinished variable in order to stop the program execution
	
	ReentrantLock ingredientsLock = new ReentrantLock(); // lock used each time some thread wants to modify the ingredientsInCoven
	ReentrantLock demonsLock = new ReentrantLock(); // lock used each time some thread wants to modify the demons
	ReentrantLock covenLock = new ReentrantLock(); // lock used each time some thread wants to modify the coven matrix
	ReentrantLock undeadsInCovenLock = new ReentrantLock(); // lock used each time some thread wants to modify the undeadsInCoven
	CyclicBarrier demonsBarrier; // the barrier for sleeping demons
	Coven(int covenID, GrandSorcerer grandSorcerer)
	{
		this.covenHelper = new CovenHelper();
		this.grandSorcerer = grandSorcerer;
		this.demons = new ArrayList<Demon>();
		this.witchesInCoven = new ArrayList<Witch>();
		this.undeadsInCoven = new ArrayList<Undead>();
		this.coven = new Integer[covenHelper.N][covenHelper.N];
		this.coven = covenHelper.coven;
		this.ingredientsInCoven = new ConcurrentHashMap<String, Integer>(covenHelper.ingredientsInCoven);
		this.covenID = covenID;
		this.covenReset = new CovenReset(this);
		this.demonsBarrier = new CyclicBarrier(covenHelper.N / 2); // when n/2 demons are on the main diagonal we wake them up again
	}
	/**
	 * We start the thread that resets the coven ingredients every 10 seconds. we also print the ingredients present in the coven every 5 seconds
	 * This function represents the thread
	 */
	public void run()
	{
		covenReset.start(); // covenReset will make the values of ingredientsInCoven 0 every 10 seconds
		while(grandSorcerer.gameEnded == false)
		{
			try {
				for(Map.Entry<String, Integer> entry: ingredientsInCoven.entrySet())
				{
					//System.out.println("Ingredient = " + entry.getKey() + " pieces: " + entry.getValue() ); // we print all the ingredients and the values present in coven
				}
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Function that adds a demon in coven
	 * @param demon is the demon we are trying to add in the coven
	 * @return false if the demon couldn't be added
	 * @return true if the demon was added in the coven
	 */
	public boolean addDemon(Demon demon) {
		demonsLock.lock(); // we lock the demonsLock so no other thread can modify the demons arraylist while we work on it

		int X = demon.X;
		int Y = demon.Y;



		if(coven[X][Y] != 0) { // we tried to add the demon but it's position in matrix was already taken by another demon

			demonsLock.unlock();
			//System.out.println("The position the demon tried to occupy was already taken. The demon went back to his world waiting for a new position");
			return false;

		}else {

			coven[X][Y] = demon.demonID; // We mark the presence of the demon in matrix by placing his ID on the current position
			
			//System.out.println("Demon " + demon.demonID + " has spawned in coven " + covenID);

			demons.add(demon); // We add the demon in the demons arraylist

			demon.start(); // We start the demon so he can start moving, calculate social skill and penalty rounds.

			//demon.currentPosition();

			demonsLock.unlock(); //we unlock the demonsLock

			return true;
		}
	}
	/**
	 * Function that moves a demon in a random direction
	 * @param demon the demon we are going to move
	 */
	public void moveDemon(Demon demon) {

		covenLock.lock(); // we lock the covenLock so the matrix won't be modified while we work on it
		
		Random rand = new Random();
		int moveNumber = rand.nextInt(4);//We pick a random move direction. 0 right 1 up 2 down 3 left
		
		while(true)// we check if the random move direction we picked is possible. if it is we move on, otherwise we pick another direction.
		{
			if(moveNumber == 0 && canMoveRight(demon))
				break;
			if(moveNumber == 1 && canMoveUp(demon))
				break;
			if(moveNumber == 2 && canMoveDown(demon))
				break;
			if(moveNumber == 3 && canMoveLeft(demon))
				break;
			moveNumber = rand.nextInt(4);
		}

			if(moveNumber == 0 && canMoveRight(demon)) { // if the random movement direction is right and the demon can move right

				coven[demon.X][demon.Y] = 0; // the actual position is marked with 0
				coven[demon.X][demon.Y + 1] = demon.demonID; // the next position is marked with demonID

				createIngredient(demon); // the demon creates ingredients

				// Change Demon's current position
				demon.moveDemon(demon.X, demon.Y + 1); // we modify demon's X and Y

			}

			if(moveNumber == 1 && canMoveUp(demon)) { // if the random movement direction is up and the demon can move up

				coven[demon.X][demon.Y] = 0; // the actual position is marked with 0
				coven[demon.X - 1][demon.Y] = demon.demonID; // the next position is marked with demonID

				createIngredient(demon); // the demon creates ingredients

				// Change Demon's current position
				demon.moveDemon(demon.X - 1, demon.Y); // we modify demon's X and Y

			}

			if(moveNumber == 2 && canMoveDown(demon)) { // if the random movement direction is down and the demon can move down

				coven[demon.X][demon.Y] = 0; // the actual position is marked with 0
				coven[demon.X + 1][demon.Y] = demon.demonID; // the next position is marked with demonID

				createIngredient(demon); // the demon creates ingredients

				// Change Demon's current position
				demon.moveDemon(demon.X + 1, demon.Y); // we modify demon's X and Y

			}

			if(moveNumber == 3 && canMoveLeft(demon)) {// if the random movement direction is down and the demon can move down

				coven[demon.X][demon.Y] = 0; // the actual position is marked with 0
				coven[demon.X][demon.Y - 1] = demon.demonID; // the next position is marked with demonID

				createIngredient(demon); // the demon creates ingredients

				// Change Demon's current position
				demon.moveDemon(demon.X, demon.Y - 1); // we modify demon's X and Y


			}
			if(!canMoveLeft(demon) && !canMoveRight(demon) && !canMoveUp(demon) && !canMoveDown(demon)){ // the demon is surrounded by other demons or walls.

				// Demon can't move (its's surrounded), it stops working
				demon.demonSurrounded(); // we call the demonSurrounded function that puts the demon to sleep for a random time.
			}
			
			covenLock.unlock(); // we unlock the covenLock
			//demon.currentPosition(); // we print demon's current position;

	}
	/**
	 * Function that creates a random ingredient( or more if demon's social skill is big )
	 * @param demon is the demon that creates the ingredient
	 */
	public void createIngredient(Demon demon) {
		ingredientsLock.lock(); // we are about to modify the ingredientsInCoven map so we lock the ingredientsLock because we don't want it to be modified while we work.
		if(demon.penaltyRounds == 0) { // if he doesn't have any penalty rounds
			int numberOfIngredientsDemonCanCreate = demon.socialSkill / 100 + 1; // we calculate how many ingredients can the demon make considering it's social skill
			if(numberOfIngredientsDemonCanCreate > 10) // if it is bigger than 10 we make it 10
				numberOfIngredientsDemonCanCreate = 10;
			for(int i = 0; i < numberOfIngredientsDemonCanCreate; i++) {
				Random rand = new Random();
				int ingredientNumber = rand.nextInt(10); // we pick a random ingredient
				int prev = -1;
				prev = this.ingredientsInCoven.get(covenHelper.ingredients[ingredientNumber]); // we take the previous value of the ingredient
				this.ingredientsInCoven.put(covenHelper.ingredients[ingredientNumber], prev + 1); // we update the value of the ingredient using the previous + 1
				//System.out.println("Demon " + demon.demonID + " has created ingredient : " + covenHelper.ingredients[ingredientNumber] + " in coven " + covenID); // we print what ingredient was created
			}
			
		}
		else {// if the demon has penalty rounds he can't create ingredients.
			demon.penaltyRounds--; // he waited a penalty round so we decrease the number of penalty rounds
			//System.out.println("Demon " + demon.demonID + " took a break! "); // we print that demon had a penalty round and took a break
		}
		ingredientsLock.unlock();// we unlock ingredientsLock
	}
	/**
	 * Function that checks if the left movement is possible for the demon
	 * @param demon the demon that is trying to move left
	 * @return false if the demon can't move left
	 * @return true if the demon can move left
	 */
	private boolean canMoveLeft(Demon demon) {

		if(demon.canLeft == false) // we check if the demon hit the left wall last time so he doesn't go left again
		{
			demon.canLeft = true;
			return false;
		}
		
		if(demon.Y - 1 >= 0 && coven[demon.X][demon.Y - 1] == 0) // if there is no wall left or other demon he can move
			return true;
		if(demon.Y - 1 < 0) { // if he hits the left wall
			demon.penaltyRounds += 2; // his penalty rounds increase with 2
			demon.wallsHit++; // we count how many hit walls he has so at 10 we decrease it's social skill
			demon.canLeft = false; // after he hit the left wall he can't go left again
		}
		if(demon.Y - 1 >= 0 && coven[demon.X][demon.Y - 1] != 0) // if he meets another demon
		{
			demon.socialSkill += 50; // if he meets with another demon we increase it's social skill with 50
			for(Demon adjacentDemon: demons) {// we search for the adjacent demon to increase it's social skill too
				if(adjacentDemon.demonID == coven[demon.X][demon.Y - 1])
					adjacentDemon.socialSkill += 50;
			}
		}
		return false;
	}

	/**
	 * Function that checks if the down movement is possible for the demon
	 * @param demon the demon that is trying to move down
	 * @return false if the demon can't move down
	 * @return true if the demon can move down
	 */
	private boolean canMoveDown(Demon demon) {
		if(demon.canDown == false) // we check if the demon hit the down wall last time so he doesn't go down again
		{
			demon.canDown = true;
			return false;
		}

		if(demon.X + 1 < covenHelper.N && coven[demon.X + 1][demon.Y] == 0) // if there is no wall down or other demon he can move
			return true;
		if(demon.Y - 1 >= covenHelper.N) { // if he hits the down wall
			demon.penaltyRounds += 2; // his penalty rounds increase with 2
			demon.wallsHit++; // we count how many hit walls he has so at 10 we decrease it's social skill
			demon.canDown = false;  // after he hit the down wall he can't go down again
		}
		if(demon.X + 1 < covenHelper.N && coven[demon.X + 1][demon.Y] != 0) // if he meets another demon
		{
			demon.socialSkill += 50; // if he meets with another demon we increase it's social skill with 50
			for(Demon adjacentDemon: demons) { // we search for the adjacent demon to increase it's social skill too
				if(adjacentDemon.demonID == coven[demon.X + 1][demon.Y])
					adjacentDemon.socialSkill += 50;
			}
		}
		return false;
	}

	/**
	 * Function that checks if the up movement is possible for the demon
	 * @param demon the demon that is trying to move up
	 * @return false if the demon can't move up
	 * @return true if the demon can move up
	 */
	
	private boolean canMoveUp(Demon demon) {
		if(demon.canUp == false) // we check if the demon hit the up wall last time so he doesn't go up again
		{
			demon.canUp = true;
			return false;
		}

		if(demon.X - 1 >= 0 && coven[demon.X - 1][demon.Y] == 0)  // if there is no wall up or other demon he can move
			return true;
		if(demon.X - 1 < 0) {  // if he hits the up wall
			demon.penaltyRounds += 2;  // his penalty rounds increase with 2
			demon.wallsHit++; // we count how many hit walls he has so at 10 we decrease it's social skill
			demon.canUp = false; // after he hit the up wall he can't go up again
		}
		if(demon.X - 1 >= 0 && coven[demon.X - 1][demon.Y] != 0) // if he meets another demon
		{
			demon.socialSkill += 50;// if he meets with another demon we increase it's social skill with 50
			for(Demon adjacentDemon: demons) {  // we search for the adjacent demon to increase it's social skill too
				if(adjacentDemon.demonID == coven[demon.X - 1][demon.Y])
					adjacentDemon.socialSkill += 50;
			}
		}
		return false;
	}

	/**
	 * Function that checks if the right movement is possible for the demon
	 * @param demon the demon that is trying to move right
	 * @return false if the demon can't move right
	 * @return true if the demon can move right
	 */
	
	private boolean canMoveRight(Demon demon) {

		if(demon.canRight == false) { // we check if the demon hit the right wall last time so he doesn't go right again
			demon.canRight = true;
			return false;
		}
		
		if(demon.Y + 1 < covenHelper.N && coven[demon.X][demon.Y + 1] == 0)  // if there is no wall right or other demon he can move
			return true;
		if(demon.Y - 1 >= covenHelper.N) { // if he hits the right wall
			demon.penaltyRounds += 2; // his penalty rounds increase with 2
			demon.wallsHit++; //we count how many hit walls he has so at 10 we decrease it's social skill
			demon.canRight = false; // after he hit the right wall he can't go right again
		}
		if(demon.Y + 1 < covenHelper.N && coven[demon.X][demon.Y + 1] != 0) // if he meets another demon
		{
			demon.socialSkill += 50;// if he meets with another demon we increase it's social skill with 50
			for(Demon adjacentDemon: demons) {
				if(adjacentDemon.demonID == coven[demon.X][demon.Y + 1]) // we search for the adjacent demon to increase it's social skill too
					adjacentDemon.socialSkill += 50;
			}
		}
		return false;
	}
	/**
	 * Function that resets 10% of ingredients used when a witch can't defeat the undead
	 */
	public void lose10PercentIngredients(){
		this.ingredientsLock.lock(); // we lock the ingredientsLock while we work on the ingredientsInCoven map
		
		for(Map.Entry<String, Integer> entry: ingredientsInCoven.entrySet())
		{
			ingredientsInCoven.put(entry.getKey(), (int) (entry.getValue() - 0.1 * entry.getValue())); // we replace the curent value with current value - 0.1 * current value
		}
		
		this.ingredientsLock.unlock(); // we unlock the ingredientsLock 
	}
	
}
