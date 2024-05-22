package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.categoria.RepositorioCategoria;
import com.tallerwebi.presentacion.meta.DatosMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioMetaTest {

    ServicioMeta servicioMeta;
    RepositorioMeta repositorioMetaMock;
    RepositorioCategoria repositorioCategoriaMock;
    RepositorioUsuario repositorioUsuarioMock;

    @BeforeEach
    public void init() {
        repositorioMetaMock = mock(RepositorioMeta.class);
        repositorioCategoriaMock = mock(RepositorioCategoria.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        servicioMeta = new ServicioMetaImpl(repositorioMetaMock, repositorioCategoriaMock, repositorioUsuarioMock);
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaGuardeLaMetaCorrectamente() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        // ejecucion
        servicioMeta.guardarMeta(idUsuario, datosMeta);

        // validacion
        verify(repositorioMetaMock, times(1)).guardar(any(Meta.class));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionCamposInvalidosSiNoSeEnviaCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        // ejecucion y validacion
        try {
            servicioMeta.guardarMeta(idUsuario, datosMeta);
        } catch (ExcepcionCamposInvalidos e) {
            verify(repositorioMetaMock, times(0)).guardar(any(Meta.class));
        }
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionCamposInvalidosSiNoSeEnviaMonto() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        Long idUsuario = 1L;

        // ejecucion y validacion
        try {
            servicioMeta.guardarMeta(idUsuario, datosMeta);
        } catch (ExcepcionCamposInvalidos e) {
            verify(repositorioMetaMock, times(0)).guardar(any(Meta.class));
        }
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionCategoriaConMetaExistenteSiYaExisteUnaMetaConEsaCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionCategoriaConMetaExistente.class).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(any(), any());

        // ejecucion y validacion
        assertThrows(ExcepcionCategoriaConMetaExistente.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlGuardarLaMeta() throws ExcepcionCamposInvalidos, ExcepcionBaseDeDatos {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMetaMock).guardar(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarSiExisteMetaConUsuarioYCategoria() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioMetaMock).existeMetaConUsuarioYCategoria(any(), any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarCategoriaPorNombre() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioCategoriaMock).obtenerCategoriaPorNombre(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

    @Test
    public void queAlSolicitarAlServicioGuardarMetaLanceExcepcionBaseDeDatosSiOcurreUnErrorAlConsultarUsuarioPorId() throws ExcepcionBaseDeDatos, ExcepcionCategoriaConMetaExistente {
        // preparacion
        DatosMeta datosMeta = new DatosMeta();
        datosMeta.setCategoria("Comida");
        datosMeta.setMonto(1000.0);
        Long idUsuario = 1L;

        doThrow(ExcepcionBaseDeDatos.class).when(repositorioUsuarioMock).obtenerUsuarioPorId(any());

        // ejecucion y validacion
        assertThrows(ExcepcionBaseDeDatos.class, () -> servicioMeta.guardarMeta(idUsuario, datosMeta));
    }

}
