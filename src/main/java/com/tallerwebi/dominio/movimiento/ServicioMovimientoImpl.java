package com.tallerwebi.dominio.movimiento;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class ServicioMovimientoImpl implements ServicioMovimiento, ServicioAgregarMovimiento {

    private RepositorioMovimiento repositorioMovimiento;

    public ServicioMovimientoImpl(RepositorioMovimiento repositorioMovimiento) {
        this.repositorioMovimiento = repositorioMovimiento;
    }

    @Transactional
    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) {
        List<Movimiento> movimientos = repositorioMovimiento.obtenerMovimientos(idUsuario);
        return movimientos;
    }
    @Transactional
    @Override
    public void agregarMovimiento(Movimiento movimiento) {
        repositorioMovimiento.agregarMovimiento(movimiento);
    }
}
