package unitarios;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.utilidades.UVException;

/** test modelo convocatorias.
*
*/
public class TestBEPModeloConvocatoria {
	private static final Integer CODNUM = 2;
	private static final Integer NUM = 10;
	private static final String CADENA = "cadena";
	private static final String ESTADO = "CERRADA";
	private static final Date FECHACOMISION = new java.sql.Date(Calendar.getInstance().getTime().getTime());

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
    
    /** test acierto insertar convocatoria.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar convocatoria
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaConvocatoria() throws SQLException, ParseException, UVException {
    	Convocatoria convocatoria = new Convocatoria();
		convocatoria.setCodNum(CODNUM);
		convocatoria.setFechaCierre(FECHACOMISION);
		convocatoria.setDescripcion(CADENA);
		convocatoria.setEstado(ESTADO);
		convocatoria.setNumBolsasMaximo(NUM);
		convocatoria.setNumMeritosPorBloque(NUM);
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
    	modelo.nuevaConvocatoria(convocatoria);
    	
    	Convocatoria convocatoriaCont = modelo.getConvocatoriaById(convocatoria.getCodNum());
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	Boolean eje = false;
    	
        for (Convocatoria conv : convocatorias) {
            if (conv.getCodNum() == convocatoriaCont.getCodNum()) {
            	eje = true;
            }
        }
    	
    	assertTrue("convocatoria insertada debe ser listada", eje);
    }

    /** test acierto borrar convocatoria.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar convocatoria 
     */
    @Test
    public void testA02BorraConvocatoria() throws SQLException, UVException {
    	ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
    	
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	Convocatoria convocatoria = convocatorias.get(1);
    	
    	modelo.borraConvocatoria(convocatoria);
    	
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
    	ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	Convocatoria convocatoria = convocatorias.get(0);
    	Convocatoria convocatoriaActualizada = modelo.getConvocatoriaById(convocatoria.getCodNum());
    	convocatoria.setDescripcion("ejemplo");
    	modelo.actualizaConvocatoria(convocatoria);
    	
    	assertFalse("convocatoria debe ser actualizado", convocatoria.getDescripcion().equals(convocatoriaActualizada.getDescripcion()));
    }

    /** test edicion datos convocatoria.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test
    public void testA04CambiarEstadoConvocatoria() throws SQLException, UVException {
    	ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
    	List<Convocatoria> convocatorias = modelo.listaConvocatorias();
    	Convocatoria convocatoria = convocatorias.get(0);
    	Convocatoria convocatoriaActualizada = modelo.getConvocatoriaById(convocatoria.getCodNum());
    	convocatoria.setEstado("CERRADA");
    	modelo.cambiaEstadoConvocatoria(convocatoria);

    	assertFalse("convocatoria debe ser actualizada", convocatoria.getEstado().equals(convocatoriaActualizada.getEstado()));
    }
    
    
    /** test error inserta convocatoria null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaConvocatoriaNull() throws SQLException, UVException {
    	Convocatoria convocatoria = null;
    	ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
    	modelo.nuevaConvocatoria(convocatoria);
    	fail();
    }
}
