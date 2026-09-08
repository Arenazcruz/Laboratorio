package com.unifranz.proyectointegrador;

import com.unifranz.proyectointegrador.application.dto.UsuarioDto;
import com.unifranz.proyectointegrador.application.service.UsuarioService;
import com.unifranz.proyectointegrador.domain.Usuario;
import com.unifranz.proyectointegrador.infrastructure.persistence.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EliminarLogicoServiceTests {
    @Autowired
    private UsuarioService servicio;
    @Autowired
    private UsuarioRepository repositorio;

    @Test
    void eliminarLogicoConservaDatosYOcultaSoloElUsuarioEliminado() {
        UsuarioDto eliminado = servicio.guardar(new UsuarioDto(null, "Ana", "ana@example.com"));
        UsuarioDto visible = servicio.guardar(new UsuarioDto(null, "Luis", "luis@example.com"));
        assertTrue(repositorio.findById(eliminado.getId()).orElseThrow().isActivo());

        servicio.eliminarLogico(eliminado.getId());
        repositorio.flush();

        Usuario conservado = repositorio.findById(eliminado.getId()).orElseThrow();
        assertFalse(conservado.isActivo());
        assertEquals("Ana", conservado.getNombre());
        assertEquals("ana@example.com", conservado.getEmail());
        assertEquals(2, repositorio.count());
        assertEquals(java.util.List.of(visible.getId()), servicio.listar().stream().map(UsuarioDto::getId).toList());
    }

    @Test
    void repetirEliminacionLogicaEsIdempotente() {
        UsuarioDto usuario = servicio.guardar(new UsuarioDto(null, "Ana", "ana@example.com"));

        servicio.eliminarLogico(usuario.getId());
        servicio.eliminarLogico(usuario.getId());

        assertEquals(1, repositorio.count());
        assertFalse(repositorio.findById(usuario.getId()).orElseThrow().isActivo());
        assertTrue(servicio.listar().isEmpty());
    }

    @Test
    void eliminarLogicoInexistenteDevuelve404() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.eliminarLogico(Long.MAX_VALUE));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertEquals(0, repositorio.count());
    }
}
