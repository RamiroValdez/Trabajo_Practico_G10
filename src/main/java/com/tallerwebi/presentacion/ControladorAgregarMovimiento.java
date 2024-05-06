package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAgregarMovimiento {

    @RequestMapping("/agregar-movimiento")
    public ModelAndView mostrarAgregarMovimiento() {
        ModelMap model = new ModelMap();
        model.put("agregarMovimiento", new DatosMovimiento());
        return new ModelAndView("agregar-movimiento", model);
    }

    @RequestMapping(path = "")
    public ModelAndView agregarMovimiento(DatosMovimiento datos) {

    }


}
