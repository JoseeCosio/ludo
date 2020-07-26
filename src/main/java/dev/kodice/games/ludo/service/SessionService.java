package dev.kodice.games.ludo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.dto.RegisterDto;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.domain.model.Session;
import dev.kodice.games.ludo.repository.PlayerRepository;
import dev.kodice.games.ludo.repository.SessionRepository;

@Service
public class SessionService {

	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private PlayerRepository playerRepository;

	public Session createSession(RegisterDto dto) {
		List<Session> oldSession = sessionRepository.findByPlayerId(dto.getPlayerId());
		if (oldSession.size()>0) return this.placeTaken();
		this.sessionRepository.deleteInBatch(oldSession);
		
		Session session = new Session();
		session.setPlayerId(dto.getPlayerId());
		session.setKey(generateKey());
		session.setValidUntil(LocalDateTime.now().plusHours(1));
		Player player = playerRepository.getOne(dto.getPlayerId());
		player.setName(dto.getPlayerName());
		player.setKey(session.getKey());
		playerRepository.save(player);
		return this.sessionRepository.save(session);
	}

	private String generateKey() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		LocalDateTime time = LocalDateTime.now();
		return passwordEncoder.encode(time.toString());
	}
	
	private Session placeTaken() {
		Session session = new Session();
		session.setKey("Another player has taken that seat!");
		return session;
	}

}
