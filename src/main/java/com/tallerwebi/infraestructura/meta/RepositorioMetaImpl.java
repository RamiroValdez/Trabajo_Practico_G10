package com.tallerwebi.infraestructura.meta;

import com.tallerwebi.dominio.categoria.CategoriaMovimiento;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCategoriaConMetaExistente;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.RepositorioMeta;
import com.tallerwebi.dominio.usuario.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioMetaImpl implements RepositorioMeta {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMetaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Meta meta) throws ExcepcionBaseDeDatos {
        try {
            sessionFactory.getCurrentSession().save(meta);
        }catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos();
        }

    }

    @Override
    public void existeMetaConUsuarioYCategoria(Usuario usuario, CategoriaMovimiento categoria) throws ExcepcionCategoriaConMetaExistente, ExcepcionBaseDeDatos {
        Meta meta = null;
        try{
            Session session = sessionFactory.getCurrentSession();
            meta = (Meta)session.createQuery("FROM Meta m WHERE m.usuario = :usuario AND m.categoriaMovimiento = :categoria")
                    .setParameter("usuario", usuario)
                    .setParameter("categoria", categoria)
                    .uniqueResult();
            if(meta != null) {
                throw new ExcepcionCategoriaConMetaExistente();
            }
        }catch (HibernateException e){
            throw new ExcepcionBaseDeDatos();
        }
    }
}