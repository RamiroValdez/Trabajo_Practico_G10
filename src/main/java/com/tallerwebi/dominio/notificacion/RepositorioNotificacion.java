package com.tallerwebi.dominio.notificacion;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

public interface RepositorioNotificacion {

    public void guardar(Notificacion notificacion) throws ExcepcionBaseDeDatos;
}
