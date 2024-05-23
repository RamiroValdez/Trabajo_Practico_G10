package com.tallerwebi.presentacion.movimientoCompartido;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAgregarAmigosTest {


    private ControladorAgregarAmigos controladorAgregarAmigos;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        controladorAgregarAmigos = new ControladorAgregarAmigos();
    }

    @Test
    public void queAlClickearLaOpcionAgregarAmigosEnElMenuDirijaALaVistaAgregarAmigos(){
        //preparacion
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        //ejecucion
        ModelAndView modelAndView = controladorAgregarAmigos.irAAgregarAmigos(requestMock);

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("agregar-amigos"));
    }
}
