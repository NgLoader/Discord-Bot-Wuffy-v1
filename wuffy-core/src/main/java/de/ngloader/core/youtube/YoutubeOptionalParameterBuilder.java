package de.ngloader.core.youtube;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class YoutubeOptionalParameterBuilder {

	private final Map<YoutubeOptionalParameters, Object> parameters = new HashMap<YoutubeOptionalParameters, Object>();

	public YoutubeOptionalParameterBuilder add(YoutubeOptionalParameters paramter, Object value) {
		this.parameters.put(paramter, value);

		return this;
	}

	public YoutubeOptionalParameterBuilder remove(YoutubeOptionalParameters paramter) {
		this.parameters.remove(paramter);

		return this;
	}

	public String build() {
		return this.parameters.entrySet().stream().map(entry -> String.format("&%s=%s", entry.getKey().getParameter(), entry.getValue().toString())).collect(Collectors.joining(""));
	}
}