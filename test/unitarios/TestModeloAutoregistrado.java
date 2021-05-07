package unitarios;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.ModeloUsuarioAutoregistrado;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** test para el modelo de autoaprovisionado.
 *
 */
public class TestModeloAutoregistrado {

	/** prepara la bbdd para los tests.
     */
    @BeforeClass
    public static void preparaBd() {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    	//UtilsTestAutoaprovisionado.inicializaBd();
    }
    
    /** inserta un usuario basico.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test
    public void testA01() throws SQLException, UVException {
    	ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();
    	Usuario usuario = new Usuario();
    	modelo.insertaUsuarioAutoregistrado(usuario);
    }

}
