package net.jordimp.casino.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import net.jordimp.casino.entity.Player;

public class Utils {

	// https://stackoverflow.com/a/2848268
	// used to avoid warnings in EntityManager calls.
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c)
			r.add(clazz.cast(o));
		return r;
	}

	public static String wrap(String pre, String suf) {
		String wrapToken = "{\"" + pre + "\":\"" + suf + "\"}";
		return wrapToken;
	}

	public static String extendWrap(String wrap, String pre, String suf) {
		String newWrapToken = ",\"" + pre + "\":\"" + suf + "\"}";
		String result = Optional.ofNullable(wrap).filter(sStr -> sStr.length() != 0)
				.map(sStr -> sStr.substring(0, sStr.length() - 1)).orElse(wrap);
		wrap = result + newWrapToken;
		return wrap;
	}

	public static boolean isInLoginTime(Player fly) {
		Date login = fly.getLoginDate();
		long now = System.currentTimeMillis();
		if (now <= (fly.getMaxTime() * 1000 + login.getTime())) {
			return true;
		}
		return false;
	}

}
