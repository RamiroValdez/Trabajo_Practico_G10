package com.tallerwebi.dominio.movimiento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionMovimientoNoEncontrado;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.notificacion.RepositorioNotificacion;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.movimiento.DatosAgregarMovimiento;
import com.tallerwebi.presentacion.movimiento.DatosEditarMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tallerwebi.dominio.notificacion.Notificacion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("servicioMovimiento")
@Transactional
public class ServicioMovimientoImpl implements ServicioMovimiento {

    private RepositorioNotificacion repositorioNotificacion;
    private RepositorioMovimiento repositorioMovimiento;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioMeta repositorioMeta;

    @Autowired
    public ServicioMovimientoImpl(RepositorioMovimiento repositorioMovimiento, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario, RepositorioMeta repositorioMeta, RepositorioNotificacion repositorioNotificacion) {
        this.repositorioMovimiento = repositorioMovimiento;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioMeta = repositorioMeta;
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Transactional
    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) throws ExcepcionBaseDeDatos{ //ID DE USUARIO
        return repositorioMovimiento.obtenerMovimientos(idUsuario);
    }

    @Transactional
    @Override
    public Movimiento obtenerMovimientoPorId(Long id) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos { //ID DE MOVIMIENTO
        return repositorioMovimiento.obtenerMovimientoPorId(id);
    }

    @Transactional
    @Override
    public void actualizarMovimiento(DatosEditarMovimiento datosEditarMovimiento) throws ExcepcionCamposInvalidos, ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        datosEditarMovimiento.validarCampos();
        String categoria = datosEditarMovimiento.getCategoria();
        String descripcion = datosEditarMovimiento.getDescripcion();
        Double monto = datosEditarMovimiento.getMonto();
        Long id = datosEditarMovimiento.getId();

        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(categoria);
        if (categoriaMovimiento == null) {
            Map<String, String> errores = new HashMap<>();
            errores.put("categoria", "La categoría no existe");
            throw new ExcepcionCamposInvalidos(errores);
        }

        Movimiento movimiento = repositorioMovimiento.obtenerMovimientoPorId(id);
        if (movimiento == null)
            throw new ExcepcionMovimientoNoEncontrado("El movimiento no existe");

