package dev.kodice.games.ludo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.kodice.games.ludo.domain.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long>{
	
	List<Session> findByPlayerId(Long playerId);
	
	Session findByKey(String key);


}
