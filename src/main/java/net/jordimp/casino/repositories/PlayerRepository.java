package net.jordimp.casino.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {

	List<Player> findByUserProvider(UserProvider provider);

	@Query("SELECT p FROM Player p WHERE p.loginDate > :cutoff")
	List<Player> findActivePlayers(@Param("cutoff") Date cutoff);

	@Modifying
	@Query("DELETE FROM Player p WHERE p.loginDate < :cutoff")
	int deleteExpiredPlayers(@Param("cutoff") Date cutoff);
}
