package com.posretail.repository;

import com.posretail.model.DevolucionCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevolucionCompraRepository extends JpaRepository<DevolucionCompra, Integer> {}
