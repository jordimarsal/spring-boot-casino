package net.jordimp.casino.services.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "slot")
public class Slot extends BaseGame {

	private static final String PREFIX = "slot";
	
	public Slot() {
		super(PREFIX);
	}

}
