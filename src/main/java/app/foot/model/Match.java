package app.foot.model;

import lombok.*;

import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Setter

public class Match {
    private Integer id;
    private TeamMatch teamA;
    private TeamMatch teamB;
    private String stadium;
    private Instant datetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) && Objects.equals(teamA, match.teamA) && Objects.equals(teamB, match.teamB) && Objects.equals(stadium, match.stadium) && Objects.equals(datetime, match.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamA, teamB, stadium, datetime);
    }
}
