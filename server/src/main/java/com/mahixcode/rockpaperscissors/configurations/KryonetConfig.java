package com.mahixcode.rockpaperscissors.configurations;

import com.esotericsoftware.kryonet.Server;
import com.mahixcode.rockpaperscissors.network.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class KryonetConfig {

    @Bean
    public Server server() throws IOException {
        Server server = new Server();
        server.start();
        server.bind(Network.DEFAULT_PORT);
        Network.register(server.getKryo());
        return server;
    }
}
