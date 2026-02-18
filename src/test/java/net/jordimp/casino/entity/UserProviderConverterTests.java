package net.jordimp.casino.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UserProviderConverterTests {

  private final UserProviderConverter converter = new UserProviderConverter();

  @Test
  void convertToDatabaseColumn_withNull_returnsNull() {
    String result = converter.convertToDatabaseColumn(null);

    assertNull(result);
  }

  @Test
  void convertToDatabaseColumn_withBwin_returnsName() {
    String result = converter.convertToDatabaseColumn(UserProvider.BWIN);

    assertEquals("BWIN-UUID", result);
  }

  @Test
  void convertToDatabaseColumn_withPokerStar_returnsName() {
    String result = converter.convertToDatabaseColumn(UserProvider.POKERSTAR);

    assertEquals("PokerStar-UUID", result);
  }

  @Test
  void convertToDatabaseColumn_withOther_returnsName() {
    String result = converter.convertToDatabaseColumn(UserProvider.OTHER);

    assertEquals("Other-UUID", result);
  }

  @Test
  void convertToEntityAttribute_withNull_returnsNull() {
    UserProvider result = converter.convertToEntityAttribute(null);

    assertNull(result);
  }

  @Test
  void convertToEntityAttribute_withBwin_returnsBwin() {
    UserProvider result = converter.convertToEntityAttribute("BWIN-UUID");

    assertEquals(UserProvider.BWIN, result);
  }

  @Test
  void convertToEntityAttribute_withPokerStar_returnsPokerStar() {
    UserProvider result = converter.convertToEntityAttribute("PokerStar-UUID");

    assertEquals(UserProvider.POKERSTAR, result);
  }

  @Test
  void convertToEntityAttribute_withOther_returnsOther() {
    UserProvider result = converter.convertToEntityAttribute("Other-UUID");

    assertEquals(UserProvider.OTHER, result);
  }

  @Test
  void convertToEntityAttribute_withInvalidString_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> {
      converter.convertToEntityAttribute("INVALID_PROVIDER");
    });
  }

  @Test
  void convertToEntityAttribute_withEmptyString_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> {
      converter.convertToEntityAttribute("");
    });
  }
}
