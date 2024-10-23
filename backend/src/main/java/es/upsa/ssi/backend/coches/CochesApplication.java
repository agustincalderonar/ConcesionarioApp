package es.upsa.ssi.backend.coches;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(info = @Info(title = "OpenAPI para Coches",
        version = "1.0.0"
),
        tags = {@Tag(name = "backend",
                description = "Gestión de backend"
        )
        }
)
@ApplicationPath("/")
public class CochesApplication extends Application
{
}
