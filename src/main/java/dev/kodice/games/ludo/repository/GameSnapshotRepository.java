package dev.kodice.games.ludo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.kodice.games.ludo.domain.model.GameSnapshot;

public interface GameSnapshotRepository extends JpaRepository<GameSnapshot, Long>{

	@Query(value = "SELECT u.* FROM game_snapshot u where u.g_id=:gameId", nativeQuery = true)
	List<GameSnapshot> findAllBygId(Long gameId);
	
}
