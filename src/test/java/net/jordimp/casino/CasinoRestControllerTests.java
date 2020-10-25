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

@SpringBootTest
@AutoConfigureMockMvc
class CasinoRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PlayerServiceImpl playerService;
	
	Player mockPlayer = new Player(new Date(), 300L, "MOCK-PLAYER-UUID", UserProvider.POKERSTAR );
	
	Player postPlayer = new Player(new Date(), 320L, "TEST-UUID-01", UserProvider.POKERSTAR);
	
		
	@Test
	void contextLoads() {
		assertThat(mockMvc).isNotNull();
		assertThat(playerService).isNotNull();
	}
	
	@Test
	public void getPlayer() throws Exception {

		Mockito.when(playerService.findByUUID(Mockito.anyString())).thenReturn(mockPlayer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/casino/get/MOCK-PLAYER-UUID")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("##  RESULT GET ##");
		System.out.println(result.getResponse().getContentAsString());
		System.out.println("##  EXPECTED GET ##");
		String expected = "{\"maxTime\":300,\"userProvider\":\"POKERSTAR\",\"uuid\":\"MOCK-PLAYER-UUID\"}";
		System.out.println(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void loginPlayer() throws Exception {

		Mockito.when(playerService.login(Mockito.any(Player.class))).thenReturn(postPlayer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logon")
				.content(TestUtils.asJsonString(postPlayer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("##  RESULT LOGIN PLAYER ##");
		System.out.println(result.getResponse().getContentAsString());
		System.out.println("##  EXPECTED LOGIN PLAYER ##");
		String expected = "{\"logon\":\"Player [maxTime=320, UUID=TEST-UUID-01, userProvider=POKERSTAR]\",\"result\":\"true\"}";
		System.out.println(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void logoutPlayer() throws Exception {

		Mockito.when(playerService.logout(Mockito.anyString())).thenReturn(true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logout/TEST-UUID-01")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("##  RESULT LOGOUT PLAYER ##");
		System.out.println(result.getResponse().getContentAsString());
		System.out.println("##  EXPECTED LOGOUT PLAYER ##");
		String expected = "{\"logout\":\"true\",\"result\":\"OK\"}";
		System.out.println(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}


}
