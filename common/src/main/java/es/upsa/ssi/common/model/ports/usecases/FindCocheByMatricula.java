package es.upsa.ssi.common.model.ports.usecases;


import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;

import java.util.Optional;

public interface FindCocheByMatricula
{
    Optional<Coche> execute(String matricula) throws AppException;
}
