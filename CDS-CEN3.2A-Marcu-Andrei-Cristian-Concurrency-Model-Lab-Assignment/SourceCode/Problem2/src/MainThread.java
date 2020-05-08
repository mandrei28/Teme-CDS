import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MainThread {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("execution-output.txt", "UTF-8"); // writer used to print in file
		//We create 2 threads
		ConcurrentThread Thread1 = new ConcurrentThread("Thread1");    
        ConcurrentThread Thread2 = new ConcurrentThread("Thread2");
        //We start both threads
        Thread1.start();
        Thread2.start();
        //We wait until threads finish
        try {
        	Thread1.join();
        	Thread2.join();
        }
        catch(InterruptedException e){
        	e.printStackTrace();
        }
        //We print the value of n
        System.out.println("Check the file for the last value of n");
        writer.println("After " + Thread1.iterations + " iterations from each thread n will have the following value: " + ConcurrentThread.n);
        //The conclusion is that at a bigger number of iterations n won't have the value we expect( 2 * no of iterations ) because of the synchronization. 
        //To avoid this problem we should use synchronized. Synchronization makes the variable available to only one thread at a moment of time
        writer.close();
	}

}
