package com.zexal.repository;

import org.springframework.data.repository.CrudRepository;

import com.zexal.zexal.models.Duelo;

public interface DueloRepository extends CrudRepository<Duelo, Long> {
    Duelo findByCodigo(long codigo);
}