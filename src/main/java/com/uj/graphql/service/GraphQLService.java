package com.uj.graphql.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GraphQLService {

    private final GraphQlClient graphQlClient;
    public GraphQLService(@Value("${graphql.endpoint}") String graphqlEndpoint) {
        WebClient client = WebClient.builder()
                .codecs(config -> config.defaultCodecs().maxInMemorySize(5062144))
                .baseUrl(graphqlEndpoint)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        graphQlClient = HttpGraphQlClient.builder(client).build();
    }

    public Mono<JsonNode> executeGraphQLQuery(String query, Map<String, Object> variables, String path) {
        return graphQlClient
                .document(query)
                .variables(variables)
                .retrieve(path)
                .toEntity(JsonNode.class);
    }

    public Mono<List<JsonNode>> getAllUsers(String query){
        return graphQlClient
                .document(query)
                .retrieve("getAllUsers")
                .toEntityList(JsonNode.class);
    }
}
