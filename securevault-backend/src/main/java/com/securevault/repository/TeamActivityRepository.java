package com.securevault.repository;

import com.securevault.model.TeamActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamActivityRepository extends JpaRepository<TeamActivity, Long> {
    List<TeamActivity> findByTeamIdOrderByTimestampDesc(Long teamId);
}
