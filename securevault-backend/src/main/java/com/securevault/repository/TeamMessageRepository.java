package com.securevault.repository;

import com.securevault.model.TeamMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMessageRepository extends JpaRepository<TeamMessage, Long> {
    List<TeamMessage> findByTeamIdOrderByTimestampAsc(Long teamId);
}
