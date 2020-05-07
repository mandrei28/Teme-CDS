
public class Consumer extends Thread {
	PCQ pcq;
	Consumer(PCQ pcq)
	{
		this.pcq = pcq;
	}
	public void run()
	{
		try {
			this.pcq.cons();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
