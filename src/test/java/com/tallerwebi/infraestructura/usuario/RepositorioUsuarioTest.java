package com.tallerwebi.infraestructura.usuario;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionCamposInvalidos;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.usuario.RepositorioUsuario;
import com.tallerwebi.dominio.usuario.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioUsuarioTest {

        @Autowired
        private SessionFactory sessionFactory;

        private RepositorioUsuario repositorioUsuario;

        @BeforeEach
        public void init() {
            repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioBuscarUsuarioPorEmailYPasswordLoHagaCorrectamente() throws UsuarioInexistente, ExcepcionBaseDeDatos, ExcepcionCamposInvalidos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorEmailYPassword("email", "password");

            // validacion
            assertNotNull(usuarioEncontrado);
            assertEquals(usuario.getEmail(), usuarioEncontrado.getEmail());
            assertEquals(usuario.getPassword(), usuarioEncontrado.getPassword());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioBuscarUsuarioPorEmailYPasswordConEmailIncorrectoLanceUsuarioInexistente() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Exception exception = assertThrows(UsuarioInexistente.class, () -> {
                repositorioUsuario.buscarUsuarioPorEmailYPassword("emailIncorrecto", "password");
            });

            // validacion
            assertTrue(exception instanceof UsuarioInexistente);
            assertEquals(UsuarioInexistente.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioBuscarUsuarioPorEmailYPasswordLanceExceptionBaseDeDatos()throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            SessionFactory sessionFactoryMock = mock(SessionFactory.class);
            Session sessionMock = mock(Session.class);
            when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
            when(sessionMock.createQuery(anyString())).thenThrow(new HibernateException("Simulated exception"));
            RepositorioUsuario repositorioUsuario = new RepositorioUsuarioImpl(sessionFactoryMock);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Exception exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
                repositorioUsuario.buscarUsuarioPorEmailYPassword("email", "password");
            });

            // validacion
            assertTrue(exception instanceof ExcepcionBaseDeDatos);
            assertEquals(ExcepcionBaseDeDatos.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioGuardarLoHagaCorrectamente() throws ExcepcionBaseDeDatos, UsuarioExistente {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);

            // ejecucion
            repositorioUsuario.guardar(usuario);

            // validacion
            Session session = sessionFactory.getCurrentSession();
            Usuario usuarioEncontrado = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", "email")
                    .uniqueResult();
            assertNotNull(usuarioEncontrado);
            assertEquals(usuario.getEmail(), usuarioEncontrado.getEmail());
            assertEquals(usuario.getPassword(), usuarioEncontrado.getPassword());
            assertEquals(usuario.getRol(), usuarioEncontrado.getRol());
            assertEquals(usuario.getActivo(), usuarioEncontrado.getActivo());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioGuardarConUsuarioExistenteLanceUsuarioExistente() throws ExcepcionBaseDeDatos, UsuarioExistente {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Exception exception = assertThrows(UsuarioExistente.class, () -> {
                repositorioUsuario.guardar(usuario);
            });

            // validacion
            assertTrue(exception instanceof UsuarioExistente);
            assertEquals(UsuarioExistente.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioGuardarLanceExceptionBaseDeDatos() throws ExcepcionBaseDeDatos, UsuarioExistente {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            SessionFactory sessionFactoryMock = mock(SessionFactory.class);
            Session sessionMock = mock(Session.class);
            when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
            when(sessionMock.createQuery(anyString())).thenThrow(new HibernateException("Simulated exception"));
            RepositorioUsuario repositorioUsuario = new RepositorioUsuarioImpl(sessionFactoryMock);

            // ejecucion
            Exception exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
                repositorioUsuario.guardar(usuario);
            });

            // validacion
            assertTrue(exception instanceof ExcepcionBaseDeDatos);
            assertEquals(ExcepcionBaseDeDatos.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioBuscarUsuarioPorEmailLoHagaCorrectamente() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorEmail("email");

            // validacion
            assertNotNull(usuarioEncontrado);
            assertEquals(usuario.getEmail(), usuarioEncontrado.getEmail());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioBuscarUsuarioPorEmailConEmailIncorrectoLanceUsuarioInexistente() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Exception exception = assertThrows(UsuarioInexistente.class, () -> {
                repositorioUsuario.buscarUsuarioPorEmail("emailIncorrecto");
            });

            // validacion
            assertTrue(exception instanceof UsuarioInexistente);
            assertEquals(UsuarioInexistente.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioBuscarUsuarioPorEmailLanceExceptionBaseDeDatos()throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            SessionFactory sessionFactoryMock = mock(SessionFactory.class);
            Session sessionMock = mock(Session.class);
            when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
            when(sessionMock.createQuery(anyString())).thenThrow(new HibernateException("Simulated exception"));
            RepositorioUsuario repositorioUsuario = new RepositorioUsuarioImpl(sessionFactoryMock);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Exception exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
                repositorioUsuario.buscarUsuarioPorEmail("email");
            });

            // validacion
            assertTrue(exception instanceof ExcepcionBaseDeDatos);
            assertEquals(ExcepcionBaseDeDatos.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioModificarLoHagaCorrectamente() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            LocalDate fechaNacimiento = LocalDate.of(2024, 6, 16);
            Usuario usuario = new Usuario("nombre", "apellido", "nombreUsuario", "email@test","pass", fechaNacimiento, "pais", 1122334455L, "admin", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            usuario.setActivo(false);
            repositorioUsuario.modificar(usuario);

            // validacion
            Usuario usuarioEncontrado = (Usuario) session.createQuery("FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", "email@test")
                    .uniqueResult();
            assertNotNull(usuarioEncontrado);
            assertEquals(usuario.getEmail(), usuarioEncontrado.getEmail());
            assertEquals(usuario.getPassword(), usuarioEncontrado.getPassword());
            assertEquals(usuario.getRol(), usuarioEncontrado.getRol());
            assertEquals(usuario.getActivo(), usuarioEncontrado.getActivo());
            assertEquals(usuario.getNombre(), usuarioEncontrado.getNombre());
            assertEquals(usuario.getApellido(), usuarioEncontrado.getApellido());
            assertEquals(usuario.getNombreUsuario(), usuarioEncontrado.getNombreUsuario());
            assertEquals(usuario.getPais(), usuarioEncontrado.getPais());
            assertEquals(usuario.getTelefono(), usuarioEncontrado.getTelefono());
            assertEquals(usuario.getFechaNacimiento(), usuarioEncontrado.getFechaNacimiento());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioModificarConUsuarioInexistenteLanceUsuarioInexistente() {
            // preparacion
            LocalDate fechaNacimiento = LocalDate.of(2024, 6, 16);
            Usuario usuario = new Usuario("nombre", "apellido", "nombreUsuario", "email@test","pass", fechaNacimiento, "pais", 1122334455L, "admin", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            usuario.setId(0L);
            Exception exception = assertThrows(UsuarioInexistente.class, () -> {
                repositorioUsuario.modificar(usuario);
            });

            // validacion
            assertTrue(exception instanceof UsuarioInexistente);
            assertEquals(UsuarioInexistente.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioModificarLanceExceptionBaseDeDatos() {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            usuario.setId(1L);

            Session sessionMock = mock(Session.class);
            when(sessionMock.get(Usuario.class, usuario.getId())).thenReturn(usuario);
            doThrow(new HibernateException("Simulated exception")).when(sessionMock).update(usuario);

            SessionFactory sessionFactoryMock = mock(SessionFactory.class);
            when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);

            RepositorioUsuarioImpl repositorioUsuario = new RepositorioUsuarioImpl(sessionFactoryMock);

            // ejecucion y validacion
            assertThrows(ExcepcionBaseDeDatos.class, () -> {
                repositorioUsuario.modificar(usuario);
            });
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioObtenerUsuarioPorIdLoHagaCorrectamente() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Usuario usuarioEncontrado = repositorioUsuario.obtenerUsuarioPorId(usuario.getId());

            // validacion
            assertNotNull(usuarioEncontrado);
            assertEquals(usuario.getEmail(), usuarioEncontrado.getEmail());
            assertEquals(usuario.getPassword(), usuarioEncontrado.getPassword());
            assertEquals(usuario.getRol(), usuarioEncontrado.getRol());
            assertEquals(usuario.getActivo(), usuarioEncontrado.getActivo());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioObtenerUsuarioPorIdConIdIncorrectoLanceUsuarioInexistente() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // preparacion
            Usuario usuario = new Usuario("email", "password", "rol", true);
            Session session = sessionFactory.getCurrentSession();
            session.save(usuario);

            // ejecucion
            Exception exception = assertThrows(UsuarioInexistente.class, () -> {
                repositorioUsuario.obtenerUsuarioPorId(0L);
            });

            // validacion
            assertTrue(exception instanceof UsuarioInexistente);
            assertEquals(UsuarioInexistente.class, exception.getClass());
        }

        @Test
        @Transactional
        @Rollback
        public void queAlSolicitarAlRepositorioObtenerUsuarioPorIdLanceExceptionBaseDeDatos() throws UsuarioInexistente, ExcepcionBaseDeDatos {
            // Preparación
            Long usuarioId = 1L;
            SessionFactory sessionFactoryMock = mock(SessionFactory.class);
            Session sessionMock = mock(Session.class);
            when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
            when(sessionMock.get(Usuario.class, usuarioId)).thenThrow(new HibernateException("Simulated exception"));
            RepositorioUsuario repositorioUsuario = new RepositorioUsuarioImpl(sessionFactoryMock);

            // Ejecución
            Exception exception = assertThrows(ExcepcionBaseDeDatos.class, () -> {
                repositorioUsuario.obtenerUsuarioPorId(usuarioId);
            });

            // Validación
            assertTrue(exception instanceof ExcepcionBaseDeDatos);
            assertEquals(ExcepcionBaseDeDatos.class, exception.getClass());
        }

}
