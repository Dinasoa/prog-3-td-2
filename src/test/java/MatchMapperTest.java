import app.foot.model.*;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.MatchMapper;
import app.foot.repository.mapper.PlayerMapper;
import app.foot.repository.mapper.TeamMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class MatchMapperTest {
//    Il faut d'abord mocker les dependances
//    TeamMapper est le mock de la classe
    TeamMapper teamMapper = mock(TeamMapper.class);

    PlayerMapper playerMapper = mock(PlayerMapper.class);

//    Condition de nommage : subject
    MatchMapper subject = new MatchMapper(teamMapper , playerMapper);

    @Test
    void to_domain_ok(){
//        N'importe quelle valeur
        when(teamMapper.toDomain(any())).thenReturn(Team.builder().build());
//        Il faut caster parce qu'il y a plusieurs toDomain dans playerMapper
        when(playerMapper.toDomain((PlayerScoreEntity) any())).thenReturn(PlayerScorer.builder().build());
        when(playerMapper.toDomain((PlayerEntity) any())).thenReturn(Player.builder().build());
//        Si on specifie les valeurs par defaut
//        when(teamMapper.toDomain(eq(TeamEntity.builder()
//                .id(1)
//                .name("bla")
//                .build())));

        Match expected = Match.builder().build();
        Match actual = subject.toDomain(MatchEntity.builder()
//        Liste vide
                .scorers(List.of())
                .build());
//        un autre moyen de faire marcher ce test est de set la valeur des teams en null
//        actual.setTeamA(null);
//        actual.setTeamB(null);

        assertEquals(expected , actual);
    }
}
