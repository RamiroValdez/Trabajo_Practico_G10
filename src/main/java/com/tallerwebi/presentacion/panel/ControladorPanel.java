package com.tallerwebi.presentacion.panel;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import com.tallerwebi.dominio.panel.*;
import com.tallerwebi.dominio.usuario.ServicioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorPanel {

    private ServicioPanel servicioPanel;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPanel(ServicioPanel servicioPanel, ServicioUsuario servicioUsuario) {
        this.servicioPanel = servicioPanel;
        this.servicioUsuario = servicioUsuario;
    }


    @GetMapping("/panel")
    public ModelAndView irAPanel(HttpServletRequest request) throws ExcepcionBaseDeDatos, UsuarioInexistente {
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ModelAndView("redirect:/login");
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);

        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        if (nombreUsuario == null)
            return new ModelAndView("redirect:/login");

        ModelMap model = new ModelMap();
        model.put("usuario", usuario);
        model.addAttribute("nombreUsuario", nombreUsuario);
        return new ModelAndView("panel", model);
    }



    @GetMapping("/panel/egresos")
    @ResponseBody
    public List<Movimiento> obtenerEgresos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> egresos = servicioPanel.obtenerMovimientosEgresosPorUsuario(idUsuario);
        return egresos;
    }

    @GetMapping("/panel/ingresos")
    @ResponseBody
    public List<Movimiento> obtenerIngresos(HttpServletRequest httpServletRequest) throws ExcepcionBaseDeDatos {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            return null;
        Long idUsuario = (Long) httpSession.getAttribute("idUsuario");
        List<Movimiento> ingresos = servicioPanel.obtenerMovimientosIngresosPorUsuario(idUsuario);
        return ingresos;
    }

}
