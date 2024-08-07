package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.notificacion.Notificacion;

import java.util.List;

public interface RepositorioMetaVencida {


    void guardarMetaVencida(Meta meta, Double totalGastado) throws ExcepcionBaseDeDatos;

    List<MetaVencida> obtenerMetasVencidas(Long idUsuario);

    public List<Notificacion> obtenerNotificacionMetasVencidas(Long idUsuario) throws ExcepcionBaseDeDatos;

    MetaVencida obtenerMetaVencida();
}
