package de.ngloader.core;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.util.ITickable;

public class WuffyPhantomRefernce implements ITickable {

	private static final WuffyPhantomRefernce instance = new WuffyPhantomRefernce();

	public static WuffyPhantomRefernce getInstance() {
		return WuffyPhantomRefernce.instance;
	}

	private final ReferenceQueue<Core> referenceQueue = new ReferenceQueue<Core>();
	private final Map<PhantomReference<Core>, String> references = new HashMap<PhantomReference<Core>, String>();

	@Override
	public void update() {
		Reference<? extends Core> refence = referenceQueue.poll();

		if(refence != null)
			Logger.info("PhantomRefernce", String.format("Removed core instance (%s) class", references.remove(refence)));
	}

	public void add(Core core, String instanceName) {
		references.put(new PhantomReference<Core>(core, referenceQueue), instanceName);
	}
}