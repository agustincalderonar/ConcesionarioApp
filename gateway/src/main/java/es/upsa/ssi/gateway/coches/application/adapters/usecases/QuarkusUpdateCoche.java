package es.upsa.ssi.gateway.coches.application.adapters.usecases;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.UpdateCoche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuarkusUpdateCoche implements UpdateCoche
{
    @Inject
    Repository repository;

    @Override
    public boolean execute(Coche coche) throws AppException
    {
        return repository.updateCoche(coche);
    }
}
