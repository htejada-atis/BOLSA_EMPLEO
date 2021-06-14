package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase para probar el modelo evaluador. */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloEvaluador {
	private static final Integer CODNUM = 3;  // comision1
	private static final String CODCUENTA = "comision1";
	private static final Integer AREA = 3;
	
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";
    
	/** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd
     * @throws IOException si error en ficheros
	 * @throws ParseException si error fecha
     */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
    /** test acierto insertar evaluador.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar evaluador
     * @throws ParseException si error al validar fecha
     */
	@Test
	public void testA01InsertaYBorraEvaluador() {
		try {
			Evaluador evaluador = insertaUsuario(AREA);
			Boolean activo = evaluador.isActivo();
			evaluador.setActivo(!activo);
			
			ModeloEvaluador.obtenerInstancia().borraRestauraEvaluador(evaluador, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			evaluador = ModeloEvaluador.obtenerInstancia().getEvaluadorById(evaluador.getCodNum(), evaluador.getCodNumArea());
			
			if (Boolean.TRUE.equals(activo)) {
				assertFalse(evaluador.isActivo());
			} else {
				assertTrue(evaluador.isActivo());
			}
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
    
    /** test error inserta usuario null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
	@Test(expected = UVException.class)
	public void testE01InsertaEvaluadorNull() throws SQLException, UVException {
		UsuarioBolsaEmpleo evaluador = null;
		ModeloEvaluador modelo = ModeloEvaluador.obtenerInstancia();
		modelo.insertaEvaluador(evaluador, AREA, UtilsTestBolsaEmpleo.getUsuario("personal1"));
		fail();
	}
	
	private Evaluador insertaUsuario(Integer idArea) throws SQLException, UVException {
		Evaluador evaluador = new Evaluador();
		evaluador.setCodNum(CODNUM);
		
		// comprobamos si existe
		Evaluador eva = ModeloEvaluador.obtenerInstancia().getEvaluadorById(CODNUM, idArea);		
		if (eva == null) {
			ModeloEvaluador.obtenerInstancia().insertaEvaluador(evaluador, idArea, UtilsTestBolsaEmpleo.getUsuario("personal1"));
			eva = ModeloEvaluador.obtenerInstancia().getEvaluadorById(CODNUM, idArea);
			
			assertEquals(eva.getCodNum(), CODNUM);
			assertEquals(eva.getCodCuenta(), CODCUENTA);
			assertEquals(eva.getCodNumArea(), AREA);
		}
		
		return eva; 		
	}
}
