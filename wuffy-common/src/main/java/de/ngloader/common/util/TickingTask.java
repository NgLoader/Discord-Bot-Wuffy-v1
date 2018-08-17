package de.ngloader.common.util;

import de.ngloader.common.logger.Logger;

/**
 * @author Ingrim4
 * @edited NgLoader
 */
public abstract class TickingTask implements Runnable {

	private final long ms_per_tick;

	protected boolean running = true;
	private long lastTickTime;
	private long lastLaggWarning;

	public TickingTask(long ms_per_tick) {
		this.ms_per_tick = ms_per_tick;
	}

	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void run() {
		try {
			this.lastTickTime = System.currentTimeMillis();
			var timeLeft = 0L;

			while(this.running) {
				var time = System.currentTimeMillis();
				var delta = time - this.lastTickTime;

				if(delta > 2000L && this.lastTickTime - this.lastLaggWarning >= 15000L) {
					Logger.warn(String.format("Can\'t keep up! Did the system time change, or is the server overloaded? Running %dms behind, skipping %d tick(s)", delta, (delta / this.ms_per_tick)));
					delta = 2000;
					this.lastLaggWarning = this.lastTickTime;
				}

				if(delta < 0L) {
					Logger.warn("Time ran backwards! Did the system time change?");
					delta = 0L;
				}

				timeLeft += delta;
				this.lastTickTime = time;
				while(timeLeft > this.ms_per_tick) {
					timeLeft -= this.ms_per_tick;
					this.update();
				}

				Thread.sleep(Math.max(1L, this.ms_per_tick) - timeLeft);
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