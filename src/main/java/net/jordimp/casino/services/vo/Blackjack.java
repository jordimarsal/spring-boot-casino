package net.jordimp.casino.services.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "bj")
public class Blackjack extends BaseGame{

	private static final String PREFIX = "bj";
	
	public Blackjack() {
		super(PREFIX);
	}

}
