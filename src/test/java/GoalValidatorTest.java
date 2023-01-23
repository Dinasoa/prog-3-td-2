import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.validator.GoalValidator;
import app.foot.model.Team;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

//TODO-1: complete these tests
public class GoalValidatorTest {
    GoalValidator subject = new GoalValidator();
    @Test
    void accept_ok() {
        assertDoesNotThrow(() -> subject.accept(isNotGuardianScorer()));
    }

    //Mandatory attributes not provided : scoreTime
    @Test
    void accept_ko() {
        assertThrows( RuntimeException.class , () -> subject.accept(isNotGuardianFalseWithNoScoreTime())) ;
    }

    @Test
    void when_guardian_throws_exception() {
        assertThrows(RuntimeException.class,() -> subject.accept(isGuardianScorer())) ;
    }

    @Test
    void when_score_time_greater_than_90_throws_exception() {
        assertThrows(RuntimeException.class, () -> subject.accept(playerScoreTimeGreaterThan90()));
    }

    @Test
    void when_score_time_less_than_0_throws_exception() {
        assertThrows(RuntimeException.class, () -> subject.accept(playerScoreTimeLessThan0()));
    }

    public PlayerScorer isNotGuardianScorer(){
        return PlayerScorer.builder()
                .player(playerIsNotGuardian())
                .scoreTime(45)
                .isOG(false)
                .build();
    }

    public Player playerIsNotGuardian(){
        return Player.builder()
                .id(1)
                .name("Messi")
                .isGuardian(false)
                .build();
    }

    public PlayerScorer isGuardianScorer(){
        return PlayerScorer.builder()
                .player(playerIsGuardian())
                .isOG(true)
                .scoreTime(45)
                .build();
    }

    public Player playerIsGuardian(){
        return Player.builder()
                .id(2)
                .isGuardian(true)
                .name("GuardianBoy")
                .build();
    }

    public PlayerScorer isNotGuardianFalseWithNoScoreTime(){
        return PlayerScorer.builder()
                .scoreTime(null)
                .isOG(false)
                .player(playerIsNotGuardian())
                .build();
    }

    public PlayerScorer playerScoreTimeGreaterThan90(){
        return PlayerScorer.builder()
                .scoreTime(91)
                .isOG(true)
                .player(playerIsNotGuardian())
                .build();
    }

    public PlayerScorer playerScoreTimeLessThan0(){
        return PlayerScorer.builder()
                .scoreTime(-1)
                .isOG(true)
                .player(playerIsNotGuardian())
                .build();
    }
}
