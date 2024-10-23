package es.upsa.ssi.gateway.coches.infraestructure.cdi.decorators;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.gateway.coches.infraestructure.cdi.producers.RedisCache;
import es.upsa.ssi.gateway.coches.infraestructure.cdi.qualifiers.RedisCacheDefaults;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import org.jboss.logging.Logger;

import java.util.Optional;

//INTERCEPTA los metodos del repositorio
//Cuando en los casos de uso inyectamos un repositorio, se inyecta en realidad esta clase, que tambien implementa repository
//Por lo que desde aqui usamos los metodos del repository y mandamos a los casos de uso lo que queramos
// segun este en la cache o no, ademas de guardar en la misma lo que no estaba
@Dependent
@Decorator
//Tiene prioridad sobre lo demas, por eso intercepta
@Priority(Interceptor.Priority.APPLICATION)
public abstract class QuarkusRepositoryDecorator implements Repository
{
    @Inject
    @Delegate
    @Any
    Repository repository;

    //Defino el nombre de la tabla de cache
    @Inject
    @RedisCacheDefaults(name = "coches")
    RedisCache<String, Coche> cochesCache;
    @Inject
    Logger logger;

    @Override
    public Optional<Coche> findCocheByMatricula(String matricula) throws AppException
    {
        Optional<Coche> optCoche = Optional.ofNullable(  cochesCache.get(matricula));
        //si esta en la cache lo obtenemos de ahi
        if ( optCoche.isPresent() )
        {
            logger.log(Logger.Level.INFO, String.format("Cache HIT [%s]",matricula) );
            return optCoche;
        }
        logger.log(Logger.Level.INFO, String.format("Cache MISS [%s]",matricula) );
        //Si no esta y l oencontramos en la bbdd lo guardamos en cache y lo devolvemos
        optCoche = repository.findCocheByMatricula(matricula);
        optCoche.ifPresent(coche -> cochesCache.set(matricula, coche));
        return optCoche;
    }

    @Override
    public boolean updateCoche(Coche coche) throws AppException
    {
        boolean returnedValue = repository.updateCoche(coche);
        if ( returnedValue == true )
        {
            cochesCache.set(coche.getMatricula(), coche);
        }
        return returnedValue;
    }

    @Override
    public boolean deleteCocheByMatricula(String matricula) throws AppException
    {
        boolean returnedValue = repository.deleteCocheByMatricula(matricula);
        if ( returnedValue == true )
        {
            cochesCache.del(matricula);
        }
        return returnedValue;
    }


}
