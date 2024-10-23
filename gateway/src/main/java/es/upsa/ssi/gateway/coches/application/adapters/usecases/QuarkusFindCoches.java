package es.upsa.ssi.gateway.coches.application.adapters.usecases;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.FindCoches;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class QuarkusFindCoches implements FindCoches
{
    @Inject
    Repository repository;


    @Override
    public List<Coche> execute() throws AppException
    {
        return repository.findCoches();
    }
}
