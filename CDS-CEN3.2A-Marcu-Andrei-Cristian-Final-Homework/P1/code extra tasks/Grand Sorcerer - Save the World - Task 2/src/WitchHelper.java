import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Class that creates the recipes from potions with random ingredients
 * @author marcu
 */
public class WitchHelper {
	public ConcurrentHashMap<String, ArrayList<String>> potions = new ConcurrentHashMap<String, ArrayList<String>>(); // potion recipes
	public ArrayList<String> ingredientsForPotion = new ArrayList<String>(); // ingredients for each potion
	public ArrayList<String> potionNames = new ArrayList<String>(); // names for potions
	public CovenHelper covenHelper = new CovenHelper(); // we need it to access the existent ingredients
	
	WitchHelper(){
		createPotionNames(); // we create the potion names
		for(String potionName: potionNames)
		{
			generateRandomIngredients(); // for each potion we generate random ingredients
			potions.put(potionName, ingredientsForPotion); // we put the ingredients in the recipe map(potions map)
		}
	}
	
	/**
	 * Method that generates a random number of ingredients from 4 to 8 for each potion. We pick random ingredients from the ingredients array present in CovenHelper
	 * We iterate through the ingredients and give each ingredient a 50% chance to be picked
	 * We save the recipes in a Map where the key is the potion name and the value is the ingredients recipe
	 */
	public void generateRandomIngredients() {
		Random rand = new Random();
		ArrayList<String> randomIngredientsForPotion = new ArrayList<String>(); // auxiliary arraylist that will contain the ingredients randomly picked
		ConcurrentHashMap<String, Integer> ingredientFrequency = new ConcurrentHashMap<String, Integer>(); // everytime we add an ingredient we mark it with one so we don't add the same ingredient twice
		for(String ingredient: covenHelper.ingredients)
		{
			ingredientFrequency.put(ingredient, 0); // we initialize the frequency map with 0 for each ingredient
		}
		int noIngredients = rand.nextInt(8 - 3) + 4; // we pick a random number of ingredients required for the potion from 4 to 8
		float chance; // a chance to pick the ingredient
		while(randomIngredientsForPotion.size() < noIngredients) { // while we didn't add enough ingredients in the recipe
			for(String ingredient: covenHelper.ingredients) { // we iterate through all the ingredients
				chance = rand.nextFloat(); // we pick a random float value
				if(chance <= 0.50f && ingredientFrequency.get(ingredient) == 0) { // if we didn't pick the ingredient and the random float value is lesser than 0.50f(50% chance to pick it)
					randomIngredientsForPotion.add(ingredient); // we add the ingredient
					ingredientFrequency.put(ingredient, 1); // we mark the ingredient so we don't add it again
				}
				if(randomIngredientsForPotion.size() == noIngredients) // if we have enough ingredients we stop adding
					break;
			}
		}
		ingredientsForPotion = randomIngredientsForPotion; //the arraylist will contain the random choice of ingredients
	}
	
	/**
	 * Method that creates the potion names
	 * Those are predefined by the user
	 */
	public void createPotionNames() {
		//Predefined potion names
		potionNames.add("Destroyer Potion");
		potionNames.add("Anti-undead Potion");
		potionNames.add("Flame Potion");
		potionNames.add("Strength Potion");
		potionNames.add("Cat Potion");
		
		potionNames.add("Unstoppable Potion");
		potionNames.add("Unkillable Potion");
		potionNames.add("Damage Potion");
		potionNames.add("Mana Potion");
		potionNames.add("Health Potion");
		
		potionNames.add("Tear Potion");
		potionNames.add("Scream Potion");
		potionNames.add("Freeze Potion");
		potionNames.add("Resist Potion");
		potionNames.add("Poison Potion");
		
		potionNames.add("Shock Potion");
		potionNames.add("Fire Potion");
		potionNames.add("Purify Potion");
		potionNames.add("Cleanse Potion");
		potionNames.add("Stealth Potion");
	}
}