        movimiento.setCategoria(categoriaMovimiento);
        movimiento.setDescripcion(descripcion);
        movimiento.setMonto(monto);
        repositorioMovimiento.actualizarMovimiento(movimiento);
    }

    @Transactional
    @Override
    public void eliminarMovimiento(Long idMovimiento) throws ExcepcionMovimientoNoEncontrado, ExcepcionBaseDeDatos {
        Movimiento movimiento = repositorioMovimiento.obtenerMovimientoPorId(idMovimiento);
        if (movimiento == null)
            throw new ExcepcionMovimientoNoEncontrado("No se encontró ningún movimiento con el ID proporcionado");
        repositorioMovimiento.eliminarMovimiento(movimiento);
    }

    @Transactional
    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(Long idUsuario, LocalDate fecha) throws ExcepcionBaseDeDatos {
        return repositorioMovimiento.obtenerMovimientosPorFecha(idUsuario, fecha);
    }

    @Transactional
    @Override
    public void nuevoMovimiento(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente {
        datosAgregarMovimiento.validarCampos();

        String descripcion = datosAgregarMovimiento.getDescripcion();
        Double monto = datosAgregarMovimiento.getMonto();
        String categoria = datosAgregarMovimiento.getCategoria();
        Long idAmigo = datosAgregarMovimiento.getIdAmigo();
        Double montoAmigo = datosAgregarMovimiento.getMontoAmigo();

        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        Usuario amigo = null;
        if(idAmigo != null){
            amigo = repositorioUsuario.obtenerUsuarioPorId(idAmigo);
        }

        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(categoria);

        // Movimiento para el usuario actual
        Movimiento movimientoUsuario = new Movimiento(
                descripcion,
                monto,
                LocalDate.now(),
                categoriaMovimiento,
                usuario
        );

        if(amigo != null){
            movimientoUsuario.setAmigo(amigo);
            movimientoUsuario.setMontoAmigo(montoAmigo);
        }
        repositorioMovimiento.guardarMovimiento(movimientoUsuario);

        // Movimiento para el amigo
        if(amigo != null){

            Notificacion notificacion = new Notificacion();

            notificacion.setDescripcion("El usuario " + usuario.getNombre() + " ha compartido un movimiento contigo");
            notificacion.setFecha(new Date());
            notificacion.setEstado("Pendiente");
            notificacion.setTipo("Movimiento");
            notificacion.setUsuario(amigo);
            notificacion.setUsuarioSolicitante(usuario);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String datosAgregarMovimientoJson = objectMapper.writeValueAsString(datosAgregarMovimiento);
                notificacion.setDatosAgregarMovimiento(datosAgregarMovimientoJson);
            } catch (JsonProcessingException e) {
               throw new ExcepcionBaseDeDatos("Error al guardar el movimiento");
            }


            repositorioNotificacion.guardar(notificacion);

        }
    }

    private void crearNotificacion(Meta meta) throws ExcepcionBaseDeDatos {
        Notificacion notificacion = new Notificacion();
        notificacion.setDescripcion("Has superado la meta de la categoría " + meta.getCategoriaMovimiento().getNombre());
        notificacion.setFecha(new Date());
        notificacion.setEstado("Expectante");
        notificacion.setTipo("Meta");
        notificacion.setUsuario(meta.getUsuario());
        repositorioNotificacion.guardar(notificacion);
    }

    private boolean calcularSiSuperoMeta(Meta meta, List<Movimiento> movimientos) {

        Double total = 0.0;
        for (Movimiento movimiento : movimientos) {
            total += movimiento.getMonto();
        }

        return total >= meta.getMontoMeta();

    }

    private List<Movimiento> traerMovimientosDeLaMeta(String categoria, Date fechaInicio, Date fechaFin, Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMovimiento.obtenerMovimientosFiltradosCategoriaFecha(categoria,fechaInicio,fechaFin, idUsuario);
    }

    private Meta traerMeta(String Categoria) throws ExcepcionBaseDeDatos {
        return repositorioMeta.obtenerMetaPorCategoria(Categoria);
    }


    @Transactional
    @Override
    public int calcularCantidadDePaginas(Long idUsuario, int tamanioDePaginas) throws ExcepcionBaseDeDatos{
        Long cantidadDeMovimientos = repositorioMovimiento.obtenerCantidadDeMovimientosPorId(idUsuario);
        return  (int) Math.ceil((double) cantidadDeMovimientos /tamanioDePaginas);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorPagina(Long idUsuario, int pagina, int tamanioDePagina) throws ExcepcionBaseDeDatos {
        return repositorioMovimiento.obtenerMovimientosPorPagina(idUsuario, pagina, tamanioDePagina);
    }

    @Override
    public Map<String, Double> obtenerTotalGastadoEnCategoriasConMetas(Long idUsuario) throws ExcepcionBaseDeDatos {
        List<Meta> metas = repositorioMeta.obtenerMetas(idUsuario);
        Map<String, Double> totalGastadoEnCategoriasConMetas = new HashMap<>();
        LocalDate fechaActual = LocalDate.now();
        int anio = fechaActual.getYear();
        int mes = fechaActual.getMonthValue();
        if(!metas.isEmpty()){
            for (Meta meta : metas) {
                totalGastadoEnCategoriasConMetas.put(meta.getCategoriaMovimiento().getNombre(), repositorioMovimiento.obtenerTotalPorCategoriaEnMesYAnioActual(meta.getCategoriaMovimiento().getId(), mes, anio));
            }
        }
        return totalGastadoEnCategoriasConMetas;
    }

    @Override
    public List<Notificacion> obtenerMovimientosCompartidos(Long idUsuario) throws ExcepcionBaseDeDatos {
        return repositorioMovimiento.obtenerMovimientosCompartidos(idUsuario);
    }

    @Override
    public void aceptarMovimiento(Long idNotificacion, String valor) throws ExcepcionBaseDeDatos {

        repositorioNotificacion.actualizar(idNotificacion, valor);
    }

    @Override
    public void guardarMovimientoDesdeNotificacion(Long idUsuario, DatosAgregarMovimiento datosAgregarMovimiento) throws ExcepcionBaseDeDatos, ExcepcionCamposInvalidos, UsuarioInexistente {
        datosAgregarMovimiento.validarCampos();

        String descripcion = datosAgregarMovimiento.getDescripcion();
        Double monto = datosAgregarMovimiento.getMonto();
        String categoria = datosAgregarMovimiento.getCategoria();
        Long idAmigo = datosAgregarMovimiento.getIdAmigo();
        Double montoAmigo = datosAgregarMovimiento.getMontoAmigo();

        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        Usuario amigo = null;
        if(idAmigo != null){
            amigo = repositorioUsuario.obtenerUsuarioPorId(idAmigo);
        }

        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(categoria);

        // Movimiento para el usuario actual
        Movimiento movimientoUsuario = new Movimiento(
                descripcion,
                monto,
                LocalDate.now(),
                categoriaMovimiento,
                usuario
        );
        if(amigo != null){
            movimientoUsuario.setAmigo(amigo);
            movimientoUsuario.setMontoAmigo(montoAmigo);
        }
        repositorioMovimiento.guardarMovimiento(movimientoUsuario);
    }



    public Map<String, Double> obtenerMetasConFecha(Long idUsuario) throws ExcepcionBaseDeDatos {
        List<Meta> metas = repositorioMeta.obtenerMetas(idUsuario);
        Map<String, Double> totalGastadoEnCategoriasConMetas = new HashMap<>();
        if(!metas.isEmpty()){
            for (Meta meta : metas) {
                totalGastadoEnCategoriasConMetas.put(meta.getCategoriaMovimiento().getNombre(), repositorioMovimiento.obtenerTotalPorCategoriaPorFecha(idUsuario, meta.getCategoriaMovimiento().getId(), meta.getFechaInicio(), meta.getFechaFin()));
            }
        }
        return totalGastadoEnCategoriasConMetas;
    }

    @Override
    public Double obtenerTotalGastado(Long idUsuario, Long idCategoriaMeta, Date fechaInicio, Date fechaFin) throws ExcepcionBaseDeDatos {
        return repositorioMovimiento.obtenerTotalPorCategoriaPorFecha(idUsuario, idCategoriaMeta, fechaInicio, fechaFin);
    }

    @Override
    public void calcularTodasLasMetas(Long idUsuario) throws ExcepcionBaseDeDatos {
        List<Meta> metas = repositorioMeta.obtenerMetas(idUsuario);
        for (Meta meta : metas) {
            List<Movimiento> movimientos = traerMovimientosDeLaMeta(meta.getCategoriaMovimiento().getNombre(), meta.getFechaInicio(), meta.getFechaFin(), meta.getUsuario().getId());
            if(calcularSiSuperoMeta(meta, movimientos)){
                List<Notificacion> notificaciones = contieneNotificacionEnRangoDeFecha(meta.getFechaInicio(),meta.getFechaFin(),idUsuario,"Expectante", meta.getCategoriaMovimiento().getNombre());
                List<Notificacion> notificacionesLeidas = contieneNotificacionEnRangoDeFecha(meta.getFechaInicio(),meta.getFechaFin(),idUsuario,"Leido", meta.getCategoriaMovimiento().getNombre());
                if(!notificaciones.isEmpty()){
                    for (Notificacion notificacion : notificaciones) {
                        repositorioNotificacion.actualizar(notificacion.getId(), "Leido");
                    }
                }else if(notificacionesLeidas.isEmpty()){
                    crearNotificacion(meta);
                }
            }
        }
    }

    private List<Notificacion> contieneNotificacionEnRangoDeFecha(Date fechaInicio, Date fechaFin, Long idUsuario, String estado,String nombre) {
        return  repositorioNotificacion.obtenerNotificacionesMetaFiltradaPorFecha(idUsuario, fechaInicio, fechaFin, estado,nombre);
    }
}
