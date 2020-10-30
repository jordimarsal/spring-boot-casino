package net.jordimp.casino;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.GamePlayServiceImpl;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.utils.CasinoLoggerUtils;

@SpringBootTest
@AutoConfigureMockMvc
class BetTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private GamePlayServiceImpl gamePlayService;
	
	private static final String RESULT = "RESULT";
	private static final String EXPCTD = "EXPCTD";
	
	Player testPlayer = new Player(new Date(), 320L, "TEST-UUID-02", UserProvider.POKERSTAR);
	
	Bet betLogin = new Bet(10.0, "TEST-UUID-02", "BLACKJACK-UUID", 100.0);
	Bet betBadLogin = new Bet(10.0, "TEST-UUID-02", "BLACKJACK-UUID", 100.0);
	
	Bet betBJLo = new Bet(1.0, "TEST-UUID-01", "BLACKJACK-UUID", 100.0);
	Bet betBJHi = new Bet(300.0, "TEST-UUID-01", "BLACKJACK-UUID", 100.0);
	
	private static String expLogin = "Bet done";
	private static String expBadLogin = "Player not logged or timed out";
	
	private static String expLow = "Bet out of bounds";
	private static String expHi = "Bet out of bounds";
	
		
	@Test
	void contextLoads() {
		assertThat(mockMvc).isNotNull();
		assertThat(gamePlayService).isNotNull();
	}
	
	
	@Test
	void BetWhithouAndWithloginPlayer() throws Exception {

		// BET WITHOUT LOGIN PLAYER
		
		CasinoLoggerUtils.pres(RESULT, "BET NOT LOGIN");
		Bet retBetNOLogin = gamePlayService.bet(betLogin);
		String resultStr = retBetNOLogin.getWarning();
		CasinoLoggerUtils.tres(resultStr);
		CasinoLoggerUtils.pres(EXPCTD, "BET NOT LOGIN");
		String expected = expBadLogin;
		CasinoLoggerUtils.tres(expected);
		assertEquals(expected, resultStr);
		
		
		// LOGIN
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logon")
				.content(TestUtils.asJsonString(testPlayer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		CasinoLoggerUtils.pres(RESULT, "LOGIN");
		CasinoLoggerUtils.tres(result.getResponse().getContentAsString());

		expected = "{\"logon\":\"Player [maxTime=320, UUID=TEST-UUID-02, userProvider=POKERSTAR]\",\"result\":\"true\"}";
		CasinoLoggerUtils.pres(EXPCTD, "LOGIN");
		CasinoLoggerUtils.tres(expected);
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

		
		// BET WITH LOGIN PLAYER
		
		CasinoLoggerUtils.pres(RESULT, "BET LOGIN");
		Bet retBetLogin = gamePlayService.bet(betLogin);
		resultStr = retBetLogin.getComment();
		CasinoLoggerUtils.tres(resultStr);
		
		CasinoLoggerUtils.pres(EXPCTD, "BET LOGIN");
		expected = expLogin;
		CasinoLoggerUtils.tres(expected);
		assertEquals(expected, resultStr);
	}
	
	@Test
	void BetExcessHighAndLow() throws Exception {

		// BET EXCESS HIGH
		
		CasinoLoggerUtils.pres(RESULT, "EXCESS HIGH");
		Bet retBetHi = gamePlayService.bet(betBJLo);
		CasinoLoggerUtils.tres(retBetHi.toString());
		String resultStr = retBetHi.getWarning();
		CasinoLoggerUtils.tres(resultStr);
		CasinoLoggerUtils.pres(EXPCTD, "EXCESS HIGH");
		String expected = expHi;
		CasinoLoggerUtils.tres(expected);
		assertEquals(expected, resultStr);
		
		
		// BET EXCESS LOW
		
		CasinoLoggerUtils.pres(RESULT, "EXCESS LOW");
		Bet retBetLo = gamePlayService.bet(betBJLo);
		CasinoLoggerUtils.tres(retBetLo.toString());
		resultStr = retBetLo.getWarning();
		CasinoLoggerUtils.tres(resultStr);
		CasinoLoggerUtils.pres(EXPCTD, "EXCESS LOW");
		expected = expLow;
		CasinoLoggerUtils.tres(expected);
		assertEquals(expected, resultStr);
	}

}
