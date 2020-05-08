import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Class that initializes the ingredients and the ingredients in coven map
 * @author marcu
 *
 */
public class CovenHelper {
	public String[] ingredients = {"Sugar", "Dragon's Breath", "Amethyst", "Cactus", "Turtle Shell",
			"Skull", "Oregano", "Moss", "Cow's Tongue", "Rat Parts"}; // predefined ingredients
	public ConcurrentHashMap<String, Integer> ingredientsInCoven = new ConcurrentHashMap<String, Integer>(); // the ingredientsInCoven map with value 0 for each ingredient from the predefined array
	Integer[][] coven; // coven
	Random random = new Random();
	public int N;
	private int i, j;
	/**
	 * We initialize the ingredientsInCoven map and the coven matrix 
	 */
	CovenHelper(){
		for(String ingredient: ingredients) {
			ingredientsInCoven.put(ingredient, 0); // we initialize the ingredients map with 0
		}
		this.N = random.nextInt(500 - 99) + 100; // random matrix size between 100-500
		coven = new Integer[N][N]; // we initialize the square matrix with N size
		for(i = 0; i < N; i++)
			for(j = 0; j < N; j++)
			{
				coven[i][j] = 0;// we initialize the coven map with 0. we will pass this value to each matrix from the coven in the beggining
			}
	}
}
