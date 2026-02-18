package net.jordimp.casino.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.services.GamePlayServiceImpl;
import net.jordimp.casino.services.PlayerServiceImpl;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.Utils;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/casino", method = { RequestMethod.POST, RequestMethod.GET })
public class RestPlayGameController {

	@Autowired
	private PlayerServiceImpl playerService;

	@Autowired
	private GamePlayServiceImpl gamePlayService;
	
	private static final String RESULT = "result";

	@PostMapping(value = "/logon")
	public String logon(@RequestBody Player player) {
		Player retPlayer = playerService.login(player);
		String sizeWrap = Utils.wrap("logon", player.toString());
		String ret = Utils.extendWrap(sizeWrap, RESULT, "" + isValidPlayer(retPlayer));

		CasinoLoggerUtils.debug("POST logon ret=" + ret);
		return ret;
	}

	@PostMapping(value = "/logout/{uuid}")
	public String logout(@PathVariable(value = "uuid") String uuid) {
		CasinoLoggerUtils.debug("POST logout = " + uuid);

		boolean isLogout = playerService.logout(uuid);
		String sizeWrap = Utils.wrap("logout", "" + isLogout);
		String ret = Utils.extendWrap(sizeWrap, RESULT, "OK");
		CasinoLoggerUtils.debug("POST logout: ret=" + ret);
		return ret;
	}

	@PostMapping(value = "/bet/{uuid}")
	public String bet(@PathVariable(value = "uuid") String uuid, @RequestBody Bet bet) {
		CasinoLoggerUtils.debug("POST bet = " + bet.constructor());

		// Validate UUID match
		if (!uuid.equals(bet.getPlayerUUID())) {
			throw new IllegalArgumentException(
				"Bet UUID mismatch: path UUID '" + uuid +
				"' does not match bet player UUID '" + bet.getPlayerUUID() + "'"
			);
		}

		// Validate bet amount
		if (bet.getBetAmount() == null) {
			throw new IllegalArgumentException("Bet amount cannot be null");
		}

		if (bet.getBetAmount() <= 0) {
			throw new IllegalArgumentException(
				"Bet amount must be positive: " + bet.getBetAmount()
			);
		}

		// Define reasonable maximum bet (can be configurable later)
		final double MAX_BET_AMOUNT = 10000.0;
		if (bet.getBetAmount() > MAX_BET_AMOUNT) {
			throw new IllegalArgumentException(
				"Bet amount exceeds maximum allowed: " + bet.getBetAmount() +
				" (max: " + MAX_BET_AMOUNT + ")"
			);
		}

		bet.setBetUUID(uuid);
		Bet betResult = gamePlayService.bet(bet);

		String ret = "";
		if (betResult.isBad()) {
			String sizeWrap = Utils.wrap("BAD BET", betResult.getWarning());
			ret = Utils.extendWrap(sizeWrap, RESULT, "KO");
		} else {
			String sizeWrap = Utils.wrap("bet", "" + betResult.toString());
			ret = Utils.extendWrap(sizeWrap, RESULT, "OK");
		}

		CasinoLoggerUtils.debug("POST bet: ret=" + ret);
		return ret;
	}

	@GetMapping(value = "/get/{uuid}")
	public Player getPlayer(@PathVariable(value = "uuid") String uuid) {
		CasinoLoggerUtils.debug("GET UUID: " + uuid);
		return playerService.findByUUID(uuid);
	}

	@GetMapping(value = "/gets/{uuid}")
	public String getPlayerString(@PathVariable(value = "uuid") String uuid) {
		CasinoLoggerUtils.debug("GET UUID: " + uuid);
		Player retPlayer = playerService.findByUUID(uuid);
		if (isValidPlayer(retPlayer)) {
			return retPlayer.toString();
		}
		return Utils.wrap(RESULT, "player: null");
	}

	private boolean isValidPlayer(Player player) {
		Optional<Player> playerOpt = Optional.ofNullable(player);
		String ret = playerOpt.map(ply -> ply.getUUID()).orElse("NO");
		return !ret.equals("NO");
	}

}
