import java.util.concurrent.Semaphore;

public class ReadersWriters {
	private int readCount = 0;
	private Semaphore writerSemaphore = new Semaphore(1);
	private Semaphore readerSemaphore = new Semaphore(1);
	private Semaphore orderSemaphore = new Semaphore(1);

	public void write(String writer) {


		// Obtaining access to the resource

		try {
			orderSemaphore.acquire();
			writerSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		orderSemaphore.release();

		// Writing into the resource

		try {
			System.out.println(writer + " is writing");
			Thread.sleep(1000);
			System.out.println(writer + " has stopped writing");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		writerSemaphore.release();

	}


	public void read(String reader) {

		// Obtaining access to the queue and the readCount variable

		try {
			orderSemaphore.acquire();
			readerSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		// First reader obtains access to the resource
		// so that writers are blocked

		if(readCount == 0) {
			try {
				writerSemaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Reader starts reading
		readCount++;

		orderSemaphore.release();
		readerSemaphore.release();

		// Reading the resource
		try {
			System.out.println(reader + " is reading");
			Thread.sleep(1000);
			System.out.println(reader + " has stopped reading");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// Obtaining access to modify readCount

		try {
			readerSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Reader stops reading
		readCount--;


		// Releasing access if the current reader is the last reader

		if(readCount == 0) {
			writerSemaphore.release();
		}

		readerSemaphore.release();
	}
}
