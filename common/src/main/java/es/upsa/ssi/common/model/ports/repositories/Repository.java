package es.upsa.ssi.common.model.ports.repositories;


import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;

import java.util.List;
import java.util.Optional;

public interface Repository
{
    public List<Coche> findCoches() throws AppException;
    public Optional<Coche> findCocheByMatricula(String matricula) throws AppException;
    public boolean updateCoche(Coche coche) throws AppException;
    public boolean deleteCocheByMatricula(String matricula) throws AppException;
    public Coche insertCoche(Coche coche) throws AppException;
}
