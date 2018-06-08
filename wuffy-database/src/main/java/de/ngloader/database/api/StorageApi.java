package de.ngloader.database.api;

public final class StorageApi {

	private static StorageService storageService;

	public static void setStorageService(StorageService storageService) {
        if (StorageApi.storageService != null)
            throw new UnsupportedOperationException("Cannot redefine singleton StorageService");
		StorageApi.storageService = storageService;
	}

	public static StorageService getStorageService() {
		return storageService;
	}
}
