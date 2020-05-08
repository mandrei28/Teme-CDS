import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCQ {
	int queueCapacity = 5;
	Random rand = new Random();
	int i;
	ConcurrentLinkedQueue<Integer> queue; // queue where producer inserts numbers
	Lock lock; // lock used for synchronization
	Condition notFull; // lock conditions used for notfull/notempty cases
	Condition notEmpty;
	PCQ(){
		queue = new ConcurrentLinkedQueue<Integer>(); // thread safe queue
		lock =  new ReentrantLock();
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
	}
	public void prod(int id)
	{
		for(int it = 0 ; it < 10 ; it++)
		{
			lock.lock();
			try {
				while(queue.size() == queueCapacity)
					try {
						notFull.await(); // producer waits until the queue is not full
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				i = rand.nextInt(100);
				System.out.println("Producer " + id + " added " + i);
				queue.add(i);// we add a random number from 0 to 100
				notEmpty.signalAll(); // we inform the consumer that a new item was produced and queue can't be empty
			}
			finally {
				lock.unlock();
			}
		}
	}
	
	public void cons(int id)
	{
		for(int it = 0; it < 10; it++)
		{
			lock.lock();
			try {
				while(queue.isEmpty())
					try {
						notEmpty.await(); // consumer waits until the queue is not empty
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				System.out.println("Consumer " + id + " removed " + queue.remove());
				notFull.signalAll(); // we inform the producer that an item was removed and the queue can't be full
			}
			finally {
				lock.unlock();
			}
		}
	}
}
