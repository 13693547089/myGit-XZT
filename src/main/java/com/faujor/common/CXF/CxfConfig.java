package com.faujor.common.CXF;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.faujor.service.common.SrmWebService;


@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;
    @Autowired
    private SrmWebService srmWebService;
 
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, srmWebService);
        endpoint.publish("/srm");
        return endpoint;
    }
}