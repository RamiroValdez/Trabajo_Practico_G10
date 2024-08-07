package com.tallerwebi.dominio.movimientoCompartido;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.tallerwebi.dominio.usuario.Usuario;

import java.util.List;

public interface ServicioMovimientoCompartido {

    public List<Usuario> obtenerAmigos(Long idUsuario) throws ExcepcionBaseDeDatos;

    void agregarNuevoAmigo(Long idUsuario, String email) throws Excepcion, ExcepcionBaseDeDatos, ExcepcionAmigoYaExistente, ExcepcionSolicitudEnviada, UsuarioInexistente, ExcepcionAutoAmistad, ExcepcionUsuarioNoPremium;

    public List<Notificacion> obtenerSolicitudesEnviadas(Long idUsuario) throws ExcepcionBaseDeDatos;

    void eliminarSolicitud(Long id) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente;

    List<Notificacion> obtenerSolicitudesRecibidas(Long idUsuario) throws ExcepcionBaseDeDatos;

    void aceptarSolicitud(Long id) throws ExcepcionBaseDeDatos, ExcepcionNotificacionInexistente;

    void eliminarAmigo(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;

    List<Movimiento> obtenerMovimientosCompartidos(Long idAmigo, Long idUsuario) throws ExcepcionBaseDeDatos;

    List<Notificacion> obtenerSolicitudesAceptadas(Long idUsuario) throws ExcepcionBaseDeDatos, UsuarioInexistente;
}
