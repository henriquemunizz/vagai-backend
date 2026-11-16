package br.com.fiap.vagai.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;
import org.springframework.http.*;
import org.springframework.http.server.ServerHttpResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.net.URI;

@Configuration
public class Configuracao {
    private final String origem;

    public Configuracao(@Value("${vagai.origem}") String origem) {
        URI uri = URI.create(origem);
        if (uri.getHost() == null || !java.util.Set.of("https", "http").contains(uri.getScheme())
                || !uri.getRawPath().isEmpty() || uri.getRawQuery() != null || uri.getRawFragment() != null
                || uri.getUserInfo() != null) throw new IllegalArgumentException("FRONTEND_ORIGIN inválida");
        this.origem = origem;
    }

    @Bean
    CorsFilter cors() {
        CorsConfiguration regras = new CorsConfiguration();
        regras.setAllowedOrigins(List.of(origem));
        regras.setAllowCredentials(true);
        regras.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        regras.setAllowedHeaders(List.of("Content-Type", "Accept"));
        regras.setExposedHeaders(List.of("Location"));
        UrlBasedCorsConfigurationSource rotas = new UrlBasedCorsConfigurationSource();
        rotas.registerCorsConfiguration("/**", regras);
        CorsFilter filtro = new CorsFilter(rotas);
        filtro.setCorsProcessor(new DefaultCorsProcessor() {
            @Override
            protected void rejectRequest(ServerHttpResponse resposta) throws IOException {
                resposta.setStatusCode(HttpStatus.FORBIDDEN);
                resposta.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                resposta.getBody().write("""
                        {"status":403,"mensagem":"Origem ou operação CORS não permitida."}
                        """.getBytes(StandardCharsets.UTF_8));
                resposta.flush();
            }
        });
        return filtro;
    }

    @Bean
    ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
}
