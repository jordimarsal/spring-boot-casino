package net.jordimp.casino;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

	public static String asJsonString(final Object obj) throws Exception {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

}
