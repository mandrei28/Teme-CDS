
public class Consumer implements Runnable {
	PCQ pcq;
	int id;
	Consumer(PCQ pcq, int id)
	{
		this.pcq = pcq;
		this.id = id;
	}
	public void run()
	{
		while(true) {
			this.pcq.cons(id);
			
			try {	
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
