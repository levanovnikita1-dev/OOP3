package org.example.AppDataAPI;

import org.example.Class.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Integer> {
}