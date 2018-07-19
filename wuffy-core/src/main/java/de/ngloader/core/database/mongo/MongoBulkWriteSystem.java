package de.ngloader.core.database.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bson.Document;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.WriteModel;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.logger.Logger;

public abstract class MongoBulkWriteSystem extends StorageProvider<MongoStorage> {

	private final List<WriteModel<Document>> writers = new ArrayList<WriteModel<Document>>();

	private final SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() {

		public void onResult(BulkWriteResult result, Throwable throwable) {
			Logger.info("Database MongoDB", String.format("Inserted: %s, Deleted: %s, Modified: %s, Matched: %s",
					Integer.toString(result.getInsertedCount()),
					Integer.toString(result.getDeletedCount()),
					result.isModifiedCountAvailable() ? Integer.toString(result.getModifiedCount()) : "Not Avaivible",
					Integer.toString(result.getMatchedCount())));

			if(throwable != null) {
				Logger.fatal("Database MongoDB", "Failed to bulk write", throwable);
				throwable.printStackTrace();
			}
		};
	};

	private final Throwable throwableResult = new Throwable("[Database MongoBD] Failed to bulk write");

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

	protected Thread bulkThread;

	protected MongoCollection<Document> bulkCollection;

	protected boolean running;

	public void enableBulkWrite(MongoCollection<Document> bulkCollection, String name) {
		if(bulkThread == null) {
			this.running = true;

			this.bulkThread = new Thread(() -> {
				while(MongoBulkWriteSystem.this.running) {
					try {
						Thread.sleep(1000 * 5);

						if(!writers.isEmpty())
							writeBulkQueue();
					} catch (InterruptedException e) {
						e.printStackTrace();
						this.running = false;
					}
				}

				this.bulkThread = null;
			}, String.format("Wuffy Discord Bot - Core (%s) - Database MongoDB BulkWrite (%s)", this.core.getConfig().instanceName, name));
			this.bulkThread.setDaemon(true);
		}

		this.bulkCollection = bulkCollection;

		this.bulkThread.start();
	}

	public void disableBulkWrite() {
		this.running = false;

		this.writeBulkQueue();
	}

	protected void writeBulkQueue() {
		this.readWriteLock.readLock().lock();

		List<WriteModel<Document>> writersCopy = this.writers.subList(0, this.writers.size());
		this.writers.clear();

		this.readWriteLock.readLock().unlock();

		printBatchResult.onResult(this.bulkCollection.bulkWrite(writersCopy), this.throwableResult);
	}

	public void queueBulkModel(WriteModel<Document> writeModule) {
		this.readWriteLock.writeLock().lock();
		this.writers.add(writeModule);
		this.readWriteLock.writeLock().unlock();
	}
}