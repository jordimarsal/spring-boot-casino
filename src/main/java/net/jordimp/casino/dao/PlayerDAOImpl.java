package net.jordimp.casino.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.jordimp.casino.entity.Player;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

	@Autowired
	MemoryEntities me;

	// Simple method, only admits writing or overriding
	@Override
	public void save(Player player) {
		me.persist(player);

	}

	@Override
	public boolean delete(Player player) {
		return me.remove(player);
	}

	@Override

	public Player findByUUID(String uuid) {
		return me.get(uuid);
	}

	@Override
	public Player login(Player player) {
		save(player);
		return player;
	}

	@Override
	public boolean logout(String uuid) {
		Optional<Player> playerOpt = Optional.ofNullable(findByUUID(uuid));
		return playerOpt.map(ply -> delete(ply)).orElse(false);
	}

	@Override
	public void purgeLogins() {
		me.purge();
	}

}
