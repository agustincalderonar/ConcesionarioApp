package es.upsa.ssi.backend.coches.application.adapters.repositorydao;

import es.upsa.ssi.backend.coches.infraestructure.cdi.qualifiers.JeeDataSource;
import es.upsa.ssi.common.model.ports.repositories.CacheDao;
import es.upsa.ssi.common.model.entities.Coche;
import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.exceptions.AppSQLException;
import es.upsa.ssi.common.model.exceptions.CocheNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.cache.annotation.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Implementa CacheDao, para poder usar JCache
//En el docker compose incluimos las caracteristicas de la cache
@ApplicationScoped
public class JeeCochesDao implements CacheDao
{
    //Usa el qualifier creado por mi JeeDataSource para tener acceso a la base de datos
    @Inject
    @JeeDataSource
    DataSource dataSource;

    //con esto me ahorro el poner esta secuencia cada vez que obtengo coches
    //el interface CheckedFunction esta definido al final, le pasamos el resulSet y nos devuelve un Coche
    CheckedFunction<ResultSet, Coche> cocheMapper = resultSet ->  Coche.builder()
                                                                        .withMatricula(resultSet.getString(1))
                                                                        .withMarca(resultSet.getString(2))
                                                                        .withModelo(resultSet.getString(3))
                                                                        .withAnno(resultSet.getInt(4)).build();


    @Override
    public List<Coche> findCoches() throws AppException
    {
        final String SQL = "SELECT C.MATRICULA, C.MARCA, C.MODELO, C.ANNO " +
                            "  FROM COCHES C ";

        List<Coche> coches = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery( SQL );
            )
        {
            while ( resultSet.next() )
            {
                //uso la expresion cocheMapper definida arriba
                coches.add( cocheMapper.apply( resultSet ) );
            }

        } catch (SQLException sqlException)
          {
              throw new AppSQLException(sqlException);
          }
        return coches;
    }

    @Override
    //Usando CacheResult cachearemos el resultado
    @CacheResult
    public Coche findCocheByMatricula(String matricula) throws AppException
    {
        final String SQL = "SELECT C.MATRICULA, C.MARCA, C.MODELO, C.ANNO " +
                            "  FROM COCHES C WHERE C.MATRICULA = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            )
        {
            preparedStatement.setString(1, matricula);
            try (ResultSet resultSet = preparedStatement.executeQuery()  )
            {
                if ( ! resultSet.next() ) throw new CocheNotFoundException();
                return cocheMapper.apply( resultSet ) ;
            }
        } catch (SQLException sqlException)
          {
            throw new AppSQLException(sqlException);
          }
    }

    //Con CachePut indicamos con @CacheKey y @CacheValue que estamos modificando y lo altera en la cache tambien
    @CachePut
    @Override
    public void updateCoche(@CacheKey String matricula, @CacheValue Coche coche) throws AppException
    {
        final String SQL = "UPDATE COCHES " +
                            "SET MARCA = ?, MODELO = ?, ANNO = ? " +
                            "WHERE MATRICULA = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            )
        {
            preparedStatement.setString(1, coche.getMarca());
            preparedStatement.setString(2, coche.getModelo());
            preparedStatement.setInt(3, coche.getAnno());
            preparedStatement.setString(4, matricula);
            int count = preparedStatement.executeUpdate();
            if (count == 0) throw new CocheNotFoundException();
        } catch (SQLException sqlException)
          {
              throw new AppSQLException(sqlException);
          }
    }

    @Override
    //CacheRemove hace que se elimine tambien en la cache el coche, gracias a @CacheKey
    @CacheRemove
    public void deleteCocheByMatricula(@CacheKey String matricula) throws AppException
    {
        final String SQL = "DELETE FROM COCHES " +
                            "WHERE MATRICULA = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            )
        {
            preparedStatement.setString(1, matricula);
            int count = preparedStatement.executeUpdate();
            if (count == 0) throw new CocheNotFoundException();
        } catch (SQLException sqlException)
          {
            throw new AppSQLException(sqlException);
          }
    }

    @Override
    public Coche insertCoche(Coche coche) throws AppException
    {
        final String SQL = "INSERT INTO COCHES (MATRICULA, MARCA, MODELO, ANNO) " +
                            "VALUES (NEXTVAL('SEQ_COCHES'), ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();            //LE AÑADO ESTO AL PS PARA OBTENER LA MATRICULA QUE LE ASIGNA
             PreparedStatement preparedStatement = connection.prepareStatement(SQL, new String[]{"matricula"});
            )
        {
            preparedStatement.setString(1, coche.getMarca());
            preparedStatement.setString(2, coche.getModelo());
            preparedStatement.setInt(3, coche.getAnno());
            int count = preparedStatement.executeUpdate();
            //return coche;
            //Lo hago asi porque la matricula la genera sql en vez de UUID en CochesResources, debo añadir al preparedStatement lo de arriba
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                return Coche.builder()
                        .withMatricula(rs.getString(1))
                        .withMarca(coche.getMarca())
                        .withModelo(coche.getModelo())
                        .withAnno(coche.getAnno())
                        .build();
            }
        } catch (SQLException sqlException)
          {
              throw new AppSQLException(sqlException);
          }
    }

    //Le pasamos T y devuelve R
    public static interface CheckedFunction<T, R>
    {
        //Esta es la función a la que llamaremos
        public R apply(T parameter) throws SQLException;
    }

}
