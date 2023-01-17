package app.foot.service;

import app.foot.model.Match;
import app.foot.model.PlayerScorer;
import app.foot.model.Team;
import app.foot.model.TeamMatch;
import app.foot.repository.MatchRepository;
import app.foot.repository.PlayerEntityRespository;
import app.foot.repository.PlayerScoreRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.MatchMapper;
import app.foot.repository.mapper.PlayerMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class MatchService {
    private final MatchRepository repository;
    private final PlayerScoreRepository playerScoreRepository;
    private final MatchMapper mapper;
    private final PlayerEntityRespository repositoryPlayer;

    private final PlayerMapper playerMapper;
    public List<Match> getMatches() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Match addGoalToACertainMatch (List<PlayerScorer> scorers, Integer matchesiD){
       MatchEntity match = repository.findById(matchesiD).get();

        for (PlayerScorer scorer : scorers) {
            PlayerEntity player = repositoryPlayer.findById(scorer.getPlayer().getId()).get() ;
            PlayerScoreEntity playerScorer = PlayerScoreEntity.builder()
                    .match(match)
                    .player(player)
                    .minute(scorer.getMinute())
                    .build();
            if (playerScorer.getPlayer().isGuardian()) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable");
            }
            if (playerScorer.getMinute() < 0 || playerScorer.getMinute() > 90) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable");
            }
            playerScoreRepository.save(playerScorer);
        }
        return mapper.toDomain(match);
    }
}
