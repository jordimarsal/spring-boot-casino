package net.jordimp.casino.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.jordimp.casino.dao.PlayerDAO;
import net.jordimp.casino.entity.Player;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDAO playerDAO;

	private PlayerServiceImpl() {
	}

	@Override
	@Transactional(readOnly = true)
	public Player findByUUID(String uuid) {
		return playerDAO.findByUUID(uuid);
	}

	@Override
	@Transactional
	public void delete(Player player) {
		playerDAO.delete(player);
	}

	@Override
	@Transactional
	public void save(Player player) {
		playerDAO.save(player);
	}

	@Override
	@Transactional
	public Player login(Player player) {
		return playerDAO.login(player);
	}

	@Override
	@Transactional
	public boolean logout(String uuid) {
		return playerDAO.logout(uuid);
	}

	public void purgeLogins() {
		playerDAO.purgeLogins();
		
	}

}