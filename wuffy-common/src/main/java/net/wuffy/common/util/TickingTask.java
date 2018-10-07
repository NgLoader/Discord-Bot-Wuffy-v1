package net.wuffy.common.util;

import net.wuffy.common.logger.Logger;

/**
 * @author Ingrim4
 * @edited NgLoader
 */
public abstract class TickingTask implements Runnable {

	private final long msPerTick; //20 (1000 / 50)
	private final long deltaHigher; //2000
	private final long lastLaggWarningHigher; //15000

	protected boolean running = true;
	private long lastTickTime;
	private long lastLaggWarning;

	public TickingTask(long msPerTick) {
		this.msPerTick = msPerTick;

		//TODO auto calculate by ms_per_tick
		this.deltaHigher = 2000;
		this.lastLaggWarningHigher = 15000;
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

				if(delta > this.deltaHigher && this.lastTickTime - this.lastLaggWarning >= this.lastLaggWarningHigher) {
					Logger.warn(String.format("Can\'t keep up! Did the system time change, or is the server overloaded? Running %dms behind, skipping %d tick(s)", delta, (delta / this.msPerTick)));
					delta = this.deltaHigher;
					this.lastLaggWarning = this.lastTickTime;
				}

				if(delta < 0L) {
					Logger.warn("Time ran backwards! Did the system time change?");
					delta = 0L;
				}

				timeLeft += delta;
				this.lastTickTime = time;
				while(timeLeft > this.msPerTick) {
					timeLeft -= this.msPerTick;
					this.update();
				}

				Thread.sleep(Math.max(1L, this.msPerTick) - timeLeft);
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