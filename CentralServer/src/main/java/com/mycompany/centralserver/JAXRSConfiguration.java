package com.mycompany.centralserver;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
@ApplicationPath("resources")
public class JAXRSConfiguration extends Application {
    
}
