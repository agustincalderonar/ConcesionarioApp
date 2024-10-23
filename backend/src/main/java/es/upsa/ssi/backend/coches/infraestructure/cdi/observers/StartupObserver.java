package es.upsa.ssi.backend.coches.infraestructure.cdi.observers;

import es.upsa.ssi.backend.coches.infraestructure.mpconfig.JeeFlywayProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.flywaydb.core.Flyway;

//Limpia y/o migra el flyway al iniciar la aplicacion segun la info de JeeFlywayProperties
@ApplicationScoped
public class StartupObserver
{
    @Inject
    @ConfigProperties
    private JeeFlywayProperties flywayProperties;

    @Inject
    private Flyway flyway;


    public void observesStart(@Observes @Initialized(ApplicationScoped.class) Object event )
    {
       if (flywayProperties.isCleanAtStart()   ) flyway.clean();
       if (flywayProperties.isMigrateAtStart() ) flyway.migrate();
    }
}
