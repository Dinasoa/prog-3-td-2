package app.foot.repository;

import app.foot.repository.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerEntityRespository extends JpaRepository<PlayerEntity , Integer> {
}
