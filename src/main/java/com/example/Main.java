package com.example;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

public class Main {

    public static void main(String[] args) {
        try {
            List<String> block = Flux
                    .range(0, 3)
                    .flatMap(value -> testWebClient())
                    .doOnNext(System.out::println)
                    .collectList()
                    .map(list -> list.stream().flatMap(Collection::stream)
                            .collect(Collectors.toList()))
                    .block();
            if (block.size() != 3_000) {
                throw new RuntimeException("fail");
            }
        } catch (Throwable t) {
            System.out.println(t);
        }
    }

    private static Mono<List<String>> testWebClient() {
        WebClient webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient
                                .create(ConnectionProvider.fixed(
                                        "http-ims-core", 20
                                ))
                                .tcpConfiguration(tcpClient -> tcpClient.runOn(
                                        LoopResources.create("webflux-http-ims-core")
                                ))
                                .compress(true)
                ))
                .build();

        return Flux
                .range(0, 1_000)
                .flatMap(value -> webClient
                        .get()
                        .uri("https://www.google.com")
                        .retrieve()
                        .bodyToMono(String.class)
                )
                .collectList();
    }
}
