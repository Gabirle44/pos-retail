package com.posretail.repository;

import com.posretail.model.IntentoLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntentoLoginRepository extends JpaRepository<IntentoLogin, Integer> {}
