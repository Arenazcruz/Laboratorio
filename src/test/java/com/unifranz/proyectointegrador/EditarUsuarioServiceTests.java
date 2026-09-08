package com.unifranz.proyectointegrador;

import com.unifranz.proyectointegrador.application.dto.UsuarioDto;
import com.unifranz.proyectointegrador.application.service.UsuarioService;
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
class EditarUsuarioServiceTests {
    @Autowired
    private UsuarioService servicio;
    @Autowired
    private UsuarioRepository repositorio;

    @Test
    void editarConservaIdYActualizaSoloElUsuarioSolicitado() {
        UsuarioDto original = servicio.guardar(new UsuarioDto(null, "Ana", "ana@example.com"));
        UsuarioDto otro = servicio.guardar(new UsuarioDto(null, "Luis", "luis@example.com"));

        UsuarioDto editado = servicio.editar(original.getId(),
                new UsuarioDto(otro.getId(), "Ana Maria", "ana.maria@example.com"));
        repositorio.flush();

        assertEquals(original.getId(), editado.getId());
        assertEquals("Ana Maria", editado.getNombre());
        assertEquals("ana.maria@example.com", repositorio.findById(original.getId()).orElseThrow().getEmail());
        assertEquals("Luis", repositorio.findById(otro.getId()).orElseThrow().getNombre());
        assertEquals(2, repositorio.count());
    }

    @Test
    void editarInexistenteDevuelve404SinCrearUsuario() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.editar(Long.MAX_VALUE, new UsuarioDto(null, "Ana", "ana@example.com")));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertEquals(0, repositorio.count());
    }
}
