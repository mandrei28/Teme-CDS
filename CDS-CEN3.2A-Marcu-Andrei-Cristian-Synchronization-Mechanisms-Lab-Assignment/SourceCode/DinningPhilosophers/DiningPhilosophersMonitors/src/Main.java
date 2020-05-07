
public class Main {

	public static void main(String[] args) throws InterruptedException {
		int philosophersNumber = 2; // no of philosopohers = no of forks

	    Philosopher[] philosophers = null;

	      philosophers = new Philosopher[philosophersNumber];
	      Fork[] forks = new Fork[philosophersNumber];
	      
	      for (int i = 0; i < philosophersNumber; i++) {
	    	  forks[i] = new Fork(i);
	      }

	      for (int i = 0; i < philosophersNumber; i++) {
	        philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % philosophersNumber]);
	        philosophers[i].start();
	      }
	      Thread.sleep(5000); // the time is in ms. after this time we stop the execution
	      for (Philosopher philosopher : philosophers) { 
	        philosopher.stop = true; // to stop the execution
	        philosopher.join(); // join to let the thread finish it's last loop
	      }
	      for (Philosopher philosopher2 : philosophers)
	          System.out.println("Philosopher-" + philosopher2.philosopherId + " has eat " + philosopher2.hasEat);
	      // we print the number of pieces each philosopher ate
	}

}
