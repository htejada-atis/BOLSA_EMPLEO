package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.beans.Rol;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo mis titulaciones.
*
*/
public class TestBEPModeloMisTitulaciones {
	private static final Integer CODPERSONA = 1;
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final String EXCLUIDOTIPO = "EJEMPLO";
	
	private static final Integer CODNUM = 10;
	private static final Integer CODNUM_TITULACION = 1;
	private static final String NOMBRE = "nombre";
	private static final UsuarioBolsaEmpleo USUARIO = new UsuarioBolsaEmpleo(CODPERSONA, CODCUENTA, ROL, LISTADIST, EXCLUIDO, EXCLUIDOTIPO);
	private static final Titulacion TITULACION = new Titulacion(CODPERSONA, NOMBRE);
	private static final InputStream ARCHIVO = new ByteArrayInputStream("archivo de prueba".getBytes());
    
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
    	Titulacion titulacion = new Titulacion();
    	titulacion.setCodNum(CODNUM_TITULACION);
    	titulacion.setTitulacion(TITULACION);
    	USUARIO.setCodNum(CODNUM);
    	titulacion.setUsuario(USUARIO);
    	titulacion.setDescripcion(NOMBRE);
    	titulacion.setArchivo(ARCHIVO);
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
    	modelo.insertaTitulacionUsuario(titulacion);
    	
    	Titulacion titulacionCont = modelo.listaTitulacion(titulacion.getCodNum());
    	List<Titulacion> titulaciones = modelo.listaTitulaciones();
    	Boolean eje = false;
    	
        for (Titulacion tit : titulaciones) {
            if (tit.getCodNum() == titulacionCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("titulacion insertada debe ser listado", eje);
    }

    /** test acierto borrar titulacion usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar titulacion 
     */
    @Test
    public void testA02BorraTitulacion() throws SQLException, UVException {
    	ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
    	
    	List<Titulacion> titulaciones = modelo.listaTitulaciones();
    	Titulacion titulacion = titulaciones.get(0);
    	
    	modelo.borraTitulacionUsuario(titulaciones);
    	
    	List<Titulacion> titulacionesFiltrados = modelo.listaTitulaciones();
    	Titulacion titulacion2 = titulacionesFiltrados.get(0);
    	
    	Boolean eje = false;
    	
        if (titulacion.getBorrado() != titulacion2.getBorrado()) {
        	eje = true;
        }
    	
    	assertTrue("titulacion borrada", eje);
    }
    
    /** test error inserta titulacion null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaTitulacionNull() throws SQLException, UVException {
    	Titulacion titulacion = null;
    	ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
    	modelo.insertaTitulacionUsuario(titulacion);
    	fail();
    }
}
