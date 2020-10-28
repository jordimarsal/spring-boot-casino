package net.jordimp.casino.services.handler;

import java.util.HashMap;
import java.util.Map;

import net.jordimp.casino.services.factory.GameFactory;
import net.jordimp.casino.services.vo.Game;

public class GameHandler {

	static Map<String, Game> games = new HashMap<>();

	private GameHandler() {}
	
	public static Game getGame(String uuid) {
		return getFromMap(uuid);
	}

	private static Game getFromMap(String uuid) {
		if (!games.containsKey(uuid)) {
			Game gotGame = new GameFactory().create(uuid);
			games.put(uuid, gotGame);
			return gotGame;
		}
		return games.get(uuid);
	}

}
