package es.upsa.ssi.gateway.coches.application.adapters.repositories;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.repositories.CochesDao;
import es.upsa.ssi.common.model.ports.repositories.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class QuarkusRepository implements Repository
{
    @Inject
    CochesDao productosDao;

    @Override
    public List<Coche> findCoches() throws AppException
    {
        return productosDao.findCoches();
    }

    @Override
    public Optional<Coche> findCocheByMatricula(String matricula) throws AppException
    {
        return productosDao.findCocheByMatricula(matricula);
    }

    @Override
    public boolean updateCoche(Coche coche) throws AppException
    {
        return productosDao.updateCoche(coche);
    }

    @Override
    public boolean deleteCocheByMatricula(String matricula) throws AppException
    {
        return productosDao.deleteCocheByMatricula(matricula);
    }

    @Override
    public Coche insertCoche(Coche coche) throws AppException
    {
        return productosDao.insertCoche(coche);
    }
}
