package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAlegaciones;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloAlegaciones {
	private static final String MENSAJE_ERROR_BOLSA_FUERA_DE_ALEGACIONES = "No se puede crear una alegación: la bolsa no está en estado ALEGACIONES.";
	private static final String MENSAJE_ERROR_MODIFICAR_FUERA_DE_ALEGACIONES = "No se puede modificar una alegación: la bolsa no está en estado ALEGACIONES.";
	private static final String MENSAJE_ERROR_SIN_PERMISO = "No tienes permiso para modificar esta alegación.";

	private static UsuarioBolsaEmpleo candidato1;
	private static UsuarioBolsaEmpleo candidato3;
	private static UsuarioBolsaEmpleo personal;

	@BeforeClass
	public static void preparaBd() throws SQLException, IOException, UVException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();

		candidato1 = UtilsTestBolsaEmpleo.getUsuario("candidato1");
		candidato3 = UtilsTestBolsaEmpleo.getUsuario("candidato3");
		personal = UtilsTestBolsaEmpleo.getUsuario("personal1");
	}

	@Test
	public void testA01CrearAlegacionCompruebaEstadoActualBolsa() throws SQLException, UVException {
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		Bolsa bolsaSolicitud = getPrimeraBolsaSolicitud(candidato1, convocatoria);
		limpiarAlegacionSiExiste(candidato1, bolsaSolicitud, convocatoria);
		actualizarEstadoBolsa(bolsaSolicitud.getCodNum(), ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA);

		Bolsa bolsaConEstadoObsoleto = ModeloBolsa.obtenerInstancia().getBolsaById(bolsaSolicitud.getCodNum());
		bolsaConEstadoObsoleto.setEstado(ModeloBolsa.BOLSA_ESTADO_ALEGACIONES);

		Throwable throwable = assertThrows(Throwable.class,
				() -> ModeloAlegaciones.obtenerInstancia().crearAlegacion(bolsaConEstadoObsoleto, candidato1, convocatoria));

		assertEquals(MENSAJE_ERROR_BOLSA_FUERA_DE_ALEGACIONES, throwable.getMessage());
		assertNull(ModeloAlegaciones.obtenerInstancia().getAlegacion(
				ModeloBolsa.obtenerInstancia().getBolsaById(bolsaSolicitud.getCodNum()), candidato1, convocatoria));
	}

	@Test
	public void testA02ConfirmarAlegacionRequiereBolsaEnAlegaciones() throws SQLException, UVException {
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		Bolsa bolsaSolicitud = getPrimeraBolsaSolicitud(candidato3, convocatoria);
		limpiarAlegacionSiExiste(candidato3, bolsaSolicitud, convocatoria);

		Alegacion alegacionCreada = null;
		try {
			Bolsa bolsaEnAlegaciones = actualizarEstadoBolsa(bolsaSolicitud.getCodNum(), ModeloBolsa.BOLSA_ESTADO_ALEGACIONES);
			alegacionCreada = ModeloAlegaciones.obtenerInstancia().crearAlegacion(bolsaEnAlegaciones, candidato3, convocatoria);
			assertNotNull(alegacionCreada);

			actualizarEstadoBolsa(bolsaSolicitud.getCodNum(), ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA);

			Throwable throwable = assertThrows(Throwable.class,
					() -> ModeloAlegaciones.obtenerInstancia().confirmarAlegacion(bolsaSolicitud.getCodNum(), candidato3, convocatoria));

			assertEquals(MENSAJE_ERROR_MODIFICAR_FUERA_DE_ALEGACIONES, throwable.getMessage());
			assertEquals(ModeloAlegaciones.ESTADO_PENDIENTE_ALEGACION,
					ModeloAlegaciones.obtenerInstancia().getAlegacion(
							ModeloBolsa.obtenerInstancia().getBolsaById(bolsaSolicitud.getCodNum()), candidato3, convocatoria).getEstado());
		} finally {
			actualizarEstadoBolsa(bolsaSolicitud.getCodNum(), ModeloBolsa.BOLSA_ESTADO_ALEGACIONES);
			if (alegacionCreada != null) {
				limpiarAlegacionSiExiste(candidato3, bolsaSolicitud, convocatoria);
			}
		}
	}

	@Test
	public void testA03EliminarArchivoAlegacionExigePropietario() throws SQLException, UVException {
		Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		Bolsa bolsaSolicitud = getPrimeraBolsaSolicitud(candidato1, convocatoria);
		limpiarAlegacionSiExiste(candidato1, bolsaSolicitud, convocatoria);

		try {
			Bolsa bolsaEnAlegaciones = actualizarEstadoBolsa(bolsaSolicitud.getCodNum(), ModeloBolsa.BOLSA_ESTADO_ALEGACIONES);
			ModeloAlegaciones modeloAlegaciones = ModeloAlegaciones.obtenerInstancia();
			Alegacion alegacion = modeloAlegaciones.crearAlegacion(bolsaEnAlegaciones, candidato1, convocatoria);
			assertNotNull(alegacion);

			modeloAlegaciones.agregarDescripcionAlegacionGeneral(bolsaEnAlegaciones.getCodNum(), candidato1, convocatoria, "Descripción de prueba");
			modeloAlegaciones.agregarFicheroAlegacionGeneral(
					bolsaEnAlegaciones.getCodNum(),
					candidato1,
					convocatoria,
					new ByteArrayInputStream("contenido pdf".getBytes(StandardCharsets.UTF_8)),
					"alegacion.pdf");

			BolsaEmpleoDataTable<ArchivoAlegacionGeneral> dataTable = modeloAlegaciones.datatableFicherosAlegacionGeneral(
					bolsaEnAlegaciones.getCodNum(), candidato1, convocatoria, crearParametrosDataTable());
			assertFalse(dataTable.getData().isEmpty());
			Integer archivoId = dataTable.getData().get(0).getId();

			Throwable throwable = assertThrows(Throwable.class,
					() -> modeloAlegaciones.eliminarArchivoAlegacion(Collections.singletonList(archivoId), candidato3));

			assertEquals(MENSAJE_ERROR_SIN_PERMISO, throwable.getMessage());

			BolsaEmpleoDataTable<ArchivoAlegacionGeneral> dataTableTrasError = modeloAlegaciones.datatableFicherosAlegacionGeneral(
					bolsaEnAlegaciones.getCodNum(), candidato1, convocatoria, crearParametrosDataTable());
			assertEquals(1, dataTableTrasError.getData().size());
		} finally {
			limpiarAlegacionSiExiste(candidato1, bolsaSolicitud, convocatoria);
		}
	}

	private static Bolsa getPrimeraBolsaSolicitud(UsuarioBolsaEmpleo candidato, Convocatoria convocatoria) throws SQLException, UVException {
		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato, convocatoria);
		assertNotNull(solicitud);

		List<BolsaSolicitud> bolsasSolicitud = ModeloSolicitud.obtenerInstancia().getBolsasSolicitudMeritos(solicitud);
		assertFalse(bolsasSolicitud.isEmpty());

		return ModeloBolsa.obtenerInstancia().getBolsaById(bolsasSolicitud.get(0).getCodNum());
	}

	private static Bolsa actualizarEstadoBolsa(Integer bolsaCodNum, String estado) throws SQLException, UVException {
		ModeloBolsa modeloBolsa = ModeloBolsa.obtenerInstancia();
		Bolsa bolsa = modeloBolsa.getBolsaById(bolsaCodNum);
		List<Bolsa> bolsas = Collections.singletonList(bolsa);

		if (ModeloBolsa.BOLSA_ESTADO_ALEGACIONES.equals(estado)) {
			modeloBolsa.ponerBolsasEnAlegaciones(bolsas, personal, new java.util.Date());
		} else if (ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA.equals(estado)) {
			modeloBolsa.desbloquearBolsas(bolsas, personal);
		} else if (ModeloBolsa.BOLSA_ESTADO_BAREMACION.equals(estado)) {
			modeloBolsa.ponerBolsasEnBaremacion(bolsas, personal);
		} else if (ModeloBolsa.BOLSA_ESTADO_REVISION.equals(estado)) {
			modeloBolsa.ponerBolsasEnRevision(bolsas, personal);
		} else if (ModeloBolsa.BOLSA_ESTADO_BLOQUEADA.equals(estado)) {
			modeloBolsa.bloquearBolsas(bolsas, personal);
		} else {
			throw new IllegalArgumentException("Estado de bolsa no soportado en test: " + estado);
		}

		return modeloBolsa.getBolsaById(bolsaCodNum);
	}

	private static HashMap<String, String[]> crearParametrosDataTable() {
		HashMap<String, String[]> params = new HashMap<>();
		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION,
				new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});
		return params;
	}

	private static void limpiarAlegacionSiExiste(UsuarioBolsaEmpleo candidato, Bolsa bolsa, Convocatoria convocatoria) throws SQLException, UVException {
		Alegacion alegacion = ModeloAlegaciones.obtenerInstancia().getAlegacion(
				ModeloBolsa.obtenerInstancia().getBolsaById(bolsa.getCodNum()), candidato, convocatoria);
		if (alegacion == null) {
			return;
		}

		UtilsTestBolsaEmpleo.sqlExecute(
				"DELETE FROM TBEP_SOL_MER_BOL_ALE_FILE WHERE BEPALM_CODNUM IN (SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = "
						+ alegacion.getCodNum() + ")");
		UtilsTestBolsaEmpleo.sqlExecute(
				"DELETE FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = " + alegacion.getCodNum());
		UtilsTestBolsaEmpleo.sqlExecute("DELETE FROM TBEP_ALEGACIONES WHERE CODNUM = " + alegacion.getCodNum());
	}
}