package unitarios;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloMerito;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloUsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;


/** test usuarios bolsa empleo.
*
*/
public class TestBEPModeloUsuarios {

	private static final Integer CODPERSONA = 100;
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String EMAIL = "test@test";
	private static final String RAZONEXCLUIDO = "test";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final Boolean BORRADO = true;
	private static final Date FECHAEXCLUSION = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHABORRADO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
	 * @throws ParseException si error fecha
     */
    @BeforeClass
    public static void preparaBd() throws SQLException, IOException, ParseException {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    	UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
    }
    
    /** test acierto insertar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaUsuario() throws SQLException, ParseException, UVException {
    	UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodPersona(CODPERSONA);
		usuario.setCodCuenta(CODCUENTA);
		usuario.setRol(ROL);
		usuario.setEmail(EMAIL);
		usuario.setListaDist(LISTADIST);
		usuario.setExcluido(EXCLUIDO);
		usuario.setRazonExcluido(RAZONEXCLUIDO);
		usuario.setFechaExclusion(FECHAEXCLUSION);
		usuario.setBorrado(BORRADO);
		usuario.setFechaBorrado(FECHABORRADO);
    	ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
    	modelo.insertaUsuario(usuario);
    	
    	UsuarioBolsaEmpleo usuarioCont = new UsuarioBolsaEmpleo();
    	usuarioCont = modelo.listaUsuario(usuario.getCodCuenta());
    	List<UsuarioBolsaEmpleo> usuarios = modelo.listaUsuarios();
    	Boolean eje = false;
    	
        for (UsuarioBolsaEmpleo usu : usuarios) {
            if (usu.getCodNum() == usuarioCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("usuario insertado debe ser listado", eje);
    }

    /** test acierto borrar usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar usuario 
     */
    @Test
    public void testA02BorraUsuario() throws SQLException, UVException {
    	ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
    	ModeloMerito modeloMeritos = new ModeloMerito();
    	
    	List<UsuarioBolsaEmpleo> usuarios = modelo.listaUsuarios("");
    	UsuarioBolsaEmpleo usuario = usuarios.get(0);
    	
    	List<Merito> meritos = modeloMeritos.listaMeritos();
    	List<String> meritosAcum = new ArrayList<>();
    	
        for (Merito mer : meritos) {
            if (mer.getUsuario().getCodNum() == usuario.getCodNum()) {
            	meritosAcum.add(mer.getCodNum().toString());
            }
        }
    	
    	modeloMeritos.eliminarMeritos(meritosAcum);
    	modelo.borraUsuario(usuario);
    	try {
    		modelo.listaUsuario(usuario.getCodCuenta());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	} 
    	
    	List<UsuarioBolsaEmpleo> usuariosFiltrados = modelo.listaUsuarios("");
    	assertTrue("usuario borrado no debe ser listadao", !usuariosFiltrados.contains(usuario));
    	assertTrue("usuarios debe tener un elemento menos", usuarios.size() - 1 == usuariosFiltrados.size());
    }
    
    /** test acierto editar noticia.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar noticia
     */
    @Test
    public void testA03EditarUsuario() throws SQLException, UVException {
    	ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
    	List<UsuarioBolsaEmpleo> usuarios = modelo.listaUsuarios("");
    	UsuarioBolsaEmpleo usuario = usuarios.get(0);
    	usuario.setExcluido(EXCLUIDO);
    	modelo.actualizaUsuario(usuario);
    	UsuarioBolsaEmpleo usuarioActualizado = modelo.listaUsuario(usuario.getCodCuenta());
    	
    	assertFalse("usuario debe ser actualizado", usuario.equals(usuarioActualizado));
    }

    /** test edicion datos personales usuario.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test
    public void testA04EditarDatosPersonalesUsuario() throws SQLException, UVException {
    	ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
    	List<UsuarioBolsaEmpleo> usuarios = modelo.listaUsuarios("");
    	UsuarioBolsaEmpleo usuario = usuarios.get(0);
    	usuario.setDireccion("test");
    	modelo.actualizaUsuarioMisDatos(usuario);
    	UsuarioBolsaEmpleo usuarioActualizado = modelo.listaUsuario(usuario.getCodCuenta());
    	
    	assertFalse("usuario debe ser actualizado", usuario.equals(usuarioActualizado));
    }
    
    
    /** test error inserta usuario null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaUsuarioNull() throws SQLException, UVException {
    	UsuarioBolsaEmpleo usuario = null;
    	ModeloUsuarioBolsaEmpleo modelo = new ModeloUsuarioBolsaEmpleo();
    	modelo.insertaUsuario(usuario);
    	fail();
    }
}
