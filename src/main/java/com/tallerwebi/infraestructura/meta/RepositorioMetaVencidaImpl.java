package com.tallerwebi.infraestructura.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.MetaVencida;
import com.tallerwebi.dominio.meta.RepositorioMetaVencida;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioMetaVencidaImpl implements RepositorioMetaVencida {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMetaVencidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarMetaVencida(Meta meta, Double totalGastado) throws ExcepcionBaseDeDatos {
        try {
            MetaVencida metaVencida = new MetaVencida();
            metaVencida.setUsuario(meta.getUsuario());
            metaVencida.setCategoriaMovimiento(meta.getCategoriaMovimiento());
            metaVencida.setMontoMeta(meta.getMontoMeta());
            metaVencida.setFechaInicio(meta.getFechaInicio());
            metaVencida.setFechaFin(meta.getFechaFin());
            metaVencida.setTotalGastado(totalGastado);

            sessionFactory.getCurrentSession().save(metaVencida);
        }catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos();
        }
    }

    @Transactional
    @Override
    public List<MetaVencida> obtenerMetasVencidas(Long idUsuario) {
        return sessionFactory.getCurrentSession().createQuery("from MetaVencida where usuario.id = :idUsuario", MetaVencida.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }
}
