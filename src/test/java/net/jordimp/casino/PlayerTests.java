package net.jordimp.casino;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(OrderAnnotation.class)
class PlayerTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PlayerServiceImpl playerService;
	
	Player mockPlayer = new Player(new Date(), 300L, "MOCK-PLAYER-UUID", UserProvider.BWIN );
	
	@Test
	void contextLoads() {
		assertThat(playerService).isNotNull();
		assertThat(mockMvc).isNotNull();
	}

	
	@Test
	@Order(1)
	public void loginPlayerWithoutMock() throws Exception {

		//Mockito.when(playerService.login(Mockito.any(Player.class))).thenReturn(mockPlayer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logon")
				.content(TestUtils.asJsonString(mockPlayer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("##  RESULT LOGIN PLAYER ##");
		System.out.println(result.getResponse().getContentAsString());
		System.out.println("##  EXPECTED LOGIN PLAYER ##");
		String expected = "{\"logon\":\"Player [maxTime=300, UUID=MOCK-PLAYER-UUID, userProvider=BWIN]\",\"result\":\"false\"}";
		System.out.println(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@Order(2)
	public void logoutPlayerWithoutMock() throws Exception {

		//Mockito.when(playerService.logout(Mockito.anyString())).thenReturn(true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/casino/logout/MOCK-PLAYER-UUID")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);		

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("##  RESULT LOGOUT PLAYER ##");
		System.out.println(result.getResponse().getContentAsString());
		System.out.println("##  EXPECTED LOGOUT PLAYER ##");
		String expected = "{\"logout\":\"false\",\"result\":\"OK\"}";
		System.out.println(expected);

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}


}
