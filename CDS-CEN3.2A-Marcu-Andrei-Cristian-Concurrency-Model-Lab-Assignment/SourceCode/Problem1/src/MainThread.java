import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random; 

public class MainThread {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("execution-output.txt", "UTF-8"); // writer used to print in file
		Random rand = new Random(); 
		// random input
		int n = rand.nextInt(2000000);
		// random int from 1 to 2000000
	    int k = rand.nextInt(3000);
	    //int n = 87234828;
	    //int k = 2000;

	    while(k > n)
	    {
	    	k = rand.nextInt(2000); 
	    	// we make sure that we don't have more threads than numbers
	    }    

	    long startTime = System.currentTimeMillis(); 
	    // the time when we start the threads
	    
	    PrimeThreadMethod1[] t = new PrimeThreadMethod1[k];
	    for(int i = 1; i <= k; i++ ){
	       t[i-1] = new PrimeThreadMethod1(n, k, i);
	       //We create k threads and start them
	       t[i-1].start();
	    }
	    //We wait for the threads to finish
	    for(int i = 0; i < k; i++ ){
	       try {
	           t[i].join();
	       } catch (InterruptedException e) {
	           e.printStackTrace();
	       }
	    }
	    //the time when we finish
	    long stopTime = System.currentTimeMillis();
	    //the time of execution
	    long elapsedTime = stopTime - startTime;
	    
	    //We print the result in file
	    writer.println("#####################Method1#####################");
	    writer.println("Numbers from 1 to " + n);
	    writer.println("Number of threads is " + k);
	    writer.println("Execution time : " + elapsedTime/1000 + "," + elapsedTime%1000 + "s");
	    writer.println("All the prime numbers:" + PrimeThreadMethod1.primeNumbers.toString());
	    //Random lines to separate the methods
	    writer.println("###########################################################################");
	    writer.println("###########################################################################");
	    writer.println("###########################################################################");
	    writer.println("###########################################################################");
	    writer.println("###########################################################################");
	    
	    for(int i = 1; i <= n; i++)
	    	// We create the splittedInterval map
	    {
	    	if(i % (k+1) == 0 && i > (k + 1))
	    	{
	    		continue; 
	    		// If i is divided by (k+1) without rest and it is bigger than (k+1) it can't be prime number
	    	}
	    	if(!PrimeThreadMethod2.splitedInterval.containsKey(i % (k+1)))
	    		// The key will be the threadId
	    	{
	    		PrimeThreadMethod2.splitedInterval.put(i % (k+1), new ArrayList<Integer>());
	    		//We check if we already created the key in the map. If not we create it
	    	}
	    	if(i % (k + 1) == k) {
	    		PrimeThreadMethod2.splitedInterval.get(1).add(i);
	    		// My threads are from 0 to k - 1. The operation % (k + 1) will generate results from 0 to k.
	    		// So if the result is k we move that value to thread 1.
	    		continue;
	    	}
	    	//We add the value in the arrayList
	    	PrimeThreadMethod2.splitedInterval.get(i % (k+1)).add(i);
	    }
	    
	    
	    startTime = System.currentTimeMillis(); 
	    // the time when we start the threads
	    
	    PrimeThreadMethod2[] t2 = new PrimeThreadMethod2[k];
	    for(int i = 1; i <= k; i++ ){
	       t2[i-1] = new PrimeThreadMethod2(n, k, i);
	       //We create k threads and start them
	       t2[i-1].start();
	    }
	    //We wait for the threads to finish
	    for(int i = 0; i < k; i++ ){
	       try {
	           t2[i].join();
	       } catch (InterruptedException e) {
	           e.printStackTrace();
	       }
	    }
	    //the time when we finish
	    stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    //the time of execution
	    
	    //We print the result in file
	    writer.println("#####################Method2#####################");
	    writer.println("All the prime numbers:" + PrimeThreadMethod2.primeNumbers.toString());
	    writer.println("Numbers from 1 to " + n);
	    writer.println("Number of threads is " + k);
	    writer.println("Execution time : " + elapsedTime/1000 + "," + elapsedTime%1000 + "s");
	    
	    System.out.println("Check output file"); 
	    // Console warning that tells us the results are in folder.
	    writer.close(); 
	    // We close the file writer.
	}
}
