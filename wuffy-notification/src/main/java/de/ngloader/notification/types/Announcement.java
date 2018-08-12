package de.ngloader.notification.types;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import de.ngloader.core.logger.Logger;

public abstract class Announcement implements Runnable {

	private static final ExecutorService executorService = Executors.newCachedThreadPool();

	protected abstract void update();

	protected abstract void _add(String guildId, List<Document> documents);

	protected abstract boolean isQueueEmpty();

	protected final MongoCollection<Document> collection;

	protected final int delay;

	protected final String name;
	protected final String typeName;

	protected AtomicBoolean running = new AtomicBoolean(false);

	public Announcement(MongoCollection<Document> collection, int delay, String name, String typeName) {
		this.collection = collection;
		this.delay = delay;
		this.name = name;
		this.typeName = typeName;
	}

	@Override
	public void run() {
		Announcement.executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					while(Announcement.this.running.get()) {
						try {
							Announcement.this.update();
						} catch(Exception e) {
							Logger.fatal(String.format("Wuffy Notification - %s", Announcement.this.name), "Failed to execute runnable.", e);
						} finally {
							if(!Announcement.this.isQueueEmpty())
								Thread.sleep(Announcement.this.delay);
						}
					}
				} catch(Exception e) {
					Logger.fatal(String.format("Wuffy Notification - %s", Announcement.this.name), "Failed to execute runnable.", e);
				} finally {
					Announcement.this.running.set(false);
				}
			}
		});
	}

	public void add(String guildId, List<Document> documents) {
		if(guildId == null || guildId.isEmpty() || this.running.get())
			return;

		this._add(guildId, documents);
	}

	public void tryStart() {
		if(this.running.get())
			return;

		this.running.set(true);
		this.run();
	}

	public String getTypeName() {
		return this.typeName;
	}
}