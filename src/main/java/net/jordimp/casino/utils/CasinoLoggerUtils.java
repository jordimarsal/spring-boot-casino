package net.jordimp.casino.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CasinoLoggerUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CasinoLoggerUtils.class);
	
	private CasinoLoggerUtils() {}

	/*
	 * print trace level
	 */
	public static void trace(final String msg) {
		if (LOGGER.isTraceEnabled()){
			LOGGER.trace(msg);
		}
	}

	/*
	 * print debug level
	 */
	public static void debug(final String msg) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug(msg);
		}
	}

	/*
	 * print info level
	 */
	public static void info(final String msg) {
		if (LOGGER.isInfoEnabled()){
			LOGGER.info(msg);
		}
	}

	/*
	 * print warn level
	 */
	public static void warn(final String msg) {
		if (LOGGER.isWarnEnabled()){
			LOGGER.warn(msg);
		}
	}

	/*
	 * print error level
	 */
	public static void error(final String msg) {
		if (LOGGER.isErrorEnabled()){
			LOGGER.error(msg);
		}
	}

	/*
	 * print trace level
	 */
	public static void trace(final String tag, final String msg) {
		if (LOGGER.isTraceEnabled()){
			LOGGER.trace(concat(tag, msg));
		}
	}

	/*
	 * print debug level
	 */
	public static void debug(final String tag, final  String msg) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug(concat(tag, msg));
		}
	}

	/*
	 * print info level
	 */
	public static void info(final String tag, final String msg) {
		if (LOGGER.isInfoEnabled()){
			LOGGER.info(concat(tag, msg));
		}
	}

	/*
	 * print warn level
	 */
	public static void warn(final String tag, final String msg) {
		if (LOGGER.isWarnEnabled()){
			LOGGER.warn(concat(tag, msg));
		}
	}

	/*
	 * print error level
	 */
	public static void error(final String tag, final String msg) {
		if (LOGGER.isErrorEnabled()){
			LOGGER.error(concat(tag, msg));
		}
	}

	private static String concat(final String tag, final String msg) {
		return tag.concat(" -> ").concat(msg);
	}

	/*
	 * print error level
	 */
	public static void error(final String tag, Exception e) {
		error(tag, e.getMessage());
		
	}

}
