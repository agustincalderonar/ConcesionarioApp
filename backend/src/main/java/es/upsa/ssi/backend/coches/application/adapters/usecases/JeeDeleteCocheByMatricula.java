package es.upsa.ssi.backend.coches.application.adapters.usecases;


import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.DeleteCocheByMatricula;
import es.upsa.ssi.common.model.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JeeDeleteCocheByMatricula implements DeleteCocheByMatricula
{
    @Inject
    Repository repository;


    @Override
    public boolean execute(String matricula) throws AppException
    {
        return repository.deleteCocheByMatricula(matricula);
    }
}
