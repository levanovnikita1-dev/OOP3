package org.example.AppDataAPI;

import org.example.Class.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends JpaRepository<Human, Integer> { // Проверьте, чтобы тут было строго Integer вместо Long
}