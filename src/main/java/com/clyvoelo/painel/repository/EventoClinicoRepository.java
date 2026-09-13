package com.clyvoelo.painel.repository;

import com.clyvoelo.painel.model.EventoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoClinicoRepository extends JpaRepository<EventoClinico, Long> {
    List<EventoClinico> findByStatusOrderByDataEventoAsc(String status);
}
