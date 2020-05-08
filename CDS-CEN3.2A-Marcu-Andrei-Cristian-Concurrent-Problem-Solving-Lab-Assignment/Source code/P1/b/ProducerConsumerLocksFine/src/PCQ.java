import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PCQ {
	int queueCapacity = 5;
	Random rand = new Random();
	int i;
	ConcurrentLinkedQueue<Integer> queue; // queue where producer inserts numbers
	ReentrantLock lock[] = new ReentrantLock[queueCapacity + 1]; // lock used for synchronization
	Condition notFull[] = new Condition[queueCapacity + 1]; // lock conditions used for notfull/notempty cases
	Condition notEmpty[] = new Condition[queueCapacity + 1];
	PCQ(){
		queue = new ConcurrentLinkedQueue<Integer>(); // thread safe queue
		for(int i = 0 ; i <= this.queueCapacity; i++) {
			lock[i] =  new ReentrantLock();
			notFull[i] = lock[i].newCondition();
			notEmpty[i] = lock[i].newCondition();
		}
	}
	public void prod(int id)
	{
		int lockNumber = queue.size();
		lock[lockNumber].lock();
		try {
			while(queue.size() == queueCapacity) {
				try {
					notFull[lockNumber].await();// producer waits until the queue is not full
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			i = rand.nextInt(100);
			System.out.println("Producer " + id + " added " + i);
			queue.add(i);// we add a random number from 0 to 100
			notEmpty[lockNumber].signal();
		}
		finally {
			lock[lockNumber].unlock();
		}
	}
	
	public void cons(int id)
	{
		
		int lockNumber = queue.size();
		
		lock[lockNumber].lock();
		try {
			while(queue.isEmpty()) {
				try {
					notEmpty[lockNumber].await(); // consumer waits until the queue is not empty
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Consumer " + id + " removed " + queue.remove());
			notFull[lockNumber].signal();
		}
		finally {
			lock[lockNumber].unlock();
		}
	}
}
