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
	private static final String MENSAJE_HISTORIALES_DEVUELTOS = "Debe devolver historiales";
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
		peticion.setParameter(ControladorValidar.PARAM_ITEM, item.getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_VALOR, VALOR);
		peticion.setParameter(ControladorValidar.PARAM_BOLSA, bean.getBolsa().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_CANDIDATO, bean.getCandidato().getCodNum().toString());
		peticion.setParameter(ControladorValidar.PARAM_MERITO, bean.getDatatableMeritos().getData().get(0).getCodNum().toString());
		
		RespuestaHttp respuesta = new RespuestaHttp();
		ControladorValidar controlador = new ControladorValidar();
		controlador.doPost(peticion, respuesta);
		
		VistaValidar bean2 = (VistaValidar) peticion.getUVDatos().getVistas().get(VistaValidar.class.getName());
		
		System.out.println("mensaje: " + bean2.getMensajesDeError().get(0).toString());
		assertEquals(MENSAJE_SIN_ERROR, 0, bean2.getMensajesDeError().size());
		assertEquals(MENSAJE_SIN_ADVERTENCIAS, 0, bean2.getMensajesDeAdvertencia().size());
	}
	
	
}
