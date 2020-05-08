
public class Producer implements Runnable {
	PCQ pcq;
	int id;
	Producer(PCQ pcq, int id)
	{
		this.pcq = pcq;
		this.id = id;
	}
	public void run()
	{
		while(true)
		{
			this.pcq.prod(id);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
