package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaPanel extends VistaWeb {

    public VistaPanel(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/panel");
    }

    public String obtenerTextoDeBienvenida() {
        return this.obtenerTextoDelElemento(".pagetitle h1");
    }

    public void irAlPerfil() {
        this.darClickEnElElemento("a:contains('Mi Perfil')");
    }


}
