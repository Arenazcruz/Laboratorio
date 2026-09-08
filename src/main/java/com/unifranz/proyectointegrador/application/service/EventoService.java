package com.unifranz.proyectointegrador.application.service;

import java.util.Map;

public interface EventoService {
    Map<String,Object> validarAcceso (int edad , boolean pago);
}
