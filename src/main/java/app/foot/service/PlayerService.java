package app.foot.service;

import app.foot.model.Player;
import app.foot.repository.PlayerRepository;
import app.foot.repository.TeamRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.mapper.PlayerMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    private TeamRepository teamRepository ;
    private final PlayerMapper mapper;

    public List<Player> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Player> createPlayers(List<Player> toCreate) {
        return repository.saveAll(toCreate.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toUnmodifiableList())).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<app.foot.controller.rest.Player> updatePlayers(List<app.foot.controller.rest.Player> player){
        StringBuilder exceptionBuilder = new StringBuilder( );
        List<app.foot.controller.rest.Player> playersHasBeenUpdate = new ArrayList<>() ;
        for (app.foot.controller.rest.Player player1 : player) {
            if(player1.getId() == null){
                throw (new RuntimeException(String.valueOf(exceptionBuilder.append("Player#")
                        .append(player1.getId())
                        .append("cannot be null")))) ;
            }
            PlayerEntity entity = repository.findById(mapper.toEntity(player1).getId()).get() ;
            entity.setTeam(teamRepository.findByName(player1.getName()));
            entity.setName(player1.getName());
            entity.setGuardian(player1.getIsGuardian());
            entity.setId(player1.getId());
            playersHasBeenUpdate.add(mapper.toRest(entity)) ;
            repository.save(entity) ;
        }

        return repository.saveAll(playersHasBeenUpdate.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toUnmodifiableList())).stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());

    }
}
