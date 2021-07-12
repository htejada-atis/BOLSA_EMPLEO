package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.InputStream;
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
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.informes.GenerarResultadosPDF;
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
	
	public static final String ARCHIVO = "ARCHIVO";
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
	public static final String OBSERVACION = "OBSERVACION";
	public static final String OBSERVACION_CANDIDATO = "OBSERVACION_CANDIDATO";
	public static final String RESULTADO = "RESULTADO";
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
		
		HashMap<String, Map.Entry<MeritoPreferente, Double>> preferentes = calcularMeritosPreferentes(solicitud, bolsa);
		
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
		
		BolsaResultado bol = new BolsaResultado(bolsa, desgloseTotal, desgloseDescripcion, total, totalSinAplicar);
		bol.setListaMeritos(this.getMeritosSolicitudBolsaValidados(solicitud, bolsa));
		bol.setListaMeritosExcluidos(this.getMeritosSolicitudBolsaExcluidos(solicitud, bolsa));
		bol.setListaMeritosNoEvaluados(this.getMeritosSolicitudBolsaNoEvaluados(solicitud, bolsa));
		
		Date fechaActual = new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime());
		InputStream archivoResultados = GenerarResultadosPDF.generarPDF(solicitud, bol, fechaActual);
		guardarResultadoSolicitudBolsa(bol, solicitud, archivoResultados, null, fechaActual);
	}
	
	/** Calculo total de un mérito en una solicitud para un área .
	 * @param merito .
	 * @param preferente .
	 * @return resultado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	private Double calcularMerito(MeritoResultado merito, Map.Entry<MeritoPreferente, Double> preferente) throws SQLException, UVException {
		Double puntuacion = 0.0;
		String desglose = "";
		
		boolean tieneAfinidad = merito.getItemBaremacion().getAfinidad() != null;
		
		Double pesoCategoria = merito.getItemBaremacion().getValor();
		Double pesoBloque = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getPorcentajeMaximo();
		
		if (!merito.getItemBaremacion().getIndividualizado() && tieneAfinidad) {
			desglose = "(I) (";
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
			Double valor = tieneAfinidad ? merito.getValor() : merito.getValorMeritoSolicitud();
			Double afinidad = tieneAfinidad ? merito.getValoraciones().get(0).getAfinidad().getModulacion() : 1.0;
			
			desglose = BolsaEmpleoUtils.formatoPuntuacion(valor) + " * " + afinidad + " * " + pesoCategoria + " * " + pesoBloque;
			puntuacion = valor * afinidad * pesoCategoria * pesoBloque;
		}
		
		if (preferente != null) {
			switch (preferente.getKey().getAplicable()) {
				case ModeloMeritosPreferentes.APLICABLE_APARTADO:
					if (merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getCodNum().equals(
							preferente.getKey().getAplicableApartadoBaremacion().getCodNum())) {
						desglose = "(B) " + desglose + " * " + preferente.getValue();
						puntuacion *= preferente.getValue();
					}
					break;
				case ModeloMeritosPreferentes.APLICABLE_BLOQUE:
					if (merito.getItemBaremacion().getBloqueBaremacion().getCodNum().equals(
							preferente.getKey().getAplicableBloqueBaremacion().getCodNum())) {
						desglose = "(B) " + desglose + " * " + preferente.getValue();
						puntuacion *= preferente.getValue();
					}
					break;
				case ModeloMeritosPreferentes.APLICABLE_ITEM:
					if (merito.getItemBaremacion().getCodNum().equals(
							preferente.getKey().getAplicableItemBaremacion().getCodNum())) {
						desglose = "(B) " + desglose + " * " + preferente.getValue();
						puntuacion *= preferente.getValue();
					}
					break;
			}
		}
		
		merito.setResultado(puntuacion);
		merito.setDesglose(desglose);
		
		this.guardarResultadoSolicitudBolsaMerito(merito, null);
		
		return puntuacion;
	}
	
	/** Calculo de factores de méritos preferentes .
	 * @param solicitud .
	 * @param bolsa .
	 * @return colección de factores a aplicar con el tipo y el factor .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	private HashMap<String, Map.Entry<MeritoPreferente, Double>> calcularMeritosPreferentes(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		HashMap<String, Map.Entry<MeritoPreferente, Double>> preferentes = new HashMap<>();
		
		List<MeritoPreferente> meritosPreferentes = ModeloMeritosPreferentes.obtenerInstancia().listaMeritosPreferentesActivos();
		
		for (MeritoPreferente preferente: meritosPreferentes) {
			Double factor = null;
			
			switch (preferente.getTipo()) {
				case ModeloMeritosPreferentes.TIPO_TITULACION_PREFERENTE:
					if (compruebaTitulacionesPreferentesAlArea(solicitud.getUsuario(), bolsa)) {
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
				+ "	FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ "	WHERE bepsol.BEPCON_CODNUM = ? AND bepsob.BEPBOL_CODNUM = ? AND bepsol.ESTADO = '" + ModeloSolicitud.SOLICITUD_ESTADO_CERRADA + "'";
		
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
	
	/** Devuelve los méritos validados con valoraciones de la bolsa en una solicitud .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoResultado> getMeritosSolicitudBolsaValidados(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoResultado> meritos = new ArrayList<>();
		
		String consulta = ""
				+ "	SELECT bepmer.CODNUM, bepmer.BEPITE_CODNUM, bepmer.VALOR, bepmer.DESCRIPCION, bepmer.OBSERVACION"
				+ "		, bepsbm.CODNUM AS " + CODNUM_MERITO_SOLICITUD + ", bepsbm.VALOR AS " + VALOR_MERITO_SOLICITUD
				+ "		, bepsbm.OBSERVACION_CANDIDATO, bepsbm.RESULTADO, bepsbm.DESGLOSE, bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO"
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM  = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ "	WHERE "
				+ "			bepsbo.BEPBOL_CODNUM = ?"
				+ "		AND bepsbo.BEPSOL_CODNUM = ?"
				+ "		AND bepsbm.FLGVALIDADO = 'S'"
				+ "		AND bepsbm.FLGEXCLUIDO = 'N'";
		
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
		
		String consulta = ""
				+ "	SELECT bepmer.CODNUM, bepmer.BEPITE_CODNUM, bepmer.VALOR, bepmer.DESCRIPCION, bepmer.OBSERVACION"
				+ "		, bepsbm.CODNUM AS " + CODNUM_MERITO_SOLICITUD + ", bepsbm.VALOR AS " + VALOR_MERITO_SOLICITUD
				+ "		, bepsbm.OBSERVACION_CANDIDATO, bepsbm.RESULTADO, bepsbm.DESGLOSE, bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO"
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM  = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ "	WHERE "
				+ "			bepsbo.BEPBOL_CODNUM = ?"
				+ "		AND bepsbo.BEPSOL_CODNUM = ?"
				+ "		AND bepsbm.FLGVALIDADO = 'N'"
				+ "		AND bepsbm.FLGEXCLUIDO = 'S'";
		
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
		
		String consulta = ""
				+ "	SELECT bepmer.CODNUM, bepmer.BEPITE_CODNUM, bepmer.VALOR, bepmer.DESCRIPCION, bepmer.OBSERVACION"
				+ "		, bepsbm.CODNUM AS " + CODNUM_MERITO_SOLICITUD + ", bepsbm.VALOR AS " + VALOR_MERITO_SOLICITUD
				+ "		, bepsbm.OBSERVACION_CANDIDATO, bepsbm.RESULTADO, bepsbm.DESGLOSE, bepsbm.FLGEXCLUIDO, bepsbm.FLGVALIDADO"
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM  = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ "	WHERE "
				+ "			bepsbo.BEPBOL_CODNUM = ?"
				+ "		AND bepsbo.BEPSOL_CODNUM = ?"
				+ "		AND bepsbm.FLGVALIDADO = 'N'"
				+ "		AND bepsbm.FLGEXCLUIDO = 'N'";
		
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
				+ "		bepsob.DESGLOSEDESCRIPCION, bepsob.ARCHIVO, bepsob.FECHABAREMACION, bepsol.CODNUM AS BEPSOL_CODNUM"
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
	
	/** Comprueba si el candidato tiene alguna titulación validada para el área .
	 * @param bolsa .
	 * @param candidato .
	 * @return si tiene titulaciones o no .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public boolean compruebaTitulacionesPreferentesAlArea(UsuarioBolsaEmpleo candidato, Bolsa bolsa) throws SQLException {
		String consulta = "SELECT *"
				+ " FROM TBEP_TIT_PREFERENTES_AREA beptpa"
				+ "	INNER JOIN TBEP_TITULACIONES beptit ON beptit.CODNUM = beptpa.BEPTIT_CODNUM"
				+ "	INNER JOIN TBEP_TITULACIONES_USUARIO beptus ON beptus.BEPTUS_TIT_CODNUM = beptit.CODNUM"
				+ "	WHERE 	beptus.BEPTUS_USU_CODNUM = ?"
				+ "		AND beptpa.BEPARE_CODNUM = ?"
				+ "		AND beptus.FLGVALIDADA = 'S'"
				+ "		AND beptus.FLGBORRADO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, candidato.getCodNum());
			stmt.setInt(indexParam++, bolsa.getArea().getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
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
				+ "	WHERE bepsol.BEPUSU_CODNUM = ? AND bepmer.BEPITE_CODNUM = ? AND bepmer.VALOR >= ? AND bepsbm.FLGVALIDADO = 'S' AND bepsbm.FLGEXCLUIDO = 'N'"
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
	 * @param archivo .
	 * @param usuarioUpdate .
	 * @param fechaActual .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void guardarResultadoSolicitudBolsa(BolsaResultado bolsa, Solicitud solicitud, InputStream archivo,
			UsuarioBolsaEmpleo usuarioUpdate, Date fechaActual) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_SOLICITUD_BOLSAS"
				+ "	SET TOTAL = ?, TOTALSINAPLICAR = ?, DESGLOSETOTAL = ?, DESGLOSEDESCRIPCION = ?,"
				+ " ARCHIVO = ?, FECHABAREMACION = ?, UID_USUARIO = ? WHERE BEPBOL_CODNUM = ? AND BEPSOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDouble(indexParam++, bolsa.getTotal());
			stmt.setDouble(indexParam++, bolsa.getTotalSinAplicar());
			stmt.setString(indexParam++, bolsa.getDesgloseTotal());
			stmt.setString(indexParam++, bolsa.getDesgloseDescripcion());
			stmt.setBlob(indexParam++, archivo);
			stmt.setDate(indexParam++, fechaActual);
			stmt.setString(indexParam++, usuarioUp);
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
		InputStream archivo = rs.getBlob(ARCHIVO).getBinaryStream();
		List<MeritoResultado> meritos = this.getMeritosSolicitudBolsaValidados(solicitud, bol);
		List<MeritoResultado> meritosExcluidos = this.getMeritosSolicitudBolsaExcluidos(solicitud, bol);
		List<MeritoResultado> meritosEvaluados = this.getMeritosSolicitudBolsaNoEvaluados(solicitud, bol);
		
		return new BolsaResultado(bol, desgloseTotal, desgloseDescripcion, total, totalSinAplicar, meritos, meritosExcluidos, meritosEvaluados, archivo);
	}
}
