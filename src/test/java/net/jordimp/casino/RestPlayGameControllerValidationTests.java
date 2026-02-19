package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jordimp.casino.controllers.RestPlayGameController;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.services.PlayerServiceImpl;
import net.jordimp.casino.services.dto.Bet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class RestPlayGameControllerValidationTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private PlayerServiceImpl playerService;

  // TEST 1: getPlayerString with valid player
  @Test
  void testGetPlayerString_withValidPlayer_returnsPlayerString() throws Exception {
    // Arrange
    Player player = TestUtils.createValidPlayer();
    when(playerService.findByUUID(anyString())).thenReturn(player);

    // Act
    MvcResult result =
        mockMvc
            .perform(get("/api/casino/gets/{uuid}", "test-uuid"))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String response = result.getResponse().getContentAsString();
    assertTrue(response.contains(player.toString()));
  }

  // TEST 2: getPlayerString with null player
  @Test
  void testGetPlayerString_withNullPlayer_returnsNullMessage() throws Exception {
    // Arrange
    when(playerService.findByUUID(anyString())).thenReturn(null);

    // Act
    MvcResult result =
        mockMvc
            .perform(get("/api/casino/gets/{uuid}", "test-uuid"))
            .andExpect(status().isOk())
            .andReturn();

    // Assert
    String response = result.getResponse().getContentAsString();
    assertTrue(response.contains("player: null"));
  }

  // TEST 3: bet with UUID mismatch
  @Test
  void testBet_withUUIDMismatch_throwsIllegalArgumentException() throws Exception {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setPlayerUUID("different-uuid");

    // Act & Assert
    mockMvc
        .perform(
            post("/api/casino/bet/{uuid}", "path-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bet)))
        .andExpect(status().isBadRequest());
  }

  // TEST 4: bet with null bet amount
  @Test
  void testBet_withNullBetAmount_throwsIllegalArgumentException() throws Exception {
    // Arrange
    String betJson =
        "{\"betAmount\":null,\"playerUUID\":\"same-uuid\",\"gameUUID\":\"game-456\",\"price\":100.0}";

    // Act & Assert
    mockMvc
        .perform(
            post("/api/casino/bet/{uuid}", "same-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(betJson))
        .andExpect(status().isBadRequest());
  }

  // TEST 5: bet with negative bet amount
  @Test
  void testBet_withNegativeBetAmount_throwsIllegalArgumentException() throws Exception {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setPlayerUUID("same-uuid");
    bet.setBetAmount(-10.0);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/casino/bet/{uuid}", "same-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bet)))
        .andExpect(status().isBadRequest());
  }

  // TEST 6: bet with excessive bet amount
  @Test
  void testBet_withExcessiveBetAmount_throwsIllegalArgumentException() throws Exception {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setPlayerUUID("same-uuid");
    bet.setBetAmount(20000.0); // Above MAX_BET_AMOUNT (10000.0)

    // Act & Assert
    mockMvc
        .perform(
            post("/api/casino/bet/{uuid}", "same-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bet)))
        .andExpect(status().isBadRequest());
  }
}
