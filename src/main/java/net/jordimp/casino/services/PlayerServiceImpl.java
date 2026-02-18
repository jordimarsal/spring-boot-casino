package net.jordimp.casino.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jordimp.casino.dao.PlayerDAO;
import net.jordimp.casino.entity.Player;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDAO playerDAO;

	private PlayerServiceImpl() {
	}

	@Override
	public Player findByUUID(String uuid) {
		return playerDAO.findByUUID(uuid);
	}

	@Override
	public void delete(Player player) {
		playerDAO.delete(player);
	}

	@Override
	public void save(Player player) {
		playerDAO.save(player);
	}

	@Override
	public Player login(Player player) {
		return playerDAO.login(player);
	}

	@Override
	public boolean logout(String uuid) {
		return playerDAO.logout(uuid);
	}

	public void purgeLogins() {
		playerDAO.purgeLogins();
	}
}