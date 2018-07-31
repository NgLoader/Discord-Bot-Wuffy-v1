package de.ngloader.core.youtube;

public enum YoutubePart {

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	SNIPPET("snippet"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	CONTENTDETAILS("contentDetails"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 1
	 */
	FILEDETAILS("fileDetails"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 0
	 */
	PLAYER("player"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 1
	 */
	PROCESSINGDETAILS("processingDetails"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	RECORDINGDETAILS("recordingDetails"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	STATISTICS("statistics"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	STATUS("status"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 1
	 */
	SUGGESTIONS("suggestions"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	TOPICDETAILS("topicDetails"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 0
	 */
	ID("id"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	LIVESTREAMINGDETAILS("liveStreamingDetails"),

	/**
	 * 11 qutas every second allowed.
	 * 
	 * Quotas: 2
	 */
	LOCALIZATIONS("localizations");

	private String part;

	YoutubePart(String part) {
		this.part = part;
	}

	public String getPart() {
		return part;
	}
}