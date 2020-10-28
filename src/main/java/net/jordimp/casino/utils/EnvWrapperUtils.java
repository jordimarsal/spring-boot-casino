package net.jordimp.casino.utils;

import org.springframework.core.env.Environment;

public class EnvWrapperUtils {

	private EnvWrapperUtils() {}
	
	private static Environment environment;
	
	public static void setEnv(Environment env) {
		environment = env;
	}
	
	public static Environment getEnv() {
		return environment;
	}
}
