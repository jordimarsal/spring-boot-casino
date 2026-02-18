package net.jordimp.casino.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.jordimp.casino.entity.Player;
import org.junit.jupiter.api.Test;

class UtilsTests {

  @Test
  void castList_withValidCollection_returnsListOfCorrectType() {
    Collection<?> collection = Arrays.asList("test1", "test2", "test3");

    List<String> result = Utils.castList(String.class, collection);

    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("test1", result.get(0));
    assertEquals("test2", result.get(1));
    assertEquals("test3", result.get(2));
  }

  @Test
  void castList_withEmptyCollection_returnsEmptyList() {
    Collection<?> collection = Collections.emptyList();

    List<String> result = Utils.castList(String.class, collection);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void castList_withPlayerCollection_returnsListOfPlayers() {
    Collection<?> collection = Arrays.asList(new Player(), new Player());

    List<Player> result = Utils.castList(Player.class, collection);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void castList_withInvalidType_throwsClassCastException() {
    Collection<?> collection = Arrays.asList("string1", "string2");

    assertThrows(ClassCastException.class, () -> {
      Utils.castList(Integer.class, collection);
    });
  }

  @Test
  void wrap_withValidStrings_returnsWrappedString() {
    String result = Utils.wrap("key", "value");

    assertEquals("{\"key\":\"value\"}", result);
  }

  @Test
  void wrap_withNullValue_returnsWrappedString() {
    String result = Utils.wrap("key", null);

    assertEquals("{\"key\":\"null\"}", result);
  }

  @Test
  void extendWrap_withValidStrings_extendsExistingWrap() {
    String existing = "{\"key1\":\"value1\"}";
    String result = Utils.extendWrap(existing, "key2", "value2");

    assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", result);
  }

  @Test
  void extendWrap_withEmptyWrap_createsNewWrap() {
    String result = Utils.extendWrap("", "key", "value");

    assertTrue(result.contains("\"key\":\"value\""));
  }

  @Test
  void extendWrap_withNullWrap_createsNewWrap() {
    String result = Utils.extendWrap(null, "key", "value");

    // When null, should handle gracefully
    assertNotNull(result);
  }

  @Test
  void extendWrap_withMultipleExtensions_addsAllKeys() {
    String result = Utils.extendWrap("{\"a\":\"1\"}", "b", "2");
    result = Utils.extendWrap(result, "c", "3");

    assertTrue(result.contains("\"a\":\"1\""));
    assertTrue(result.contains("\"b\":\"2\""));
    assertTrue(result.contains("\"c\":\"3\""));
  }
}
