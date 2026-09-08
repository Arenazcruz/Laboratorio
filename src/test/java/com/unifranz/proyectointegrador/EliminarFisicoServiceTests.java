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
class EliminarFisicoServiceTests {
    @Autowired
    private UsuarioService servicio;
    @Autowired
    private UsuarioRepository repositorio;

    @Test
    void eliminarFisicoBorraSoloElRegistroSolicitado() {
        UsuarioDto eliminado = servicio.guardar(new UsuarioDto(null, "Ana", "ana@example.com"));
        UsuarioDto conservado = servicio.guardar(new UsuarioDto(null, "Luis", "luis@example.com"));

        servicio.eliminarFisico(eliminado.getId());
        repositorio.flush();

        assertFalse(repositorio.existsById(eliminado.getId()));
        assertTrue(repositorio.existsById(conservado.getId()));
        assertEquals(1, repositorio.count());
    }

    @Test
    void eliminarFisicoInexistenteDevuelve404() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.eliminarFisico(Long.MAX_VALUE));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }
}
