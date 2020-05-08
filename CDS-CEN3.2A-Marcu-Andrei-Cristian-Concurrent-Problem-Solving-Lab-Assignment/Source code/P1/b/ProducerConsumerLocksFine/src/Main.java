
public class Main {

	public static void main(String[] args) throws InterruptedException {
		PCQ pcq = new PCQ();
	    
	    int producers = 4;
	    
	    int consumers = Runtime.getRuntime().availableProcessors();

	    for(int i = 0 ; i < producers; i++)
	    {
	    	PCExecutor prodExec = new PCExecutor();
	    	prodExec.execute(new Producer(pcq, i));
	    }
	    for(int i = 0 ; i < consumers; i++)
	    {
	    	PCExecutor consExec = new PCExecutor();
	    	consExec.execute(new Consumer(pcq, i));
	    }
	    	    
	}
}
