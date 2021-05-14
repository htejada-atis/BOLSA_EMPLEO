package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMisTitulaciones;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo mis titulaciones.
*
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloMisTitulaciones {
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String DOCUMENTO = "123456789A";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final String EXCLUIDOTIPO = "EJEMPLO";
	private static final Date FECHAEXCLUSIONINICIO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHAEXCLUSIONFIN = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	
	private static final Integer CODNUM = 10;
	private static final Integer CODNUM_TITULACION = 1;
	private static final String NOMBRE = "nombre";
	private static final UsuarioBolsaEmpleo USUARIO = 
			new UsuarioBolsaEmpleo(DOCUMENTO, CODCUENTA, ROL, LISTADIST, EXCLUIDO, EXCLUIDOTIPO, FECHAEXCLUSIONINICIO, FECHAEXCLUSIONFIN);
	private static final Titulacion TITULACION = new Titulacion(CODNUM, NOMBRE);
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
     * @throws IOException .
     */
    @Test
    public void testA01InsertaUsuario() throws SQLException, ParseException, UVException, IOException {
    	TitulacionUsuario titulacion = new TitulacionUsuario();
    	titulacion.setCodNum(CODNUM_TITULACION);
    	titulacion.setTitulacion(TITULACION);
    	USUARIO.setCodNum(CODNUM);
    	titulacion.setUsuario(USUARIO);
    	titulacion.setDescripcion(NOMBRE);
    	titulacion.setArchivo(ARCHIVO);
		ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
    	modelo.insertaTitulacionUsuario(titulacion);
    	
    	TitulacionUsuario titulacionCont = modelo.listaTitulacionUsuario(titulacion.getCodNum());
    	List<TitulacionUsuario> titulaciones = modelo.listaTitulacionesUsuarios("");
    	Boolean eje = false;
    	
        for (TitulacionUsuario tit : titulaciones) {
            if (tit.getCodNum().equals(titulacionCont.getCodNum())) {
            	eje = true;
            }
        }
    	
    	assertTrue("titulacion de usuario insertada debe ser listado", eje);
    }

    /** test acierto borrar titulacion usuario.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar titulacion 
     */
    @Test
    public void testA02BorraTitulacion() throws SQLException, UVException {
    	ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
    	
    	List<TitulacionUsuario> titulaciones = modelo.listaTitulacionesUsuarios("");
    	TitulacionUsuario titulacion = titulaciones.get(0);
    	
    	modelo.borraTitulacionUsuario(titulaciones);
    	
    	List<TitulacionUsuario> titulacionesFiltrados = modelo.listaTitulacionesUsuarios("");
    	TitulacionUsuario titulacion2 = titulacionesFiltrados.get(0);
    	
    	Boolean eje = false;
    	
        if (!titulacion.getBorrado().equals(titulacion2.getBorrado())) {
        	eje = true;
        }
    	
    	assertTrue("titulacion borrada", eje);
    }
    
    /** test error inserta titulacion null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     * @throws IOException .
     */
    @Test(expected = UVException.class)
    public void testE01InsertaTitulacionNull() throws SQLException, UVException, IOException {
    	TitulacionUsuario titulacion = null;
    	ModeloMisTitulaciones modelo = ModeloMisTitulaciones.obtenerInstancia();
    	modelo.insertaTitulacionUsuario(titulacion);
    	fail();
    }
}
