package dev.kodice.games.ludo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.kodice.games.ludo.domain.model.Game;

public interface GameRepository extends JpaRepository<Game,Long> {

}
