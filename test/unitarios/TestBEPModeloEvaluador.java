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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase para probar el modelo evaluador. */
public class TestBEPModeloEvaluador {
	private static final Integer CODPERSONA = 1;
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String EMAIL = "test@test";
	private static final String RAZONEXCLUIDO = "test";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final Boolean BORRADO = true;
	private static final Date FECHAEXCLUSION = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Date FECHABORRADO = new java.sql.Date(Calendar.getInstance().getTime().getTime());
	private static final Integer AREA = 3;
    
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
    
    /** test acierto insertar evaluador.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar evaluador
     * @throws ParseException si error al validar fecha
     */
    @Test
    public void testA01InsertaAfinidad() throws SQLException, ParseException, UVException {
		Evaluador evaluador = new Evaluador();
		evaluador.setCodPersona(CODPERSONA);
		evaluador.setCodCuenta(CODCUENTA);
		evaluador.setRol(ROL);
		evaluador.setEmail(EMAIL);
		evaluador.setListaDist(LISTADIST);
		evaluador.setExcluido(EXCLUIDO);
		evaluador.setRazonExcluido(RAZONEXCLUIDO);
		evaluador.setFechaExclusion(FECHAEXCLUSION);
		evaluador.setBorrado(BORRADO);
		evaluador.setFechaBorrado(FECHABORRADO);
		evaluador.setCodNumArea(AREA);    	

    }

    /** test acierto borrar afinidad.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar afinidad 
     */
    @Test
    public void testA02BorraAfinidad() throws SQLException, UVException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	List<Afinidad> afinidades = modelo.listaAfinidades();
    	Afinidad afinidad = afinidades.get(0);
    	modelo.borraAfinidad(afinidad);
    	try {
    		modelo.getAfinidadById(afinidad.getCodNum());
    		fail();
    	} catch (UVException e) {
    		//se expera excepcion
    	}
    	List<Afinidad> afinidadesFiltradas = modelo.listaAfinidades();
    	assertTrue("fichero borrado no debe ser listado", !afinidadesFiltradas.contains(afinidad));
    }
    
    /** test acierto editar afinidad.
     * @throws SQLException si error en bd
     * @throws UVException si error el validar afinidad
     */
    @Test
    public void testA03EditarAfinidad() throws SQLException, UVException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	List<Afinidad> afinidades = modelo.listaAfinidades();
    	Afinidad afinidad = afinidades.get(0);
    	modelo.actualizaAfinidad(afinidad);
    	Afinidad afinidadActualizada = modelo.getAfinidadById(afinidad.getCodNum());
    	
    	assertFalse("afinidad debe ser actualizada", afinidad.equals(afinidadActualizada));
    }
    
    
    /** test error inserta usuario null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
    @Test(expected = UVException.class)
    public void testE01InsertaAfinidadNull() throws SQLException, UVException {
    	Afinidad afinidad = null;
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();
    	modelo.nuevaAfinidad(afinidad);
    	fail();
    }
}
