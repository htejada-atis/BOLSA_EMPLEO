package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
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
	public static final int ORDER_COLUMN_INDEX_BAREMALE = 7;
	
	public static final int ORDER_COLUMN_INDEX_ID_RESULTADOS = 0;
	public static final int ORDER_COLUMN_INDEX_COD_AREA_RESULTADOS = 1;
	public static final int ORDER_COLUMN_INDEX_DESC_AREA_RESULTADOS = 2;
	public static final int ORDER_COLUMN_INDEX_FECHA_BAREMACION_RESULTADOS = 3;
	
	public static final String MENSAJE_ERROR_BOLSA_NULL = "Bolsa no puede estar vacía";

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

		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM WHERE 1=1 ";

		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepbol.ESTADO", DataTableColumn.COLUMN_TYPE_EXACT);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTUALIZADA, "bepbol.FECHAACTUALIZACION", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUEO, "bepbol.FECHABLOQUEO", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESBLOQUEO, "bepbol.FECHADEBLOQUEO", DataTableColumn.COLUMN_TYPE_DATE);
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
	 * @return listado de bolsas de empleo .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<BolsaResultado> listaBolsasResultadosDatatable(Map<String, String[]> params)
			throws SQLException, UVException {
		List<BolsaResultado> bolsas = new ArrayList<>();
		BolsaEmpleoDataTable<BolsaResultado> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepbol.*,"
				+ "		("
				+ "		SELECT"
				+ "			CASE"
				+ "				WHEN bepbol.FECHABAREMACION > bepcon.FECHACIERRE THEN 1"
				+ "				ELSE 0"
				+ "			END"
				+ "		FROM TBEP_CONVOCATORIAS bepcon"
				+ "		WHERE ROWNUM = 1"
				+ "		) AS RESULTADOS_ACTUALES"
				+ "	FROM TBEP_BOLSAS bepbol"
				+ "	INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ "	WHERE 1=1";

		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_RESULTADOS, "bepbol.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_COD_AREA_RESULTADOS, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESC_AREA_RESULTADOS, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHA_BAREMACION_RESULTADOS, "bepbol.FECHABAREMACION", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int indexParam = 1;
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
	 * Devuelve el total de bolsas .
	 * 
	 * @return num bolsas
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si bolsa no es existe
	 */
	public Integer getTotalBolsas() throws SQLException, UVException {
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
	 * @throws UVException  si bolsa no es existe
	 */
	public Integer getBolsasBloqueadas() throws SQLException, UVException {
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
	 * @throws UVException  si bolsa no es existe
	 */
	public Integer getBolsasRevisadas() throws SQLException, UVException {
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
	 * @throws UVException  si bolsa no es existe
	 */
	public Integer getBolsasBaremables() throws SQLException, UVException {
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
	 * @throws SQLException .
	 */
	public void ponerBolsasEnAlegaciones(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuario) throws SQLException {
		/*
		 * Evaluar méritos después del proceso de evaluación. Se resuelven las
		 * alegaciones. Las comisiones no pueden acceder a evaluar méritos.
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_ALEGACIONES, usuario);
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
	 * @throws SQLException .
	 */
	public void ponerBolsasComoPendientesBaremacion(List<Bolsa> bolsas, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		for (int i = 0; i < bolsas.size(); i++) {
			this.ponerBolsaComoPendienteBaremacion(bolsas.get(i), usuarioUpdate);
		}
	}

	/** Establece la bolsa como pendiente de baremación .
	 * @param bolsa .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void ponerBolsaComoPendienteBaremacion(Bolsa bolsa, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
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
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void baremarBolsa(Bolsa bolsa, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_BOLSAS SET FECHABAREMACION = ?, FLGPENBAREMACION = 'N', UID_USUARIO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(indexParam++, usuarioUp);
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

		return bolsa;
	}
}
