package es.upsa.ssi.gateway.coches.application.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@OpenAPIDefinition(
        info = @Info(title = "OpenAPI para coches",
                     version = "1.0.0",
                     description = "Frontend para gestionar los coches",
                      license = @License(name = "Apache 2.0",
                                         url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        tags = {@Tag(name = "coches",
                     description = "Gestión de coches"
                    )
        }
)
@ApplicationPath("/")
public class GatewayApplication extends Application
{
}
