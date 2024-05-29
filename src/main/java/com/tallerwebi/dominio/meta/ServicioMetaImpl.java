package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.excepcion.ExcepcionMetaNoExistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.presentacion.meta.DatosEditarMeta;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Transactional
@Service
public class ServicioMetaImpl implements ServicioMeta{
    private RepositorioMeta repositorioMeta;
    private RepositorioCategoria repositorioCategoria;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioMetaImpl(RepositorioMeta repositorioMeta, RepositorioCategoria repositorioCategoria, RepositorioUsuario repositorioUsuario) {
        this.repositorioMeta = repositorioMeta;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Transactional
    @Override
    public void guardarMeta(Long idUsuario, DatosMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        datosMeta.validarCampos();
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(datosMeta.getCategoria());
        repositorioMeta.existeMetaConUsuarioYCategoria(usuario, categoriaMovimiento);
        Meta meta = new Meta(usuario, categoriaMovimiento, datosMeta.getMonto());
        repositorioMeta.guardar(meta);
    }

    @Override
    public Meta obtenerMetaPorId(Long idMeta) throws ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {
        return repositorioMeta.obtenerMetaPorId(idMeta);
    }

    @Override
    public void eliminarMeta(Long idUsuario, DatosMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionCategoriaConMetaExistente, ExcepcionBaseDeDatos {
        datosMeta.validarCampos();
        Usuario usuario = repositorioUsuario.obtenerUsuarioPorId(idUsuario);
        CategoriaMovimiento categoriaMovimiento = repositorioCategoria.obtenerCategoriaPorNombre(datosMeta.getCategoria());
        repositorioMeta.eliminarMeta(usuario, datosMeta);
    }

    @Override
    public void actualizarMeta(DatosEditarMeta datosMeta) throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionMetaNoExistente {

        repositorioMeta.actualizarMeta(datosMeta.getId());
    }


}
