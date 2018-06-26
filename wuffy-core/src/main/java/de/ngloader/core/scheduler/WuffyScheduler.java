package de.ngloader.core.scheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.ngloader.core.Core;
import de.ngloader.core.ITickable;
import de.ngloader.core.logger.Logger;

public class WuffyScheduler implements ITickable {

	private final Core core;

	private final AtomicInteger taskIds = new AtomicInteger();

	private final List<WuffyTaskInfo> taskAfter = new LinkedList<>();
	private final List<WuffyTaskInfo> taskRepeat = new LinkedList<>();

	public WuffyScheduler(Core core) {
		this.core = core;
	}

	@Override
	public void update() {
		try {
			for(WuffyTaskInfo taskInfo : taskAfter)
				if(taskInfo.tick()) {
					taskAfter.remove(taskInfo);
					taskInfo.getTask().onRun();
				}

			this.taskRepeat.forEach(taskInfo -> {
				try {
					if(taskInfo.tick())
						taskInfo.getTask().onRun();
				} catch(Exception e) {
					e.printStackTrace();
					Logger.fatal("Tickingtask - Scheduler (Repeat)", "Error by running repeating task. Removing task from scheduler", e);
					taskRepeat.remove(taskInfo);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			Logger.fatal("Tickingtask - Scheduler", "Error by running scheduler", e);
		}
	}

	/**
	 * 
	 * @param task
	 * @param delay in ticks (20 ticks -> 1 second)
	 * @return WuffyInfoTask
	 */
	public int runTaskAfter(WuffyTask task, int delay) {
		return this.runTaskAfter(this.core, task, delay);
	}

	/**
	 * 
	 * @param core
	 * @param task
	 * @param delay in ticks (20 ticks -> 1 second)
	 * @return WuffyInfoTask
	 */
	public int runTaskAfter(Core core, WuffyTask task, int delay) {
		var taskInfo = new WuffyTaskInfo(core, task, this.taskIds.incrementAndGet(), delay);
		this.taskAfter.add(taskInfo);
		return taskInfo.getTaskId();
	}

	/**
	 * 
	 * @param task
	 * @param delay in ticks (20 ticks -> 1 second)
	 * @param repeat in ticks (20 ticks -> 1 second)
	 * @return WuffyInfoTask
	 */
	public int runTaskRepeat(WuffyTask task, int delay, int repeat) {
		return this.runTaskRepeat(this.core, task, delay, repeat);
	}

	/**
	 * 
	 * @param core
	 * @param task
	 * @param delay in ticks (20 ticks -> 1 second)
	 * @param repeat in ticks (20 ticks -> 1 second)
	 * @return WuffyInfoTask
	 */
	public int runTaskRepeat(Core core, WuffyTask task, int delay, int repeat) {
		var taskInfo = new WuffyTaskInfo(core, task, true, this.taskIds.incrementAndGet(), delay, repeat);
		this.taskRepeat.add(taskInfo);
		return taskInfo.getTaskId();
	}

	/**
	 * 
	 * @param task
	 * @return true when the task was stopped or false when the task can not be stopped (not exist)
	 */
	public boolean cancelTask(WuffyTask task) {
		if(!this.taskAfter.stream().anyMatch(taskInfo -> taskInfo.getTask().equals(task)) && !this.taskRepeat.stream().anyMatch(taskInfo -> taskInfo.getTask().equals(task)))
			return false;

		this.taskAfter.removeAll(this.taskAfter.stream().filter(taskInfo -> taskInfo.getTask().equals(task)).collect(Collectors.toList()));
		this.taskRepeat.removeAll(this.taskRepeat.stream().filter(taskInfo -> taskInfo.getTask().equals(task)).collect(Collectors.toList()));
		return true;
	}

	/**
	 * 
	 * @param taskId
	 * @return true when the task was stopped or false when the task can not be stopped (not exist)
	 */
	public boolean cancelTask(int taskId) {
		if(!this.taskAfter.stream().anyMatch(taskInfo -> taskInfo.getTaskId() == taskId) && !this.taskRepeat.stream().anyMatch(taskInfo -> taskInfo.getTaskId() == taskId))
			return false;

		this.taskAfter.removeAll(this.taskAfter.stream().filter(taskInfo -> taskInfo.getTaskId() == taskId).collect(Collectors.toList()));
		this.taskRepeat.removeAll(this.taskRepeat.stream().filter(taskInfo -> taskInfo.getTaskId() == taskId).collect(Collectors.toList()));
		return true;
	}

	/**
	 * 
	 * @param core
	 * @return true when the task was stopped or false when the task can not be stopped (not exist)
	 */
	public boolean cancelTask(Core core) {
		if(!this.taskAfter.stream().anyMatch(taskInfo -> taskInfo.getCore().equals(core)) && !this.taskRepeat.stream().anyMatch(taskInfo -> taskInfo.getCore().equals(core)))
			return false;

		this.taskAfter.removeAll(this.taskAfter.stream().filter(taskInfo -> taskInfo.getCore().equals(core)).collect(Collectors.toList()));
		this.taskRepeat.removeAll(this.taskRepeat.stream().filter(taskInfo -> taskInfo.getCore().equals(core)).collect(Collectors.toList()));
		return true;
	}

	/**
	 * 
	 * @return true when any task was stopped or false when no task exist to stop
	 */
	public boolean cancelAllTasks() {
		if(this.taskAfter.isEmpty() && this.taskRepeat.isEmpty())
			return false;

		this.taskAfter.clear();
		this.taskRepeat.clear();
		return true;
	}

	public Core getCore() {
		return core;
	}
}