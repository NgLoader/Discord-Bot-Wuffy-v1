package de.ngloader.core.scheduler;

import de.ngloader.core.Core;

public class WuffyTaskInfo {

	private final Core core;

	private final int taskId;

	private final WuffyTask task;

	private int delay = 0;
	private int ticks = 0;

	private int currentlyDelay = 0;
	private int currentlyTick = 0;

	private boolean loop = false;

	public WuffyTaskInfo(Core core, WuffyTask task, int taskId, int delay) {
		this.core = core;
		this.task = task;
		this.taskId = taskId;
		this.delay = delay;
		this.currentlyDelay = delay;
	}

	public WuffyTaskInfo(Core core, WuffyTask task, int taskId, int delay, int ticks) {
		this.core = core;
		this.task = task;
		this.taskId = taskId;
		this.delay = delay;
		this.currentlyDelay = delay;
		this.ticks = ticks;
		this.currentlyTick = ticks;
	}

	public WuffyTaskInfo(Core core, WuffyTask task, boolean loop, int taskId, int delay, int ticks) {
		this.core = core;
		this.task = task;
		this.taskId = taskId;
		this.loop = loop;
		this.delay = delay;
		this.currentlyDelay = delay;
		this.ticks = ticks;
		this.currentlyTick = ticks;
	}

	public boolean tick() {
		if(this.currentlyDelay != 0)
			this.currentlyDelay--;
		else if(this.currentlyTick != 0)
			this.currentlyTick--;
		else {
			if(this.loop)
				this.currentlyTick = this.ticks;
			return true;
		}
		return false;
	}

	public Core getCore() {
		return this.core;
	}

	public int getCurrentlyDelay() {
		return this.currentlyDelay;
	}

	public int getCurrentlyTick() {
		return this.currentlyTick;
	}

	public int getDelay() {
		return this.delay;
	}

	public int getTicks() {
		return this.ticks;
	}

	public int getTaskId() {
		return this.taskId;
	}

	public WuffyTask getTask() {
		return this.task;
	}

	public boolean isLoop() {
		return loop;
	}
}