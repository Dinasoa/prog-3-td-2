import app.foot.model.*;
import app.foot.repository.MatchRepository;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.PlayerMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//TODO-2: complete these tests
public class PlayerMapperTest {
    MatchRepository matchRepository = mock(MatchRepository.class) ;
    PlayerRepository playerRepository = mock(PlayerRepository.class) ;

    PlayerMapper subject = new PlayerMapper(matchRepository , playerRepository);
    @Test
    void player_to_domain_ok() {
        app.foot.model.Player expected = Player.builder()
                .id(1)
                .isGuardian(false)
                .teamName("PSG")
                .name("Mbappe")
                .build();

        app.foot.model.Player actual = subject.toDomain(PlayerEntity.builder()
                        .id(1)
                        .team(TeamEntity.builder()
                                .id(1)
                                .name("PSG")
                                .build())
                        .guardian(false)
                        .name("Mbappe")
                .build());
        assertEquals(expected , actual);
    }

    @Test
    void player_scorer_to_domain_ok() {
        PlayerScoreEntity playerScoreEntity = PlayerScoreEntity.builder()
                .id(1)
                .player(PlayerEntity.builder()
                        .id(1)
                        .name("Mbappe")
                        .team(TeamEntity.builder()
                                .id(1)
                                .name("PSG")
                                .build())
                        .guardian(false)
                        .build())
                .minute(45)
                .ownGoal(true)
                .build();

        PlayerScorer expected = PlayerScorer.builder()
                .player(subject.toDomain(playerScoreEntity.getPlayer()))
                .minute(45)
                .isOwnGoal(true)
                .build();

        PlayerScorer actual = subject.toDomain(playerScoreEntity);

        assertEquals(expected , actual);
    }

    @Test
    void player_scorer_to_entity_ok() {
       PlayerScorer scorer = PlayerScorer.builder()
               .player(player())
               .minute(45)
               .isOwnGoal(false)
               .build();
       when(playerRepository.findById(1)).thenReturn(Optional.ofNullable(playerEntity())) ;
       when(matchRepository.findById(1)).thenReturn(Optional.ofNullable(matchEntity())) ;

       int matchId = 1 ;

       PlayerScoreEntity actual = subject.toEntity(matchId , scorer) ;

       PlayerScoreEntity expected = PlayerScoreEntity.builder()
               .match(matchEntity())
               .minute(45)
               .ownGoal(false)
               .player(playerEntity())
               .build();

       assertEquals(expected , actual);
    }

    public Player player(){
        return Player.builder()
                .id(1)
                .name("Mbappe")
                .teamName("PSG")
                .isGuardian(false)
                .build() ;
    }

    public PlayerEntity playerEntity (){
        return PlayerEntity.builder()
                .id(1)
                .name("Mbappe")
                .team(teamEntity())
                .guardian(false)
                .build();
    }

    public TeamEntity teamEntity(){
        return TeamEntity.builder()
                .id(1)
                .name("PSG")
                .build();
    }

    public MatchEntity matchEntity(){
        ArrayList<PlayerScoreEntity> scorers = new ArrayList<PlayerScoreEntity>() ;

        PlayerScoreEntity scorer1 = PlayerScoreEntity.builder()
                .id(1)
                .match(MatchEntity.builder().build())
                .minute(20)
                .ownGoal(true)
                .player(playerEntity())
                .build() ;

        PlayerScoreEntity scorer2 = PlayerScoreEntity.builder()
                .id(1)
                .match(MatchEntity.builder().build())
                .minute(45)
                .ownGoal(true)
                .player(playerEntity())
                .build() ;


        scorers.add(scorer1);
        scorers.add(scorer2);

        return MatchEntity.builder()
                .scorers(scorers)
                .teamA(
                        TeamEntity.builder()
                                .id(1)
                                .name("PSG")
                                .build()
                )
                .teamB(
                        TeamEntity.builder()
                                .id(2)
                                .name("FCB")
                                .build()
                )
                .stadium("Qatar")
//                .datetime()
                .build();
    }

}
