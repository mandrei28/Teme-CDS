
public class Producer extends Thread {
	PCQ pcq;
	Producer(PCQ pcq)
	{
		this.pcq = pcq;
	}
	public void run()
	{
		try {
			this.pcq.prod();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
