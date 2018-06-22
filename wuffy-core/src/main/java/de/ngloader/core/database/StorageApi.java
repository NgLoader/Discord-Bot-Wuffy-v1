package de.ngloader.core.database;

public final class StorageApi {

	private static IStorageService storageService;

	public static void setStorageService(IStorageService storageService) {
        if (StorageApi.storageService != null)
            throw new UnsupportedOperationException("Cannot redefine singleton StorageService");
		StorageApi.storageService = storageService;
	}

	public static IStorageService getStorageService() {
		return storageService;
	}
}