package net.jordimp.casino;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.PlayerServiceImpl;
import net.jordimp.casino.utils.CasinoLoggerUtils;

@SpringBootTest
@AutoConfigureMockMvc
class CasinoRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PlayerServiceImpl playerService;
	
	Player mockPlayer = new Player(new Date(), 300L, "MOCK-PLAYER-UUID", UserProvider.POKERSTAR);
	
	Player postPlayer = new Player(new Date(), 320L, "TEST-UUID-01", UserProvider.POKERSTAR);
	
	private static final String RESULT = "RESULT";
	private static final String EXPCTD = "EXPCTD";
		
	@Test
	void contextLoads() {
		assertThat(mockMvc).isNotNull();
		assertThat(playerService).isNotNull();
	}
	
	@Test
	void getPlayer() throws Exception {

		Mockito.when(playerService.findByUUID(Mockito.anyString())).thenReturn(mockPlayer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/casino/get/MOCK-PLAYER-UUID")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		CasinoLoggerUtils.pres(RESULT, "GET");
		CasinoLoggerUtils.tres(result.getResponse().getContentAsString());

		CasinoLoggerUtils.pres(EXPCTD, "GET");
		String expected = "{\"maxTime\":300,\"userProvider\":\"POKERSTAR\",\"uuid\":\"MOCK-PLAYER-UUID\"}";
		CasinoLoggerUtils.tres(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	void loginPlayer() throws Exception {

		Mockito.when(playerService.login(Mockito.any(Player.class))).thenReturn(postPlayer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logon")
				.content(TestUtils.asJsonString(postPlayer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		CasinoLoggerUtils.pres(RESULT, "LOGIN PLAYER");
		CasinoLoggerUtils.tres(result.getResponse().getContentAsString());
		
		CasinoLoggerUtils.pres(EXPCTD, "LOGIN PLAYER");
		String expected = "{\"logon\":\"Player [maxTime=320, UUID=TEST-UUID-01, userProvider=POKERSTAR]\",\"result\":\"true\"}";
		CasinoLoggerUtils.tres(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	void logoutPlayer() throws Exception {

		Mockito.when(playerService.logout(Mockito.anyString())).thenReturn(true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logout/TEST-UUID-01")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"logout\":\"true\",\"result\":\"OK\"}";
		
		CasinoLoggerUtils.pres(RESULT, "LOGOUT PLAYER");
		CasinoLoggerUtils.tres(result.getResponse().getContentAsString());
		
		CasinoLoggerUtils.pres(EXPCTD, "LOGOUT PLAYER");
		CasinoLoggerUtils.tres(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}


}
