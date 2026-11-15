package ar.edu.gimnasio.repositorio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/** Consulta tipada interna. Se usa dentro de una sesion JDO abierta. */
final class ConsultasJdo {
    private ConsultasJdo() { }

    static <T> List<T> todos(PersistenceManager pm, Class<T> tipo) {
        Query consulta = pm.newQuery(tipo);
        try {
            Collection<?> filas = (Collection<?>) consulta.execute();
            List<T> resultado = new ArrayList<>();
            for (Object fila : filas) {
                resultado.add(tipo.cast(fila));
            }
            return resultado;
        } finally {
            consulta.closeAll();
        }
    }
}
