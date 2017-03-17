
public class WriteThread1 extends Thread {
	private Launcher1 tm;

	public WriteThread1(Launcher1 tm) {
		this.tm = tm;
	}

	public void run() {

		tm.writingFile();
	}
}
