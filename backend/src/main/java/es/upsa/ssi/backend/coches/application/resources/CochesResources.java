package es.upsa.ssi.backend.coches.application.resources;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.entities.CochePojo;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.usecases.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/coches")
@RequestScoped
public class CochesResources
{
    //Inyectamos cada caso de uso
    @Inject
    FindCoches findCochesUseCase;

    @Inject
    FindCocheByMatricula findCocheByMatriculaUseCase;

    @Inject
    InsertCoche insertCocheUseCase;

    @Inject
    UpdateCoche updateCocheUseCase;

    @Inject
    DeleteCocheByMatricula deleteCocheByMatriculaUsecase;

    @Context
    UriInfo uriInfo;

    @Operation(operationId = "listCoches",
            summary = "Acceso a los datos de todos los coches registrados",
            description = "Devuelve los datos de todos los coches registrados en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se devuelve los datos de los coches",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY,
                                    implementation = Coche.class
                            )
                    )
            ),
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCoches() throws AppException
    {
        return Response.ok()
                       .entity( findCochesUseCase.execute() )
                       .build();
    }



    @Operation(operationId = "listCocheByMatricula",
            summary = "Acceso a los datos de un coche identificado por su matricula",
            description = "Devuelve los datos del coche identificado a través de su matricula"
    )
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Se ha localizado el coche y se devuelven sus datos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Coche.class
                            )
                    )
            ),
            @APIResponse(responseCode = "404",
                    description = "No existe un coche con la matricula proporcionada"
            )
    })
    @GET
    @Path("{matricula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCocheByMatricula(@PathParam("matricula") String matricula) throws AppException
    {
        //Usamos la funcion execute de cada caso de uso segun cual necesitemos
        return findCocheByMatriculaUseCase.execute(matricula)
                                      .map( coche -> Response.ok()
                                                                .entity(coche)
                                                                .build()
                                          )
                                      .orElse( Response.status( Response.Status.NOT_FOUND )
                                                       .build()
                                             );
    }


    @Operation(operationId = "insertCoche",
            description = "Crea un coche",
            summary = "Registra un nuevo coche en el sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Se ha creado el coche. Se devuelve sus datos entre los que se incluye su matricula",
                    headers = @Header(name = HttpHeaders.LOCATION,
                            description = "URI con la que acceder al coche creado",
                            schema = @Schema(type = SchemaType.STRING,
                                    format = "uri"
                            )
                    ),
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Coche.class
                            )
                    )
            )
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //Inserta un cochePojo, que es un coche sin matricula
    public Response insertCoche(CochePojo data) throws AppException
    {
        Coche coche = Coche.builder()
                                    .withMatricula( null )
                                    .withMarca(data.getMarca() )
                                    .withModelo(data.getModelo() )
                                    .withAnno(data.getAnno() )
                                    .build();
        Coche newCoche = insertCocheUseCase.execute(coche);
        return Response.created( uriInfo.getBaseUriBuilder()
                                        .path("{matricula}")
                                        .resolveTemplate("matricula", newCoche.getMatricula())
                                        .build()
                               )
                       .entity(newCoche)
                       .build();
    }

    @Operation(operationId = "updateCocheByMatricula",
            description = "Modifica los datos de un coche",
            summary = "Modifica los datos del coche identificado por su matricula"
    )
    @APIResponses({
            @APIResponse(responseCode = "204",
                    description = "Se han modificado los datos del coche",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.OBJECT,
                                    implementation = Coche.class
                            )
                    )
            ),

            @APIResponse(responseCode = "404",
                    description = "No existe el coche identificado por la matricula indicada"
            )
    })
    @PUT
    @Path("{matricula}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCocheByMatricula(@PathParam("matricula") String matricula, CochePojo data) throws AppException
    {
        Coche coche = Coche.builder()
                                    .withMatricula( matricula )
                                    .withMarca(data.getMarca() )
                                    .withModelo(data.getModelo() )
                                    .withAnno(data.getAnno() )
                                    .build();
        return updateCocheUseCase.execute( coche ) ? Response.noContent().build() : Response.status( Response.Status.NOT_FOUND ).build();
    }

    @Operation(operationId = "removeCocheByMatricula",
            description = "Elimina un coche",
            summary = "Elimina el coche identificado por su matricula"
    )
    @APIResponses({
            @APIResponse(responseCode = "204",
                    description = "Se ha eliminado el coche"
            ),

            @APIResponse(responseCode="404",
                    description="No existe el coche identificado por esa matricula"
            )
    })
    @DELETE
    @Path("{matricula}")
    public Response removeCocheByMatricula(@PathParam("matricula") String matricula) throws AppException
    {
        return deleteCocheByMatriculaUsecase.execute(matricula) ? Response.noContent().build() : Response.status( Response.Status.NOT_FOUND ).build();
    }

}
