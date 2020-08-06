package dev.kodice.games.ludo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.kodice.games.ludo.domain.model.Meeple;

public interface MeepleRepository extends JpaRepository<Meeple, Long>{

}
