package net.jordimp.casino.services;

import net.jordimp.casino.entity.Player;

public interface PlayerService {

	void delete(Player player);

	void save(Player player);

	Player findByUUID(String UUID);

	Player login(Player player);

	boolean logout(String uuid);

}
