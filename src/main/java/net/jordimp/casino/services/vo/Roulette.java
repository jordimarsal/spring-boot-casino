package net.jordimp.casino.services.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "roul")
public class Roulette extends BaseGame{

	private static final String PREFIX = "roul";
	
	public Roulette() {
		super(PREFIX);
	}

}
