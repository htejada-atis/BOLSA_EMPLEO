package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ParametrosConfiguracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para los resultados de las solicitudes .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloResultados {
	private static final String NOMBREDEESTACLASE = ModeloResultados.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	
	public static final int ORDER_COLUMN_INDEX_NIF_CANDIDATOS = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_CANDIDATOS = 1;
	public static final int ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS = 2;
	
	public static final String MERITOS_VALIDADOS = "Méritos evaluados";
	public static final String MERITOS_EXCLUIDOS = "Méritos excluidos";
	public static final String MERITOS_NO_EVALUADOS = "Méritos no evaluados";
	
	public static final String MENSAJE_SIN_MERITOS_EVALUADOS = "No hay méritos evaluados";
	public static final String MENSAJE_SIN_MERITOS_EXCLUIDOS = "No hay méritos excluidos";
	public static final String MENSAJE_SIN_MERITOS_NO_EVALUADOS = "No hay méritos no evaluados";
	public static final String MENSAJE_ERROR_BOLSA_NULL = "Bolsa no puede estar vacía";
	public static final String MENSAJE_ERROR_MERITO_NULL = "Mérito no puede estar vacío";
	
	public static final String ACREDITACIONES_VALIDADAS = "ACREDITACIONES_VALIDADAS";
	public static final String BEPBOL_CODNUM = "BEPBOL_CODNUM";
	public static final String BEPITE_CODNUM = "BEPITE_CODNUM";
	public static final String BEPMER_CODNUM = "BEPMER_CODNUM";
	public static final String BEPSOL_CODNUM = "BEPSOL_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String CODNUM_MERITO_SOLICITUD = "CODNUM_MERITO_SOLICITUD";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String DESGLOSE = "DESGLOSE";
	public static final String DESGLOSEDESCRIPCION = "DESGLOSEDESCRIPCION";
	public static final String DESGLOSETOTAL = "DESGLOSETOTAL";
	public static final String FACTOR = "FACTOR";
	public static final String FLGEXCLUIDO = "FLGEXCLUIDO";
	public static final String FLGVALIDADO = "FLGVALIDADO";
	public static final String ITEM_SBM = "ITEM_SBM";
	public static final String OBSERVACION = "OBSERVACION";
	public static final String OBSERVACION_CANDIDATO = "OBSERVACION_CANDIDATO";
	public static final String RESULTADO = "RESULTADO";
	public static final String TITULACIONES_VALIDADAS = "TITULACIONES_VALIDADAS";
	public static final String TOTAL = "TOTAL";
	public static final String TOTALSINAPLICAR = "TOTALSINAPLICAR";
	public static final String VALOR = "VALOR";
	public static final String VALOR_MERITO_SOLICITUD = "VALOR_MERITO_SOLICITUD";
	
	public static final String MERITO_EXCLUIDO = "S";
	public static final String MERITO_VALIDADO = "S";
	
		
	protected static ModeloResultados eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloResultados();
		}
	}

    /**
     * Obtiene una instancia de la conexión .
     * @return instancia .
     */
	public static ModeloResultados obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
        }
		return eInstancia;
    }
	
	/** Calculo total de una solicitud para un área .
	 * @param solicitud .
	 * @param bolsa .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public void calcularSolicitud(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		Double totalSinAplicar = 0.0;
		
		LOGGER.log(Level.FINER, String.format("CALCULANDO SOLICITUD [%d] [%d]", solicitud.getCodNum(), solicitud.getUsuario().getCodNum()));
				
		List<TitulacionUsuario> listaTitulaciones = ModeloTitulacion.obtenerInstancia().listaTitulacionesPreferentesAlArea(solicitud.getUsuario(), bolsa);
		List<MeritoPreferenteUsuario> listaAcreditaciones = ModeloMeritosPreferentesCandidato.obtenerInstancia().
				listaMeritosPreferentesValidadosUsuarioPorPosesion(solicitud.getUsuario());
		
		HashMap<String, Map.Entry<MeritoPreferente, Double>> preferentes = calcularMeritosPreferentes(solicitud, bolsa, listaTitulaciones);
		
		List<MeritoResultado> meritos = this.getMeritosSolicitudBolsaValidados(solicitud, bolsa);
		for (MeritoResultado merito: meritos) {
			totalSinAplicar += calcularMerito(merito, preferentes.containsKey(ModeloMeritosPreferentes.TIPO_MERITO) 
					? preferentes.get(ModeloMeritosPreferentes.TIPO_MERITO) : null);
		}
		
		Double total = totalSinAplicar;
		String desgloseTotal = null;
		String desgloseDescripcion = null;
		
		for (String tipoPreferente: preferentes.keySet()) {
			if (!tipoPreferente.equals(ModeloMeritosPreferentes.TIPO_MERITO)) {
				total *= preferentes.get(tipoPreferente).getValue();
				desgloseTotal = (desgloseTotal == null ? BolsaEmpleoUtils.formatoPuntuacion(totalSinAplicar).toString() : desgloseTotal)
						+ " * " + preferentes.get(tipoPreferente).getValue();
				desgloseDescripcion = (desgloseDescripcion == null ? "Total sin aplicar" : desgloseDescripcion) + " * "
						+ (tipoPreferente.equals(ModeloMeritosPreferentes.TIPO_POSESION) ? "Factor acreditación" : "Factor titulación");
			}
		}
		
		BolsaResultado bol = new BolsaResultado(bolsa, desgloseTotal, desgloseDescripcion, BolsaEmpleoUtils.formatoPuntuacion(total),
				BolsaEmpleoUtils.formatoPuntuacion(totalSinAplicar));
		bol.setListaMeritos(this.getMeritosSolicitudBolsaValidados(solicitud, bolsa));
		bol.setListaMeritosExcluidos(this.getMeritosSolicitudBolsaExcluidos(solicitud, bolsa));
		bol.setListaMeritosNoEvaluados(this.getMeritosSolicitudBolsaNoEvaluados(solicitud, bolsa));
		
		Date fechaActual = new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
		guardarResultadoSolicitudBolsa(bol, solicitud, null, fechaActual, listaTitulaciones, listaAcreditaciones);
	}
	
	/** Calculo total de un mérito en una solicitud para un área .
	 * @param merito .
	 * @param preferente .
	 * @return resultado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:ExecutableStatementCount"})
	private Double calcularMerito(MeritoResultado merito, Map.Entry<MeritoPreferente, Double> preferente) throws SQLException, UVException {
		Double puntuacion = 0.0;
		String desglose = "";
		
		ItemBaremacion item = merito.getItemMeritoSolicitud() != null ? merito.getItemMeritoSolicitud() : merito.getItemBaremacion();
		
		boolean tieneAfinidad = item.getAfinidad() != null;
		
		Double pesoCategoria = item.getValor();
		Double pesoBloque = item.getBloqueBaremacion().getApartadoBaremacion().getPorcentajeMaximo();
		
		if (!item.getIndividualizado() && tieneAfinidad) {
			desglose = "(D) (";
			for (int i = 0; i < merito.getValoraciones().size(); i++) {
				Double valor = merito.getValoraciones().get(i).getValor();
				Double afinidad = merito.getValoraciones().get(i).getAfinidad().getModulacion();
				puntuacion += valor * afinidad;
				desglose += BolsaEmpleoUtils.formatoPuntuacion(valor) + " * " + BolsaEmpleoUtils.formatoPuntuacion(afinidad);
				desglose += i < merito.getValoraciones().size() - 1 ? " + " : "";
			}
			desglose += ")" + " * " + pesoCategoria + " * " + pesoBloque;
			puntuacion *= pesoCategoria * pesoBloque;
		} else {
			this.checkValoracionesCorrectas(merito, tieneAfinidad);
			
			Double valor = merito.getValorMeritoSolicitud() != null && merito.getValorMeritoSolicitud() != 0 ? merito.getValorMeritoSolicitud() : merito.getValor();
			Double afinidad = tieneAfinidad ? merito.getValoraciones().get(0).getAfinidad().getModulacion() : 1.0;
			
			desglose = BolsaEmpleoUtils.formatoPuntuacion(valor) + " * " + afinidad + " * " + pesoCategoria + " * " + pesoBloque;
			puntuacion = valor * afinidad * pesoCategoria * pesoBloque;
		}
		
		if (preferente != null) {
			switch (preferente.getKey().getAplicable()) {
				case ModeloMeritosPreferentes.APLICABLE_APARTADO:
					if (item.getBloqueBaremacion().getApartadoBaremacion().getCodNum().equals(
							preferente.getKey().getAplicableApartadoBaremacion().getCodNum())) {
						desglose = desglose + " * (" + preferente.getKey().getPrefijoInforme() + ") " + preferente.getValue();
						puntuacion *= preferente.getValue();
					}
					break;
				case ModeloMeritosPreferentes.APLICABLE_BLOQUE:
					if (item.getBloqueBaremacion().getCodNum().equals(
							preferente.getKey().getAplicableBloqueBaremacion().getCodNum())) {
						desglose = desglose + " * (" + preferente.getKey().getPrefijoInforme() + ") " + preferente.getValue();
						puntuacion *= preferente.getValue();
					}
					break;
				case ModeloMeritosPreferentes.APLICABLE_ITEM:
					if (item.getCodNum().equals(
							preferente.getKey().getAplicableItemBaremacion().getCodNum())) {
						desglose = desglose + " * (" + preferente.getKey().getPrefijoInforme() + ") " + preferente.getValue();
						puntuacion *= preferente.getValue();
					}
					break;
				default:
					throw new UVException("Campo aplicable no contemplado " + preferente.getKey().getAplicable());
			}
		}
		
		merito.setResultado(puntuacion);
		merito.setDesglose(desglose);
		
		this.guardarResultadoSolicitudBolsaMerito(merito, null);
		
		return puntuacion;
	}
	
	/**
	 * Comprueba si el mérito en la solicitud tiene valoraciones. Aplicable a mérito individualizados con afinidad.
	 * @param merito .
	 * @param tieneAfinidad .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private void checkValoracionesCorrectas(MeritoResultado merito, boolean tieneAfinidad) throws SQLException, UVException {
		if (tieneAfinidad && merito.getValoraciones().isEmpty()) {
			Bolsa bolsa = null;
			
			String sql = ""
					+ " SELECT bepsbo.BEPBOL_CODNUM "
					+ " FROM TBEP_SOL_BOL_MERITOS bepsbm "
					+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM "
					+ " WHERE bepsbm.CODNUM = ? ";
			
			try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(sql)) {
				stmt.setInt(1, merito.getCodNumMeritoSolicitud());
				try (ResultSet rs = stmt.executeQuery()) {
					if (!rs.next()) {
						throw new UVException("Solicitud bolsa mérito no encontrada " + merito.getCodNumMeritoSolicitud());
					}
					
					bolsa = ModeloBolsa.obtenerInstancia().getBolsaById(rs.getInt("BEPBOL_CODNUM"));						
				}
			}
			
			String err = String.format("Mérito sin valoraciones. Bolsa: [%s] Usuario: [%d - %s - %s] Mérito: [%d]",
					bolsa.getArea().getDescripcion(),
					merito.getUsuario().getCodNum(),
					merito.getUsuario().getPrsNif(),
					merito.getUsuario().getNombre() + " " + merito.getUsuario().getPrimerApellido() + merito.getUsuario().getSegundoApellido(),   
					merito.getCodNum()
			);
			LOGGER.log(Level.FINER, err);
			throw new UVException(err);
		}
	}
	
	/** Calculo de factores de méritos preferentes .
	 * @param solicitud .
	 * @param bolsa .
	 * @param listaTitulaciones .
	 * @return colección de factores a aplicar con el tipo y el factor .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	private HashMap<String, Map.Entry<MeritoPreferente, Double>> calcularMeritosPreferentes(Solicitud solicitud, Bolsa bolsa,
			List<TitulacionUsuario> listaTitulaciones) throws SQLException, UVException {
		HashMap<String, Map.Entry<MeritoPreferente, Double>> preferentes = new HashMap<>();
		
		List<MeritoPreferente> meritosPreferentes = ModeloMeritosPreferentes.obtenerInstancia().listaMeritosPreferentesActivos();
		
		for (MeritoPreferente preferente: meritosPreferentes) {
			Double factor = null;
			
			switch (preferente.getTipo()) {
				case ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE:
					if (listaTitulaciones.size() > 0) {
						factor = preferente.getFactor();
					}
					break;
				case ModeloMeritosPreferentes.TIPO_POSESION:
					factor = compruebaAcreditaciones(solicitud.getUsuario());
					break;
				case ModeloMeritosPreferentes.TIPO_MERITO:
					if (compruebaMeritoPreferente(preferente, solicitud.getUsuario(), bolsa)) {
						factor = preferente.getFactor();
					}
					break;
				default:
					throw new UVException("Tipo de mérito preferentes no contemplado " + preferente.getTipo());
			}
			
			if (factor != null) {
				preferentes.put(preferente.getTipo(), new AbstractMap.SimpleEntry<>(preferente, factor));
			}
		}
		
		return preferentes;
	}
	
	/** Lista de solicitudes asociadas a una bolsa .
	 * @param bolsa .
	 * @param convocatoria .
	 * @return solicitudes .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public List<Solicitud> listaSolicitudesBolsa(Bolsa bolsa, Convocatoria convocatoria) throws SQLException, UVException {
		List<Solicitud> listaSolicitudes = new ArrayList<>();
		
		String consulta = "SELECT bepsol.* FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "	WHERE bepsol.BEPCON_CODNUM = ? AND bepsob.BEPBOL_CODNUM = ? AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					listaSolicitudes.add(ModeloSolicitud.obtenerInstancia().createSolicitudFromResultSet(rs, false));
				}
			}
		}
		
		return listaSolicitudes;
	}
	
	/** Lista de resultados de un candidato en un área .
	 * @param bolsa .
	 * @param convocatoria .
	 * @param params .
	 * @return datatable de resultados .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<CandidatoResultadoTable> listadoResultadosCandidatosArea(Bolsa bolsa, Convocatoria convocatoria, Map<String, String[]> params)
			throws SQLException, UVException {
		List<CandidatoResultadoTable> rows = new ArrayList<>();
		BolsaEmpleoDataTable<CandidatoResultadoTable> dataTable = new BolsaEmpleoDataTable<>(params);
		
		if (convocatoria == null) {
			dataTable.setData(rows);
			return dataTable;
		}
		
		String consulta = "SELECT bepusu.*, bepsob.TOTAL"
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " WHERE bepsol.BEPCON_CODNUM = ?"
				+ "     AND bepsob.BEPBOL_CODNUM = ?"
				+ "     AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'"
				+ "     AND bepusu.FLGBORRADO = 'N'";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NIF_CANDIDATOS, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATOS, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS, "bepsob.TOTAL", DataTableColumn.COLUMN_TYPE_DOUBLE);

		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmtCount.setInt(paramIndex, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmtCount.setInt(paramIndex, bolsa.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					CandidatoResultadoTable candidato = new CandidatoResultadoTable(
							ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(CODNUM)));
					candidato.setTotal(rs.getDouble(TOTAL));
					rows.add(candidato);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}

		return dataTable;
	}
	
	/**
	 * Devuelve el listado de candidatos resultados para exportar en csv.
	 * @param bolsa .
	 * @param convocatoria .
	 * @param separator .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<String[]> listadoResultadosCandidatosAreaCsv(Bolsa bolsa, Convocatoria convocatoria, String separator) throws SQLException, UVException {
		String consulta = "SELECT bepusu.*, bepsob.TOTAL"
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " WHERE bepsol.BEPCON_CODNUM = ?"
				+ "     AND bepsob.BEPBOL_CODNUM = ?"
				+ "     AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'"
				+ "     AND bepusu.FLGBORRADO = 'N'";
		
		List<String[]> rows = new ArrayList<>();
		
		rows.add(new String[] {"NIF", "NOMBRE", "EMAIL", "TOTAL"});
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(CODNUM));

					String nif = BolsaEmpleoUtils.string2csv(usuario.getPrsNif(), separator);
					String nombre = BolsaEmpleoUtils.string2csv(
							String.format("\"%s %s %s\"", usuario.getNombre() != null ? usuario.getNombre() : "",
									usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : "",
									usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : ""), separator);
					String email = BolsaEmpleoUtils.string2csv(usuario.getEmail(), separator);
					String total = BolsaEmpleoUtils.string2csv(String.valueOf(rs.getDouble(TOTAL)), separator);

					rows.add(new String[] {nif, nombre, email, total});
				}
			}
		}
		
		return rows;
	}
	
	/**
	 * Devuelve el listado de candidatos y areas y sus resultados resultados para exportar en csv.
	 * @param convocatoria .
	 * @param separator .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<String[]> listadoResultadosCandidatosConvocatoriaCsv(Convocatoria convocatoria, String separator) throws SQLException, UVException {
		String consulta = ""
				+ " SELECT bepare.ID_AREA_CONOCIMIENTO, bepare.DES_AREA_CONOCIMIENTO, bepusu.*, bepsob.TOTAL"
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsob.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " WHERE bepsol.BEPCON_CODNUM = ?"
				+ "     AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'"
				+ "     AND bepusu.FLGBORRADO = 'N'";
		
		List<String[]> rows = new ArrayList<>();
		
		rows.add(new String[] {"COD. AREA", "AREA", "NIF", "NOMBRE", "EMAIL", "TOTAL"});
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(CODNUM));
					
					String idArea = BolsaEmpleoUtils.string2csv(rs.getString("ID_AREA_CONOCIMIENTO"), separator);
					String area = BolsaEmpleoUtils.string2csv(rs.getString("DES_AREA_CONOCIMIENTO"), separator);
					String nif = BolsaEmpleoUtils.string2csv(usuario.getPrsNif(), separator);
					String nombre = BolsaEmpleoUtils.string2csv(
							String.format("%s %s %s", usuario.getNombre() != null ? usuario.getNombre() : "",
									usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : "",
									usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : ""), separator);
					String email = BolsaEmpleoUtils.string2csv(usuario.getEmail(), separator);
					String total = BolsaEmpleoUtils.string2csv(String.valueOf(rs.getDouble(TOTAL)), separator);
					
					rows.add(new String[] {idArea, area, nif, nombre, email, total});
				}
			}
		}
		
		return rows;
	}
	
	/** Devuelve los méritos validados con valoraciones de la bolsa en una solicitud .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoResultado> getMeritosSolicitudBolsaValidados(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoResultado> meritos = new ArrayList<>();
		
		String consulta = this.getMeritosSolicitudBolsaQueryBase()
				+ " AND bepsbm.FLGVALIDADO = 'S'"
				+ " AND bepsbm.FLGEXCLUIDO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					meritos.add(this.createMeritoResultadoFromResultset(rs));
				}
			}
		}
		
		return meritos;
	}
	
	/** Devuelve los méritos con valoraciones de la bolsa en una solicitud que están excluidos .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoResultado> getMeritosSolicitudBolsaExcluidos(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoResultado> meritos = new ArrayList<>();
		
		String consulta = this.getMeritosSolicitudBolsaQueryBase()
				+ " AND bepsbm.FLGVALIDADO = 'N'"
				+ " AND bepsbm.FLGEXCLUIDO = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					meritos.add(this.createMeritoResultadoFromResultset(rs));
				}
			}
		}
		
		return meritos;
	}
	
	/** Devuelve los méritos con valoraciones de la bolsa en una solicitud sin evaluar .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoResultado> getMeritosSolicitudBolsaNoEvaluados(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoResultado> meritos = new ArrayList<>();
		
		String consulta = this.getMeritosSolicitudBolsaQueryBase()
				+ " AND bepsbm.FLGVALIDADO = 'N'"
				+ "AND bepsbm.FLGEXCLUIDO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					meritos.add(this.createMeritoResultadoFromResultset(rs));
				}
			}
		}
		
		return meritos;
	}
	
	/**
	 * Devuelve al consutla base para .
	 * 	- getMeritosSolicitudBolsaValidados
	 *  - getMeritosSolicitudBolsaExcluidos
	 *  - getMeritosSolicitudBolsaNoEvaluados
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	private String getMeritosSolicitudBolsaQueryBase() {
		return ""
				+ "	SELECT bepmer.CODNUM, bepmer.BEPITE_CODNUM AS BEPITE_CODNUM, bepmer.VALOR, bepmer.DESCRIPCION, bepmer.OBSERVACION"
				+ "     , bepsbm.CODNUM AS " + CODNUM_MERITO_SOLICITUD + ", bepsbm.VALOR AS " + VALOR_MERITO_SOLICITUD
				+ "     , bepsbm.OBSERVACION_CANDIDATO, bepsbm.RESULTADO, bepsbm.DESGLOSE, bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO"
				+ "     , bepsbm.BEPITE_CODNUM AS ITEM_SBM"
				+ "     , bepsol.BEPUSU_CODNUM AS USUARIO_CODNUM"
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM  = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ " WHERE "
				+ "         bepsbo.BEPBOL_CODNUM = ?"
				+ "     AND bepsbo.BEPSOL_CODNUM = ?";
	}
	
	/** 
	 * Devuelve la bolsa con los resultados de la solicitud de un candidato para esa bolsa .
	 * @param bolsa .
	 * @param candidato .
	 * @param convocatoria .
	 * @return resultado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaResultado getBolsaResultado(Bolsa bolsa, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria) throws SQLException, UVException {
		
		String consulta = "SELECT bepsob.BEPBOL_CODNUM, bepsob.TOTAL, bepsob.TOTALSINAPLICAR, bepsob.DESGLOSETOTAL, "
				+ "		bepsob.DESGLOSEDESCRIPCION, bepsob.FECHABAREMACION, bepsol.CODNUM AS BEPSOL_CODNUM,"
				+ "		bepsob.ACREDITACIONES_VALIDADAS, bepsob.TITULACIONES_VALIDADAS"
				+ " FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "	WHERE 	bepsol.BEPCON_CODNUM = ?"
				+ "		AND bepsob.BEPBOL_CODNUM = ?"
				+ "		AND bepsol.ESTADO = ?"
				+ "		AND bepsol.BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, convocatoria.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setString(indexParam++, ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
			stmt.setInt(indexParam++, candidato.getCodNum());
			
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe la solicitud para la bolsa");
				}
				
				return this.createBolsaResultadoFromResultset(rs);
			}
		}
	}
	
	/** Comprueba si el candidato tiene alguna acreditación validada .
	 * @param candidato .
	 * @return factor de la acreditación .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Double compruebaAcreditaciones(UsuarioBolsaEmpleo candidato) throws SQLException {
		String consulta = "SELECT bepmpo.FACTOR"
				+ "	FROM TBEP_MER_PRE_USUARIO bepmpu"
				+ "	INNER JOIN TBEP_MERITOS_PREFERENTES bepmep ON bepmep.CODNUM = bepmpu.BEPMEP_CODNUM"
				+ "	INNER JOIN TBEP_MER_PRE_OPCIONES bepmpo ON bepmpo.CODNUM = bepmpu.BEPMPO_CODNUM"
				+ "	WHERE 	bepmpu.BEPUSU_CODNUM = ?"
				+ "		AND bepmpu.FLGVALIDADO = 'S'"
				+ "		AND bepmpu.FLGBORRADO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, candidato.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next() ? rs.getDouble(FACTOR) : null;
			}
		}
	}
	
	/** Comprueba si el candidato tiene un mérito preferente validado en una bolsa .
	 * @param meritoPreferente .
	 * @param candidato .
	 * @param bolsa .
	 * @return si tiene mérito preferente o no .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public boolean compruebaMeritoPreferente(MeritoPreferente meritoPreferente, UsuarioBolsaEmpleo candidato, Bolsa bolsa) throws SQLException {
		String consulta = "SELECT bepmer.CODNUM"
				+ "	FROM TBEP_SOLICITUDES bepsol"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM"
				+ "	INNER JOIN TBEP_SOL_BOL_MERITOS bepsbm ON bepsbm.BEPSBO_CODNUM = bepsob.CODNUM"
				+ "	INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	WHERE 	bepsol.BEPUSU_CODNUM = ?"
				+ "		AND bepmer.BEPITE_CODNUM = ? "
				+ "		AND (CASE WHEN bepsbm.VALOR IS NOT NULL THEN bepsbm.VALOR ELSE bepmer.VALOR END) >= ? "
				+ "		AND bepsbm.FLGVALIDADO = 'S' "
				+ "		AND bepsbm.FLGEXCLUIDO = 'N'"
				+ "		AND bepsob.BEPBOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, candidato.getCodNum());
			stmt.setInt(indexParam++, meritoPreferente.getTipoItemBaremacion().getCodNum());
			stmt.setDouble(indexParam++, meritoPreferente.getBase());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	/** Guardar resultado de la solicitud para una bolsa .
	 * @param bolsa .
	 * @param solicitud .
	 * @param usuarioUpdate .
	 * @param fechaActual .
	 * @param listaTitulaciones .
	 * @param listaAcreditaciones .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void guardarResultadoSolicitudBolsa(BolsaResultado bolsa, Solicitud solicitud, UsuarioBolsaEmpleo usuarioUpdate, Date fechaActual,
			List<TitulacionUsuario> listaTitulaciones, List<MeritoPreferenteUsuario> listaAcreditaciones) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_SOLICITUD_BOLSAS"
				+ "     SET TOTAL = ?, TOTALSINAPLICAR = ?, DESGLOSETOTAL = ?, DESGLOSEDESCRIPCION = ?,"
				+ "     FECHABAREMACION = ?, UID_USUARIO = ?, TITULACIONES_VALIDADAS = ?, ACREDITACIONES_VALIDADAS = ?"
				+ " WHERE BEPBOL_CODNUM = ? AND BEPSOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDouble(indexParam++, bolsa.getTotal());
			stmt.setDouble(indexParam++, bolsa.getTotalSinAplicar());
			stmt.setString(indexParam++, bolsa.getDesgloseTotal());
			stmt.setString(indexParam++, bolsa.getDesgloseDescripcion());
			stmt.setDate(indexParam++, fechaActual);
			stmt.setString(indexParam++, usuarioUp);
			
			Clob clobTitulaciones = conexion.createClob();
			String titulaciones = "";
			for (TitulacionUsuario titulacion: listaTitulaciones) {
				titulaciones += titulacion.getCodNum() + " (Id) - " + (titulacion.getTitulacion() != null ? titulacion.getTitulacion().getNombre()
							: "Otra titulación: " + titulacion.getOtraTitulacion()) + "\n";
			}
			clobTitulaciones.setString(1, titulaciones);
			stmt.setClob(indexParam++, clobTitulaciones);
			
			Clob clobAcreditaciones = conexion.createClob();
			ParametrosConfiguracion config = ModeloParametrosConfiguracion.obtenerInstancia().getParametroByNombre("bolsaempleo.local.codMeritoPreferente");
			String acreditaciones = "";
			for (MeritoPreferenteUsuario acreditacion: listaAcreditaciones) {
				acreditaciones += acreditacion.getCodNum() + " (Id) - " + config.getValor() 
					+ "." + acreditacion.getMeritoPreferente().getCodigo() + " " + acreditacion.getMeritoPreferente().getNombre() + " " 
					+ (acreditacion.getMeritoPreferenteOpcion() != null ? acreditacion.getMeritoPreferenteOpcion().getNombre() : "") + "\n";
			}
			clobAcreditaciones.setString(1, acreditaciones);
			stmt.setClob(indexParam++, clobAcreditaciones);
			
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Guardar resultado de un mérito en una solicitud para una bolsa .
	 * @param merito .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void guardarResultadoSolicitudBolsaMerito(MeritoResultado merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException(MENSAJE_ERROR_MERITO_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_SOL_BOL_MERITOS"
				+ "	SET RESULTADO = ?, DESGLOSE = ?, UID_USUARIO = ?"
				+ "	WHERE BEPMER_CODNUM = ? AND CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDouble(indexParam++, merito.getResultado());
			stmt.setString(indexParam++, merito.getDesglose());
			stmt.setString(indexParam++, usuarioUp);
			stmt.setInt(indexParam++, merito.getCodNum());
			stmt.setInt(indexParam++, merito.getCodNumMeritoSolicitud());
			stmt.executeUpdate();
		}
	}
	
	/** Crea un mérito resultado a partir de un resultset .
	 * @param rs .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public MeritoResultado createMeritoResultadoFromResultset(ResultSet rs) throws SQLException, UVException {
		MeritoResultado merito = new MeritoResultado();
		
		// valores del mérito
		merito.setCodNum(rs.getInt(CODNUM));
		merito.setItemBaremacion(ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt(BEPITE_CODNUM)));
		merito.setValor(rs.getDouble(VALOR));
		merito.setDescripcion(rs.getString(DESCRIPCION));
		merito.setObservacion(rs.getString(OBSERVACION));
		
		// valores del mérito en la solicitud
		Integer idMeritoSolicitud = rs.getInt(CODNUM_MERITO_SOLICITUD);
		merito.setCodNumMeritoSolicitud(idMeritoSolicitud);
		merito.setValoraciones(ModeloSolicitud.obtenerInstancia().getValoracionesMeritoSolicitud(idMeritoSolicitud, false));
		merito.setObservacionCandidato(rs.getString(OBSERVACION_CANDIDATO));
		merito.setDesglose(rs.getString(DESGLOSE));
		merito.setValorMeritoSolicitud(rs.getDouble(VALOR_MERITO_SOLICITUD));
		merito.setResultado(rs.getDouble(RESULTADO));
		merito.setExcluido(rs.getString(FLGEXCLUIDO).equals(MERITO_EXCLUIDO));
		merito.setValidado(rs.getString(FLGVALIDADO).equals(MERITO_VALIDADO));
		merito.setItemMeritoSolicitud(rs.getInt(ITEM_SBM) != 0 ? ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt(ITEM_SBM)) : null);
		
		// usuario del mérito
		merito.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("USUARIO_CODNUM")));
		
		return merito;
	}
	
	/** Crea una bolsa resultado a partir de un resultset .
	 * @param rs .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaResultado createBolsaResultadoFromResultset(ResultSet rs) throws SQLException, UVException {
		Bolsa bol = ModeloBolsa.obtenerInstancia().getBolsaById(rs.getInt(BEPBOL_CODNUM));
		String desgloseTotal = rs.getString(DESGLOSETOTAL);
		String desgloseDescripcion = rs.getString(DESGLOSEDESCRIPCION);
		Double total = rs.getDouble(TOTAL);
		Double totalSinAplicar = rs.getDouble(TOTALSINAPLICAR);
		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudById(rs.getInt(BEPSOL_CODNUM));
		List<MeritoResultado> meritos = this.getMeritosSolicitudBolsaValidados(solicitud, bol);
		List<MeritoResultado> meritosExcluidos = this.getMeritosSolicitudBolsaExcluidos(solicitud, bol);
		List<MeritoResultado> meritosEvaluados = this.getMeritosSolicitudBolsaNoEvaluados(solicitud, bol);
		Clob acreditaciones = rs.getClob(ACREDITACIONES_VALIDADAS);
		Clob titulaciones = rs.getClob(TITULACIONES_VALIDADAS);
		
		return new BolsaResultado(bol, desgloseTotal, desgloseDescripcion, total, totalSinAplicar, meritos, meritosExcluidos, 
				meritosEvaluados, acreditaciones, titulaciones);
	}
	
}