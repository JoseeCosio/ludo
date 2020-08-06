package dev.kodice.games.ludo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import dev.kodice.games.ludo.domain.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

	@Modifying
	@Query(value = "UPDATE game_state SET extra_turn='true' where game_state.id=:gameId ;", nativeQuery = true)
	void setExtraTurn(Long gameId);

	@Modifying
	@Query(value = "UPDATE game_state SET extra_turn='false' where game_state.id=:gameId ;", nativeQuery = true)
	void removeExtraTurn(Long gameId);

	@Modifying
	@Query(value = "UPDATE game_state SET rolled=:dice where game_state.id=:gameId ;", nativeQuery = true)
	void setRolled(int dice, Long gameId);

	@Modifying
	@Query(value = "UPDATE game_state SET roll='true', moving='false' where game_state.id=:gameId ;", nativeQuery = true)
	void setRoll(Long gameId);

	@Modifying
	@Query(value = "UPDATE game_state SET roll='false', moving='true' where game_state.id=:gameId ;", nativeQuery = true)
	void setMove(Long gameId);

}
