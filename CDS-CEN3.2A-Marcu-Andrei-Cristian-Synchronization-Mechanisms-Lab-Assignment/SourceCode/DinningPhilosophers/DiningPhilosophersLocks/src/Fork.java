import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {
    Lock lock = new ReentrantLock();
    private final int ForkId;

    public Fork(int id) {
      this.ForkId = id;
    }

    public boolean pickUp(String name, String where) throws InterruptedException {
      if (lock.tryLock()) {
        System.out.println(name + " picked up " + where + " fork-" + ForkId);
        return true;
      }
      return false;
    }

    public void putDown(String name, String where) {
    	lock.unlock();
      System.out.println(name + " put down " + where + " fork-" + ForkId);
    }
}
