package unitarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.Conexion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloAfinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBaremacionItems;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloBolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloConvocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * test solicitud.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBEPModeloSolicitud {
	private static final String MENSAJE_ERROR_HAY_EXCEPCION = "Excepción no esperada: %s";	
	private static UsuarioBolsaEmpleo candidato1;
	private static UsuarioBolsaEmpleo candidato5;

	/**
	 * prepara la bd con los datos iniciales.
	 * 
	 * @throws SQLException   si error en bd
	 * @throws IOException    si error en ficheros
	 * @throws UVException .
	 * @throws ParseException si error fecha
	 */
	@BeforeClass
	public static void preparaBd() throws SQLException, IOException, UVException {
		Conexion.setConexionUvirtual(BbddRunner.obtenerDataSourceUv());
		Conexion.setConexionArcos(BbddRunner.obtenerDataSourceArcos());
		Conexion.setConexionUxxiRrhh(BbddRunner.obtenerDataSourceRh());
		Conexion.setConexionUxxiAc(BbddRunner.obtenerDataSourceAc());
		UtilsTestBolsaEmpleo.inicializaBolsaEmpleo();
		
		candidato1 = UtilsTestBolsaEmpleo.getUsuario("candidato1");
		candidato5 = UtilsTestBolsaEmpleo.getUsuario("candidato5");
	}
	
	/**
	 * listaSolicitudesDatatable.
	 */
	@Test
	public void testA01listaSolicitudesDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		// params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {null});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			BolsaEmpleoDataTable<Solicitud> dt = ModeloSolicitud.obtenerInstancia().listaSolicitudesDatatable(candidato1, params);
			assertFalse(dt.getData().isEmpty());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaAreaSolicitudSeleccionadasDatatable.
	 */
	@Test
	public void testA02listaAreaSolicitudSeleccionadasDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		// params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {null});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			BolsaEmpleoDataTable<Bolsa> dt = ModeloSolicitud.obtenerInstancia().listaAreaSolicitudSeleccionadasDatatable(params, s);
			assertFalse(dt.getData().isEmpty());
			
			// getSolicitudByConvocatoriaUsuario null
			Solicitud s2 = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato5, c);
			assertNull(s2);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * listaBolsasSolicitudesDatatable.
	 */
	@Test
	public void testA03listaBolsasSolicitudesDatatable() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		// params.put(BolsaEmpleoDataTable.PARAM_ORDER_BY, new String[] {null});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});

		try {
			ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
			
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = modeloSolicitud.getSolicitudByConvocatoriaUsuario(candidato1, c);
			
			// bolsas solicitud
			BolsaEmpleoDataTable<BolsaSolicitudTable> dtBolsas = modeloSolicitud.listaBolsasSolicitudesDatatable(s, params);			
			assertFalse(dtBolsas.getData().isEmpty());
			Bolsa b = dtBolsas.getData().get(0);
			
			// meritos solicitud bolsa
			BolsaEmpleoDataTable<MeritoSolicitudTable> dtMeritos = modeloSolicitud.listaMeritosSolicitudDatatable(params, s.getUsuario(), b, s);
			assertFalse(dtMeritos.getData().isEmpty());
			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
		
	/**
	 * haySolicitudAbiertaParaConvocatoria.
	 * @throws SQLException .
	 */
	@Test
	public void testA04haySolicitudAbiertaParaConvocatoria() throws SQLException {
		Convocatoria c = null;
		boolean cerrarConvocatoria = false;
		
		try {
					
			ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
			
			c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();			
			assertFalse(modelo.haySolicitudAbiertaParaConvocatoria(candidato1, c));
			
			// comprobamos si la convocatoria está abierta
			if (c.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
				cerrarConvocatoria = true;
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = '" + ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA 
						+ "' WHERE CODNUM = " + c.getCodNum());
				c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			}
			
			// creamos solicitud abierta para otro candidato						
			Solicitud s = modelo.nuevaSolicitud(candidato5, c, candidato5);
			assertTrue(ModeloSolicitud.obtenerInstancia().haySolicitudAbiertaParaConvocatoria(candidato5, c));
			
			// limpiamos solicitud creada
			UtilsTestBolsaEmpleo.sqlExecute("DELETE FROM TBEP_SOLICITUDES WHERE CODNUM = " + s.getCodNum());						
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		} finally {
			if (c != null && cerrarConvocatoria) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = '" + ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA 
						+ "' WHERE CODNUM = " + c.getCodNum());
			}
		}
	}
	
	/**
	 * getSolicitudCerradaByConvocatoriaUsuario.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	public void testA05getSolicitudCerradaByConvocatoriaUsuario() throws SQLException, UVException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		
		try {
			modelo.getSolicitudCerradaByConvocatoriaUsuario(candidato1, c);								
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
		
		Throwable throwable = assertThrows(Throwable.class,
				() -> modelo.getSolicitudCerradaByConvocatoriaUsuario(candidato5, c));

		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloSolicitud.MENSAJE_ERROR_SIN_SOLICITUD_CERRADA, throwable.getMessage());
	}
	
	/**
	 * getMeritoSolicitud.
	 */
	@Test
	public void testA06getMeritoSolicitud() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});
		
		try {
			ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			
			Solicitud s = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);						
			BolsaEmpleoDataTable<BolsaSolicitudTable> dtBolsas = modelo.listaBolsasSolicitudesDatatable(s, params);			
			Bolsa b = dtBolsas.getData().get(0);
			BolsaEmpleoDataTable<MeritoSolicitudTable> dtMeritos = modelo.listaMeritosSolicitudDatatable(params, s.getUsuario(), b, s);
			Merito meritoSolicitud = dtMeritos.getData().get(0).getMerito();
									
			MeritoSolicitud ms = modelo.getMeritoSolicitud(s, b, meritoSolicitud);
			assertEquals(ms.getMerito(), meritoSolicitud);
			
			// creamos merito
			Merito meritoNuevo = new Merito();
			meritoNuevo.setArchivo(new ByteArrayInputStream("archivo de prueba".getBytes()));
			meritoNuevo.setDescripcion("test");
			meritoNuevo.setItemBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion().get(0));
			meritoNuevo.setObservacion("test obs");
			meritoNuevo.setUsuario(candidato1);
			meritoNuevo.setValor(1.0);
			Integer codMeritoNuevo = ModeloMerito.obtenerInstancia().insertaMerito(meritoNuevo, candidato1);
			Merito meritoLeido = ModeloMerito.obtenerInstancia().getMeritoById(codMeritoNuevo);
						
			// error merito no encontrado
			Throwable throwable = assertThrows(Throwable.class, () -> modelo.getMeritoSolicitud(s, b, meritoLeido));

			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD, throwable.getMessage());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getMeritoSolicitudByConvocatoria.
	 */
	@Test
	public void testA07getMeritoSolicitudByConvocatoria() {
		HashMap<String, String[]> params = new HashMap<>();

		params.put(BolsaEmpleoDataTable.PARAM_CURRENT_PAGE, new String[] {"0"});
		params.put(BolsaEmpleoDataTable.PARAM_PAGE_SIZE, new String[] {"10"});
		params.put(BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION, new String[] {BolsaEmpleoDataTable.PARAM_ORDER_DIRECTION_VALUE_ASC});
		
		try {
			ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
			
			List<BolsaSolicitud> listaBolsaSolicitud = ModeloSolicitud.obtenerInstancia().getBolsasSolicitudMeritos(s);
			BolsaSolicitud sb = listaBolsaSolicitud.get(0);
			Merito meritoBolsa = sb.getListaMeritos().get(0).getMerito();
			MeritoSolicitud ms = modelo.getMeritoSolicitudByConvocatoria(c, sb, meritoBolsa);
			assertEquals(ms.getMerito(), meritoBolsa);
						
			// creamos merito
			Merito meritoNuevo = new Merito();
			meritoNuevo.setArchivo(new ByteArrayInputStream("archivo de prueba".getBytes()));
			meritoNuevo.setDescripcion("test");
			meritoNuevo.setItemBaremacion(ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion().get(0));
			meritoNuevo.setObservacion("test obs");
			meritoNuevo.setUsuario(candidato1);
			meritoNuevo.setValor(1.0);
			Integer codMeritoNuevo = ModeloMerito.obtenerInstancia().insertaMerito(meritoNuevo, candidato1);
			Merito meritoLeido = ModeloMerito.obtenerInstancia().getMeritoById(codMeritoNuevo);
						
			// error merito no encontrado
			Throwable throwable = assertThrows(Throwable.class, () -> modelo.getMeritoSolicitudByConvocatoria(c, sb, meritoLeido));

			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD, throwable.getMessage());
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getBolsasSolicitud.
	 */
	@Test
	public void testA08getBolsasSolicitud() {
		try {
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			List<Bolsa> bolsas = ModeloSolicitud.obtenerInstancia().getBolsasSolicitud(s);
			assertFalse(bolsas.isEmpty());			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getBolsasSolicitudMeritos.
	 */
	@Test
	public void testA09getBolsasSolicitudMeritos() {
		try {
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			List<BolsaSolicitud> sb = ModeloSolicitud.obtenerInstancia().getBolsasSolicitudMeritos(s);
			assertFalse(sb.isEmpty());			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * getMeritosSolicitudBolsa.
	 */
	@Test
	public void testA10getMeritosSolicitudBolsa() {
		try {
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			List<BolsaSolicitud> sb = ModeloSolicitud.obtenerInstancia().getBolsasSolicitudMeritos(s);
			BolsaSolicitud bolsaSolicitud = sb.get(0);
			
			List<MeritoSolicitud> ms = ModeloSolicitud.obtenerInstancia().getMeritosSolicitudBolsa(s, bolsaSolicitud);
			assertFalse(ms.isEmpty());			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		}
	}
	
	/**
	 * asignarBolsasASolicitud.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@Test
	public void testA11asignarBolsasASolicitud() throws SQLException, UVException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
		Solicitud s = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
		
		Throwable throwable = assertThrows(Throwable.class, () -> modelo.asignarBolsasASolicitud(s, new ArrayList<Bolsa>(), candidato1));
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloSolicitud.MENSAJE_ERROR_SOLICITUD_CERRADA, throwable.getMessage());
		
		try {
			UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'ABIERTA' WHERE CODNUM = " + s.getCodNum());
			Solicitud sAbierta = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
						
			List<Bolsa> bolsas = new ArrayList<>();
			bolsas.add(getBolsaSinAdscripcion());
						
			ModeloSolicitud.obtenerInstancia().asignarBolsasASolicitud(sAbierta, bolsas, candidato1);
			List<BolsaSolicitud> sb = modelo.getBolsasSolicitudMeritos(s);
			assertEquals(sb.size(), 1);
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		} finally {
			UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'CERRADA' WHERE CODNUM = " + s.getCodNum());
		}
	}

	/**
	 * desasignarBolsasASolicitud.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	@Test
	public void testA12desasignarBolsasASolicitud() throws SQLException, UVException {
		Solicitud sAbierta = null;
		
		try {			
			ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			
			Throwable throwable = assertThrows(Throwable.class, () -> modelo.desasignarBolsasASolicitud(s, new ArrayList<Bolsa>(), candidato1));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_SOLICITUD_CERRADA, throwable.getMessage());
			
			UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'ABIERTA' WHERE CODNUM = " + s.getCodNum());
			sAbierta = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
			
			List<Bolsa> bolsas = new ArrayList<>();
			Bolsa bolsa = getBolsaSinAdscripcion(); 
			bolsas.add(bolsa);			
			modelo.desasignarBolsasASolicitud(sAbierta, bolsas, candidato1);
			
			Bolsa find = null;
			for (Bolsa b : bolsas) {
				if (b.equals(bolsa)) {
					find = b;
				}
			}
			assertEquals(find, bolsa);					
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		} finally {
			if (sAbierta != null) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'CERRADA' WHERE CODNUM = " + sAbierta.getCodNum());
			}
		}
	}
	
	/**
	 * asignarMeritosASolicitudBolsa.
	 * borrarMeritoDeSolicitudBolsa.
	 * @throws SQLException .
	 */
	@Test
	public void testA12asignarMeritosASolicitudBolsa() throws SQLException {
		Solicitud sAbierta = null;
		Bolsa bolsa = null;
		String estadoBolsa = null;  
		
		try {
			// creamos un merito no individualizado
			Merito meritoNuevo = new Merito(); 
			meritoNuevo.setArchivo(new ByteArrayInputStream("archivo de prueba".getBytes()));
			meritoNuevo.setDescripcion("test");
			meritoNuevo.setItemBaremacion(getItemBaremacionNoIdividualizado());
			meritoNuevo.setObservacion("test obs");
			meritoNuevo.setUsuario(candidato1);
			meritoNuevo.setValor(1.0);
			ModeloMerito.obtenerInstancia().insertaMerito(meritoNuevo, candidato1);
			
			// al turrón
			ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Solicitud s = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
						
			bolsa = getBolsaSinAdscripcion();
			if (!bolsa.getEstado().equals(ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA)) {
				estadoBolsa = bolsa.getEstado();
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_BOLSAS SET ESTADO = '" + ModeloBolsa.BOLSA_ESTADO_DESBLOQUEADA + "' WHERE CODNUM = " 
						+ bolsa.getCodNum());
			}
			Bolsa bolsaFinal = ModeloBolsa.obtenerInstancia().getBolsaById(bolsa.getCodNum());
			Merito meritoIndividualizado = getMeritoIndividualizadoAfinidad();
			Merito meritoNoIndividualizado = getMeritoNoIndividualizadoAfinidad();
			
			Throwable throwable = assertThrows(Throwable.class, () -> modelo.asignarMeritosASolicitudBolsa(s, bolsaFinal, meritoIndividualizado, candidato1));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_SOLICITUD_CERRADA, throwable.getMessage());
			
			UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'ABIERTA' WHERE CODNUM = " + s.getCodNum());
			sAbierta = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
			
			// asignar meritos a bolsa
			this.asignarMeritoABolsa(sAbierta, bolsa, meritoIndividualizado, candidato1);
			this.asignarMeritoABolsa(sAbierta, bolsa, meritoNoIndividualizado, candidato1);
			
			// asignamos valoraciones meritoIndividualizado
			Afinidad aIndividualizado = getAfinidadTipo(meritoIndividualizado.getItemBaremacion().getAfinidad()); 					
			modelo.asignarAfinidadMeritoIndividualizado(sAbierta, bolsa, meritoIndividualizado, aIndividualizado, candidato1);
			
			MeritoSolicitud msFindMeritoIndividualizado = null;
			for (MeritoSolicitud ms : modelo.getMeritosSolicitudBolsa(sAbierta, bolsa)) {
				if (ms.getMerito().getCodNum().equals(meritoIndividualizado.getCodNum())) {
					msFindMeritoIndividualizado = ms;
					break;
				}				
			}
			assertNotNull(msFindMeritoIndividualizado);
			assertEquals(msFindMeritoIndividualizado.getValoraciones().size(), 1);
			assertEquals(msFindMeritoIndividualizado.getValoraciones().get(0).getAfinidad(), aIndividualizado);			
			assertEquals(msFindMeritoIndividualizado.getValoraciones().get(0).getValor(), Double.valueOf(0.0)); // null
			
			// asignamos valoraciones meritoIndividualizado
			Map<Afinidad, Double> afinidadesNoInd = new HashMap<>();
			afinidadesNoInd.put(getAfinidadTipo(meritoNoIndividualizado.getItemBaremacion().getAfinidad()), meritoNoIndividualizado.getValor());
			modelo.asignarAfinidadMeritoNoIndividualizado(sAbierta, bolsa, meritoNoIndividualizado, afinidadesNoInd, candidato1);
			
			MeritoSolicitud msFindMeritoNoIndividualizado = null;
			for (MeritoSolicitud ms : modelo.getMeritosSolicitudBolsa(sAbierta, bolsa)) {
				if (ms.getMerito().getCodNum().equals(meritoNoIndividualizado.getCodNum())) {
					msFindMeritoNoIndividualizado = ms;
					break;
				}				
			}
			assertNotNull(msFindMeritoNoIndividualizado);
			assertEquals(msFindMeritoNoIndividualizado.getValoraciones().size(), 1);
			assertEquals(msFindMeritoNoIndividualizado.getValoraciones().get(0).getValor(), meritoNoIndividualizado.getValor());
			
			// eliminar meritos individualizado de bolsa
			modelo.borrarMeritoDeSolicitudBolsa(sAbierta, bolsa, meritoIndividualizado, candidato1);
			Merito meritoFind = null;
			for (MeritoSolicitud ms : modelo.getMeritosSolicitudBolsa(sAbierta, bolsa)) {
				if (ms.getMerito().getCodNum().equals(meritoIndividualizado.getCodNum())) {
					meritoFind = ms.getMerito();
					break;
				}
			}
			assertNull(meritoFind);
			
			// eliminar meritos no individualizado de bolsa
			modelo.borrarMeritoDeSolicitudBolsa(sAbierta, bolsa, meritoNoIndividualizado, candidato1);
			meritoFind = null;
			for (MeritoSolicitud ms : modelo.getMeritosSolicitudBolsa(sAbierta, bolsa)) {
				if (ms.getMerito().getCodNum().equals(meritoNoIndividualizado.getCodNum())) {
					meritoFind = ms.getMerito();
					break;
				}
			}
			assertNull(meritoFind);
			
		} catch (SQLException | UVException ex) {
			fail(String.format(MENSAJE_ERROR_HAY_EXCEPCION, ex.toString()));
		} finally {
			if (sAbierta != null) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'CERRADA' WHERE CODNUM = " + sAbierta.getCodNum());
			}
			if (estadoBolsa != null) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_BOLSAS SET ESTADO = '" + estadoBolsa + "' WHERE CODNUM = " + bolsa.getCodNum());
			}
		}
	}
	
	
	/**
	 * getMeritoSolicitudById.
	 */
	@Test
	public void testE01getMeritoSolicitudById() {
		Throwable throwable = assertThrows(Throwable.class, () -> ModeloSolicitud.obtenerInstancia().getMeritoSolicitudById(null));
		assertEquals(UVException.class, throwable.getClass());
		assertEquals(ModeloSolicitud.MENSAJE_ERROR_NO_EXISTE_MERITO_SOLICITUD, throwable.getMessage());
	}
	
	/**
	 * nuevaSolicitud.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	public void testE02nuevaSolicitudConvocatoriaNoAbierta() throws SQLException, UVException {
		Convocatoria c = null;
		boolean abrirConvocatoria = false;
		
		try {
			c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			
			if (c.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_ABIERTA)) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = 'CERRADA' WHERE CODNUM = " + c.getCodNum());
				abrirConvocatoria = true;
			}
					
			Convocatoria cerrada = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(c.getCodNum());
			
			Throwable throwable = assertThrows(Throwable.class, () -> ModeloSolicitud.obtenerInstancia().nuevaSolicitud(candidato1, cerrada, candidato1));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_CONVOCATORIA_NO_ABIERTA, throwable.getMessage());					
		} finally {
			if (c != null && abrirConvocatoria) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = 'ABIERTA' WHERE CODNUM = " + c.getCodNum());	
			}				
		}
	}
	
	/**
	 * nuevaSolicitud.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	public void testE03nuevaSolicitudYaTienesSolicitud() throws SQLException, UVException {
		Convocatoria c = null;
		boolean cerrarConvocatoria = false;
						
		boolean cerrarSolicitud = false;
		Solicitud solicitud = null;
				
		try {
			c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
					
			if (c.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = 'ABIERTA' WHERE CODNUM = " + c.getCodNum());
				cerrarConvocatoria = true;
			}
			
			solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			if (solicitud.getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_CERRADA)) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'ABIERTA' WHERE CODNUM = " + solicitud.getCodNum());
				cerrarSolicitud = true;
			}
			
			Convocatoria abierta = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(c.getCodNum());
			Throwable throwable = assertThrows(Throwable.class, () -> ModeloSolicitud.obtenerInstancia().nuevaSolicitud(candidato1, abierta, candidato1));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_SOLICITUDES_ABIERTAS, throwable.getMessage());
		} finally {
			if (c != null && cerrarConvocatoria) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = 'CERRADA' WHERE CODNUM = " + c.getCodNum());	
			}
			
			if (solicitud != null && cerrarSolicitud) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'CERRADA' WHERE CODNUM = " + solicitud.getCodNum());
			}
		}
	}
	
	/**
	 * nuevaSolicitud.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	public void testE04yaHaySolicitudesAbiertas() throws SQLException, UVException {
		Convocatoria c = null;
		Solicitud solicitud = null;
		boolean cerrarConvocatoria = false;
		boolean abrirSolicitud = false;
		
		try {
			c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			
			if (c.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_CERRADA)) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = 'ABIERTA' WHERE CODNUM = " + c.getCodNum());
				cerrarConvocatoria = true;
			}
			
			solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato1, c);
			if (solicitud.getEstado().equals(ModeloSolicitud.SOLICITUD_ESTADO_ABIERTA)) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'CERRADA' WHERE CODNUM = " + solicitud.getCodNum());
				abrirSolicitud = true;
			}
			
			Convocatoria abierta = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(c.getCodNum());
			
			Throwable throwable = assertThrows(Throwable.class, () -> ModeloSolicitud.obtenerInstancia().nuevaSolicitud(candidato1, abierta, candidato1));
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_YA_TIENES_SOLICITUD, throwable.getMessage());
		} finally {
			if (c != null && cerrarConvocatoria) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_CONVOCATORIAS SET ESTADO = 'CERRADA' WHERE CODNUM = " + c.getCodNum());	
			}
			if (solicitud != null && abrirSolicitud) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'ABIERTA' WHERE CODNUM = " + solicitud.getCodNum());
			}
		}
	}
	
	/**
	 * nuevaSolicitud.
	 * @throws UVException .
	 * @throws SQLException .
	 */
	@Test
	public void testE05asignarAfinidadMeritoIndividualizado() throws SQLException, UVException {
		Solicitud s = null;
		
		try {
			ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
			
			Convocatoria c = ModeloConvocatoria.obtenerInstancia().getUltimaConvocatoria();
			Afinidad aIndividualizado = getAfinidadTipo("AA");
			Merito meritoIndividualizado = getMeritoIndividualizadoAfinidad();
			Bolsa bolsa = getBolsaSinAdscripcion();
			s = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
			UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'ABIERTA' WHERE CODNUM = " + s.getCodNum());
			Solicitud sNew = modelo.getSolicitudByConvocatoriaUsuario(candidato1, c);
						
			Throwable throwable = assertThrows(Throwable.class, () -> 
				modelo.asignarAfinidadMeritoIndividualizado(sNew, bolsa, meritoIndividualizado, aIndividualizado, candidato1));
			
			assertEquals(UVException.class, throwable.getClass());
			assertEquals(ModeloSolicitud.MENSAJE_ERROR_TIPO_AFINIDAD, throwable.getMessage());			
		} finally {
			if (s != null) {
				UtilsTestBolsaEmpleo.sqlExecute("UPDATE TBEP_SOLICITUDES SET ESTADO = 'CERRADA' WHERE CODNUM = " + s.getCodNum());	
			}
		}
	}
	
	private Bolsa getBolsaSinAdscripcion() throws SQLException, UVException {
		// buscamos bolsa para asignarla a la solicitud
		Bolsa bolsaSinAdscripcion = null;
		for (Bolsa b : ModeloBolsa.obtenerInstancia().getBolsas()) {
			// ASIGNATURAS SIN ADSCRIPCIÓN
			if (b.getArea().getIdAreaExterno().equals("000")) { 
				bolsaSinAdscripcion = b;
				break;
			}				
		}
		assertNotNull(bolsaSinAdscripcion);
		return bolsaSinAdscripcion;
	}
	
	private Merito getMeritoIndividualizadoAfinidad() throws SQLException, UVException {
		return getMeritoAfinidad(true);
	}
	
	private Merito getMeritoNoIndividualizadoAfinidad() throws SQLException, UVException {
		return getMeritoAfinidad(false);
	}
	
	private Merito getMeritoAfinidad(boolean individualizado) throws SQLException, UVException {
		for (Merito merito : ModeloMerito.obtenerInstancia().listaMeritos()) {
			if (merito.getItemBaremacion().getIndividualizado().equals(individualizado) && merito.getItemBaremacion().getAfinidad() != null) {
				return merito;
			}
		}
		fail("Merito con afinidad no encontrado");
		return null;
	}

	private Afinidad getAfinidadTipo(String tipo) throws SQLException {
		for (Afinidad a : ModeloAfinidad.obtenerInstancia().listaAfinidades()) {
			if (a.getCodigo().equals(tipo)) {
				return a;
			}
		}
		fail("Afinidad no encontrada");
		return null;
	}

	private void asignarMeritoABolsa(Solicitud s, Bolsa bolsa, Merito m, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		ModeloSolicitud modelo = ModeloSolicitud.obtenerInstancia();
		
		modelo.asignarMeritosASolicitudBolsa(s, bolsa, m, usuario);
		Merito meritoFind = null;
		for (MeritoSolicitud ms : modelo.getMeritosSolicitudBolsa(s, bolsa)) {
			if (ms.getMerito().getCodNum().equals(m.getCodNum())) {
				meritoFind = ms.getMerito();
				break;
			}				
		}
		assertNotNull(meritoFind);
	}
	
	private ItemBaremacion getItemBaremacionNoIdividualizado() throws SQLException, UVException {
		for (ItemBaremacion ib : ModeloBaremacionItems.obtenerInstancia().listaItemBaremacion()) {
			if (ib.getAfinidad() != null && ib.getIndividualizado().equals(false)) {
				return ib;
			}
		}
		fail("Item no encontrado");
		return null;
	}
}
