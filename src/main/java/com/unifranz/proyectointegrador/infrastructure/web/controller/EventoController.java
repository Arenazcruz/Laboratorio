package com.unifranz.proyectointegrador.infrastructure.web.controller;

import com.unifranz.proyectointegrador.application.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/evento")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("/validarAcceso")
    public Map<String, Object> validarAcceso (@RequestBody JsonNode entrada){
     int edad = entrada.get("edad").asInt();
     boolean pago = entrada.get("pago").asBoolean();
     return eventoService.validarAcceso(edad, pago);
    }
}
