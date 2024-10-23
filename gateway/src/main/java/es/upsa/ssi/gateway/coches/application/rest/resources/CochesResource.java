package es.upsa.ssi.gateway.coches.application.rest.resources;

import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.entities.CochePojo;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.ports.usecases.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@RequestScoped
@Tag(name = "coches")
@Path("coches")
public class CochesResource
{
    @Context
    UriInfo uriInfo;

    @Inject
    FindCoches findCochesUseCase;

    @Inject
    FindCocheByMatricula findCocheByMatriculaUseCase;

    @Inject
    InsertCoche insertCocheUseCase;

    @Inject
    UpdateCoche updateCocheUseCase;

    @Inject
    DeleteCocheByMatricula deleteCocheByMatriculaUseCase;


    @Operation(operationId = "findCoches",
               summary = "Obtiene la información de todos los coches",
               description = "Accede al backend para obtener toda la informacion de los coches registrados"
    )
    @APIResponses({
                    @APIResponse(responseCode = "200",
                                 description = "Se devuelve un lista de coches",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                                    schema = @Schema(type = SchemaType.ARRAY,
                                                                      implementation = Coche.class
                                                                    )
                                                   )
                                )
                  })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCoches() throws AppException
    {
        return Response.ok()
                       .entity( findCochesUseCase.execute() )
                       .build();

    }



    @Operation(operationId = "findCocheByMatricula",
               summary = "Accede a los datos de un coche identificado por su matricula",
               description = "Accede al backend para obtener toda la informacion del coche identificado por su matricula"
              )
    @APIResponses({
                    @APIResponse(responseCode = "200",
                                 description = "Se obtiene los datos del coche",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                                    schema = @Schema(type = SchemaType.OBJECT,
                                                                     implementation = Coche.class
                                                                    )
                                                   )
                                ),
                    @APIResponse(responseCode = "404",
                                 description = "No existe el coche identificado por la matricula proporcionada"
                                )
                   })
    @GET
    @Path("{matricula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCocheByMatricula(@Parameter(required = true,
                                                description = "Matricula del coche",
                                                in = ParameterIn.PATH,
                                                name = "matricula",
                                                schema = @Schema(type = SchemaType.STRING)
                                               )
                                    @PathParam("matricula") String matricula) throws AppException
    {
        return findCocheByMatriculaUseCase.execute(matricula)
                               .map( coche -> Response.ok()
                                                         .entity( coche )
                                                         .build()
                                   )
                               .orElse( Response.status( Response.Status.NOT_FOUND )
                                                 .entity( "No existe el coche con matricula " + matricula )
                                                .build()
                                      );

    }



    @Operation(operationId = "insertCoche",
               description = "Añade un coche",
               summary = "Accede al backend para añadir un nuevo coche"
              )
    @APIResponses({
                    @APIResponse(responseCode = "201",
                                 description = "Se ha se ha añadido el coche",
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
    public Response insertCoche(@RequestBody(description = "Datos del coche",
                                                required = true,
                                                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                                                   schema = @Schema(type = SchemaType.OBJECT,
                                                                                    implementation = CochePojo.class
                                                                                   )
                                                                  )
                                               )
                                    CochePojo cochePojo) throws AppException
    {
        Coche coche = insertCocheUseCase.execute(Coche.builder()
                                                        .withModelo(cochePojo.getModelo())
                                                        .withMarca(cochePojo.getMarca())
                                                        .withAnno(cochePojo.getAnno())
                                                        .build()
                                                         );

        URI productoURI = uriInfo.getBaseUriBuilder()
                                 .path("coches/{matricula}")
                                 .resolveTemplate("matricula", coche.getMatricula())
                                 .build();


        return Response.created( productoURI )
                       .entity( coche )
                       .build();
    }


    @Operation(operationId = "updateCoche",
               description = "Modifica los datos de un coche",
               summary = "Accede al backend para modificar los datos de un coche identificado por su matricula"
              )
    @APIResponses({
                    @APIResponse(responseCode = "204",
                                 description = "Se ha modificado los datos del coche"
                                ),
                    @APIResponse(responseCode = "404",
                                 description = "No existe el coche"
                                )
                  })
    @PUT
    @Path("{matricula}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCoche(@Parameter(required = true,
                                              description = "Matricula del coche",
                                              in = ParameterIn.PATH,
                                              name = "matricula",
                                              schema = @Schema(type = SchemaType.STRING)
                                             )
                                   @PathParam("matricula")String matricula,
                                   @RequestBody(description = "Datos del coche",
                                                required = true,
                                                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                                                   schema = @Schema(type = SchemaType.OBJECT,
                                                                                    implementation = CochePojo.class
                                                                                   )
                                                                  )
                                               )
                                   CochePojo cochePojo) throws AppException
    {
        Coche coche = Coche.builder()
                            .withMatricula(matricula)
                            .withModelo(cochePojo.getModelo())
                            .withMarca(cochePojo.getMarca())
                            .withAnno(cochePojo.getAnno())
                            .build();

        return updateCocheUseCase.execute(coche) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }


    @Operation(operationId = "deleteCocheByMatricula",
               description = "Elimina un coche",
               summary = "Accede al backend para eliminar un coche identificado por su matricula"
              )
    @APIResponses({
                    @APIResponse(responseCode = "204",
                                 description = "Se ha eliminado el coche"
                                ),
                    @APIResponse(responseCode="404",
                                 description="No existe el coche identificado por la matricula proporcionado"
                                )
                  })
    @DELETE
    @Path("{matricula}")
    public Response deleteCocheByMatricula(@Parameter(required = true,
                                                  description = "Matricula del coche",
                                                  in = ParameterIn.PATH,
                                                  name = "matricula",
                                                  schema = @Schema(type = SchemaType.STRING)
                                                 )
                                       @PathParam("matricula") String matricula) throws AppException
    {
        return deleteCocheByMatriculaUseCase.execute(matricula) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
