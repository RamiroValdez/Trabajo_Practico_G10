package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaPerfil extends VistaWeb {

    public VistaPerfil(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/mi-perfil");
    }

    public void darClickEnEditarDatosPersonales(){
        this.darClickEnElElemento("a:contains('Editar datos personales')");
    }

    public String obtenerNombreDeUsuario() {
        return this.obtenerTextoDelElemento("div.mb-3:nth-of-type(3) span");
    }

    public void irAlPanel() {
        this.darClickEnElElemento("a.logo");
    }

}