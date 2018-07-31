package de.ngloader.notification;

public abstract class TickingTask implements Runnable {

	private static final long MS_PER_TICK = 1000L;

	protected boolean running = true;

	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void run() {
		try {

			while(this.running) {
				this.update();
				Thread.sleep(MS_PER_TICK);
			}
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			this.stop();
		}
	}

	protected abstract void update();

	protected abstract void stop();
}