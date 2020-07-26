package dev.kodice.games.ludo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.kodice.games.ludo.domain.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
