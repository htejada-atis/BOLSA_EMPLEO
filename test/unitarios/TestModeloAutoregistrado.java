package unitarios;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestAutoregistrado;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modulo.autoregistrado.modelo.ModeloUsuarioAutoregistrado;
import es.ujaen.uvirtual.utilidades.UVException;

/** test para el modelo de autoaprovisionado.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestModeloAutoregistrado {

	static final String TEXTO = "prueba";
	Usuario usuario;
	ModeloUsuarioAutoregistrado modelo = new ModeloUsuarioAutoregistrado();

	/** prepara la bbdd para los tests.
	 * @throws IOException si error io
	 * @throws SQLException si error db
     */
    @BeforeClass
    public static void preparaBd() throws SQLException, IOException {
    	BbddRunner.conectarBd();
    	UtilsTestAutoregistrado.inicializaDb();
    }
    
    /** prepara un usuario generico antes de cada test.
     */
    @Before
    public void preparaUsuario() {
    	usuario = new Usuario();
    	usuario.setNombre(TEXTO);
    	usuario.setApellido1(TEXTO);
    	usuario.setDocumentoNumero(TEXTO);
    	usuario.setDocumentoTipo("PAS");
    	usuario.setDominio("ujaen.es");
    	usuario.setUid(TEXTO);
    	usuario.setEmailCuentaPersona("prueba@pruebaNoexssta3384.com");
    }
    
    /** inserta un usuario basico.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test
    public void testA01InsertaUsuarioAutoRegistrado() throws SQLException, UVException {
    	modelo.insertaUsuarioAutoregistrado(usuario);
    }
    
    /** Inserta peticion cambio.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test
    public void testA02InsertaPeticionCambio() throws SQLException, UVException {
    	modelo.insertaPeticionCambio(usuario.getEmailCuentaPersona(), TEXTO, "123456", "127.0.0.1");
    }
    
    /** verifica una peticion de cambio.
     * @throws SQLException si error sql
     * @throws UVException si error uv
     */
    @Test
    public void testA02IVerificaPeticionCambio() throws SQLException, UVException {
    	String solicitud = "123456abcd";
    	String codigoTemporal = "123456";
    	String ipOrigenPeticion = "127.0.0.1";
    	String ipOrigenValidacion = "127.0.0.2";
    	modelo.insertaPeticionCambio(usuario.getEmailCuentaPersona(), solicitud, codigoTemporal, ipOrigenPeticion);
    	String clave = modelo.verificaPeticionCambio(usuario.getEmailCuentaPersona(), solicitud, codigoTemporal, ipOrigenValidacion);
    	assertTrue(modelo.validaClaveUsuario(usuario.getEmailCuentaPersona(), clave, ipOrigenPeticion));
    }
    

    /** inserta un usuario con correo mal.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test(expected = UVException.class)
    public void testE01UsuarioCorreoMalFormado() throws SQLException, UVException {
    	usuario.setEmailCuentaPersona("prueba@prueba@com");
    	modelo.insertaUsuarioAutoregistrado(usuario);
    }

    /** inserta un usuario null.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test(expected = UVException.class)
    public void testE01UsuarioNull() throws SQLException, UVException {
    	modelo.insertaUsuarioAutoregistrado(null);
    }
    
    /** inserta un usuario con nombre null.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test(expected = UVException.class)
    public void testE01UsuarioNombreNull() throws SQLException, UVException {
    	usuario.setNombre(null);
    	modelo.insertaUsuarioAutoregistrado(usuario);
    }

    /** inserta un usuario con nombre vacio.
     * @throws SQLException si error bd
     * @throws UVException si error uv
     */
    @Test(expected = UVException.class)
    public void testE01UsuarioNombreVacio() throws SQLException, UVException {
    	usuario.setNombre("");
    	modelo.insertaUsuarioAutoregistrado(usuario);
    }
}
