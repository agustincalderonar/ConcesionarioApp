package es.upsa.ssi.backend.coches.application.adapters.repositorydao;

import es.upsa.ssi.common.model.ports.repositories.CacheDao;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.exceptions.CocheNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JeeRepository implements Repository
{
    @Inject
    CacheDao cochesDao;

    @Override
    public List<Coche> findCoches() throws AppException
    {
        return cochesDao.findCoches();
    }

    @Override
    public Optional<Coche> findCocheByMatricula(String matricula) throws AppException
    {
        try
        {
            return Optional.of( cochesDao.findCocheByMatricula(matricula) );

        } catch (CocheNotFoundException nfe)
          {
            return Optional.empty();
          }
    }

    @Override
    public boolean updateCoche(Coche coche) throws AppException
    {
        try
        {
            cochesDao.updateCoche(coche.getMatricula(), coche);
            return true;
        } catch (CocheNotFoundException nfe)
          {
              return false;
          }
    }

    @Override
    public boolean deleteCocheByMatricula(String matricula) throws AppException
    {
        try
        {
            cochesDao.deleteCocheByMatricula(matricula);
            return true;
        } catch (CocheNotFoundException nfe)
          {
              return false;
          }
    }

    @Override
    public Coche insertCoche(Coche coche) throws AppException
    {
        return cochesDao.insertCoche(coche);
    }
}
