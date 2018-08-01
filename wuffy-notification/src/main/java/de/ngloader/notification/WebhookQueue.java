package de.ngloader.notification;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.Document;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.util.Pair;
import de.ngloader.core.util.WebhookUtil;

public class WebhookQueue implements Runnable {

	private static final Queue<Pair<String, String>> QUEUE = new ConcurrentLinkedQueue<Pair<String, String>>();
	private static final WebhookQueue WEBHOOK_QUEUE = new WebhookQueue();
	private static final long MS_PER_TICK = 200L;

	private static boolean working = false;

	static {
		new Thread(() -> {
			WebhookQueue.WEBHOOK_QUEUE.run();
		}, "Wuffy notification - Webhook queue").start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Executing queue started.");

			WebhookQueue.WEBHOOK_QUEUE.running = false;

			int currently = 0;
			for(Pair<String, String> pair : WebhookQueue.QUEUE) {
				currently++;

				System.out.println("Executing queue: " + currently + "/" + WebhookQueue.QUEUE.size() + ".");
				System.out.println(String.format("[Webhook queue] response: \"%s\".", WebhookUtil.send(pair.getKey(), pair.getValue())));

				try {
					Thread.sleep(MS_PER_TICK);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Executing queue finished.");
		}));
	}

	public static void add(String webhook, String message) {
		while(WebhookQueue.working || WebhookQueue.QUEUE.size() > 1000)
			try {
				if(!WebhookQueue.working) {
					WebhookQueue.working = true;

					Logger.debug("Webhook queue", "queue has 1000 entrys. blocking add thread and wait until queue is empty!");
				}

				Thread.sleep(WebhookQueue.MS_PER_TICK);

				if(WebhookQueue.QUEUE.isEmpty()) {
					Logger.debug("Webhook queue", "queue is now empty. unblocking threads!");
					WebhookQueue.working = false;
				}
			} catch (Exception e) {
				Logger.fatal("Webhook queue", "Error by waiting until queue is empty", e);
				WebhookQueue.working = false;
			}

		WebhookQueue.QUEUE.add(new Pair<String, String>(webhook, message));
	}

	protected boolean running = true;

	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void run() {
		try {
			int fails = 0;
			String message = null;

			Pair<String, String> retry = null;

			while(this.running) {
				try {
					if(!WebhookQueue.QUEUE.isEmpty()) {
						Pair<String, String> pair = retry != null ? retry : WebhookQueue.QUEUE.poll();

						String response = WebhookUtil.send(pair.getKey(), pair.getValue());
						Logger.debug("Webhook queue", String.format("response: \"%s\".", response));

						if(!response.isEmpty()) {
							try {
								Document document = Document.parse(response);

								if(document.containsKey("message") && document.containsKey("retry_after")) {
									Logger.warn("Webhook queue", String.format("%s. Retry in: %s.", document.getString("message"), document.getInteger("retry_after")));
									Thread.sleep(document.getInteger("retry_after"));

									if(retry == null || !retry.equals(pair))
										retry = pair;
									else {
										retry = null;
										Logger.warn("Webhook queue", "Removing currently object from queue. because it has two trys!");
									}
								} else if(document.containsKey("message"))
									Logger.warn("Webhook queue", String.format("Message: %s", document.getString("message")));
							} catch(Exception e) {
								Logger.fatal("Webhook queue", "Failed to process response.", e);
							}
						} else if(retry != null)
							retry = null;
					}
					Thread.sleep(MS_PER_TICK);

					//Resetting error report (last message was success)
					fails = 0;
					message = null;
				} catch(Exception e) {
					Logger.fatal("Webhook queue", "Failed to execute queue.", e);

					if(message == null)
						message = e.getClass().getSimpleName();
					else if(message.equals(e.getClass().getSimpleName()))
						fails++;
					else
						message = e.getClass().getSimpleName();

					if(fails == 2) {
						try {
							WebhookQueue.QUEUE.poll();
							Logger.fatal("Webhook queue", "Reachted to fails with the same message. removing one element form queue!", null);
						} catch(Exception e2) {
							WebhookQueue.QUEUE.clear();
							Logger.fatal("Webhook queue", "Failed to remove one element from queue. Clearing queue!", null);
						}
					}
				}
			}
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			this.running = false;
		}
	}
}