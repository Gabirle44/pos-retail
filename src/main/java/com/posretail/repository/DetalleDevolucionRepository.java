package com.posretail.repository;

import com.posretail.model.DetalleDevolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleDevolucionRepository extends JpaRepository<DetalleDevolucion, Integer> {}
