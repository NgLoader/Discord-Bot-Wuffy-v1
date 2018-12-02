package net.wuffy.core.database.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bson.Document;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.WriteModel;

import net.wuffy.common.logger.Logger;

public class MongoBulkWriteSystem {

	private final List<WriteModel<Document>> writers = new ArrayList<WriteModel<Document>>();

	private final SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() {

		public void onResult(BulkWriteResult result, Throwable throwable) {
			if(throwable != null) {
				throwable.printStackTrace();
				Logger.fatal("Database MongoDB", "Failed to bulk write", throwable);
			} else
				Logger.debug("Database MongoDB", String.format("Inserted: %s, Deleted: %s, Modified: %s, Matched: %s",
						Integer.toString(result.getInsertedCount()),
						Integer.toString(result.getDeletedCount()),
						result.isModifiedCountAvailable() ? Integer.toString(result.getModifiedCount()) : "Not Avaivible",
						Integer.toString(result.getMatchedCount())));
		};
	};

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

	private Thread bulkThread;
	private boolean running;

	private MongoCollection<Document> bulkCollection;

	private String name;

	public MongoBulkWriteSystem(String name) {
		this.name = name;
	}

	public void enableBulkWrite(MongoCollection<Document> bulkCollection) {
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
			}, String.format("Wuffy Discord Bot - Database MongoDB BulkWrite (%s)", this.name));
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

		List<WriteModel<Document>> writersCopy = new ArrayList<WriteModel<Document>>(this.writers);
		this.writers.clear();

		this.readWriteLock.readLock().unlock();

		if(writersCopy.isEmpty())
			return;

		BulkWriteResult result = null;
		Throwable throwable = null;

		try {
			result = this.bulkCollection.bulkWrite(writersCopy);
		} catch(Exception e) {
			throwable = e;
		}

		printBatchResult.onResult(result, throwable);
	}

	public void queueBulkModel(WriteModel<Document> writeModule) {
		this.readWriteLock.writeLock().lock();
		this.writers.add(writeModule);
		this.readWriteLock.writeLock().unlock();
	}
}