import java.util.ArrayList;

public class Philosopher extends Thread {
	private Boolean isReceived;
	private String name;
	private int leftFork;
	private int rightFork;
	private ArrayList<Channel<Boolean>> forks;


	public Philosopher(int leftFork, int rightFork, String name, ArrayList<Channel<Boolean>> forks2) {
		this.leftFork = leftFork;
		this.rightFork = rightFork;
		this.forks = forks2;
		this.name = name;
	}

	public void run() {

		while(true) {


			try {
				System.out.println(name + " is thinking");
				Thread.sleep(2000);
				isReceived = (forks.get(leftFork)).receive();
				isReceived = (forks.get(rightFork)).receive();
				System.out.println(name + " is eating");
				Thread.sleep(2000);
				(forks.get(leftFork)).send(true);
				(forks.get(rightFork)).send(true);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		}
	}
}
