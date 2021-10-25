package controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import controlador.implementacion.PeticionHttp;
import controlador.implementacion.RespuestaHttp;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidar;
import es.ujaen.uvirtual.modulo.bolsaempleo.controlador.ControladorValidarNoAfines;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaValidar;
import es.ujaen.uvirtual.utilidades.UVException;

/** test controlador validar no afines .
 * @author ATISoluciones
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPControladorValidarAfines {
	
	private static final String MENSAJE_BOLSAS_DEVUELTAS = "Debe devolver bolsas";
	private static final String MENSAJE_BOLSA_DEVUELTA = "Debe devolver bolsa";
	private static final String MENSAJE_CANDIDATOS_DEVUELTOS = "Debe devolver candidatos";
	private static final String MENSAJE_CANDIDATO_DEVUELTO = "Debe devolver candidato";
	private static final String MENSAJE_CON_ERROR = "Debe devolver error";
	private static final String MENSAJE_CON_ERROR_ESPERADO = "El mensaje de error debe coincidir";
	private static final String MENSAJE_CON_EXITO_ESPERADO = "El mensaje de éxito debe coincidir";
	private static final String MENSAJE_MERITOS_DEVUELTOS = "Debe devolver méritos";
	private static final String MENSAJE_MERITO_DEVUELTO = "Debe devolver mérito";
	private static final String MENSAJE_SIN_ADVERTENCIAS = "No debe mostrar advertencias";
	private static final String MENSAJE_SIN_ERROR = "No debe devolver error";
	private static final String MENSAJE_SIN_EXITO = "No debe exito";
	
	private static final String OBSERVACIONES = "observaciones";
	private static final String VALOR = "1";
	
	
    /** prepara la bd con los datos iniciales.
     * @throws SQLException si error en bd .
     * @throws IOException si error en io .
     */
	@BeforeClass
	public static void preparaBd() throws IOException, SQLException {
		BbddRunner.conectarBd();
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
	}
    
	// método para obtener la vista con una lista de áreas .
	private VistaValidar obtenerAreas() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "5");
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_DESC);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		return (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
	}
    
    // método para obtener la vista con una lista de candidatos .
	private VistaValidar obtenerCandidatos() throws IOException {
		VistaValidar bean = obtenerAreas();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getDatatableBolsas().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		return (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
	}
    
	// método para obtener la vista con una lista de méritos .
	private VistaValidar obtenerMeritos() throws IOException {
		VistaValidar bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_DATATABLE_MERITOS);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		return (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
	}
    
	/** Obtener áreas, sin parametro definido .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA01() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doGet(peticion, respuesta);

		VistaValidar bean = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable áreas .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA02ObtenerAreas() throws IOException {
		VistaValidar bean = obtenerAreas();
		
		assertNotEquals(MENSAJE_BOLSAS_DEVUELTAS, 0, bean.getDatatableBolsas().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar área .
	 * @throws IOException si error io .
	 */
	@Test
	public void testA03SeleccionarArea() throws IOException {
		VistaValidar bean = obtenerAreas();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_BOLSA_SELECCIONADA);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA,
				bean.getDatatableBolsas().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);

		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertNotNull(MENSAJE_BOLSA_DEVUELTA, bean2.getBolsa());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable candidatos .
	 * @throws IOException .
	 */
	@Test
	public void testA04ObtenerCandidatos() throws IOException {
		VistaValidar bean = obtenerCandidatos();
		
		assertNotEquals(MENSAJE_CANDIDATOS_DEVUELTOS, 0, bean.getDatatableCandidatos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar candidato .
	 * @throws IOException .
	 */
	@Test
	public void testA05SeleccionarCandidato() throws IOException {
		VistaValidar bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_CANDIDATO_SELECCIONADO);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO,
				bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);

		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertNotNull(MENSAJE_CANDIDATO_DEVUELTO, bean2.getCandidato());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Obtener datatable méritos .
	 * @throws IOException .
	 */
	@Test
	public void testA06ObtenerMeritos() throws IOException {
		VistaValidar bean = obtenerMeritos();
		
		assertNotEquals(MENSAJE_MERITOS_DEVUELTOS, 0, bean.getDatatableMeritos().getData().size());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean.getMensajesDeAdvertencia().size());
	}
	
	/** Seleccionar mérito .
	 * @throws IOException .
	 */
	@Test
	public void testA07SeleccionarMerito() throws IOException {
		VistaValidar bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_MERITO_SELECCIONADO);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);

		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Guardar mérito .
	 * @throws IOException .
	 */
	@Test
	public void testA08GuardarMerito() throws IOException {
		VistaValidar bean = obtenerMeritos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_MODIFICAR_MERITO);
		peticion.setParameter(ControladorValidar.PARAM_ITEM, bean.getDatatableMeritos().getData().get(0).getItemBaremacion().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_VALOR, VALOR);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorValidar.MENSAJE_EXITO_MERITO_MODIFICAR, bean2.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Cambia el ítem del mérito a uno sin afinidad .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 */
	@Test
	public void testA09GuardarMeritoCambiarItemASinAfinidad() throws IOException, SQLException, UVException {
		VistaValidar bean = obtenerMeritos();
		
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modelo.listaItemBaremacion().stream().filter(i -> Objects.equals(i.getAfinidad(), null)).findFirst().orElse(null);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_MODIFICAR_MERITO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSAS,
				"[" + bean.getBolsa().getCodNum().toString() + "]");
		peticion.setParameter(ControladorValidar.PARAM_ITEM, item.getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_VALOR, VALOR);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorValidar.MENSAJE_EXITO_MERITO_MODIFICAR_AFINIDAD, bean2.getMensajesDeExito().get(1));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Cambia el ítem del mérito a uno no individualizado .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 */
	@Test
	public void testA10GuardarMeritoCambiarItemANoIndividualizado() throws IOException, SQLException, UVException {
		VistaValidar bean = obtenerMeritos();
		
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modelo.listaItemBaremacion().stream().filter(i -> Objects.equals(i.getIndividualizado(), false)).findFirst().orElse(null);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_MODIFICAR_MERITO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSAS,
				"[" + bean.getBolsa().getCodNum().toString() + "]");
		peticion.setParameter(ControladorValidar.PARAM_ITEM, item.getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_VALOR, VALOR);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Excluir mérito no individualizado .
	 * @throws IOException .
	 */
	@Test
	public void testA11ExcluirMerito() throws IOException {
		VistaValidar bean = obtenerMeritos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_VALIDAR_MERITO);
		peticion.setParameter(ControladorValidar.PARAM_EXCLUIR_MERITO, "");
		peticion.setParameter(ControladorValidar.PARAM_OBSERVACION_CANDIDATO, OBSERVACIONES);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_BOLSA_MERITO, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_AFINIDADES, "{'4': " + bean.getDatatableMeritos().getData().get(0).getValor() + "}");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);

		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, String.format(
				ControladorValidar.MENSAJE_EXITO_MERITO_EXCLUIDO, bean2.getBolsa().getArea().getDescripcion()), bean2.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Cambia el ítem del mérito a uno no individualizado .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws IOException .
	 */
	@Test
	public void testA12GuardarMeritoCambiarItemAIndividualizado() throws IOException, SQLException, UVException {
		VistaValidar bean = obtenerMeritos();
		
		ModeloBaremacionItems modelo = ModeloBaremacionItems.obtenerInstancia();
		ItemBaremacion item = modelo.listaItemBaremacion().stream().filter(i -> Objects.equals(i.getIndividualizado(), true) 
				&& i.getAfinidad() != null).findFirst().orElse(null);
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_MODIFICAR_MERITO);
		peticion.setParameter(ControladorValidarNoAfines.PARAM_BOLSAS,
				"[" + bean.getBolsa().getCodNum().toString() + "]");
		peticion.setParameter(ControladorValidar.PARAM_ITEM, item.getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_VALOR, VALOR);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, ControladorValidar.MENSAJE_EXITO_MERITO_MODIFICAR, bean2.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Aceptar mérito individualizado .
	 * @throws IOException .
	 */
	@Test
	public void testA13AceptarMerito() throws IOException {
		VistaValidar bean = obtenerMeritos();
		
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_VALIDAR_MERITO);
		peticion.setParameter(ControladorValidar.PARAM_ACEPTAR_MERITO, "");
		peticion.setParameter(ControladorValidar.PARAM_OBSERVACION_CANDIDATO, OBSERVACIONES);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_BOLSA_MERITO, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_AFINIDADES, "{'4': " + bean.getDatatableMeritos().getData().get(0).getValor() + "}");
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);

		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_CON_EXITO_ESPERADO, String.format(
				ControladorValidar.MENSAJE_EXITO_MERITO_VALIDADO, bean2.getBolsa().getArea().getDescripcion()), bean2.getMensajesDeExito().get(0));
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** Historial de validación del mérito .
	 * @throws IOException .
	 */
	@Test
	public void testA14HistorialValidacion() throws IOException {
		VistaValidar bean = obtenerMeritos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_HISTORIAL_VALIDACION);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertNotNull(MENSAJE_MERITO_DEVUELTO, bean2.getMerito());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	/** acción no válida .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE01AccionNoValida() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, "accionnovalida");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doGet(peticion, respuesta);

		VistaValidar bean = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(MENSAJE_CON_ERROR_ESPERADO, ControladorValidar.MENSAJE_ERROR_ACCION_NO_CONTEMPLADA, bean.getMensajesDeError().get(0));
	}
	
	/** Obtener datatable áreas con parámetro no válido para forzar el error .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE02ObtenerAreasParametroNoValido() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_DATATABLE_BOLSAS);
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
	}
	
	/** Obtener datatable candidatos con parámetro no válido para forzar el error .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE03ObtenerCandidatosParametroNoValido() throws IOException {
		VistaValidar bean = obtenerAreas();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_DATATABLE_CANDIDATOS);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getDatatableBolsas().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);

		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Obtener datatable méritos con parámetro no válido para forzar el error .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE04ObtenerMeritosParametroNoValido() throws IOException {
		VistaValidar bean = obtenerCandidatos();

		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaPersonal();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_DATATABLE_MERITOS);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getDatatableCandidatos().getData().get(0).getCodNum().toString());
		peticion.setParameter(BolsaEmpleoDataTable.PARAM_ORDER_BY, "9");

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());

		assertEquals(MENSAJE_CON_ERROR, 1, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean2.getMensajesDeExito().size());
	}
	
	/** Error de permiso .
	 * @throws IOException si error io .
	 */
	@Test
	public void testE06ErrorUsuarioRol() throws IOException {
		PeticionHttp peticion = UtilsTestBolsaEmpleo.peticionAutenticadaCandidato();
		peticion.setParameter(ControladorValidar.PARAM_ACCION, ControladorValidar.ACCION_INDEX);

		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		VistaValidar bean = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		assertEquals(MENSAJE_CON_ERROR, 1, bean.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_EXITO, 0, bean.getMensajesDeExito().size());
		assertEquals(ControladorValidar.MENSAJE_ERROR_SIN_PERMISO, bean.getMensajesDeError().get(0).toString());
	}
	
}
