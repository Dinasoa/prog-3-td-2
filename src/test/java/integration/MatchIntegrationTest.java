package integration;

import app.foot.FootApi;
import app.foot.controller.rest.*;
import app.foot.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.assertThrowsExceptionMessage;
import static utils.TestUtils.nullScoreTimeScorer;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class MatchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();  //Allow 'java.time.Instant' mapping

    @Test
    void read_match_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedMatch2(), actual);
    }

    @Test
    void read_matches() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse() ;

        assertEquals(HttpStatus.OK.value() , response.getStatus());
    }

    @Test
    void add_goals_to_match3_ok() throws Exception {
        ArrayList<PlayerScorer> scorers = new ArrayList<>();
        PlayerScorer scorer = PlayerScorer.builder()
                .scoreTime(30)
                .isOG(false)
                .player(Player.builder()
                        .id(6)
                        .isGuardian(false)
                        .teamName("E3")
                        .name("J6")
                        .build())
                .build();

        scorers.add(scorer);
        Match match = Match.builder()
                .datetime(Instant.parse("2023-01-01T14:00:00Z"))
                .teamA(TeamMatch.builder()
                        .scorers(List.of(PlayerScorer.builder()
                                .scoreTime(30)
                                .isOG(true)
                                .player(Player.builder()
                                        .id(6)
                                        .teamName("E3")
                                        .isGuardian(false)
                                        .build())
                                .build()))
                        .score(1)
                        .team(Team.builder()
                                .id(1)
                                .name("E1")
                                .build())
                        .build())
                .teamB(TeamMatch.builder()
                        .scorers(List.of(PlayerScorer.builder().build()))
                        .team(Team.builder()
                                .id(3)
                                .name("E3")
                                .build())
                        .score(0)
                        .build())
                .stadium("S3")
                .id(3)
                .build();

        int MATCH_ID = 3 ;
        MockHttpServletResponse response = mockMvc.perform(post("/matches/3/goals" , MATCH_ID)
                        .content(objectMapper.writeValueAsString(scorers))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

//        List<Match> actual = (List<Match>) convertFromHttpResponse(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals(match , actual);
    }

    @Test
    void add_goals_to_match3_ko() throws java.lang.Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/matches/3/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                                .scoreTime(30)
                                .isOG(false)
                                .player(Player.builder()
                                        .id(6)
                                        .isGuardian(true)
                                        .teamName("E3")
                                        .name("J6")
                                        .build())
                                .build())))
                )
                .andReturn()
                .getResponse();
        assertThrowsExceptionMessage("400 BAD_REQUEST : Player#6 is a guardian so they cannot score",
                BadRequestException.class, () -> response.getStatus());
    }

    private static Match expectedMatch2() {
        return Match.builder()
                .id(2)
                .teamA(teamMatchA())
                .teamB(teamMatchB())
                .stadium("S2")
                .datetime(Instant.parse("2023-01-01T14:00:00Z"))
                .build();
    }

    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA() {
        return TeamMatch.builder()
                .team(team2())
                .score(2)
                .scorers(List.of(PlayerScorer.builder()
                                .player(player3())
                                .scoreTime(70)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player6())
                                .scoreTime(80)
                                .isOG(true)
                                .build()))
                .build();
    }

    private static Team team3() {
        return Team.builder()
                .id(3)
                .name("E3")
                .build();
    }

    private static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
                .isGuardian(false)
                .build();
    }

    private static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .isGuardian(false)
                .build();
    }

    private static Team team2() {
        return Team.builder()
                .id(2)
                .name("E2")
                .build();
    }
    private Match convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType match = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Match.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                match);
    }
    public static void assertThrowsExceptionMessage(String message, Class exceptionClass, Executable executable) {
        Throwable exception = assertThrows(exceptionClass, executable);
        assertEquals(message, exception.getMessage());
    }
}
