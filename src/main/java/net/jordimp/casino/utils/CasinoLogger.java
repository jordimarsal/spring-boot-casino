package net.jordimp.casino.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CasinoLogger {

	private static Logger logger = LoggerFactory.getLogger(CasinoLogger.class);

	public static void trace(String msg) {
		logger.trace(msg);
	}

	public static void debug(String msg) {
		logger.debug(msg);
	}

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void warn(String msg) {
		logger.warn(msg);
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void trace(String TAG, String msg) {
		logger.trace(union(TAG, msg));
	}

	public static void debug(String TAG, String msg) {
		logger.debug(union(TAG, msg));
	}

	public static void info(String TAG, String msg) {
		logger.info(union(TAG, msg));
	}

	public static void warn(String TAG, String msg) {
		logger.warn(union(TAG, msg));
	}

	public static void error(String TAG, String msg) {
		logger.error(union(TAG, msg));
	}

	private static String union(String TAG, String msg) {
		return TAG + " -> " + msg;
	}

	public static void error(String tAG, InterruptedException e) {
		error(tAG, e.getMessage());
		
	}

}
