package net.wuffy.notification;

public abstract class TickingTask implements Runnable {

	private static final long MS_PER_TICK = 1000L;

	protected boolean running = false;

	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void run() {
		try {
			this.running = true;

			while(this.running) {
				this.update();
				Thread.sleep(MS_PER_TICK);
			}
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			this.running = false;

			this.stop();
		}
	}

	protected abstract void update();

	protected abstract void stop();
}