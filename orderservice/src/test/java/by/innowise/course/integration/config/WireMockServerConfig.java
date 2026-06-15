package by.innowise.course.integration.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.ServiceInstanceListSuppliers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
public class WireMockServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    WireMockServer wireMockServer() {
        return new WireMockServer(options().dynamicPort());
    }

    @Bean
    ServiceInstanceListSupplier serviceInstanceListSupplier(
            WireMockServer wireMockServer
    ) {
        return ServiceInstanceListSuppliers.from(
                "user-service",
                new DefaultServiceInstance(
                        "user-service-1",
                        "user-service",
                        "localhost",
                        wireMockServer.port(),
                        false
                )
        );
    }
}