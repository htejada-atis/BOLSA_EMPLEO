package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de bolsas Modelo - Operaciones con nombres:
 * lista, actualiza, borra, inserta Controlador - Opers. con nombres: obtener,
 * cambiar, eliminar, agregar
 *
 * @author ATISoluciones 2021
 */
public class ModeloBolsa {

	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_AREA = 2;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 3;
	public static final int ORDER_COLUMN_INDEX_ACTUALIZADA = 4;
	public static final int ORDER_COLUMN_INDEX_BLOQUEO = 5;
	public static final int ORDER_COLUMN_INDEX_DESBLOQUEO = 6;
	public static final int ORDER_COLUMN_INDEX_BAREMACION = 7;
	public static final int ORDER_COLUMN_INDEX_BAREMALE = 8;

	public static final int ORDER_COLUMN_INDEX_ID_RESULTADOS = 0;
	public static final int ORDER_COLUMN_INDEX_COD_AREA_RESULTADOS = 1;
	public static final int ORDER_COLUMN_INDEX_DESC_AREA_RESULTADOS = 2;
	public static final int ORDER_COLUMN_INDEX_FECHA_BAREMACION_RESULTADOS = 3;

	public static final String MENSAJE_ERROR_BOLSA_NULL = "Bolsa no puede estar vacía";
	public static final String MENSAJE_ERROR_BAREMAR_BOLSA_NO_BLOQUEADA = "Para baremar una bolsa su estado debe ser 'BLOQUEADA'";
	public static final String MENSAJE_ERROR_BAREMAR_BOLSA_NO_BAREMABLE = "Para baremar una bolsa debe ser baremable";

	public static final String BOLSA_ESTADO_BLOQUEADA = "BLOQUEADA";
	public static final String BOLSA_ESTADO_REVISION = "REVISION";
	public static final String BOLSA_ESTADO_BAREMACION = "BAREMACION";
	public static final String BOLSA_ESTADO_ALEGACIONES = "ALEGACIONES";
	public static final String BOLSA_ESTADO_DESBLOQUEADA = "DESBLOQUEADA";

	public static final String BOLSA_BAREMABLE = "S";
	public static final String BOLSA_NO_BAREMABLE = "N";

	private static final String TOTAL = "total";

	protected static ModeloBolsa eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloBolsa();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 *
	 * @return instancia
	 */
	public static ModeloBolsa obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de bolsas de empleo.
	 *
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<Bolsa> listaBolsaEmpleoDatatable(Map<String, String[]> params)
			throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = ""
			+ " SELECT bepbol.* "
			+ " FROM TBEP_BOLSAS bepbol "
			+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
			+ " WHERE 1=1 ";

		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepbol.ESTADO", DataTableColumn.COLUMN_TYPE_EXACT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTUALIZADA, "bepbol.FECHAACTUALIZACION", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUEO, "bepbol.FECHABLOQUEO", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESBLOQUEO, "bepbol.FECHADEBLOQUEO", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMACION, "bepbol.FECHABAREMACION", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int indexParam = 1;
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(this.createFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}

