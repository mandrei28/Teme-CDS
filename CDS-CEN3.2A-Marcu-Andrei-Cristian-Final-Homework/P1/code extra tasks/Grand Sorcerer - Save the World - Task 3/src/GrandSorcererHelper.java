
/**
 * Class that creates the demon spawner
 * @author marcu
 */
public class GrandSorcererHelper {
	GrandSorcerer grandSorcerer;
	DemonSpawner demonSpawners[];
	
	/**
	 * Creates the demon spawners and starts them
	 * @param grandSorcerer - used in order to acces the covens and covens number
	 */
	GrandSorcererHelper(GrandSorcerer grandSorcerer)
	{
		this.grandSorcerer = grandSorcerer;
		demonSpawners = new DemonSpawner[this.grandSorcerer.covensNo]; // the number of demon spawners is equal to the number of covens. each coven will have a spawner
		for(Coven coven: this.grandSorcerer.covens)
		{
			for(int i = 0; i < this.grandSorcerer.covensNo; i++)
			{
				this.demonSpawners[i] = new DemonSpawner(coven);
				this.demonSpawners[i].start(); // we start the spawners
			}
		}
	}
}
