package es.upsa.ssi.common.model.ports.repositories;



import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;

import java.util.List;

//Es el interface que implementamos en el Dao para usar la cache
public interface CacheDao
{
    public List<Coche> findCoches() throws AppException;
    public Coche findCocheByMatricula(String matricula) throws AppException;
    public void updateCoche(String matricula, Coche coche) throws AppException;
    public void deleteCocheByMatricula(String matricula) throws AppException;
    public Coche insertCoche(Coche coche) throws AppException;
}
