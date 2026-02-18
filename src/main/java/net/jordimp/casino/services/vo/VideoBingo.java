package net.jordimp.casino.services.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vb")
public class VideoBingo extends BaseGame {

	private static final String PREFIX = "vb";
	
	public VideoBingo() {
		super(PREFIX);
	}

}
