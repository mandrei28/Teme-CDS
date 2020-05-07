
public class Consumer extends Thread{
	PCQ pcq;
	Consumer(PCQ pcq)
	{
		this.pcq = pcq;
	}
	public void run()
	{
		try {
			pcq.cons();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
