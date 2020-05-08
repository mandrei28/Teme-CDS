import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class PrimeThreadMethod2 extends Thread{
	private int threadNumber; // Thread identifier
	
	public static volatile ConcurrentHashMap<Integer, ArrayList<Integer> > splitedInterval = new ConcurrentHashMap<Integer, ArrayList<Integer>>(); 
	// I've used a map which keeps the numbers each thread should check
	// So each splitedInterval[threadId] will have an arrayList which contains the numbers it should check.
	// This map is created in main. If we create it inside the class the code will be executed multiple times.
	// I've used ConcurrentHashMap in order to be thread safe.
	public static volatile List<Integer> primeNumbers = Collections.synchronizedList (new ArrayList<Integer>());
	// The list contains the prime numbers we find. It's a synchronizedList which is thread-safe.
	PrimeThreadMethod2(int n, int k, int threadNumber)
	{
		this.threadNumber = threadNumber;		
		if(k > n)
			throw new ArrayIndexOutOfBoundsException("More threads than numbers"); 
		// We can't have more threads than numbers
	}
	public void run()
	{
		for( Entry<Integer, ArrayList<Integer>> entry: PrimeThreadMethod2.splitedInterval.entrySet()) 
			// We iterate through all the elements in map
	    {
	    	if(threadNumber - 1 == entry.getKey()) 
	    		// We check if we found the key which is equal to threadId
	    	{
	    		for(int i: entry.getValue()) 
	    			// We get the arrayList which has the key equal to threadId
	    		{
					Boolean primeFlag = false; 
					//Boolean value that checks if the number is prime or not
					int sqrt = (int) (Math.ceil(Math.sqrt((double) i )));
					// sqrt in order to iterate from 2 to sqrt
					for( int j = 2 ; j <= sqrt; j++ )
					{
						if( i == 2 )
							// 2 is the first prime number
						{
							System.out.println("Method2 " + "Thread number " + threadNumber + " added the number :" + i);
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
						System.out.println("Method2 " + "Thread number " + threadNumber + " added the number :" + i);
						primeNumbers.add(i);
						//If the number it's not prime and it is different than 1 we add it in the ArrayList.
					}
	    		}
	    	}
		}	
	}
}
