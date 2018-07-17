package de.ngloader.core.database.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bson.Document;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.WriteModel;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.logger.Logger;

public abstract class MongoBulkWriteSystem extends StorageProvider<MongoStorage> {

	protected final List<WriteModel<Document>> writers = new CopyOnWriteArrayList<WriteModel<Document>>();

	protected final SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() {

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

	protected Thread bulkThread;

	protected MongoCollection<Document> bulkCollection;

	protected void writeBulkQueue() {
		final List<WriteModel<Document>> writers = new ArrayList<WriteModel<Document>>();

		this.writers.forEach(write -> {
			writers.add(write);
			this.writers.remove(write);
		});

		printBatchResult.onResult(this.bulkCollection.bulkWrite(writers), null);
	}

	public void enableBulkWrite(MongoCollection<Document> bulkCollection, String name) {
		if(bulkThread == null) {
			this.bulkThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true)
						try {
							Thread.sleep(1000 * 60 * 5);

							if(!writers.isEmpty())
								writeBulkQueue();
						} catch (InterruptedException e) {
//							e.printStackTrace();
						}
				}
			}, String.format("Wuffy Discord Bot - Core (%s) - Database MongoDB BulkWrite (%s)", this.core.getConfig().instanceName, name));
			this.bulkThread.setDaemon(true);
		}

		this.bulkCollection = bulkCollection;

		this.bulkThread.start();
	}

	public void disableBulkWrite() {
		if(this.bulkThread != null && this.bulkThread.isAlive())
			this.bulkThread.interrupt();

		this.writeBulkQueue();
	}

	public void queueBulkModel(WriteModel<Document> writeModule) {
		this.writers.add(writeModule);

		if(this.writers.size() > 10000) //max 10 groups mean -> mongodb create groups, 1000 operations are one group
			this.writeBulkQueue();
	}
}