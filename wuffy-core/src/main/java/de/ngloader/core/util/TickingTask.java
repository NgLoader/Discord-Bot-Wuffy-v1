package de.ngloader.core.util;

import de.ngloader.api.WuffyServer;

/**
 * @author Ingrim4
 */
public abstract class TickingTask implements Runnable {

	private static final long MS_PER_TICK = 1000L /50L;

	protected boolean running = true;
	private long lastTickTime;
	private long lastLaggWarning;

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
					WuffyServer.getLogger().warn(String.format("Can\'t keep up! Did the system time change, or is the server overloaded? Running %dms behind, skipping %d tick(s)", delta, (delta / MS_PER_TICK)));
					delta = 2000;
					this.lastLaggWarning = this.lastTickTime;
				}

				if(delta < 0L) {
					WuffyServer.getLogger().warn("Time ran backwards! Didi the system time change?");
					delta = 0L;
				}

				timeLeft += delta;
				this.lastTickTime = time;
				while(timeLeft > MS_PER_TICK) {
					timeLeft -= MS_PER_TICK;
					this.update();
				}

				Thread.sleep(Math.max(1L, MS_PER_TICK) - timeLeft);
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