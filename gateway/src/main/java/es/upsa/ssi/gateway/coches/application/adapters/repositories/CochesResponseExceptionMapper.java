package es.upsa.ssi.gateway.coches.application.adapters.repositories;

import es.upsa.ssi.common.model.exceptions.AppException;
import es.upsa.ssi.common.model.exceptions.CocheNotFoundException;
import es.upsa.ssi.gateway.coches.application.adapters.exceptions.RemoteCochesApiException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class CochesResponseExceptionMapper implements ResponseExceptionMapper<AppException>
{
    @Override
    public AppException toThrowable(Response response)
    {
        return switch ( Response.Status.fromStatusCode(response.getStatus() ) )
               {
                  case NOT_FOUND -> new CocheNotFoundException();
                  default        -> new RemoteCochesApiException();
               };

    }
}