		return dataTable;
	}

	/** Listado de bolsas para resultados .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param usuario .
	 * @param convocatoria .
	 * @return listado de bolsas de empleo .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<BolsaResultado> listaBolsasResultadosDatatable(UsuarioBolsaEmpleo usuario, Convocatoria convocatoria, Map<String, String[]> params)
			throws SQLException, UVException {
		List<BolsaResultado> bolsas = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaResultado> dataTable = new BolsaEmpleoDataTable<>(params);

		boolean evaluador = usuario.getRol().getValor().equals(ModeloRol.ROL_MIEMBRO_COMISION)
				|| usuario.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);

		String consulta = ""
				+ " SELECT bepbol.*, "
				+ "		(SELECT CASE "
				+ "		 	WHEN bepcon.ESTADO = 'CERRADA' THEN 1 "
				+ "		 	WHEN bepcon.ESTADO = 'FINALIZADA' THEN 1 "
				+ "			ELSE 0 "
				+ "		END FROM TBEP_CONVOCATORIAS bepcon WHERE bepcon.CODNUM = ?) AS RESULTADOS_ACTUALES"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ (evaluador ? " INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM AND bepeva.FLGACTIVO = 'S'" : "")
				+ "	WHERE"
				+ (evaluador ? " bepeva.BEPUSU_CODNUM = ?" : " 1=1");

		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_RESULTADOS, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COD_AREA_RESULTADOS, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESC_AREA_RESULTADOS, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int indexParam = 1;
			stmt.setInt(indexParam, convocatoria.getCodNum());
			stmtCount.setInt(indexParam++, convocatoria.getCodNum());
			if (evaluador) {
				stmt.setInt(indexParam, usuario.getCodNum());
				stmtCount.setInt(indexParam++, usuario.getCodNum());
			}
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(new BolsaResultado(this.createFromResultSet(rs), rs.getBoolean("RESULTADOS_ACTUALES")));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}

		return dataTable;
	}

	/**
	 * Devuelve todas las bolsas del sistema.
	 *
	 * @return bolsas
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si bolsa no es existe
	 */
	public List<Bolsa> getBolsas() throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		String consulta = "SELECT bepbol.* " + "FROM TBEP_BOLSAS bepbol";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(this.createFromResultSet(rs));
				}
			}
		}
		return bolsas;
	}

	/** Devuelve las bolsas pendientes de baremación .
	 * @return bolsas .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si bolsa no es existe .
	 */
	public List<Bolsa> getBolsasPendientesBaremacion() throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol WHERE bepbol.FLGPENBAREMACION = 'S'";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(this.createFromResultSet(rs));
				}
			}
		}
		return bolsas;
	}

	/**
	 * Devuelve las areas del departamento de un área .
	 * @param area .
	 * @param solicitud .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Bolsa> getBolsasByAreaDepartamentoEnSolicitud(Area area, Solicitud solicitud) throws SQLException, UVException {
		ArrayList<Bolsa> bolsas = new ArrayList<>();
		String consulta = ""
				+ " SELECT DISTINCT bepbol.* "
				+ " FROM TBEP_AREAS bepare "
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.BEPARE_CODNUM = bepare.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.BEPBOL_CODNUM = bepbol.CODNUM AND bepsbo.BEPSOL_CODNUM = ?"
				+ " INNER JOIN TBEP_AREAS_DEPARTAMENTOS bepade ON bepade.BEPARE_CODNUM = bepare.CODNUM"
				+ " WHERE bepade.BEPDEP_CODNUM IN ("
				+ "     SELECT bepade.BEPDEP_CODNUM FROM TBEP_AREAS_DEPARTAMENTOS bepade"
				+ "     WHERE bepade.BEPARE_CODNUM = ?"
				+ " )";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, area.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(this.createFromResultSet(rs));
				}
			}
		}

		return bolsas;
	}

	/**
	 * Devuelve una bolsa por su id.
	 *
	 * @param codNum id de bolsa
	 * @return bolsa
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si bolsa no es existe
	 */
	public Bolsa getBolsaById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol WHERE bepbol.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe la bolsa con id " + codNum);
				}

				return this.createFromResultSet(rs);
			}
		}
	}

	/**
	 * Devuelve un listado de bolsas por su id.
	 *
	 * @param ids codnum de la bolsa
	 * @return listado de bolsas leidas
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public List<Bolsa> getBolsasByIds(int[] ids) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();

		for (int i = 0; i < ids.length; i++) {
			bolsas.add(this.getBolsaById(ids[i]));
		}

		return bolsas;
	}

	/**
	 * Devuleve la bolsa asociada a la bolsa.
	 * @param area .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Bolsa getBolsaByArea(Area area) throws SQLException, UVException {
		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol WHERE bepbol.BEPARE_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, area.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe la bolsa con id " + area.getCodNum());
				}
				return this.createFromResultSet(rs);
			}
		}
	}

	/**
	 * Devuelve el total de bolsas .
	 *
	 * @return num bolsas
	 * @throws SQLException en caso de error en la BD
	 */
	public Integer getTotalBolsas() throws SQLException {
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_BOLSAS";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return rs.getInt(TOTAL);
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve el total de bolsas bloqueadas .
	 *
	 * @return num bolsas
	 * @throws SQLException en caso de error en la BD
	 */
	public Integer getBolsasBloqueadas() throws SQLException {
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_BOLSAS WHERE ESTADO='BLOQUEADA'";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return rs.getInt(TOTAL);
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve el total de bolsas revisadas .
	 *
	 * @return num bolsas
	 * @throws SQLException en caso de error en la BD
	 */
	public Integer getBolsasRevisadas() throws SQLException {
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_BOLSAS WHERE ESTADO='REVISION'";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return rs.getInt(TOTAL);
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve el total de bolsas baremables .
	 *
	 * @return num bolsas
	 * @throws SQLException en caso de error en la BD
	 */
	public Integer getBolsasBaremables() throws SQLException {
		String consulta = "SELECT COUNT(*) AS total FROM TBEP_BOLSAS WHERE FLGBAREMABLE='S'";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return rs.getInt(TOTAL);
				}
			}
		}
		return null;
	}

	/**
	 * Obtiene el estado de una bolsa a partir de su código numérico.
	 *
	 * @param codNum Código numérico de la bolsa.
	 * @return El estado de la bolsa, o null si no se encuentra.
	 */
	public String getEstadoPorCodNum(int codNum) throws SQLException {
		String estado = null;
		String sql = "SELECT estado FROM TBEP_BOLSAS WHERE CODNUM = ?";
		try (Connection connection = ConexionUvirtual.obtenerInstancia();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, codNum);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					estado = resultSet.getString(ModeloArea.ESTADO);
				}
			}
		}
		return estado;
	}

	/**
	 * Bloquea bolsas.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void bloquearBolsas(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		/*
		 * Estado por defecto de las bolsas. Los candidatos NO PUEDEN añadir nuevo
		 * méritos. La bolsa se puede baremas (si es baremable)
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_BLOQUEADA, usuario);
	}

	/**
	 * Pone bolsas en revisión.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void ponerBolsasEnRevision(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		/*
		 * Se está revisando la bolsa. Usuarios no pueden introducir méritos y los
		 * miembros de la comisión no pueden evaluar. Se utiliza para un primer filtrado
		 * de candidatos apuntados a las bolsas
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_REVISION, usuario);
	}

	/**
	 * Pone bolsa en baremación.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void ponerBolsasEnBaremacion(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		/*
		 * Las comisiones pueden evaluar méritos (solo en este estado)
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_BAREMACION, usuario);
	}

	/**
	 * Pone bolsa en alegaciones.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @param fechaFinAlegaciones Fecha límite para presentar alegaciones
	 * @throws SQLException .
	 */
	public void ponerBolsasEnAlegaciones(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario, Date fechaFinAlegaciones) throws SQLException {
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_ALEGACIONES, usuario);
		this.actualizarFechaFinAlegaciones(bolsas, fechaFinAlegaciones, usuario);
	}

	/**
	 * Sobrecarga que acepta la fecha como String en formato `dd/MM/yyyy HH:mm` o `dd/MM/yyyy HH:mm:ss`.
	 * Si faltan los segundos, añade `:59` antes de parsear.
	 */
	public void ponerBolsasEnAlegaciones(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario, String fechaFinAlegacionesStr) throws SQLException, UVException {
		if (fechaFinAlegacionesStr == null || fechaFinAlegacionesStr.trim().isEmpty()) {
			throw new UVException("Debe indicar fecha de fin de alegaciones");
		}
		try {
			String fechaAjustada = fechaFinAlegacionesStr.trim();
			if (fechaAjustada.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}")) {
				fechaAjustada += ":59";
			}
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			sdf.setLenient(false);
			Date fechaFinAlegaciones = sdf.parse(fechaAjustada);
			this.ponerBolsasEnAlegaciones(bolsas, usuario, fechaFinAlegaciones);
		} catch (java.text.ParseException ex) {
			throw new UVException("Formato de fecha inválido. Utilice dd/MM/yyyy HH:mm");
		}
	}

	/**
	 * Actualiza la fecha de fin de alegaciones de las bolsas indicadas.
	 */
	private void actualizarFechaFinAlegaciones(List<Bolsa> bolsas, Date fechaFinAlegaciones, UsuarioBolsaEmpleo usuario) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		String query = "UPDATE TBEP_BOLSAS SET FECHAFINALEGACIONES = ?, UID_USUARIO = ? WHERE CODNUM IN (" + params + ")";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			if (fechaFinAlegaciones != null) {
				stmt.setTimestamp(indexParam++, new java.sql.Timestamp(fechaFinAlegaciones.getTime()));
			} else {
				stmt.setNull(indexParam++, Types.TIMESTAMP);
			}
			stmt.setString(indexParam++, usuario.getCodCuenta());
			for (Bolsa bolsa : bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());
			}
			stmt.executeUpdate();
		}
	}

	/**
	 * Devuelve las bolsas en estado ALEGACIONES cuya fecha de fin de alegaciones ha expirado.
	 * @return bolsas con alegaciones expiradas
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si no se puede leer la bolsa
	 */
	public List<Bolsa> getBolsasAlegacionesExpiradas() throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol"
				+ " WHERE bepbol.ESTADO = ?"
				+ " AND bepbol.FECHAFINALEGACIONES IS NOT NULL"
				+ " AND bepbol.FECHAFINALEGACIONES <= SYSDATE";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, BOLSA_ESTADO_ALEGACIONES);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bolsas.add(this.createFromResultSet(rs));
				}
			}
		}
		return bolsas;
	}

	/**
	 * Bloquea una bolsa desde el estado ALEGACIONES (tarea programada).
	 * Cambia el estado a BLOQUEADA y establece la fecha de bloqueo.
	 *
	 * @param bolsa Bolsa a bloquear
	 * @throws SQLException en caso de error en BD
	 * @throws UVException  si la bolsa no está en estado ALEGACIONES
	 */
	public void bloquearBolsaDesdeAlegaciones(Bolsa bolsa) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}
		if (!BOLSA_ESTADO_ALEGACIONES.equals(bolsa.getEstado())) {
			throw new UVException("La bolsa no está en estado ALEGACIONES");
		}

		String query = "UPDATE TBEP_BOLSAS SET ESTADO = ?, FECHABLOQUEO = ?, FECHADEBLOQUEO = NULL, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, BOLSA_ESTADO_BLOQUEADA);
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(indexParam++, "TAREA_PROGRAMADA");
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Desbloquea bolsas.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void desbloquearBolsas(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		/*
		 * Se pueden introducir nuevos méritos por los candidatos. Para que esto ocurra,
		 * TODAS LAS BOLSAS DEBEN ESTAR EN ESTE ESTADO
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_DESBLOQUEADA, usuario);
	}


	/** Establece las bolsas como pendientes de baremación .
	 * @param bolsas .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void ponerBolsasComoPendientesBaremacion(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		for (int i = 0; i < bolsas.size(); i++) {
			this.ponerBolsaComoPendienteBaremacion(bolsas.get(i), usuarioUpdate);
		}
	}

	/** Establece la bolsa como pendiente de baremación .
	 * @param bolsa .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void ponerBolsaComoPendienteBaremacion(Bolsa bolsa, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (!bolsa.getEstado().equals(BOLSA_ESTADO_BLOQUEADA)) {
			throw new UVException(MENSAJE_ERROR_BAREMAR_BOLSA_NO_BLOQUEADA);
		}

		if (!bolsa.getBaremable()) {
			throw new UVException(MENSAJE_ERROR_BAREMAR_BOLSA_NO_BAREMABLE);
		}

		String query = "UPDATE TBEP_BOLSAS SET FLGPENBAREMACION = 'S', UID_USUARIO = ? WHERE CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.executeUpdate();
		}
	}

	/** Baremar bolsa .
	 * @param bolsa .
	 * @param usuarioUpdate .
	 * @param definitiva .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void baremarBolsa(Bolsa bolsa, boolean definitiva, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}

		if (!bolsa.getEstado().equals(BOLSA_ESTADO_BLOQUEADA)) {
			throw new UVException(MENSAJE_ERROR_BAREMAR_BOLSA_NO_BLOQUEADA);
		}

		if (!bolsa.getBaremable()) {
			throw new UVException(MENSAJE_ERROR_BAREMAR_BOLSA_NO_BAREMABLE);
		}

		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		String query = " UPDATE TBEP_BOLSAS SET FLGPENBAREMACION = 'N', UID_USUARIO = ?, ";

		if (definitiva) {
			query += " FECHABAREMACIONFIN = ? ";
		} else {
			query += " FECHABAREMACION = ?, FECHABAREMACIONFIN = NULL ";
		}

		query += " WHERE CODNUM = ? ";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioUp);
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Indica si la bolsa está marcada para contratación.
	 * @param bolsa .
	 * @param usuario .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void habilitarBolsaParaContratacion(Bolsa bolsa, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}

		if (!bolsa.getEstado().equals(BOLSA_ESTADO_BLOQUEADA)) {
			throw new UVException(MENSAJE_ERROR_BAREMAR_BOLSA_NO_BLOQUEADA);
		}

		if (!bolsa.getBaremable()) {
			throw new UVException(MENSAJE_ERROR_BAREMAR_BOLSA_NO_BAREMABLE);
		}

		if (bolsa.getFechaBaremacionDefinitiva() == null) {
			throw new UVException("La bolsa no tiene fecha de baremación definitiva");
		}

		String query = "UPDATE TBEP_BOLSAS SET FECHAHABCONTRATOS = ?, UID_USUARIO = ? WHERE CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(indexParam++, usuario.getCodCuenta());
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Establece la area asociada a la bolsa como baremable.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void ponerAreaComoBaremable(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		this.cambiarFlagBaremableBolsas(bolsas, BOLSA_BAREMABLE, usuario);
	}

	/**
	 * Establece la area asociada a la bolsa como baremable.
	 *
	 * @param bolsas  .
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void ponerAreaComoNoBaremable(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		this.cambiarFlagBaremableBolsas(bolsas, BOLSA_NO_BAREMABLE, usuario);
	}

	private void cambiarEstadoBolsas(List<Bolsa> bolsas, String estado, UsuarioBolsaEmpleo usuario)
			throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		String updateColumns = " ESTADO = ?, UID_USUARIO = ? ";

		if (estado.equals(BOLSA_ESTADO_BLOQUEADA) || estado.equals(BOLSA_ESTADO_DESBLOQUEADA)) {
			updateColumns += ", FECHABLOQUEO = ?, FECHADEBLOQUEO = ? ";
		}

		String query = "UPDATE TBEP_BOLSAS SET " + updateColumns + " WHERE CODNUM IN (" + params + ")";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			stmt.setString(indexParam++, estado);
			stmt.setString(indexParam++, usuario.getCodCuenta());

			if (estado.equals(BOLSA_ESTADO_BLOQUEADA)) {
				stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
				stmt.setNull(indexParam++, Types.DATE);
			} else if (estado.equals(BOLSA_ESTADO_DESBLOQUEADA)) {
				stmt.setNull(indexParam++, Types.DATE);
				stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			}

			for (Bolsa bolsa : bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());
			}
			stmt.executeUpdate();
		}
	}

	private void cambiarFlagBaremableBolsas(List<Bolsa> bolsas, String baremable, UsuarioBolsaEmpleo usuario)
			throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		String query = "UPDATE TBEP_BOLSAS SET FLGBAREMABLE = ?, UID_USUARIO = ? WHERE CODNUM IN (" + params + ")";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, baremable);
			stmt.setString(indexParam++, usuario.getCodCuenta());

			for (Bolsa bolsa : bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());
			}
			stmt.executeUpdate();
		}
	}

	/**
	 * Comprueba si hay bolsas baremables.
	 *
	 * @return .
	 * @throws SQLException .
	 */
	public boolean hayBolsasBaremables() throws SQLException {
		String consulta = "SELECT COUNT(1) as numero_bolsas_abiertas " + "FROM TBEP_BOLSAS bepbol "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
				+ "WHERE bepbol.FLGBAREMABLE='S'";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next() && rs.getInt("numero_bolsas_abiertas") > 0) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Crea una una bolsa a partir de un ResultSet.
	 *
	 * @param rs .
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Bolsa createFromResultSet(ResultSet rs) throws SQLException, UVException {
		Bolsa bolsa = new Bolsa();
		bolsa.setCodNum(rs.getInt("CODNUM"));
		bolsa.setArea(ModeloArea.obtenerInstancia().getAreaById(rs.getInt("BEPARE_CODNUM")));
		bolsa.setEstado(rs.getString("ESTADO"));
		bolsa.setBaremable(BOLSA_BAREMABLE.equals(rs.getString("FLGBAREMABLE")));
		bolsa.setFechaActualizacion(rs.getTimestamp("FECHAACTUALIZACION"));
		bolsa.setFechaBloqueo(rs.getTimestamp("FECHABLOQUEO"));
		bolsa.setFechaDesBloqueo(rs.getTimestamp("FECHADEBLOQUEO"));
		bolsa.setFechaBaremacion(rs.getDate("FECHABAREMACION"));
		bolsa.setFechaBaremacionDefinitiva(rs.getDate("FECHABAREMACIONFIN"));
		bolsa.setFechaHabilitarContratos(rs.getDate("FECHAHABCONTRATOS"));
		bolsa.setFechaFinAlegaciones(rs.getTimestamp("FECHAFINALEGACIONES"));

		return bolsa;
	}

	/**
	 * Listado de bolsas para csv.
	 * @param separator .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<String[]> exportarBolsasCsv(String separator) throws SQLException, UVException {
		String consulta = ""
				+ " SELECT bepare.ID_AREA_CONOCIMIENTO, bepare.DES_AREA_CONOCIMIENTO, bepbol.ESTADO, bepbol.FLGBAREMABLE, "
				+ "		bepbol.FECHABLOQUEO, bepbol.FECHADEBLOQUEO, bepbol.FECHABAREMACION, bepbol.FECHABAREMACIONFIN, bepbol.FECHAHABCONTRATOS "
				+ " FROM TBEP_BOLSAS bepbol"
				+ "	LEFT JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM";

		List<String[]> rows = new ArrayList<>();

		rows.add(new String[] {"COD.AREA", "AREA", "ESTADO", "BAREMABLE", "FECHA BLOQUEO", "FECHA DESBLOQUEO", "FECHA BAREMACION PROVISIONAL",
				"FECHA BAREMACION DEFINITIVA", "FECHA HABILITAR CONTRATACION"});

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String codigo = BolsaEmpleoUtils.string2csv(rs.getString("ID_AREA_CONOCIMIENTO"), separator);
					String area = BolsaEmpleoUtils.string2csv(rs.getString("DES_AREA_CONOCIMIENTO"), separator);
					String estado = BolsaEmpleoUtils.string2csv(rs.getString("ESTADO"), separator);
					String baremable = BolsaEmpleoUtils.string2csv(rs.getString("FLGBAREMABLE"), separator);
					String fechaBloqueo = BolsaEmpleoUtils.date2csv((Date) rs.getTimestamp("FECHABLOQUEO"), separator);
					String fechaDesBloqueo = BolsaEmpleoUtils.date2csv((Date) rs.getTimestamp("FECHADEBLOQUEO"), separator);
					String fechaBaremacion = BolsaEmpleoUtils.date2csv((Date) rs.getTimestamp("FECHABAREMACION"), separator);
					String fechaBaremacionDefinitiva = BolsaEmpleoUtils.date2csv((Date) rs.getTimestamp("FECHABAREMACIONFIN"), separator);
					String fechaContratacion = BolsaEmpleoUtils.date2csv((Date) rs.getTimestamp("FECHAHABCONTRATOS"), separator);

					rows.add(new String[] {codigo, area, estado, baremable, fechaBloqueo, fechaDesBloqueo, fechaBaremacion, fechaBaremacionDefinitiva,
							fechaContratacion});
				}
			}
		}

		return rows;
	}
}
