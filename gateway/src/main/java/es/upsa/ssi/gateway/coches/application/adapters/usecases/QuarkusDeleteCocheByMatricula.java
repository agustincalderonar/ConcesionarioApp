package es.upsa.ssi.gateway.coches.application.adapters.usecases;

import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.DeleteCocheByMatricula;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class QuarkusDeleteCocheByMatricula implements DeleteCocheByMatricula
{
    @Inject
    Repository repository;


    @Override
    public boolean execute(String matricula) throws AppException
    {
        return repository.deleteCocheByMatricula(matricula);
    }
}
