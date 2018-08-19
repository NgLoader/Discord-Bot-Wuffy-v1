package net.wuffy.bot.command;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommandExecuter {

	private final CommandHandler handler;

	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1000);
	private final ExecutorService executorService = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS, this.queue, new ThreadPoolExecutor.DiscardPolicy());

	public CommandExecuter(CommandHandler handler) {
		this.handler = handler;

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Stopping command executorService.");
			this.executorService.shutdown();
		}));
	}

	public void add(Runnable runnable) {
		this.executorService.submit(runnable);
	}

	public CommandHandler getHandler() {
		return this.handler;
	}
}