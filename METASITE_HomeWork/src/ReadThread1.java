public class ReadThread1 extends Thread {
	private Launcher1 tm;

	public ReadThread1(Launcher1 tm) {
		this.tm = tm;
	}

	public void run() {

		tm.readingFile();
	}

}
