public class Fork {
    private final int ForkId;
    static boolean state = false; // the state for each fork

    public Fork(int id) {
      this.ForkId = id;
    }
    // we make the methods synchronized in order to use monitors
    public synchronized boolean pickUp(String name, String where) throws InterruptedException {
      if (state == false) {// if the fork it's not picked up
        System.out.println(name + " picked up " + where + " fork-" + ForkId);
        state = true; // the forks is now picked up.
        return true;
      }
      return false;
    }
 // we make the methods synchronized in order to use monitors
    public synchronized void putDown(String name, String where) {
      System.out.println(name + " put down " + where + " fork-" + ForkId);
      state = false; // the fork is put down
    }
}
