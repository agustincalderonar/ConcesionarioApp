package es.upsa.ssi.backend.coches.infraestructure.mpconfig;

import es.upsa.ssi.backend.coches.infraestructure.cdi.qualifiers.JeeDataSource;
import jakarta.enterprise.context.Dependent;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

//Aqui configuramos el flyway, obtenemos las propiedades jee.flyway del microplofile-config.properties
//Estas propiedades se inyectan en el StartupObserver, y sus datos de usan en el FlywayProducer
@ConfigProperties(prefix = "jee.flyway")
@Dependent
@Data
public class JeeFlywayProperties
{
    @ConfigProperty(name = "datasource-name", defaultValue = JeeDataSource.DEFAULT_VALUE)
    private String dataSourceName;
    private String[] locations;

    @ConfigProperty(name = "clean-at-start", defaultValue = "true")
    private boolean cleanAtStart;

    @ConfigProperty(name = "migrate-at-start", defaultValue = "true")
    private boolean migrateAtStart;
}
