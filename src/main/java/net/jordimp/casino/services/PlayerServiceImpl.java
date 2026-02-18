package net.jordimp.casino.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.exceptions.PlayerNotFoundException;
import net.jordimp.casino.repositories.PlayerRepository;
import net.jordimp.casino.utils.CasinoLoggerUtils;

@Service("playerService")
@Transactional
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	// Constructor must be public/protected for CGLIB proxy (used by @Transactional)
	protected PlayerServiceImpl() {
	}

	@Override
	@Transactional(readOnly = true)
	public Player findByUUID(String uuid) {
		return playerRepository.findById(uuid)
				.orElseThrow(() -> new PlayerNotFoundException(uuid));
	}

	@Override
	public void delete(Player player) {
		playerRepository.delete(player);
	}

	@Override
	public void save(Player player) {
		playerRepository.save(player);
	}

	@Override
	public Player login(Player player) {
		return playerRepository.save(player);
	}

	@Override
	public boolean logout(String uuid) {
		Player player = playerRepository.findById(uuid)
				.orElseThrow(() -> new PlayerNotFoundException(uuid));
		player.setLoginDate(null);
		playerRepository.save(player);
		return true;
	}

	public void purgeLogins() {
		Date cutoff = new Date(System.currentTimeMillis() - 300000); // 5 minutes
		int deleted = playerRepository.deleteExpiredPlayers(cutoff);
		CasinoLoggerUtils.info("PURGE", "Deleted " + deleted + " expired players");
	}
}