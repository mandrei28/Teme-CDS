import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrimeThreadMethod1 extends Thread{
	private int threadNumber; // thread it
	private int n, k, q, r;
	
	public static volatile List<Integer> primeNumbers = Collections.synchronizedList (new ArrayList<Integer>());
	// In this list we add prime numbers everytime we find one.
	//It's a synchronizedList in order to be thread-safe.
	PrimeThreadMethod1(int n, int k, int threadNumber)
	{
		this.n = n;
		this.k = k;
		this.threadNumber = threadNumber;
		this.q = this.n / this.k;
		this.r = n % k;
		
		if(k > n)
			throw new ArrayIndexOutOfBoundsException("More threads than numbers"); 
		// We can't have more threads than numbers.
	}
	public void run()
	{
		//The general formula doesn't treat the case from [1, r]. So i've added the numbers from [1, r] to thread number 1.
		int rest;
		if(threadNumber == 1)
			rest = 0;
		else
			rest = r;
		for( int i = (threadNumber - 1) * q + rest + 1; i <= threadNumber * q + r; i++)	
			//We split the interval in multiple invervals using the threadNumber
		{
			Boolean primeFlag = false;
			//Boolean value that checks if the number is prime or not
			int sqrt = (int) (Math.ceil(Math.sqrt((double) i )));
			// sqrt in order to iterate from 2 to sqrt
			for( int j = 2 ; j <= sqrt; j++ )
			{
				if( i == 2 )// 2 is the first prime number
				{
					System.out.println("Method1 " + "Thread number " + threadNumber + " added the number :" + i);
					primeFlag = true;
					primeNumbers.add(i);
					// we add 2 in the ArrayList, we make the flag true so we don't add 2 two times, and we break the loop because the next values are pointless.
					break;
				}
				if(i % j == 0)
				{
					primeFlag = true;
					//we've found a divider, the number is not prime. We break the loop because the next values are pointless.
					break;
				}
			}
			if(primeFlag == false && i != 1)
			{
				System.out.println("Method1 " + "Thread number " + threadNumber + " added the number :" + i);
				primeNumbers.add(i);
				//If the number it's not prime and it is different than 1 we add it in the ArrayList.
			}
		}
	}
}
