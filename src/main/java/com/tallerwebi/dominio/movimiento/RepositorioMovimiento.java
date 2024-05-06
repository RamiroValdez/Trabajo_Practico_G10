package com.tallerwebi.dominio.movimiento;

import java.util.List;

public interface RepositorioMovimiento {
    List<Movimiento> obtenerMovimientos(Long idUsuario);

    void agregarMovimiento(Movimiento movimiento);
}
