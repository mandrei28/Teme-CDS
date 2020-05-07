public class Producer extends Thread {
	PCQ pcq;
	Producer(PCQ pcq)
	{
		this.pcq = pcq;
	}
	public void run()
	{
		try {
			pcq.prod();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
