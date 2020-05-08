import java.util.ArrayList;

public class Fork extends Thread {

		private Boolean isReceived;
		private int i;
		private ArrayList<Channel<Boolean>> forks;
		
		public Fork(int i, ArrayList<Channel<Boolean>> forks) {
			this.i = i;
			this.forks = forks;
		}
		public void run() {
			
			while(true) {
	
				try {
					(forks.get(i)).send(true);
					isReceived = (forks.get(i)).receive();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
}
