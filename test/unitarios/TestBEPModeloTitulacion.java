package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloTitulacion;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar el modelo titulación. */
public class TestBEPModeloTitulacion {
	
    private static final String NOMBRE_TITULACION = "nombre titulacion";
    
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
    
    /** test acierto insertar titulacion.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar titulación
     */
    @Test
    public void testA01InsertaTitulacion() throws SQLException, UVException {
    	Titulacion titulacion = new Titulacion();
    	titulacion.setNombre(NOMBRE_TITULACION);
    	ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
    	modelo.insertaTitulacion(titulacion);
    	List<Titulacion> titulaciones = modelo.listaTitulaciones();
    	assertTrue("titulacion insertada debe ser listada", titulaciones.contains(titulacion));
    }
    
    /** test acierto borrar titulacion.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar titulación 
     */
    @Test
    public void testA02BorraTitulacion() throws SQLException, UVException {
    	ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
    	List<Titulacion> titulaciones = modelo.listaTitulaciones();
    	Titulacion titulacion = titulaciones.get(0);
    	modelo.borraTitulacion(titulacion);
    	try {
    		modelo.listaTitulacion(titulacion.getCodNum());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	}
    	List<Titulacion> titulacionesFiltradas = modelo.listaTitulaciones();
    	assertTrue("titulación borrada no debe ser listada", !titulacionesFiltradas.contains(titulacion));
    	assertTrue("titulaciones debe tener un elemento menos", titulaciones.size() - 1 == titulacionesFiltradas.size());
    }
    
    /** test acierto editar titulación.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar titulación
     */
    @Test
    public void testA03EditarTitulacion() throws SQLException, UVException {
    	ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
    	List<Titulacion> titulaciones = modelo.listaTitulaciones();
    	Titulacion titulacion = titulaciones.get(0);
    	titulacion.setNombre("nombre actualizado");
    	modelo.actualizaTitulacion(titulacion); 
    	Titulacion titulacionActualizada = modelo.listaTitulacion(titulacion.getCodNum());
    	assertTrue("titulación debe ser actualizada", titulacion.equals(titulacionActualizada)); 
    }
    
    /** test error inserta titulación null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaTitulacionNull() throws SQLException, UVException {
    	Titulacion titulacion = null;
    	ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
    	modelo.insertaTitulacion(titulacion);
    	fail();
    }
    
    /** test error inserta titulación sin nombre.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE02InsertaTitulacionSinNombre() throws SQLException, UVException {
    	Titulacion titulacion = new Titulacion();
    	ModeloTitulacion modelo = ModeloTitulacion.obtenerInstancia();
    	modelo.insertaTitulacion(titulacion);
    	fail();
    }
    
}