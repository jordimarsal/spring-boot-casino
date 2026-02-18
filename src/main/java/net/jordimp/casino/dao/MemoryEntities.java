package net.jordimp.casino.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.Utils;

@Component
public class MemoryEntities {

	private static final Map<String, Player> players = new ConcurrentHashMap<>();

	public void persist(Player player) {
		players.put(player.getUUID(), player);
	}

	public boolean remove(Player player) {
		return players.remove(player.getUUID(), player);
	}

	public Player get(String uuid) {
		return players.getOrDefault(uuid, null);
	}

	public void purge() {
		players.entrySet().removeIf(entry -> !Utils.isInLoginTime(entry.getValue()));
		CasinoLoggerUtils.info("PURGE", "Players in memory = " + players.size());
	}

	// For testing purposes
	public void clear() {
		players.clear();
	}

}
