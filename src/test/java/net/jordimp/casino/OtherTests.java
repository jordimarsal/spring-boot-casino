package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.utils.Utils;

@SpringBootTest
@AutoConfigureMockMvc
class OtherTests {

	@Test
	void playerinTime() throws Exception {

		Player player = new Player(new Date(), 20L, "TEST-UUID-01", UserProvider.POKERSTAR);

		System.out.println("##  RESULT PLAYER IN TIME ##");
		String result = "" + Utils.isInLoginTime(player);
		System.out.println(result);
		System.out.println("##  EXPECTED PLAYER IN TIME ##");
		String expected = "true";
		System.out.println(expected);

		assertEquals(expected, result);
	}

	@Test
	void stringFormat() throws Exception {

		String win = Bet.WIN;
		Double prize = 10.0;

		System.out.println("##  RESULT PLAYER IN TIME ##");
		String result = String.format(win, prize);
		System.out.println(result);
		System.out.println("##  EXPECTED PLAYER IN TIME ##");
		String expected = "WIN! You have earned 10.0 credits!!!";
		System.out.println(expected);

		assertEquals(expected, result);
	}

}
