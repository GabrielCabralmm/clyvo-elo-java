package com.clyvoelo.painel.repository;

import com.clyvoelo.painel.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByStatusOrderByDataCriacaoAsc(String status);
}
