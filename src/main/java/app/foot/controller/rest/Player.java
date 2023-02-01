package app.foot.controller.rest;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Player {
    private Integer id;
    private String name;
    private String teamName;
    private Boolean isGuardian;
}
