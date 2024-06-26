package com.tallerwebi.dominio.categoria;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

import javax.persistence.EntityManager;
import java.util.List;

public interface RepositorioCategoria {
    CategoriaMovimiento obtenerCategoriaPorNombre(String nombre) throws ExcepcionBaseDeDatos;
    List<CategoriaMovimiento> obtenerCategorias() throws ExcepcionBaseDeDatos;
    public void actualizarColor(CategoriaMovimiento categoria) throws ExcepcionBaseDeDatos;
    void setEntityManager(EntityManager entityManagerMock);
}
