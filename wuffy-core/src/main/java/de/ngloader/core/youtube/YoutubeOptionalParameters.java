package de.ngloader.core.youtube;

public enum YoutubeOptionalParameters {

	/**
	 * For: Channels, Videos
	 * 
	 * string
	 * The hl parameter instructs the API to retrieve localized resource metadata for a specific application language that the YouTube website supports.
	 * The parameter value must be a language code included in the list returned by the i18nLanguages.list method.
	 * 
	 * If localized resource details are available in that language, the resource's snippet.localized object will contain the localized values. However, if localized details are not available, the snippet.localized object will contain resource details in the resource's default language.
	 */
	HL("hl"),

	/**
	 * For: Search, Channels, Comments, Videos, PlaylistItems
	 * 
	 * unsigned integer
	 * The maxResults parameter specifies the maximum number of items that should be returned in the result set. Acceptable values are 0 to 50, inclusive. The default value is 5.
	 */
	MAXRESULTS("maxResults"),

	/**
	 * For: Search, Channels, Videos, PlaylistItems
	 * 
	 * 	string
	 * This parameter can only be used in a properly authorized request. Note: This parameter is intended exclusively for YouTube content partners.
	 * 
	 * The onBehalfOfContentOwner parameter indicates that the request's authorization credentials identify a YouTube CMS user who is acting on behalf of the content owner specified in the parameter value.
	 * This parameter is intended for YouTube content partners that own and manage many different YouTube channels.
	 * It allows content owners to authenticate once and get access to all their video and channel data, without having to provide authentication credentials for each individual channel.
	 * The CMS account that the user authenticates with must be linked to the specified YouTube content owner.
	 */
	ONBEHALFOFCOUNTENTOWNER("onBehalfContentOwner"),

	/**
	 * For: Search, Channels, Comments, Videos, PlaylistItems
	 * 
	 * string
	 * The pageToken parameter identifies a specific page in the result set that should be returned. In an API response, the nextPageToken and prevPageToken properties identify other pages that could be retrieved.
	 */
	PAGETOKEN("pageToken"),

	/**
	 * For: Comments
	 * 
	 * string
	 * This parameter indicates whether the API should return comments formatted as HTML or as plain text. The default value is html.
	 * 
	 * Acceptable values are:
	 * html – Returns the comments in HTML format. This is the default value.
	 * plainText – Returns the comments in plain text format.
	 */
	TEXTFORMAT("textFormat"),

	/**
	 * For: Search
	 * 
	 * string
	 * The channelId parameter indicates that the API response should only contain resources created by the channel. 
	 * 
	 * Note: Search results are constrained to a maximum of 500 videos if your request specifies a value for the channelId parameter and sets the type parameter value to video, but it does not also set one of the forContentOwner, forDeveloper, or forMine filters.
	 */
	CHANNELID("channelId"),

	/**
	 * For: Search
	 * 
	 * string
	 * The channelType parameter lets you restrict a search to a particular type of channel.
	 * 
	 * Acceptable values are:
	 * any – Return all channels.
	 * show – Only retrieve shows.
	 */
	CHANNELTYPE("channelType"),

	/**
	 * For: Search
	 * 
	 * string
	 * The eventType parameter restricts a search to broadcast events. If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * completed – Only include completed broadcasts.
	 * live – Only include active broadcasts.
	 * upcoming – Only include upcoming broadcasts.
	 */
	EVENTTYPE("eventType"),

	/**
	 * For: Search
	 * 
	 * string
	 * The location parameter, in conjunction with the locationRadius parameter, defines a circular geographic area and also restricts a search to videos that specify, in their metadata, a geographic location that falls within that area.
	 * The parameter value is a string that specifies latitude/longitude coordinates e.g. (37.42307,-122.08427).
	 * 
	 * The location parameter value identifies the point at the center of the area.
	 * The locationRadius parameter specifies the maximum distance that the location associated with a video can be from that point for the video to still be included in the search results.
	 * The API returns an error if your request specifies a value for the location parameter but does not also specify a value for the locationRadius parameter.
	 */
	LOCATION("location"),

	/**
	 * For: Search
	 * 
	 * string
	 * The locationRadius parameter, in conjunction with the location parameter, defines a circular geographic area.
	 * 
	 * The parameter value must be a floating point number followed by a measurement unit.
	 * Valid measurement units are m, km, ft, and mi. For example, valid parameter values include 1500m, 5km, 10000ft, and 0.75mi. The API does not support locationRadius parameter values larger than 1000 kilometers.
	 */
	LOCATIONRADIUS("locationRadius"),

