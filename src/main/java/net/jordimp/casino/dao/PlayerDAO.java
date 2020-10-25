package net.jordimp.casino.dao;

import net.jordimp.casino.entity.Player;

public interface PlayerDAO {

	public void save(Player player);

	public boolean delete(Player player);

	public Player findByUUID(String UUID);

	public Player login(Player player);

	public boolean logout(String uuid);

	public void purgeLogins();

}
