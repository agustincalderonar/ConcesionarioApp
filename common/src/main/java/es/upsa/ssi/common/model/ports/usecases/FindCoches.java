package es.upsa.ssi.common.model.ports.usecases;


import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;

import java.util.List;

public interface FindCoches
{
    List<Coche> execute() throws AppException;
}
