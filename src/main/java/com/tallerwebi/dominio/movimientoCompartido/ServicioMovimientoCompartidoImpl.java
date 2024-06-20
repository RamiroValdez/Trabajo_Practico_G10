package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioMovimientoCompartido")
@Transactional
public class ServicioMovimientoCompartidoImpl implements ServicioMovimientoCompartido {
    private RepositorioMovimientoCompartido repositorioMovimientoCompartido;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioMeta repositorioMeta;

    @Autowired
    public ServicioMovimientoCompartidoImpl(RepositorioMovimientoCompartido repositorioMovimientoCompartido, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario, RepositorioMeta repositorioMeta) {
        this.repositorioMovimientoCompartido = repositorioMovimientoCompartido;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMeta = repositorioMeta;
    }

    @Transactional
    @Override
    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos { //ID DE USUARIO
        return repositorioMovimientoCompartido.obtenerAmigos(idUsuario);
    }

    @Transactional
    @Override
   public void agregarNuevoAmigo(Long idUsuario, String email) throws ExcepcionBaseDeDatos{
        repositorioMovimientoCompartido.agregarNuevoAmigo(idUsuario, email);
    }

}