package ar.edu.gimnasio.config;

import java.util.function.Function;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.springframework.stereotype.Component;

/** Una sola PMF compartida y un PersistenceManager distinto por operacion. */
@Component
public class UnidadDeTrabajoJdo {
    private final PersistenceManagerFactory factory;

    public UnidadDeTrabajoJdo(PersistenceManagerFactory factory) {
        this.factory = factory;
    }

    public <T> T leer(Function<PersistenceManager, T> accion) {
        // Tambien se abre transaccion de lectura para no depender de
        // la configuracion NontransactionalRead del proveedor JDO.
        return ejecutarEnTransaccion(accion);
    }

    public <T> T escribir(Function<PersistenceManager, T> accion) {
        return ejecutarEnTransaccion(accion);
    }

    private <T> T ejecutarEnTransaccion(Function<PersistenceManager, T> accion) {
        PersistenceManager pm = factory.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            T resultado = accion.apply(pm);
            tx.commit();
            return resultado;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
}
