import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCQ {
	int queueCapacity = 5;
	Random rand = new Random();
	int i;
	Queue<Integer> queue; // queue where producer inserts numbers
	Lock lock; // lock used for synchronization
	Condition notFull; // lock conditions used for notfull/notempty cases
	Condition notEmpty;
	PCQ(){
		queue = new LinkedList<>();
		lock =  new ReentrantLock();
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
	}
	public void prod() throws InterruptedException
	{
		for(int it = 0 ; it < 10 ; it++)
		{
			lock.lock();
			try {
				while(queue.size() == queueCapacity)
					notEmpty.await();// producer waits until the queue is not full
				i = rand.nextInt(100);
				System.out.println("Producer added " + i);
				queue.add(i);// we add a random number from 0 to 100
				Thread.sleep(500);
				notFull.signalAll();
			}
			finally {
				lock.unlock();
			}
		}
	}
	
	public void cons() throws InterruptedException
	{
		for(int it = 0; it < 10; it++)
		{
			lock.lock();
			try {
				while(queue.isEmpty())
					notFull.await();// consumer waits until the queue is not empty
				System.out.println("Consumer removed " + queue.remove());
				Thread.sleep(2000);
				notEmpty.signalAll();
			}
			finally {
				lock.unlock();
			}
		}
	}
}
