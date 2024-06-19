package com.zexal.repository;

import org.springframework.data.repository.CrudRepository;

import com.zexal.zexal.models.Duelo;
import com.zexal.zexal.models.Participante;

public interface ParticipanteRepository extends CrudRepository<Participante, String> {
    Iterable<Participante> findByDuelo(Duelo duelo);
    Participante findByCPF(String CPF);
}
