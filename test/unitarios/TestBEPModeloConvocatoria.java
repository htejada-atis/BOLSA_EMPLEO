package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test modelo convocatorias.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloConvocatoria {
	private static final Integer NUM = 10;
	private static final String CADENA = "cadena";
	private static final String ESTADO = "CERRADA";
	private static final Date FECHACOMISION = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}

	/**
	 * test acierto insertar convocatoria.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws UVException    si error al validar convocatoria
	 * @throws ParseException si error al validar fecha
	 */
	@Test
	public void testA01InsertaConvocatoria() throws SQLException, ParseException, UVException {
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setFechaCierre(FECHACOMISION);
		convocatoria.setDescripcion(CADENA);
		convocatoria.setEstado(ESTADO);
		convocatoria.setNumBolsasMaximo(NUM);
		convocatoria.setNumMeritosPorBloque(NUM);
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		Integer codNum = modelo.nuevaConvocatoria(convocatoria, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());

		Convocatoria convocatoriaCont = modelo.getConvocatoriaById(codNum);
		assertEquals(Formateador.formatoFecha(convocatoriaCont.getFechaCierre(), Formateador.FORMATO_FECHA_DDMMYYYY), 
				Formateador.formatoFecha(FECHACOMISION, Formateador.FORMATO_FECHA_DDMMYYYY));
		assertEquals(convocatoriaCont.getDescripcion(), CADENA);
		assertEquals(convocatoriaCont.getEstado(), ESTADO);
		assertEquals(convocatoriaCont.getNumBolsasMaximo(), NUM);
		assertEquals(convocatoriaCont.getNumMeritosPorBloque(), NUM);

		List<Convocatoria> convocatorias = modelo.listaConvocatorias();
		Boolean eje = false;

		for (Convocatoria conv : convocatorias) {
			if (conv.getCodNum().equals(convocatoriaCont.getCodNum())) {
				eje = true;
			}
		}

		assertTrue("convocatoria insertada debe ser listada", eje);
	}

	/**
	 * test acierto borrar convocatoria.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error al validar convocatoria
	 */
	@Test
	public void testA02BorraConvocatoria() throws SQLException, UVException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		// creamos convocatoria
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setFechaCierre(FECHACOMISION);
		convocatoria.setDescripcion(CADENA);
		convocatoria.setEstado(ESTADO);
		convocatoria.setNumBolsasMaximo(NUM);
		convocatoria.setNumMeritosPorBloque(NUM);
		Integer codNum = modelo.nuevaConvocatoria(convocatoria, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		Convocatoria convocatoriaCont = modelo.getConvocatoriaById(codNum);
		List<Convocatoria> convocatorias = modelo.listaConvocatorias();
		
		// borramos
		modelo.borraConvocatoria(convocatoriaCont, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		List<Convocatoria> convocatoriasFiltradas = modelo.listaConvocatorias();
		
		assertTrue("convocatoria borrada no debe ser listada", !convocatoriasFiltradas.contains(convocatoria));
		assertTrue("convocatorias debe tener un elemento menos", convocatorias.size() - 1 == convocatoriasFiltradas.size());
	}

	/**
	 * test acierto editar convocatoria.
	 * 
	 * @throws SQLException si error en bd
	 * @throws UVException  si error el validar convocatoria
	 */
	@Test
	public void testA03EditarConvocatoria() throws SQLException, UVException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		List<Convocatoria> convocatorias = modelo.listaConvocatorias();
		Convocatoria convocatoria = convocatorias.get(0);
		Convocatoria convocatoriaActualizada = modelo.getConvocatoriaById(convocatoria.getCodNum());
		convocatoria.setDescripcion("ejemplo");
		modelo.actualizaConvocatoria(convocatoria, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());

		assertFalse("convocatoria debe ser actualizado",
				convocatoria.getDescripcion().equals(convocatoriaActualizada.getDescripcion()));
	}

	/**
	 * test edicion datos convocatoria.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test
	public void testA04CambiarEstadoConvocatoria() throws SQLException, UVException {
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		
		List<Convocatoria> convocatorias = modelo.listaConvocatorias();
		Convocatoria convocatoriaActualizada = modelo.getConvocatoriaById(convocatorias.get(0).getCodNum());
		convocatoriaActualizada.setEstado("CERRADA");
		
		modelo.cambiaEstadoConvocatoria(convocatoriaActualizada, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		Convocatoria convocatoriaUpd = modelo.getConvocatoriaById(convocatoriaActualizada.getCodNum());

		assertTrue("convocatoria debe ser actualizada", convocatoriaActualizada.getEstado().equals(convocatoriaUpd.getEstado()));
	}

	/**
	 * test error inserta convocatoria null.
	 * 
	 * @throws SQLException si error bd
	 * @throws UVException  error experado
	 */
	@Test(expected = UVException.class)
	public void testE01InsertaConvocatoriaNull() throws SQLException, UVException {
		Convocatoria convocatoria = null;
		ModeloConvocatoria modelo = ModeloConvocatoria.obtenerInstancia();
		modelo.nuevaConvocatoria(convocatoria, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		fail();
	}
}
