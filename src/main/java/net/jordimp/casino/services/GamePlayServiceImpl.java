package net.jordimp.casino.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.services.handler.Jugada;

@Service("gamePlayService")
public class GamePlayServiceImpl implements GamePlayService {

	@Autowired
	private PlayerService playerService;

	private GamePlayServiceImpl() {
	}

	@Override
	public Bet bet(Bet bet) {

		Optional<Player> playerOpt = Optional.ofNullable(playerService.findByUUID(bet.getPlayerUUID()));
		return Jugada.bet(bet, playerOpt);
	}

}