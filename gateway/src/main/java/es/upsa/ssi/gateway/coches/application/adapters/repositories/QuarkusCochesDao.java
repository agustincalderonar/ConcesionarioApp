package es.upsa.ssi.gateway.coches.application.adapters.repositories;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.entities.CochePojo;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.exceptions.CocheNotFoundException;
import es.upsa.ssi.common.model.ports.repositories.CochesDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class QuarkusCochesDao implements CochesDao
{

    //Injecta remoteApi, con el que hace las peticiones al backend
    @Inject
    @RestClient
    CochesRemoteApi remoteApi;

    @Override
    public List<Coche> findCoches() throws AppException
    {
        return remoteApi.queryCoches();
    }

    @Override
    public Optional<Coche> findCocheByMatricula(String matricula) throws AppException
    {
        try
        {
            Coche coche = remoteApi.queryCocheByMatricula(matricula);
            return Optional.of(coche);

        } catch (CocheNotFoundException exception)
          {
              return Optional.empty();

          }
    }

    @Override
    public boolean updateCoche(Coche coche) throws AppException
    {
        try
        {
            CochePojo cochePojo = CochePojo.builder()
                                            .withMarca(coche.getMarca())
                                            .withModelo(coche.getModelo())
                                            .withAnno(coche.getAnno())
                                            .build();
            remoteApi.requestUpdateCocheByMatricula(coche.getMatricula(), cochePojo);
            return true;
        } catch (CocheNotFoundException productoNotFoundException)
          {
              return false;
          }
    }

    @Override
    public boolean deleteCocheByMatricula(String matricula) throws AppException
    {
        try
        {
            remoteApi.requestRemoveCocheByMatricula(matricula);
            return true;
        } catch (CocheNotFoundException productoNotFoundException)
          {
            return false;
          }
    }

    @Override
    public Coche insertCoche(Coche coche) throws AppException
    {
        CochePojo cochePojo = CochePojo.builder()
                                        .withMarca(coche.getMarca())
                                        .withModelo(coche.getModelo())
                                        .withAnno(coche.getAnno())
                                        .build();

        return remoteApi.requestInsertCoche( cochePojo );
    }


}
