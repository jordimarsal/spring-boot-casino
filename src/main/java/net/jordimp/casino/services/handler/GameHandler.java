package net.jordimp.casino.services.handler;

import java.util.HashMap;
import java.util.Map;

import net.jordimp.casino.services.factory.GameFactory;
import net.jordimp.casino.services.vo.Game;

public class GameHandler {

	static Map<String, Game> games = new HashMap<String, Game>();

	public static Game getGame(String UUID) {
		return getFromMap(UUID);
	}

	private static Game getFromMap(String UUID) {
		if (!games.containsKey(UUID)) {
			Game gotGame = new GameFactory().create(UUID);
			games.put(UUID, gotGame);
			return gotGame;
		}
		return games.get(UUID);
	}

}
