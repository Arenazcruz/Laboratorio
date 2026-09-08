package com.unifranz.proyectointegrador.application.service;

import com.unifranz.proyectointegrador.application.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {
    void eliminarFisico(Long id);
    UsuarioDto guardar (UsuarioDto usuarioDto);
    List<UsuarioDto> listar();
    void eliminarLogico(Long id);
}
