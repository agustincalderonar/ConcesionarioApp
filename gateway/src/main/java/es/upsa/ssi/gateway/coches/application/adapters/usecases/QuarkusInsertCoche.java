package es.upsa.ssi.gateway.coches.application.adapters.usecases;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.InsertCoche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class QuarkusInsertCoche implements InsertCoche
{
    @Inject
    Repository repository;

    @Override
    public Coche execute(Coche coche) throws AppException
    {
        if ( Objects.isNull( coche.getMatricula() ) )
        {
            coche = coche.withMatricula(UUID.randomUUID().toString());
        }
        return repository.insertCoche(coche);
    }
}
