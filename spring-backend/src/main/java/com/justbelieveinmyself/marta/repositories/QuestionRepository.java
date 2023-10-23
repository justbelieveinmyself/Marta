package com.justbelieveinmyself.marta.repositories;

import com.justbelieveinmyself.marta.domain.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
