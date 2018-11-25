package net.wuffy.common;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.IWuffyPhantomReference;

public class WuffyPhantomRefernce implements ITickable {

	private static final WuffyPhantomRefernce INSTANCE = new WuffyPhantomRefernce();

	public static WuffyPhantomRefernce getInstance() {
		return WuffyPhantomRefernce.INSTANCE;
	}

	private final ReferenceQueue<IWuffyPhantomReference> referenceQueue = new ReferenceQueue<IWuffyPhantomReference>();
	private final Map<PhantomReference<IWuffyPhantomReference>, String> references = new HashMap<PhantomReference<IWuffyPhantomReference>, String>();

	@Override
	public void update() {
		for(Reference<? extends IWuffyPhantomReference> reference = this.referenceQueue.poll(); reference != null; reference = this.referenceQueue.poll()) {
			if(reference != null)
				Logger.info("PhantomRefernce", String.format("Removed \"%s\"", this.references.remove(reference)));
		}
	}

	public void add(IWuffyPhantomReference clazz, String instanceName) {
		this.references.put(new PhantomReference<IWuffyPhantomReference>(clazz, this.referenceQueue), instanceName);
	}
}