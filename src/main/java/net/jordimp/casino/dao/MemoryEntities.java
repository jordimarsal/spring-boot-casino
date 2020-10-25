package net.jordimp.casino.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.utils.CasinoLogger;
import net.jordimp.casino.utils.Utils;

@Component
public class MemoryEntities {

	static Map<String, Player> players = new HashMap<String, Player>();

	public void persist(Player player) {
		players.put(player.getUUID(), player);
	}

	public boolean remove(Player player) {
		if (players.containsKey(player.getUUID())) {
			players.remove(player.getUUID(), player);
			return true;
		}
		return false;
	}

	public Player get(String uuid) {
		return players.getOrDefault(uuid, null);
	}

	public void purge() {
		players.entrySet().removeIf(entry -> !Utils.isInLoginTime(entry.getValue()));
		CasinoLogger.info("PURGE", "Players in memory = "+players.size());
	}

}
