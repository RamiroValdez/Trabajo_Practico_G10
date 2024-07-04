package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaPanel;
import com.tallerwebi.punta_a_punta.vistas.VistaPerfil;
import com.tallerwebi.punta_a_punta.vistas.VistaEditarPerfil;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestPuntaAPunta {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaPanel vistaPanel;
    VistaPerfil vistaPerfil;
    VistaEditarPerfil vistaEditarPerfil;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaPanel = new VistaPanel(page);
        vistaPerfil = new VistaPerfil(page);
        vistaEditarPerfil = new VistaEditarPerfil(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void testModificarNombreUsuario() {
        // Obtener el texto de bienvenida
        String textoBienvenidaInicial = vistaPanel.obtenerTextoDeBienvenida();

        // Ir al perfil y obtener el texto del nombre de usuario
        vistaPanel.irAlPerfil();
        String nombreUsuarioInicial = vistaPerfil.obtenerNombreDeUsuario();

        // Hacer clic en "Editar datos personales" y modificar el nombre de usuario
        vistaPerfil.darClickEnEditarDatosPersonales();
        String nuevoNombreUsuario = "NuevoNombreUsuario";
        vistaEditarPerfil.escribirNombreUsuario(nuevoNombreUsuario);

        // Hacer clic en "Guardar"
        vistaEditarPerfil.darClickEnGuardarDatosPersonales();

        // Obtener el nombre de usuario y verificar que se haya modificado correctamente
        String nombreUsuarioFinal = vistaPerfil.obtenerNombreDeUsuario();
        assertEquals(nuevoNombreUsuario, nombreUsuarioFinal);

        // Ir al panel y obtener el mensaje de bienvenida con el nombre cambiado
        vistaPerfil.irAlPanel();
        String textoBienvenidaFinal = vistaPanel.obtenerTextoDeBienvenida();
        assertTrue(textoBienvenidaFinal.contains(nuevoNombreUsuario));
    }
}
