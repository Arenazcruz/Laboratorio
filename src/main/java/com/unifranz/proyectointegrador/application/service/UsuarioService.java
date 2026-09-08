package com.unifranz.proyectointegrador.application.service;

import com.unifranz.proyectointegrador.application.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {
    UsuarioDto guardar (UsuarioDto usuarioDto);
    List<UsuarioDto> listar();
}
