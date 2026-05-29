package com.posretail.repository;

import com.posretail.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {}