	/**
	 * For: Search
	 * 
	 * string
	 * The pageToken parameter identifies a specific page in the result set that should be returned.
	 * In an API response, the nextPageToken and prevPageToken properties identify other pages that could be retrieved.
	 */
	ORDER("order"),

	/**
	 * For: Search
	 * 
	 * string
	 * The order parameter specifies the method that will be used to order resources in the API response. The default value is relevance.
	 * 
	 * Acceptable values are:
	 * date – Resources are sorted in reverse chronological order based on the date they were created.
	 * rating – Resources are sorted from highest to lowest rating.
	 * relevance – Resources are sorted based on their relevance to the search query. This is the default value for this parameter.
	 * title – Resources are sorted alphabetically by title.
	 * videoCount – Channels are sorted in descending order of their number of uploaded videos.
	 * viewCount – Resources are sorted from highest to lowest number of views. For live broadcasts, videos are sorted by number of concurrent viewers while the broadcasts are ongoing.
	 */
	PUBLISHEDAFTER("publishedAfter"),

	/**
	 * For: Search
	 * 
	 * datetime
	 * The publishedBefore parameter indicates that the API response should only contain resources created before or at the specified time.
	 * The value is an RFC 3339 formatted date-time value (1970-01-01T00:00:00Z).
	 */
	PUBLICSHEDBEFORE("publishedBefore"),

	/**
	 * For: Search
	 * 
	 * string
	 * The q parameter specifies the query term to search for.
	 * 
	 * Your request can also use the Boolean NOT (-) and OR (|) operators to exclude videos or to find videos that are associated with one of several search terms.
	 * For example, to search for videos matching either "boating" or "sailing", set the q parameter value to boating|sailing.
	 * Similarly, to search for videos matching either "boating" or "sailing" but not "fishing", set the q parameter value to boating|sailing -fishing.
	 * Note that the pipe character must be URL-escaped when it is sent in your API request.
	 * The URL-escaped value for the pipe character is %7C.
	 */
	Q("q"),

	/**
	 * For: Search, Videos
	 * 
	 * string
	 * The regionCode parameter instructs the API to return search results for videos that can be viewed in the specified country.
	 * The parameter value is an ISO 3166-1 alpha-2 country code.
	 */
	REGIONCODE("regionCode"),

	/**
	 * For: Search
	 * 
	 * string
	 * The relevanceLanguage parameter instructs the API to return search results that are most relevant to the specified language.
	 * The parameter value is typically an ISO 639-1 two-letter language code.
	 * However, you should use the values zh-Hans for simplified Chinese and zh-Hant for traditional Chinese.
	 * Please note that results in other languages will still be returned if they are highly relevant to the search query term.
	 */
	RELEVANCELANGUAGE("relevanceLanguage"),

	/**
	 * For: Search
	 * 
	 * string
	 * The safeSearch parameter indicates whether the search results should include restricted content as well as standard content.
	 * 
	 * Acceptable values are:
	 * moderate – YouTube will filter some content from search results and, at the least, will filter content that is restricted in your locale. Based on their content, search results could be removed from search results or demoted in search results. This is the default parameter value.
	 * none – YouTube will not filter the search result set.
	 * strict – YouTube will try to exclude all restricted content from the search result set. Based on their content, search results could be removed from search results or demoted in search results.
	 */
	SAFESEARCH("saveSearch"),

	/**
	 * For: Search
	 * 
	 * string
	 * The topicId parameter indicates that the API response should only contain resources associated with the specified topic. The value identifies a Freebase topic ID.
	 * 
	 * Important: Due to the deprecation of Freebase and the Freebase API, the topicId parameter started working differently as of February 27, 2017. At that time, YouTube started supporting a small set of curated topic IDs, and you can only use that smaller set of IDs as values for this parameter.
	 */
	TPOCIID("topicId"),

