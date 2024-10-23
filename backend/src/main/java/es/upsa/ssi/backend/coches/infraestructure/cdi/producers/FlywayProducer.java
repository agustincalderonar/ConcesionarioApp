package es.upsa.ssi.backend.coches.infraestructure.cdi.producers;

import es.upsa.ssi.backend.coches.infraestructure.mpconfig.JeeFlywayProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

//Configura el flyway sirviendose de las funciones de Utils
@ApplicationScoped
public class FlywayProducer
{
    //Inyecta JeeFlywayProperties, del que obtiene su informacion
    @Inject
    @ConfigProperties
    JeeFlywayProperties flywayProperties;

    @Inject
    Config config;

    //Es el productor de objetos Flyway
    //Usa las funciones creadas en Utils
    @Produces
    @Singleton
    public Flyway produceFlyway()
    {
        String dataSourceName = flywayProperties.getDataSourceName();
        DataSource dataSource = Utils.createDataSource(config, dataSourceName);

        Logger logger = Logger.getLogger( FlywayProducer.class.getSimpleName() );

        logger.log( Level.INFO, flywayProperties.toString() );


        return Flyway.configure()
                     .dataSource( dataSource )
                     .cleanDisabled( !flywayProperties.isCleanAtStart() )
                     .locations( flywayProperties.getLocations() )
                     .load();
    }
}
