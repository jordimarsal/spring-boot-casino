package net.jordimp.casino.services.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "poker")
public class Poker extends BaseGame {

	private static final String PREFIX = "poker";
	
	public Poker() {
		super(PREFIX);
	}

}
