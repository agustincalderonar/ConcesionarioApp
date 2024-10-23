package es.upsa.ssi.backend.coches.infraestructure.mpconfig;

import es.upsa.ssi.backend.coches.infraestructure.cdi.qualifiers.JeeDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

//Aqui configuramos el datasource, obtenemos las propiedades jee.datasource del microplofile-config.properties
//Sus datos de usan en Utils
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@ConfigProperties(prefix = JeeDatasourceProperties.DEFAULT_PREFIX)
public class JeeDatasourceProperties
{
    public static final String DEFAULT_PREFIX = "jee.datasource";
    private static String createConfigPropertiesPrefixFrom(String dataSourceName)
    {
        if ( JeeDataSource.DEFAULT_VALUE.equals(dataSourceName) ) return DEFAULT_PREFIX;
        return new StringBuilder(DEFAULT_PREFIX).append(".").append(dataSourceName).toString();
    }

    public static JeeDatasourceProperties from(Config config, String dataSourceName)
    {
        String prefix = createConfigPropertiesPrefixFrom(dataSourceName);
        return JeeDatasourceProperties.builder()
                                      .withDataSourceClass( config.getValue(getPropertyFullName(prefix, "datasource-class"), Class.class ) )
                                      .withUrl( config.getValue(getPropertyFullName(prefix, "url"), String.class ) )
                                      .withUser( config.getValue(getPropertyFullName(prefix, "user"), String.class ) )
                                      .withPassword( config.getValue(getPropertyFullName(prefix, "password"), String.class ) )
                                      .build();
    }

    private static String getPropertyFullName(String prefix, String propertyName)
    {
        return new StringBuilder(prefix).append(".").append(propertyName).toString();
    }


    @ConfigProperty(name = "datasource-class")
    private Class dataSourceClass;
    private String url;
    private String user;
    private String password;
}
