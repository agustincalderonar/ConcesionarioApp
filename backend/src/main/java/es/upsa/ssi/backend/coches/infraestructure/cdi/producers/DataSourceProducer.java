package es.upsa.ssi.backend.coches.infraestructure.cdi.producers;

import es.upsa.ssi.backend.coches.infraestructure.cdi.qualifiers.JeeDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;

import javax.sql.DataSource;

//Configura el DataSource con los datos de microprofile-config.properties
//Usa las funciones de Utils
@ApplicationScoped
public class DataSourceProducer
{
    @Inject
    private Config config;

    @Produces
    @JeeDataSource
    @Dependent
    public DataSource produceJeeDataSource(InjectionPoint injectionPoint)
    {
        JeeDataSource jeeDataSource = Utils.findQualifier(injectionPoint, JeeDataSource.class, JeeDataSource.Literal.DEFAULT);
        String dataSourceName = jeeDataSource.value().trim();
        return Utils.createDataSource(config, dataSourceName);
    }

}
