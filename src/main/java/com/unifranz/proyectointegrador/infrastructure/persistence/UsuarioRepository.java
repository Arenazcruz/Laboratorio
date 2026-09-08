package com.unifranz.proyectointegrador.infrastructure.persistence;

import com.unifranz.proyectointegrador.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    List<Usuario> findAllByActivoTrue();
}
