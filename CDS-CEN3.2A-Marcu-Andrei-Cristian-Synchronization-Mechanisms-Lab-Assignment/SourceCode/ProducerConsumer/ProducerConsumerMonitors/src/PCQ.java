import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class PCQ {
	int queueCapacity = 5;
	Random rand = new Random();
	int i;
	Queue<Integer> queue; // queue where producer inserts numbers
	PCQ(){
		queue = new LinkedList<>();
	}
	public void prod() throws InterruptedException
	{
		for(int it = 0 ; it < 10 ; it++)
		{
			synchronized(this) // synchronized keyword for monitors
			{
				while(queue.size() == queueCapacity)
					wait();// producer waits until the queue is not full
				i = rand.nextInt(100);
				System.out.println("Producer added " + i);
				queue.add(i); // we add random number from 0 to 100
				notify(); // producer notifies that he added a number
				Thread.sleep(500);
			}
		}
	}
	
	public void cons() throws InterruptedException
	{
		for(int it = 0; it < 10; it++)
		{
			synchronized (this) // synchronized keyword for monitors
			{
				while(queue.isEmpty())
					wait();// consumer waits until the queue is not empty
				System.out.println("Consumer removed " + queue.remove());
				notify();// consumer notifies that he removed a number
				Thread.sleep(2000);
			}
		}
	}
}
