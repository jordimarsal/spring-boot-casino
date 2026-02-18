package net.jordimp.casino.repositories;

import java.util.List;
import net.jordimp.casino.services.dto.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, String> {

  List<Bet> findByPlayerUUID(String playerUUID);

  @Query("SELECT b FROM Bet b WHERE b.playerUUID = :playerUuid ORDER BY b.createdAt DESC")
  List<Bet> findByPlayerUUIDOrderByCreatedAtDesc(@Param("playerUuid") String playerUuid);

  List<Bet> findByBadFalse();
}