	/**
	 * For: Search
	 * 
	 * string
	 * The type parameter restricts a search query to only retrieve a particular type of resource.
	 * The value is a comma-separated list of resource types.
	 * The default value is video,channel,playlist.
	 * 
	 * Acceptable values are:
	 * channel
	 * playlist
	 * video
	 */
	TYPE("type"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoCaption parameter indicates whether the API should filter video search results based on whether they have captions.
	 * If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Do not filter results based on caption availability.
	 * closedCaption – Only include videos that have captions.
	 * none – Only include videos that do not have captions.
	 */
	VIDEOCAPTION("videoCaption"),

	/**
	 * For: Search, Videos
	 * 
	 * 	string
	 * The videoCategoryId parameter filters video search results based on their category. If you specify a value for this parameter, you must also set the type parameter's value to video.
	 */
	VIDEOCATEGORYID("videoCategoryId"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoDefinition parameter lets you restrict a search to only include either high definition (HD) or standard definition (SD) videos.
	 * HD videos are available for playback in at least 720p, though higher resolutions, like 1080p, might also be available. If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Return all videos, regardless of their resolution.
	 * high – Only retrieve HD videos.
	 * standard – Only retrieve videos in standard definition.
	 */
	VIDEODEFINITION("videoDefinition"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoDimension parameter lets you restrict a search to only retrieve 2D or 3D videos.
	 * If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * 2d – Restrict search results to exclude 3D videos.
	 * 3d – Restrict search results to only include 3D videos.
	 * any – Include both 3D and non-3D videos in returned results. This is the default value.
	 */
	VIDEODIMENSION("videoDimension"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoDuration parameter filters video search results based on their duration.
	 * If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Do not filter video search results based on their duration. This is the default value.
	 * long – Only include videos longer than 20 minutes.
	 * medium – Only include videos that are between four and 20 minutes long (inclusive).
	 * short – Only include videos that are less than four minutes long.
	 */
	VIDEODURATION("videoDuration"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoEmbeddable parameter lets you to restrict a search to only videos that can be embedded into a webpage.
	 * If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Return all videos, embeddable or not.
	 * true – Only retrieve embeddable videos.
	 */
	VIDEOEMBEDDABLE("videoEmbeddable"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoLicense parameter filters search results to only include videos with a particular license.
	 * YouTube lets video uploaders choose to attach either the Creative Commons license or the standard YouTube license to each of their videos. If you specify a value for this parameter,
	 * you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Return all videos, regardless of which license they have, that match the query parameters.
	 * creativeCommon – Only return videos that have a Creative Commons license. Users can reuse videos with this license in other videos that they create.
	 * youtube – Only return videos that have the standard YouTube license.
	 */
	VIDEOLICENSE("videoLicense"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoSyndicated parameter lets you to restrict a search to only videos that can be played outside youtube.com.
	 * If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Return all videos, syndicated or not.
	 * true – Only retrieve syndicated videos.
	 */
	VIDEOSYNDICATED("videoSyndicated"),

	/**
	 * For: Search
	 * 
	 * string
	 * The videoType parameter lets you restrict a search to a particular type of videos. If you specify a value for this parameter, you must also set the type parameter's value to video.
	 * 
	 * Acceptable values are:
	 * any – Return all videos.
	 * episode – Only retrieve episodes of shows.
	 * movie – Only retrieve movies.
	 */
	VIDEOTYPE("videoType"),

	/**
	 * For: Videos
	 * 
	 * unsigned integer
	 * The maxHeight parameter specifies the maximum height of the embedded player returned in the player.embedHtml property.
	 * You can use this parameter to specify that instead of the default dimensions, the embed code should use a height appropriate for your application layout.
	 * If the maxWidth parameter is also provided, the player may be shorter than the maxHeight in order to not violate the maximum width.
	 * Acceptable values are 72 to 8192, inclusive.
	 */
	MAXHEIGHT("maxHeight"),

	/**
	 * For: Videos
	 * 
	 * unsigned integer
	 * The maxWidth parameter specifies the maximum width of the embedded player returned in the player.embedHtml property.
	 * You can use this parameter to specify that instead of the default dimensions, the embed code should use a width appropriate for your application layout.
	 * 
	 * If the maxHeight parameter is also provided, the player may be narrower than maxWidth in order to not violate the maximum height. Acceptable values are 72 to 8192, inclusive.
	 */
	MAXWIDTH("maxWidth"),

	/**
	 * For: PlaylistItems
	 * 
	 * string
	 * The videoId parameter specifies that the request should return only the playlist items that contain the specified video.
	 */
	VIDEOID("videoId");

	private String parameter;

	YoutubeOptionalParameters(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return this.parameter;
	}
}