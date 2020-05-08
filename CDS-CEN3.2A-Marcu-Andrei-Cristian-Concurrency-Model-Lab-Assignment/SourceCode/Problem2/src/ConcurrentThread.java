
public class ConcurrentThread extends Thread {
	static volatile int n = 0; // I've used static in order to keep it's value in all the instances of ConcurrentThread class. 
	//Also it is a volatile variable because the cache memory of each thread can alter the value, so using volatile will make it thread-safe, the value being saved in memory instead of CPU cache.
	String message;// To keep an identification for threads.
	private int temp;
	int iterations = 500000;
	
	public ConcurrentThread(String message)
	{
		this.message = message; // thread id
		this.temp = 0; // temp value is initially 0
	}
	
	public void run() {
		for(int i = 1 ; i <= iterations; i++)
		{
			this.temp = n;
			n = this.temp + 1;
			System.out.println("The value of n in " + this.message + " iteration " + i + " is " + n);
		}
	}
}
