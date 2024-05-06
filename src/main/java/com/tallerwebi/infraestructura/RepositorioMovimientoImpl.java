package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.RepositorioMovimiento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioMovimiento")
public class RepositorioMovimientoImpl implements RepositorioMovimiento {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMovimientoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movimiento> obtenerMovimientos(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();
        Usuario usuario =  session.get(Usuario.class, idUsuario);

        return new ArrayList<>(usuario.getMovimientos()) ;
    }

    @Override
    public void agregarMovimiento(Movimiento movimiento) {
        sessionFactory.getCurrentSession().save(movimiento);
    }


}
