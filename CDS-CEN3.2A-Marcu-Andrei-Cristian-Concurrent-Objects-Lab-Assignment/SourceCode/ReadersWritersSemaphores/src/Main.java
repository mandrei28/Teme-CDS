
public class Main {

	public static void main(String[] args) {
		ReadersWriters rw = new ReadersWriters();
		int readersNumber = 5;
		int writersNumber = 2;
		int i;


		Reader[] readers = new Reader[readersNumber + 1];


		// Initializing the readers

		for(i = 1 ; i <= readersNumber ; i++) {
			readers[i] = new Reader(rw, "Reader" + i);
		}

		Writer[] writers = new Writer[writersNumber + 1];


		// Initializing the writers

		for(i = 1 ; i <= writersNumber ; i++) {
			writers[i] = new Writer(rw, "Writer" + i);
		}


		// Starting the threads execution

		for(i = 1 ; i <= writersNumber ; i++) {
			writers[i].start();
		}
		
		for(i = 1 ; i <= readersNumber ; i++) {
			readers[i].start();
		}


		for(i = 1 ; i <= readersNumber ; i++) {
			try {
				readers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for(i = 1 ; i <= writersNumber ; i++) {
			try {
				writers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
