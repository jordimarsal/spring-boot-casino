package net.jordimp.casino.services.handler;

import java.util.Optional;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.services.vo.Game;
import net.jordimp.casino.utils.CasinoLogger;
import net.jordimp.casino.utils.Utils;

public class Jugada {
	private static final String TRANSACTION = "TRANSACTION";
	private static final String BET_DONE = "Bet done";

	public static Bet bet(Bet bet, Optional<Player> playerOpt) {
		if (bet == null || bet.getPlayerUUID() == null || bet.getGameUUID() == null) {
			bet = badBet(bet, Bet.E_NULL);
			return bet;
		}
		if (isInTimeBet(bet, playerOpt)) {
			bet = processBet(bet);
		} else {
			bet = badBet(bet, Bet.W_NO_TIME);
		}

		return bet;
	};

	private static boolean isInTimeBet(Bet bet, Optional<Player> playerOpt) {
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
		Optional<Game> gameOpt = Optional.ofNullable(GameHandler.getGame(bet.getGameUUID()));
		if (gameOpt.isPresent()) {
			bet = twitch(bet, gameOpt.get());
		} else {
			bet = badBet(bet, Bet.W_NO_GAME);
		}

		return bet;
	}

	private static Bet twitch(Bet bet, Game game) {
		if (bet.getBetAmount() > game.getMaxBet() || bet.getBetAmount() < game.getMinBet()) {
			bet = badBet(bet, Bet.W_LIMIT_BET);
			return bet;
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
		CasinoLogger.info(TRANSACTION, bet.toString());
		return bet;
	}
}
