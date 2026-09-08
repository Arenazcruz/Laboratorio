package com.unifranz.proyectointegrador.infrastructure.web.controller;


import com.unifranz.proyectointegrador.application.dto.UsuarioDto;
import com.unifranz.proyectointegrador.application.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDto> guardarUsuario (@RequestBody UsuarioDto usuarioDto){
        UsuarioDto usuario = usuarioService.guardar(usuarioDto);
        return ResponseEntity.ok(usuario);
    }
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarUsuarios(){
        return ResponseEntity.ok(usuarioService.listar());
    }

}
