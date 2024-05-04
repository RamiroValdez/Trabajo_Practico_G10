package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ControladorMovimientoTest {
    ControladorMovimiento controladorMovimiento;
    ServicioMovimiento servicioMovimientoMock;
    HttpServletRequest httpServletRequestMock;
    HttpSession httpSessionMock;

    @BeforeEach
    public void init(){
        servicioMovimientoMock = mock(ServicioMovimiento.class);
        controladorMovimiento = new ControladorMovimiento(servicioMovimientoMock);
        httpServletRequestMock = mock(HttpServletRequest.class);
        httpSessionMock = mock(HttpSession.class);
    }
    @Test
    public void queAlClickearLaOpcionVerMovimientosEnElMenuDirijaALaVistaMovimientosYMuestreLosMovimientos(){
        //preparacion
        Movimiento movimientoMock1 = mock(Movimiento.class);
        Movimiento movimientoMock2 = mock(Movimiento.class);
        when(servicioMovimientoMock.obtenerMovimientos(anyLong())).thenReturn(Arrays.asList(movimientoMock1, movimientoMock2));
        when(httpServletRequestMock.getSession(false)).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("idUsuario")).thenReturn(anyLong());

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientos(httpServletRequestMock);
        List<Movimiento> movimientosActual =  (List<Movimiento>) modelAndView.getModel().get("movimientos");

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos"));
        assertThat(movimientosActual, IsIterableWithSize.iterableWithSize(2));
        assertThat(movimientosActual, Matchers.containsInAnyOrder(movimientoMock1, movimientoMock2));
        verify(httpSessionMock, times(1)).getAttribute("idUsuario");
    }

    @Test
    public void queAlQuererIraBarraMovimientosYNoExistaUsuarioLoguadoMeRedirijaAlLoguin(){
        //preparacion
        when(httpServletRequestMock.getSession(false)).thenReturn(null);

        //ejecucion
        ModelAndView modelAndView = controladorMovimiento.obtenerMovimientos(httpServletRequestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(httpSessionMock, times(0)).getAttribute("idUsuario");

    }

}