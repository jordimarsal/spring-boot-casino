package net.jordimp.casino.services.handler;

import java.util.Optional;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.services.vo.Game;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.Utils;

public class Jugada {
	private static final String TRANSACTION = "TRANSACTION";
	private static final String BET_DONE = "Bet done";
	
	private Jugada() {}

	public static Bet bet(Bet bet, Optional<Player> playerOpt) {
		if (hasNullValues(bet)) {
			return badBet(bet, Bet.E_NULL);
		}
		if(isBalanceExhausted(bet)) {
			return badBet(bet, Bet.E_NO_FUNDS);
		}
		if (isInTimeBet(playerOpt)) {
			return processBet(bet);
		}
		return badBet(bet, Bet.W_NO_TIME);
	}

	private static boolean isBalanceExhausted(Bet bet) {
		return bet.getBalancePlayer() - bet.getBetAmount() <= 0;
	}

	private static boolean hasNullValues(Bet bet) {
		return bet.getPlayerUUID() == null || bet.getGameUUID() == null;
	}

	private static boolean isInTimeBet(Optional<Player> playerOpt) {
		boolean isValid = false;
		if (playerOpt.isPresent()) {
			isValid = Utils.isInLoginTime(playerOpt.get());
		}
		return isValid;
	}

	private static Bet badBet(Bet bet, String warning) {
		bet.setWarning(warning);
		bet.setPrizeAmount(0.0);
		bet.setBad(true);
		return bet;
	}

	private static Bet processBet(Bet bet) {
		Bet retBet = null;
		Optional<Game> gameOpt = Optional.ofNullable(GameHandler.getGame(bet.getGameUUID()));
		if (gameOpt.isPresent()) {
			retBet = twitch(bet, gameOpt.get());
		} else {
			retBet = badBet(bet, Bet.W_NO_GAME);
		}

		return retBet;
	}

	private static Bet twitch(Bet bet, Game game) {
		if (bet.getBetAmount() > game.getMaxBet() || bet.getBetAmount() < game.getMinBet()) {
			return badBet(bet, Bet.W_LIMIT_BET);
		}
		bet.setPrizeAmount(0.0);
		bet.setComment(BET_DONE);
		boolean win = Math.random() < game.getProbability();
		if (win) {
			bet.setPrizeAmount(game.getPrize());
			bet.setComment(String.format(Bet.WIN, game.getPrize()));
		}
		bet.setBalancePlayer(bet.getBalancePlayer() - bet.getBetAmount());
		bet.setBalancePlayer(bet.getBalancePlayer() + bet.getPrizeAmount());
		CasinoLoggerUtils.info(TRANSACTION, bet.toString());
		return bet;
	}
}
