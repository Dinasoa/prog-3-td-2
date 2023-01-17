package app.foot.repository;

import app.foot.repository.entity.PlayerScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerScoreRepository extends JpaRepository<PlayerScoreEntity , Integer> {
}
