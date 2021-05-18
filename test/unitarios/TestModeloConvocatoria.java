package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestDocentia;
import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
import es.ujaen.uvirtual.modelo.ModeloDocentia;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.utilidades.UVException;

/** Clase para probar el modelo convocatoria. */
public class TestModeloConvocatoria {
	
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String FECHA_STRING_EJEMPLO = "31/12/2028";
    private static final String NOMBRE_CONVOCATORIA = "nombre convocatoria";
    private static final String OBSERVACIONES_CONVOCATORIA = "observaciones convocatoria";
    private static java.util.Date fechaEjemplo; 
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
	 * @throws ParseException si error fecha
	 * @throws UVException si error uv
     */
    @BeforeClass
    public static void preparaBd() throws SQLException, IOException, ParseException, UVException {
    	DataSource ds = BbddRunner.obtenerDataSourceUv();
    	Conexion.setConexionUvirtual(ds);
    	UtilsTestDocentia.inicializaDocentia();
    	fechaEjemplo = new SimpleDateFormat(FORMATO_FECHA).parse(FECHA_STRING_EJEMPLO);
    }
    
    /** test acierto insertar convocatoria.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar convocatoria
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaConvocatoria() throws SQLException, ParseException, UVException {
    	Convocatoria convocatoria = new Convocatoria();
    	convocatoria.setEstado(ModeloDocentia.EstadoConvocatoria.CERRADA.toString());
    	convocatoria.setFechaComision(fechaEjemplo);
    	convocatoria.setFechaLimite(fechaEjemplo);
    	convocatoria.setNombreConvocatoria(NOMBRE_CONVOCATORIA);
    	convocatoria.setObservaciones(OBSERVACIONES_CONVOCATORIA);
    	ModeloDocentia modelo = new ModeloDocentia();
    	modelo.insertaConvocatoria(convocatoria);
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	assertTrue("convocatoria insertada debe ser listada", convocatorias.contains(convocatoria));
    }

    /** test acierto borrar convocatoria.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar convocatoria 
     */
    @Test
    public void testA02BorraConvocatoria() throws SQLException, UVException {
    	ModeloDocentia modelo = new ModeloDocentia();
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	Convocatoria convocatoria = convocatorias.get(0);
    	modelo.borraConvocatoria(convocatoria);
    	try {
    		modelo.listaConvocatoria(convocatoria.getIdConvocatoria());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	} 
    	List<Convocatoria> convocatoriasFiltradas = modelo.listaConvocatorias();
    	assertTrue("convocatoria borrada no debe ser listada", !convocatoriasFiltradas.contains(convocatoria));
    	assertTrue("convocatorias debe tener un elemento menos", convocatorias.size() - 1 == convocatoriasFiltradas.size());
    }
    
    /** test acierto editar convocatoria.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar convocatoria
     */
    @Test
    public void testA03EditarConvocatoria() throws SQLException, UVException {
    	ModeloDocentia modelo = new ModeloDocentia();
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	Convocatoria convocatoria = convocatorias.get(0);
    	convocatoria.setNombreConvocatoria("nombre actualizado");
    	modelo.actualizaConvocatoria(convocatoria); 
    	Convocatoria convocatoriaActualizada = modelo.listaConvocatoria(convocatoria.getIdConvocatoria());
    	assertTrue("convocatoria debe ser actualizada", convocatoria.equals(convocatoriaActualizada)); 
    }

    /** test error inserta convocatoria null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaConvocatoriaNull() throws SQLException, UVException {
    	Convocatoria convocatoria = null;
    	ModeloDocentia modelo = new ModeloDocentia();
    	modelo.insertaConvocatoria(convocatoria);
    	fail();
    }
    
    /** test error inserta convocatoria sin nombre.
     * @throws SQLException si error bd
     * @throws UVException error experado
     * @throws ParseException si error en fecha
     */
    @Test(expected = UVException.class)
    public void testE02InsertaConvocatoriaSinNombre() throws SQLException, UVException, ParseException {
    	Convocatoria convocatoria = new Convocatoria();
    	convocatoria.setEstado(ModeloDocentia.EstadoConvocatoria.CERRADA.toString());
    	convocatoria.setFechaComision(fechaEjemplo);
    	convocatoria.setFechaLimite(fechaEjemplo);
    	convocatoria.setObservaciones(OBSERVACIONES_CONVOCATORIA);
    	ModeloDocentia modelo = new ModeloDocentia();
    	modelo.insertaConvocatoria(convocatoria);
    	fail();
    }

    /** test error inserta convocatoria sin fecha limite.
     * @throws SQLException si error bd
     * @throws UVException error experado
     * @throws ParseException si error en fecha
     */
    @Test(expected = UVException.class)
    public void testE03InsertaConvocatoriaSinFechaLimite() throws SQLException, UVException, ParseException {
    	Convocatoria convocatoria = new Convocatoria();
    	convocatoria.setEstado(ModeloDocentia.EstadoConvocatoria.CERRADA.toString());
    	convocatoria.setFechaComision(fechaEjemplo);
    	convocatoria.setNombreConvocatoria(NOMBRE_CONVOCATORIA);
    	convocatoria.setObservaciones(OBSERVACIONES_CONVOCATORIA);
    	ModeloDocentia modelo = new ModeloDocentia();
    	modelo.insertaConvocatoria(convocatoria);
    	fail();
    }
    
    /** test error inserta convocatoria sin fecha comision.
     * @throws SQLException si error bd
     * @throws UVException error experado
     * @throws ParseException si error en fecha
     */
    @Test(expected = UVException.class)
    public void testE04InsertaConvocatoriaSinFechaComision() throws SQLException, UVException, ParseException {
    	Convocatoria convocatoria = new Convocatoria();
    	convocatoria.setEstado(ModeloDocentia.EstadoConvocatoria.CERRADA.toString());
    	convocatoria.setFechaLimite(fechaEjemplo);
    	convocatoria.setNombreConvocatoria(NOMBRE_CONVOCATORIA);
    	convocatoria.setObservaciones(OBSERVACIONES_CONVOCATORIA);
    	ModeloDocentia modelo = new ModeloDocentia();
    	modelo.insertaConvocatoria(convocatoria);
    	fail();
    }
    
}