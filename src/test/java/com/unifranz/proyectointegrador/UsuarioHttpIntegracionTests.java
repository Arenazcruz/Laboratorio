package com.unifranz.proyectointegrador;

import com.unifranz.proyectointegrador.infrastructure.persistence.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.datasource.url=jdbc:h2:mem:crud-http-tests;DB_CLOSE_DELAY=-1",
                "spring.jpa.hibernate.ddl-auto=create-drop"})
class UsuarioHttpIntegracionTests {
    @Value("${local.server.port}")
    private int puerto;
    @Autowired
    private ObjectMapper json;
    @Autowired
    private UsuarioRepository repositorio;

    private final HttpClient cliente = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    @Test
    void flujoHttpCompletoEditaDesactivaYBorraElMismoUsuario() throws Exception {
        HttpResponse<String> creado = enviar("POST", "/usuarios",
                """
                {"nombre":"Ana","email":"ana@example.com"}
                """);
        assertEquals(200, creado.statusCode());
        long id = json.readTree(creado.body()).get("id").asLong();
        String ruta = "/usuarios/" + id;

        HttpResponse<String> editado = enviar("PUT", ruta,
                """
                {"id":9223372036854775807,"nombre":"Ana Maria","email":"editada@example.com"}
                """);
        assertEquals(200, editado.statusCode());
        JsonNode datos = json.readTree(editado.body());
        assertEquals(id, datos.get("id").asLong());
        assertEquals("Ana Maria", datos.get("nombre").asText());
        assertEquals("editada@example.com", datos.get("email").asText());
        assertTrue(apareceEnListado(id));

        HttpResponse<String> baja = enviar("DELETE", ruta + "/logico", null);
        assertEquals(204, baja.statusCode());
        assertTrue(baja.body().isEmpty());
        assertFalse(apareceEnListado(id));
        assertFalse(repositorio.findById(id).orElseThrow().isActivo());
        assertEquals(204, enviar("DELETE", ruta + "/logico", null).statusCode());

        assertEquals(404, enviar("PUT", ruta,
                """
                {"nombre":"No debe cambiar","email":"otro@example.com"}
                """).statusCode());
        assertEquals("Ana Maria", repositorio.findById(id).orElseThrow().getNombre());

        HttpResponse<String> borrado = enviar("DELETE", ruta + "/fisico", null);
        assertEquals(204, borrado.statusCode());
        assertTrue(borrado.body().isEmpty());
        assertFalse(repositorio.existsById(id));
        assertEquals(404, enviar("DELETE", ruta + "/fisico", null).statusCode());
        assertEquals(404, enviar("DELETE", ruta + "/logico", null).statusCode());
    }

    @Test
    void contratoHttpDistingueUsuarioInexistenteYCuerpoFaltante() throws Exception {
        assertEquals(404, enviar("PUT", "/usuarios/9223372036854775807",
                """
                {"nombre":"No existe","email":"noexiste@example.com"}
                """).statusCode());
        assertEquals(400, enviar("PUT", "/usuarios/9223372036854775807", null).statusCode());
        assertEquals(400, enviar("DELETE", "/usuarios/abc/fisico", null).statusCode());
    }

    private boolean apareceEnListado(long id) throws Exception {
        HttpResponse<String> respuesta = enviar("GET", "/usuarios", null);
        assertEquals(200, respuesta.statusCode());
        return StreamSupport.stream(json.readTree(respuesta.body()).spliterator(), false)
                .anyMatch(usuario -> usuario.get("id").asLong() == id);
    }

    private HttpResponse<String> enviar(String metodo, String ruta, String cuerpo) throws Exception {
        HttpRequest.Builder solicitud = HttpRequest.newBuilder(URI.create("http://localhost:" + puerto + ruta))
                .timeout(Duration.ofSeconds(10));
        if (cuerpo != null) {
            solicitud.header("Content-Type", "application/json");
        }
        solicitud.method(metodo, cuerpo == null ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(cuerpo));
        return cliente.send(solicitud.build(), HttpResponse.BodyHandlers.ofString());
    }
}
