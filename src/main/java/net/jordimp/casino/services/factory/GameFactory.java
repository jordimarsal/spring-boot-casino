package net.jordimp.casino.services.factory;

import net.jordimp.casino.services.vo.Blackjack;
import net.jordimp.casino.services.vo.Game;
import net.jordimp.casino.services.vo.Poker;
import net.jordimp.casino.services.vo.Roulette;
import net.jordimp.casino.services.vo.Slot;
import net.jordimp.casino.services.vo.VideoBingo;

public class GameFactory implements AbstractFactory<Game> {

	@Override
	public Game create(String gameType) {
		Game result;
		switch (gameType) {
		case "VIDEOBINGO-UUID":
			result = new VideoBingo();
			break;
		case "BLACKJACK-UUID":
			result = new Blackjack();
			break;
		case "POKER-UUID":
			result = new Poker();
			break;
		case "ROULETTE-UUID":
			result = new Roulette();
			break;
		case "SLOT-UUID":
			result = new Slot();
			break;
		default:
			result = null;
			break;
		}
		return result;
	}

}
