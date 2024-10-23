package es.upsa.ssi.backend.coches.application.adapters.usecases;

import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.ports.usecases.InsertCoche;
import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JeeInsertCoche implements InsertCoche
{
    @Inject
    Repository repository;

    /*@Override
    public Coche execute(Coche coche) throws AppException
    {
        if ( Objects.isNull( coche.getMatricula() ) )
        {
            //Le da una matricula, por lo que ya no es necesario darsela con la seguencia sql
            coche = coche.withMatricula(UUID.randomUUID().toString());
        }
        return repository.insertCoche(coche);
    }
     */

    //En vez de darle la matricula con UUID se la generará sql
    @Override
    public Coche execute(Coche coche) throws AppException
    {
        return repository.insertCoche(coche);
    }
}
