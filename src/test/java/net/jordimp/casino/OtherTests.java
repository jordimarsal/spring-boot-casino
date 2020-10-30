package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.Utils;

@SpringBootTest
@AutoConfigureMockMvc
class OtherTests {

	private static final String RESULT = "RESULT";
	private static final String EXPCTD = "EXPCTD";
	
	@Test
	void playerinTime() throws Exception {

		Player player = new Player(new Date(), 20L, "TEST-UUID-01", UserProvider.POKERSTAR);

		CasinoLoggerUtils.pres(RESULT, "PLAYER IN TIME");
		String result = "" + Utils.isInLoginTime(player);
		CasinoLoggerUtils.tres(result);
		
		String expected = "true";
		CasinoLoggerUtils.pres(EXPCTD, "PLAYER IN TIME");
		CasinoLoggerUtils.tres(expected);

		assertEquals(expected, result);
	}

	@Test
	void stringFormat() throws Exception {

		String win = Bet.WIN;
		Double prize = 10.0;

		CasinoLoggerUtils.pres(RESULT, "STRING FORMAT");
		String result = String.format(win, prize);
		CasinoLoggerUtils.tres(result);
		CasinoLoggerUtils.pres(EXPCTD, "STRING FORMAT");
		String expected = "WIN! You have earned 10.0 credits!!!";
		CasinoLoggerUtils.tres(expected);

		assertEquals(expected, result);
	}

}
