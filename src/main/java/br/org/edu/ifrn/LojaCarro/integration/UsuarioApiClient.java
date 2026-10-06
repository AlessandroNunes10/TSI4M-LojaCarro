package br.org.edu.ifrn.LojaCarro.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UsuarioApiClient {
    private final RestClient restClient;

    public UsuarioApiClient(@Value("${usuario.api.url:http://localhost:8081}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public String buscarNome(Long usuarioId) {
        if (usuarioId == null) return "USUARIO_NAO_INFORMADO";
        try {
            UsuarioResponse usuario = restClient.get().uri("/usuario/{id}", usuarioId)
                    .retrieve().body(UsuarioResponse.class);
            return usuario != null && usuario.nome() != null ? usuario.nome() : "USUARIO_NAO_ENCONTRADO";
        } catch (Exception ex) {
            return "USUARIO_API_INDISPONIVEL";
        }
    }

    private record UsuarioResponse(Long id, String nome, String email, String senha) {}
}