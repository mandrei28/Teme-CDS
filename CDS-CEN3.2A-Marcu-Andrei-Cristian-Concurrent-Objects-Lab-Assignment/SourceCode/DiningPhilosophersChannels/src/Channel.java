
public class Channel<T> {
	private T chan = null;
	int ready = 0;
	
	public synchronized void send(T mes) throws InterruptedException {
		// save the message in channel
		chan = mes;
		++ready;
		notifyAll();
		while(chan != null) {
			wait();// wait until the message is received, after that the channel becomes null
		}
	}
	
	
	public synchronized T receive() throws InterruptedException {
		
		while(ready == 0) {
			wait();
		}
		--ready;
		T tmp = chan; // we save the message
		chan = null;
		notifyAll();// notify all the senders
		return(tmp);
	}
}	
