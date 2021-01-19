package basicos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.Usuario;

/** test crear usuario.
 *
 */
public class TestCrearUsuario {
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
     */
    @BeforeClass
    public static void preparaBd() {
    	BbddRunner.conectarBd();
    }

    /** creacion usuarios correctos.
     */
    @Test
    public void testA01() {
    	Usuario usuarioUsig = CrearUsuario.usuario("usig");
    	assertNotNull(usuarioUsig);
    	Usuario usuarioEstudiante = CrearUsuario.usuario("estudiante1");
    	assertNotNull(usuarioEstudiante);
    }

    /** Creacion usuario no existe.
     */
    @Test
    public void testA02() {
    	Usuario usuarioUsig = CrearUsuario.usuario("usig");
    	String cadena = usuarioUsig.toString();
    	assertNotNull(cadena);
    }

    /** Creacion usuario no existe.
     */
    @Test
    public void testE01() {
    	Usuario usuarioError = CrearUsuario.usuario("noexiste");
    	assertNull(usuarioError);
    }
}
