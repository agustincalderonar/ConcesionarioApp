package es.upsa.ssi.common.model.ports.usecases;


import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;

public interface UpdateCoche
{
    boolean execute(Coche coche) throws AppException;
}
