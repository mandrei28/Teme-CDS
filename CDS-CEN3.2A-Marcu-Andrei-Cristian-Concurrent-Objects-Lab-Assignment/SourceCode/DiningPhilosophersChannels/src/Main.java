import java.util.ArrayList;

public class Main {
public static void main(String[] args) throws InterruptedException {
		int philosophersNumber = 4; // no of philosopohers = no of forks
		ArrayList<Channel<Boolean>> forks = new ArrayList<Channel<Boolean>>();
		Fork[] forksArray = new Fork[philosophersNumber];
		Philosopher[] philosophers = new Philosopher[philosophersNumber];
		
		for(int i = 0 ; i < 5 ; ++i) {
			forks.add(new Channel<Boolean>());
		}
						
		
		for (int i = 0; i < philosophersNumber; i++) {
	    	  forksArray[i] = new Fork(i, forks);
	    	  forksArray[i].start();
	    }
		
		for (int i = 0; i < philosophersNumber; i++) {
	        philosophers[i] = new Philosopher(i, (i + 1) % philosophersNumber, "Philo" + i, forks);
	        philosophers[i].start();
	    }
		
		Thread.sleep(5000); // the time is in ms. after this time we stop the execution
		
		
		for(Philosopher philosopher: philosophers)
		{
			philosopher.join();
		}

	}
}
