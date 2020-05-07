import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {
	Semaphore sem = new Semaphore(1);
    private final int ForkId;

    public Fork(int id) {
      this.ForkId = id;
    }

    public boolean pickUp(String name, String where) throws InterruptedException {
      if (sem.tryAcquire()) {// if the fork is not used the philosopher picks it up
        System.out.println(name + " picked up " + where + " fork-" + ForkId);
        return true;
      }
      return false;
    }

    public void putDown(String name, String where) {
    	sem.release();// the philosopher releases the fork
      System.out.println(name + " put down " + where + " fork-" + ForkId);
    }
}
