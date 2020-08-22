package dev.kodice.games.ludo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import dev.kodice.games.ludo.domain.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	@Modifying
	@Query(value = "UPDATE player SET turn='true' where player.id=:id", nativeQuery = true)
	void setTurn(Long id);
	
	@Modifying
	@Query(value = "UPDATE player SET turn='false' where player.id=:id", nativeQuery = true)
	void removeTurn(Long id);
	
	@Query(value = "SELECT p.* from player p\r\n" + 
			"			join game_player on p.id= game_player.player_id\r\n" + 
			"			join game on game_player.game_id = game.id\r\n" + 
			"			where game.id=:gameId", nativeQuery = true)
	List<Player> findByGameId(Long gameId);
	
}
