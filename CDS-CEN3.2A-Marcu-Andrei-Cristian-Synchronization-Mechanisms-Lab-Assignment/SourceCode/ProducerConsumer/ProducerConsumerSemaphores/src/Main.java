
public class Main {
	public static void main(String args[]) throws InterruptedException
	{
		PCQ pcq = new PCQ();
	    Thread producer = new Thread(new Producer(pcq));
	    Thread consumer = new Thread(new Consumer(pcq));
	    producer.start();
	    consumer.start();
	    producer.join();
	    consumer.join();   
	}
}
