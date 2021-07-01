package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.Formateador;
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
	
	public static final String MENSAJE_ERROR_BOLSA_NULL = "Bolsa no puede estar vacía";
	public static final String MENSAJE_ERROR_MERITO_NULL = "Mérito no puede estar vacío";
	
	public static final String BEPBOL_CODNUM = "BEPBOL_CODNUM";
	public static final String BEPITE_CODNUM = "BEPITE_CODNUM";
	public static final String BEPMER_CODNUM = "BEPMER_CODNUM";
	public static final String BEPSOL_CODNUM = "BEPSOL_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String CODNUM_MERITO_SOLICITUD = "CODNUM_MERITO_SOLICITUD";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String DESGLOSE = "DESGLOSE";
	public static final String DESGLOSETOTAL = "DESGLOSETOTAL";
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
	public static final String SOLICITUD_ESTADO_ABIERTA = "ABIERTA";
	public static final String SOLICITUD_ESTADO_CERRADA = "CERRADA";
	
		
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
		
		if (compruebaTitulacionesPreferentesAlArea(solicitud.getUsuario(), bolsa)) {
			
		}
		
		List<MeritoResultado> meritos = this.getMeritosSolicitudBolsa(solicitud, bolsa);
		for (MeritoResultado merito: meritos) {
			totalSinAplicar += calcularMerito(merito);
		}
		
		Double total = totalSinAplicar;
		String desgloseTotal = BolsaEmpleoUtils.formatoPuntuacion(totalSinAplicar) + " * ";
		
		BolsaResultado bol = new BolsaResultado(bolsa, desgloseTotal, total, totalSinAplicar);
		
		guardarResultadoSolicitudBolsa(bol, solicitud, null, null);
	}
	
	/** Calculo total de un mérito en una solicitud para un área .
	 * @param merito .
	 * @return resultado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	private Double calcularMerito(MeritoResultado merito) throws SQLException, UVException {
		Double puntuacion = 0.0;
		String desglose = "";
		
		boolean tieneAfinidad = merito.getItemBaremacion().getAfinidad() != null;
		
		Double valor = tieneAfinidad ? merito.getValor() : merito.getValorMeritoSolicitud();
		Double afinidad = tieneAfinidad ? merito.getValoraciones().get(0).getAfinidad().getModulacion() : 1.0;
		Double pesoCategoria = merito.getItemBaremacion().getValor();
		Double pesoBloque = merito.getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getPorcentajeMaximo();
		
		if (!merito.getItemBaremacion().getIndividualizado() && tieneAfinidad) {
			valor = 0.0;
			afinidad = 0.0;
			for (int i = 0; i < merito.getValoraciones().size(); i++) {
				valor += merito.getValoraciones().get(i).getValor();
				afinidad += merito.getValoraciones().get(i).getAfinidad().getModulacion();
			}
		} else {
			desglose = BolsaEmpleoUtils.formatoPuntuacion(valor) + " * " + afinidad + " * " + pesoCategoria + " * " + pesoBloque;
		}
		
		puntuacion = valor * afinidad * pesoCategoria * pesoBloque;
		
		merito.setResultado(puntuacion);
		merito.setDesglose(desglose);
		
		this.guardarResultadoSolicitudBolsaMerito(merito, null);
		
		return puntuacion;
	}
	
	private void calcularMeritosYTitulacionesPreferentes(Solicitud solicitud, Bolsa bolsa) {
		
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
				+ "	WHERE bepsol.BEPCON_CODNUM = ? AND bepsob.BEPBOL_CODNUM = ? AND bepsol.ESTADO = '" + SOLICITUD_ESTADO_CERRADA + "'";
		
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
				+ "	WHERE bepsol.BEPCON_CODNUM = ? AND bepsob.BEPBOL_CODNUM = ? AND bepsol.ESTADO = '" + SOLICITUD_ESTADO_CERRADA + "'";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NIF_CANDIDATOS, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATOS, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_PUNTUACION_CANDIDATOS, "bepsob.TOTAL", DataTableColumn.COLUMN_TYPE_NUMBER);

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
	 * Devuelve los méritos con valoraciones de la bolsa en una solicitud .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoResultado> getMeritosSolicitudBolsa(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoResultado> meritos = new ArrayList<>();
		
		String consulta = "SELECT bepmer.CODNUM, bepmer.BEPITE_CODNUM, bepmer.VALOR, bepmer.DESCRIPCION, bepmer.OBSERVACION"
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
				+ "		bepsob.ARCHIVO, bepsol.CODNUM AS BEPSOL_CODNUM"
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
			stmt.setString(indexParam++, SOLICITUD_ESTADO_CERRADA);
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
				+ "	INNER JOIN TBEP_TITULACIONES_USUARIO beptus ON beptus.BEPTUS_TIT_CODNUM = beptpa.CODNUM"
				+ "	WHERE 	beptus.BEPTUS_USU_CODNUM = ?"
				+ "		AND beptpa.BEPARE_CODNUM = ?"
				+ "		AND beptus.FLGVALIDADA = 'S'"
				+ "		AND beptus.FLGBORRADO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, candidato.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				return !rs.next();
			}
		}
	}
	
	/** Guardar resultado de la solicitud para una bolsa .
	 * @param bolsa .
	 * @param solicitud .
	 * @param archivo .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void guardarResultadoSolicitudBolsa(BolsaResultado bolsa, Solicitud solicitud, InputStream archivo,
			UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_SOLICITUD_BOLSAS"
				+ "	SET TOTAL = ?, TOTALSINAPLICAR = ?, DESGLOSETOTAL = ?,"
				+ " ARCHIVO = ?, UID_USUARIO = ? WHERE BEPBOL_CODNUM = ? AND BEPSOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDouble(indexParam++, bolsa.getTotal());
			stmt.setDouble(indexParam++, bolsa.getTotalSinAplicar());
			stmt.setString(indexParam++, bolsa.getDesgloseTotal());
			stmt.setBlob(indexParam++, archivo);
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
		Double total = rs.getDouble(TOTAL);
		Double totalSinAplicar = rs.getDouble(TOTALSINAPLICAR);
		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudById(rs.getInt(BEPSOL_CODNUM));
		List<MeritoResultado> meritos = this.getMeritosSolicitudBolsa(solicitud, bol);
		
		return new BolsaResultado(bol, desgloseTotal, total, totalSinAplicar, meritos);
	}
}
