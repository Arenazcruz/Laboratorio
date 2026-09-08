package com.unifranz.proyectointegrador;

import com.unifranz.proyectointegrador.application.dto.UsuarioDto;
import com.unifranz.proyectointegrador.application.service.UsuarioService;
import com.unifranz.proyectointegrador.infrastructure.persistence.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EliminarUsuarioIntegracionTests {
    @Autowired
    private UsuarioService servicio;
    @Autowired
    private UsuarioRepository repositorio;

    @Test
    void sePuedeBorrarFisicamenteUnUsuarioDadoDeBajaLogica() {
        UsuarioDto usuario = servicio.guardar(new UsuarioDto(null, "Ana", "ana@example.com"));
        servicio.eliminarLogico(usuario.getId());
        assertTrue(repositorio.existsById(usuario.getId()));

        servicio.eliminarFisico(usuario.getId());
        repositorio.flush();

        assertFalse(repositorio.existsById(usuario.getId()));
        assertTrue(servicio.listar().isEmpty());
    }
}
