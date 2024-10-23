package es.upsa.ssi.common.model.ports.usecases;


import es.upsa.ssi.common.model.exceptions.AppException;

//Los casos de uso vendrian a ser como los antiguos service, la primera capa, pero ahora dividida
public interface DeleteCocheByMatricula
{
    //A todos los metodos de los casos de uso los llamamos execute(podriamos llamarlo DeleteProducto por ejemplo)
    boolean execute(String matricula) throws AppException;
}
