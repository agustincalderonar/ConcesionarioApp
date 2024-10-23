package es.upsa.ssi.gateway.coches.application.adapters.repositories;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.entities.CochePojo;
import es.upsa.ssi.common.model.exceptions.AppException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

//Accede al backend
//Accedemos a la url des configKey(definido en application.properties) con el path definido en path
@RegisterRestClient(configKey = "coches.remote.api")
@RegisterProvider(CochesResponseExceptionMapper.class)
//Este path apunta al backend MUY IMPORTANTE
@Path("/coches")
public interface CochesRemoteApi
{
    //estos metodos acceden al backend, las peticiones y tipos de objeto se corresponden a las de CochesResources del backend
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Coche> queryCoches();

    @GET
    @Path("{matricula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Coche queryCocheByMatricula(@PathParam("matricula") String matricula) throws AppException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Coche requestInsertCoche(CochePojo cochePojo);

    @PUT
    @Path("{matricula}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void requestUpdateCocheByMatricula(@PathParam("matricula") String matricula, CochePojo cochePojo) throws AppException;

    @DELETE
    @Path("{matricula}")
    public void requestRemoveCocheByMatricula(@PathParam("matricula") String matricula) throws AppException;

}
