package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioAgregarMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAgregarMovimiento {

    public ServicioAgregarMovimiento servicioAgregarMovimiento;

    @Autowired
    public ControladorAgregarMovimiento(ServicioAgregarMovimiento servicioAgregarMovimiento) {
        this.servicioAgregarMovimiento = servicioAgregarMovimiento;
    }

    @RequestMapping("/agregar-movimiento")
    public ModelAndView mostrarAgregarMovimiento() {
        ModelMap model = new ModelMap();
        model.put("agregarMovimiento", new DatosMovimiento());
        return new ModelAndView("agregar-movimiento", model);
    }

    @RequestMapping(path = "/agregar-datos", method = RequestMethod.POST)
    public ModelAndView agregarMovimiento(@ModelAttribute("agregarMovimiento")Movimiento movimiento) {
        ModelMap model = new ModelMap();
        servicioAgregarMovimiento.agregarMovimiento(movimiento);
        return new ModelAndView("redirect:/movimientos");
    }


}
