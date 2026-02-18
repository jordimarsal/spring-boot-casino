package net.jordimp.casino.services;

import java.util.Optional;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.services.handler.Jugada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gamePlayService")
public class GamePlayServiceImpl implements GamePlayService {

  @Autowired private PlayerService playerService;

  private GamePlayServiceImpl() {}

  @Override
  public Bet bet(Bet bet) {

    Player player = null;
    try {
      player = playerService.findByUUID(bet.getPlayerUUID());
    } catch (net.jordimp.casino.exceptions.PlayerNotFoundException e) {
      // Player not found - playerOpt will be Optional.empty()
    }
    Optional<Player> playerOpt = Optional.ofNullable(player);
    return Jugada.bet(bet, playerOpt);
  }
}
