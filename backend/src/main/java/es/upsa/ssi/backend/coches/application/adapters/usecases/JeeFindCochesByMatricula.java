package es.upsa.ssi.backend.coches.application.adapters.usecases;

import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.FindCocheByMatricula;
import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class JeeFindCochesByMatricula implements FindCocheByMatricula
{
    @Inject
    Repository repository;

    @Override
    public Optional<Coche> execute(String matricula) throws AppException
    {
        return repository.findCocheByMatricula(matricula);
    }
}
