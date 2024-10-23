package es.upsa.ssi.common.model.exceptions;

import java.sql.SQLException;

public class AppSQLException extends AppException
{
    public AppSQLException(SQLException cause) {
        super(cause);
    }
}
