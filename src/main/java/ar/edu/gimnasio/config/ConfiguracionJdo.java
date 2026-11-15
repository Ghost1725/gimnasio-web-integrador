package ar.edu.gimnasio.config;

import java.util.Properties;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionJdo {

    @Bean(destroyMethod = "close")
    public PersistenceManagerFactory persistenceManagerFactory(
            @Value("${gimnasio.db.url}") String url,
            @Value("${gimnasio.db.user}") String usuario,
            @Value("${gimnasio.db.password}") String clave) {

        Properties propiedades = new Properties();
        propiedades.setProperty("javax.jdo.PersistenceManagerFactoryClass",
                "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
        propiedades.setProperty("javax.jdo.option.ConnectionDriverName", "com.mysql.cj.jdbc.Driver");
        propiedades.setProperty("javax.jdo.option.ConnectionURL", url);
        propiedades.setProperty("javax.jdo.option.ConnectionUserName", usuario);
        propiedades.setProperty("javax.jdo.option.ConnectionPassword", clave);
        propiedades.setProperty("javax.jdo.option.Optimistic", "false");
        propiedades.setProperty("datanucleus.schema.autoCreateAll", "true");
        propiedades.setProperty("datanucleus.cache.level2.type", "none");

        return JDOHelper.getPersistenceManagerFactory(propiedades);
    }
}
