/**
 * Class that starts the cicle execution
 * @author marcu
 */
public class Main {

	public static void main(String[] args) {
		GrandSorcerer grandSorcerer = new GrandSorcerer();
		grandSorcerer.start(); // we start the grand sorcerer which will begin the cicle of thread starts
		//the grand sorcerer will create the grand sorcerer helper which will create the demon spawners
		//the grand sorcerer will create covens, witches and undeads

	}

}
