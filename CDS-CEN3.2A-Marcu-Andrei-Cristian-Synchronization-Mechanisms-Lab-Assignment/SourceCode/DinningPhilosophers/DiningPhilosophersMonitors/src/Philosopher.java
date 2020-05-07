
public class Philosopher extends Thread {
	 public int philosopherId;
	    private Fork leftFork; // each philosopher has two forks, left and right
	    private Fork rightFork;
	    public boolean stop = false;  // used to stop the execution after a certain time
	    int hasEat = 0;

	    public Philosopher(int id, Fork leftFork , Fork rightFork ) {
	      this.philosopherId = id;
	      this.leftFork = leftFork;
	      this.rightFork = rightFork;
	    }

	    @Override
	    public void run() {

	      try {
	        while (!stop) {
	          think();
	          if (leftFork.pickUp("Philosopher-" + philosopherId, "left")) {
	            if (rightFork.pickUp("Philosopher-" + philosopherId, "right")) {
	              eat();
	              rightFork.putDown("Philosopher-" + philosopherId, "right");
	            }
	            leftFork.putDown("Philosopher-" + philosopherId, "left");
	          }
	        }
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }

	    private void think() throws InterruptedException {
	      System.out.println("Philosopher-" + philosopherId + " is thinking");
	      Thread.sleep(1000);
	    }

	    private void eat() throws InterruptedException {
	      System.out.println("Philosopher-" + philosopherId + " is eating");
	      hasEat++;//how many times the philosopher ate
	      Thread.sleep(1000);
	    }
}
