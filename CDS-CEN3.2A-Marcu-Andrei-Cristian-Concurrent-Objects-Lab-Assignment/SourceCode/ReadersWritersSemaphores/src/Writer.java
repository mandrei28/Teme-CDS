
public class Writer extends Thread {
	ReadersWriters rw = new ReadersWriters();
	String name;
	Writer(ReadersWriters rw, String name)
	{
		this.rw = rw;
		this.name = name;
	}
	public void run() {
		for(int i = 0; i < 10; i++)
		{
			rw.write(name);
		}
	}
}
