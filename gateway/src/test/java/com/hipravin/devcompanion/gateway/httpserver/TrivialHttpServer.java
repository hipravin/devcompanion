package com.hipravin.devcompanion.gateway.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrivialHttpServer {
    private static final Logger log = LoggerFactory.getLogger(TrivialHttpServer.class);

    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
    private HttpServer server;

    private final String hostname;
    private final int port;

    public TrivialHttpServer(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext("/", new TrivialHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        log.info("Trivial http server started listening on port {}", port);
    }

    public void stop() {
        if(server != null) {
            server.stop(5);
        }
    }

    static class TrivialHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            requestAsResponse(httpExchange);
        }

        private void requestAsResponse(HttpExchange exchange) throws IOException {
            try (OutputStream outputStream = exchange.getResponseBody()) {
                String response = exchange.getRequestMethod() + " " + exchange.getRequestURI().toString();

                exchange.sendResponseHeaders(200, response.length());
                outputStream.write(response.getBytes());
                outputStream.flush();
            }
        }
    }
}
