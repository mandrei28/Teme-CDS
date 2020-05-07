import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;
public class PCQ {
	Semaphore semP; // semaphore for producer
	Semaphore semC; // semaphore for consumer
	Queue<Integer> queue; //queue where producer inserts numbers
	int queueCapacity = 1;
	Random rand = new Random();// to insert a random value in queue
	int i;
	PCQ(){
		semP = new Semaphore(1);
		semC = new Semaphore(1);
		queue = new LinkedList<>();
	}
	public void prod() throws InterruptedException
	{
		for(int it = 0 ; it < 10 ; it++)
		{
			try {
			semP.acquire();// producer semaphore is locked
			}
			catch (InterruptedException e)
			{
			}
			if(queue.size() < queueCapacity)
			{
				i = rand.nextInt(100);
				queue.add(i);// we add random number from 0 to 100
				System.out.println("Producer added " + i);
			}
			semC.release(); // consumer semaphore is released
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void cons() throws InterruptedException
	{
		for(int it = 0 ; it < 10 ; it++)
		{
			try {
				semC.acquire(); // consumer semaphore is locked
			}
			catch(InterruptedException e)
			{
			}
			if(!queue.isEmpty())
				System.out.println("Consumer removed " + queue.remove());
			semP.release(); // producer semaphore is released
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
