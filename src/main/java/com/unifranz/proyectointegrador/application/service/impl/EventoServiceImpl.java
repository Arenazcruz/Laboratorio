package com.unifranz.proyectointegrador.application.service.impl;

import com.unifranz.proyectointegrador.application.service.EventoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class EventoServiceImpl implements EventoService {
    private int totalConsultas = 0;
    private int totalRechazados = 0;

    @Override
    public Map<String, Object> validarAcceso (int edad , boolean pago){
        boolean puedeEntrar;
        String motivo;

        if (edad < 18){
            puedeEntrar = false;
            motivo = "menor de edad";
        } else if (!pago) {
            puedeEntrar = false;
            motivo= "no pago";
        } else {
            puedeEntrar = true;
            motivo = "permitido";
        }
       totalConsultas = totalConsultas + 1;
        if (!puedeEntrar) {
            totalRechazados = totalRechazados + 1;
        }
        Map<String, Object> salida = new HashMap<>();
        salida.put("puedeEntrar", puedeEntrar);
        salida.put("motivo",motivo);
        salida.put("totalConsultas",totalConsultas);
        salida.put("totalRechazados",totalRechazados);
        return salida;
    }
}
