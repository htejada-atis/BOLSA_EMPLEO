package unitarios;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloEvaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;


/** Clase para probar el modelo evaluador. */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloEvaluador {
	private static final Integer CODNUM = 1000;
	private static final String CODCUENTA = "test2";
	private static final Rol ROL = new Rol(1050);
	private static final String EMAIL = "test@test";
	private static final String RAZONEXCLUIDO = "test";
	private static final Boolean LISTADIST = true;
	private static final Boolean EXCLUIDO = false;
	private static final Boolean BORRADO = true;
	private static final Date FECHAEXCLUSION = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Date FECHABORRADO = new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
	private static final Integer AREA = 3;
    
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
	public void testA01InsertaEvaluador() throws SQLException, ParseException, UVException {
		Evaluador evaluador = new Evaluador();
		evaluador.setCodNum(CODNUM);
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

		ModeloEvaluador.obtenerInstancia().insertaEvaluador(evaluador, AREA, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
	}

    /** test acierto borrar evaluador.
     * @throws SQLException si error en bd
     * @throws UVException si error al validar evaluador 
     */
	@Test
	public void testA02BorraEvaluador() throws SQLException, UVException {
		ModeloEvaluador modelo = ModeloEvaluador.obtenerInstancia();
		List<Evaluador> evaluadores = modelo.listaEvaluadores();
		Evaluador evaluador = evaluadores.get(evaluadores.size() - 1);
		Evaluador evaluadorAcum = modelo.getEvaluadorById(evaluador.getCodNum());
		modelo.borraRestauraEvaluador(evaluador, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());

		assertTrue("evaluador borrado no debe ser listado", !evaluadorAcum.getBorrado().equals(evaluador.getBorrado()));
	}
    
    /** test error inserta usuario null.
     * @throws SQLException si error bd
     * @throws UVException error experado
     */
	@Test(expected = UVException.class)
	public void testE01InsertaEvaluadorNull() throws SQLException, UVException {
		UsuarioBolsaEmpleo evaluador = null;
		ModeloEvaluador modelo = ModeloEvaluador.obtenerInstancia();
		modelo.insertaEvaluador(evaluador, AREA, UtilsTestBolsaEmpleo.getUsuarioPersonalLogeado());
		fail();
	}
}
