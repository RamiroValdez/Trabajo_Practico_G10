package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaEditarPerfil extends VistaWeb {

    public VistaEditarPerfil(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/perfil/editar");
    }

    public void escribirNombreUsuario(String nombreUsuario){
        this.escribirEnElElemento("#nombre-usuario", nombreUsuario);
    }

    public void darClickEnGuardarDatosPersonales(){
        this.darClickEnElElemento("button.btn.btn-primary");
    }

}
