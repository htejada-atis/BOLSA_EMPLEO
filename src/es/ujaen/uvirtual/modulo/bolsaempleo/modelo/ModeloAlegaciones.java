package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaCandidatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaMisResultados;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.ficheros.FileSystemUtils;

/**
 * Clase de modelo para la gestión de alegaciones
 *
 * @author ATISoluciones 2026
 */
public class ModeloAlegaciones {
	protected static ModeloAlegaciones eInstancia;
	private static final String NOMBREDEESTACLASE = ModeloAlegaciones.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	public static final int ORDER_COLUMN_INDEX_CONVOCATORIA = 0;
	public static final int ORDER_COLUMN_INDEX_AREA = 1;
	public static final int ORDER_COLUMN_INDEX_DOCUMENTO = 2;
	public static final int ORDER_COLUMN_INDEX_CANDIDATO = 3;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 4;
	public static final int ORDER_COLUMN_INDEX_ID = 5;

	public static final int MIS_ORDER_COLUMN_INDEX_CONVOCATORIA = 0;
	public static final int MIS_ORDER_COLUMN_INDEX_AREA = 1;
	public static final int MIS_ORDER_COLUMN_INDEX_ESTADO = 2;
	public static final int MIS_ORDER_COLUMN_INDEX_FECHA_CREACION = 3;
	public static final int MIS_ORDER_COLUMN_INDEX_FECHA_CONFIRMACION = 4;
	public static final int MIS_ORDER_COLUMN_INDEX_ID = 5;

	public static final String ESTADO_PENDIENTE_ALEGACION = "PENDIENTE";
	public static final String ESTADO_PRESENTADA_ALEGACION = "PRESENTADA";
	public static final String ESTADO_ENRESOLUCION_ALEGACION = "ENRESOLUCION";
	public static final String ESTADO_ENVIADAALDEPARTAMENTO_ALEGACION = "ENDEPARTAMENTO";
	public static final String ESTADO_INFORMADA_ALEGACION = "INFORMADA";
	public static final String ESTADO_RESUELTA_ALEGACION = "RESUELTA";

	public static final String MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE = "No existe el usuario con el id indicando";

	// ficheros en disco
	public static final String ESQUEMA_TBEP_SOL_MER_BOL_ALE_FILE = "G_INTRANET";
	public static final String TABLA_TBEP_SOL_MER_BOL_ALE_FILE = "TBEP_SOL_MER_BOL_ALE_FILE";
	public static final String ID_TBEP_SOL_MER_BOL_ALE_FILE = "CODNUM";

	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloAlegaciones();
		}
	}

	public static ModeloAlegaciones obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Obtiene la alegación asociada a un candidato, una bolsa y una convocatoria.
	 *
	 * @param bolsa        Bolsa de empleo vinculada a la alegación
	 * @param candidato    Usuario candidato que presentó la solicitud
	 * @param convocatoria Convocatoria de la solicitud
	 * @return Alegación existente o null si no se encuentra ninguna
	 * @throws SQLException Si ocurre un error de acceso a la base de datos
	 */
	public Alegacion getAlegacion(Bolsa bolsa, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria) throws SQLException {
		if (bolsa == null || bolsa.getCodNum() == null || candidato == null || candidato.getCodNum() == null || convocatoria == null || convocatoria.getCodNum() == null) {
			return null;
		}

		// Buscamos la alegación asociada a la solicitud del candidato para la
		// convocatoria y la bolsa indicada.
		String consulta = "SELECT bepale.* "
				+ "FROM TBEP_ALEGACIONES bepale "
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM "
				+ "INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM "
				+ "WHERE bepsol.BEPUSU_CODNUM = ? "
				+ "  AND bepsol.BEPCON_CODNUM = ? "
				+ "  AND bepsbo.BEPBOL_CODNUM = ? "
				+ "ORDER BY bepale.CODNUM DESC FETCH FIRST 1 ROW ONLY";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, candidato.getCodNum());
			stmt.setInt(indexParam++, convocatoria.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Alegacion alegacion = new Alegacion();
					alegacion.setCodNum(rs.getInt("CODNUM"));
					alegacion.setBepsboCodNum(rs.getInt("BEPSBO_CODNUM"));
					alegacion.setEstado(rs.getString("ESTADO"));
					alegacion.setFechaCreacion(rs.getTimestamp("FECHACREACION"));
					alegacion.setFechaConfirmacion(rs.getTimestamp("FECHACONFIRMACION"));
					alegacion.setFechaEnvioDepartamento(rs.getTimestamp("FECHAENVIODEPARTAMENTO"));
					alegacion.setFechaResolucionDepartamento(rs.getTimestamp("FECHARESOLUCIONDEPARTAMENTO"));
					alegacion.setFechaResolucionFinal(rs.getTimestamp("FECHARESOLUCIONFINAL"));
					alegacion.setDesResolucionDep(rs.getString("DESRESOLUCIONDEP"));
					alegacion.setDesResolucionFinal(rs.getString("DESRESOLUCIONFINAL"));
					alegacion.setUsuario(cargarUsuarioModificador(rs.getString("UID_USUARIO")));
					alegacion.setCandidato(candidato);
					return alegacion;
				}
			}
		}

		return null;
	}

	/**
	 * Crea una alegación para un candidato para una bolsa. Comprobamos que no
	 * exista previamente una alegación.
	 *
	 * @param bolsa        Bolsa de empleo
	 * @param candidato    Usuario candidato que solicita la alegación
	 * @param convocatoria Convocatoria de la solicitud
	 * @return Alegación creada
	 * @throws SQLException En caso de error con la base de datos
	 * @throws UVException  En caso de datos inválidos o violaciones de negocio
	 */
	public Alegacion crearAlegacion(Bolsa bolsa, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria) throws SQLException, UVException {
		return crearAlegacion(bolsa, candidato, convocatoria, candidato);
	}

	/**
	 * Crea una alegación para un candidato para una bolsa. Comprobamos que no
	 * exista previamente una alegación.
	 *
	 * @param bolsa          Bolsa de empleo
	 * @param candidato      Usuario candidato que solicita la alegación
	 * @param convocatoria   Convocatoria de la solicitud
	 * @param usuarioCreador Usuario que realiza la creación (puede ser el propio
	 *                       candidato)
	 * @return Alegación creada
	 * @throws SQLException En caso de error con la base de datos
	 * @throws UVException  En caso de datos inválidos o violaciones de negocio
	 */
	public Alegacion crearAlegacion(Bolsa bolsa, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria, UsuarioBolsaEmpleo usuarioCreador) throws SQLException, UVException {
		if (bolsa == null || bolsa.getCodNum() == null || candidato == null || candidato.getCodNum() == null || convocatoria == null || convocatoria.getCodNum() == null) {
			throw new UVException("No se puede crear una alegación sin bolsa, candidato o convocatoria.");
		}

		if (!esBolsaEnEstadoAlegaciones(bolsa.getCodNum())) {
			throw new UVException("No se puede crear una alegación: la bolsa no está en estado ALEGACIONES.");
		}

		if (esFechaFinAlegacionesSuperada(bolsa.getCodNum())) {
			throw new UVException("El plazo para presentar alegaciones ha finalizado.");
		}

		if (usuarioCreador == null || usuarioCreador.getCodCuenta() == null) {
			throw new UVException("No se puede crear una alegación sin un usuario creador válido.");
		}

		Alegacion alegacionExistente = getAlegacion(bolsa, candidato, convocatoria);
		if (alegacionExistente != null) {
			throw new UVException("Ya existe una alegación para esta solicitud de bolsa.");
		}

		Solicitud solicitud = ModeloSolicitud.obtenerInstancia().getSolicitudByConvocatoriaUsuario(candidato, convocatoria);
		if (solicitud == null) {
			throw new UVException("No existe una solicitud para esta convocatoria.");
		}

		// Obtenemos el identificador de la bolsa dentro de la solicitud
		Integer bepsboCodNum = null;
		String consultaBepsbo = "SELECT CODNUM FROM TBEP_SOLICITUD_BOLSAS WHERE BEPSOL_CODNUM = ? AND BEPBOL_CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consultaBepsbo)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.setInt(indexParam++, bolsa.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					bepsboCodNum = rs.getInt("CODNUM");
				}
			}

			if (bepsboCodNum == null) {
				throw new UVException("La bolsa no forma parte de la solicitud.");
			}

			String insert = "INSERT INTO TBEP_ALEGACIONES (BEPSBO_CODNUM, ESTADO, UID_USUARIO) VALUES (?, ?, ?)";
			try (PreparedStatement stmtIns = conexion.prepareStatement(insert)) {
				stmtIns.setInt(1, bepsboCodNum);
				stmtIns.setString(2, "PENDIENTE");
				stmtIns.setString(3, usuarioCreador.getCodCuenta());
				stmtIns.executeUpdate();
			}
		}

		return getAlegacion(bolsa, candidato, convocatoria);
	}

	/**
	 * Listado de alegaciones para la vista admin (DataTable).
	 */
	public BolsaEmpleoDataTable<Alegacion> listaAlegacionesEmpleoDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuarioLogeado, Convocatoria convocatoria) throws SQLException, UVException {
		List<Alegacion> alegaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Alegacion> dataTable = new BolsaEmpleoDataTable<>(params);

		boolean director = usuarioLogeado.getRol().getValor().equals(ModeloRol.ROL_DIRECTOR_DEPARTAMENTO);

		String consulta = "SELECT bepale.CODNUM, bepale.ESTADO, bepale.FECHACREACION, bepale.FECHACONFIRMACION,"
				+ " bepale.FECHAENVIODEPARTAMENTO, bepale.FECHARESOLUCIONDEPARTAMENTO, bepale.FECHARESOLUCIONFINAL,"
				+ " bepale.DESRESOLUCIONDEP, bepale.DESRESOLUCIONFINAL,"
				+ " bepare.DES_AREA_CONOCIMIENTO AS AREA_DESCRIPCION, bepare.CODNUM AS AREA_CODNUM,"
				+ " bepusu.VUAJA_PRSNIF AS CANDIDATO_DOCUMENTO,"
				+ " bepusu.VUAJA_STRNOMBRE AS CANDIDATO_NOMBRE,"
				+ " bepusu.VUAJA_STRAPELLIDO1 AS CANDIDATO_APELLIDO1,"
				+ " bepusu.VUAJA_STRAPELLIDO2 AS CANDIDATO_APELLIDO2,"
				+ " bepusu.CODNUM AS CANDIDATO_CODNUM,"
				+ " bepcon.CODNUM AS CONVOCATORIA_CODNUM,"
				+ " bepcon.CURSO AS CONVOCATORIA_CURSO"
				+ " FROM TBEP_ALEGACIONES bepale"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ (director ? " INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepare.CODNUM AND bepeva.FLGACTIVO = 'S'" : "")
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " INNER JOIN TBEP_CONVOCATORIAS bepcon ON bepcon.CODNUM = bepsol.BEPCON_CODNUM"
				+ " WHERE"
				+ (director ? " bepeva.BEPUSU_CODNUM = ?" : " 1=1");

		if (convocatoria != null && convocatoria.getCodNum() != null) {
			consulta += " AND bepcon.CODNUM = " + convocatoria.getCodNum();
		}

		dataTable.setColumn(ORDER_COLUMN_INDEX_CONVOCATORIA, "bepcon.CURSO", DataTableColumn.COLUMN_TYPE_OPTION);
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_CANDIDATO, "bepusu.VUAJA_STRAPELLIDO1");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepale.ESTADO", DataTableColumn.COLUMN_TYPE_OPTION);
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepale.CODNUM", DataTableColumn.COLUMN_TYPE_IGNORE);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			if (director) {
				stmt.setInt(paramIndex, usuarioLogeado.getCodNum());
				stmtCount.setInt(paramIndex, usuarioLogeado.getCodNum());
				paramIndex++;
			}
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Alegacion ale = new Alegacion();
					ale.setCodNum(rs.getInt("CODNUM"));
					ale.setEstado(rs.getString("ESTADO"));
					ale.setFechaCreacion(rs.getTimestamp("FECHACREACION"));
					ale.setFechaConfirmacion(rs.getTimestamp("FECHACONFIRMACION"));
					ale.setFechaEnvioDepartamento(rs.getTimestamp("FECHAENVIODEPARTAMENTO"));
					ale.setFechaResolucionDepartamento(rs.getTimestamp("FECHARESOLUCIONDEPARTAMENTO"));
					ale.setFechaResolucionFinal(rs.getTimestamp("FECHARESOLUCIONFINAL"));

					Area area = new Area();
					area.setCodNum(rs.getInt("AREA_CODNUM"));
					area.setDescripcion(rs.getString("AREA_DESCRIPCION"));
					ale.setArea(area);

					Convocatoria conv = new Convocatoria();
					conv.setCodNum(rs.getInt("CONVOCATORIA_CODNUM"));
					conv.setCurso(rs.getString("CONVOCATORIA_CURSO"));
					ale.setConvocatoria(conv);

					UsuarioBolsaEmpleo candidato = new UsuarioBolsaEmpleo();
					candidato.setCodNum(rs.getInt("CANDIDATO_CODNUM"));
					candidato.setPrsNif(rs.getString("CANDIDATO_DOCUMENTO"));
					candidato.setNombre(rs.getString("CANDIDATO_NOMBRE"));
					candidato.setPrimerApellido(rs.getString("CANDIDATO_APELLIDO1"));
					candidato.setSegundoApellido(rs.getString("CANDIDATO_APELLIDO2"));
					ale.setCandidato(candidato);

					alegaciones.add(ale);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(alegaciones);
		}

		return dataTable;
	}

	/**
	 * Obtiene una alegación por su CODNUM, incluyendo datos de área, convocatoria y candidato.
	 */
	public Alegacion getAlegacionByCodNum(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El identificador de la alegación es obligatorio.");
		}

		String consulta = "SELECT bepale.*,"
				+ " bepare.CODNUM AS AREA_CODNUM,"
				+ " bepare.ID_AREA_CONOCIMIENTO AS AREA_ID,"
				+ " bepare.DES_AREA_CONOCIMIENTO AS AREA_DESCRIPCION,"
				+ " bepcon.CODNUM AS CONVOCATORIA_CODNUM,"
				+ " bepusu.CODNUM AS CANDIDATO_CODNUM"
				+ " FROM TBEP_ALEGACIONES bepale"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " INNER JOIN TBEP_CONVOCATORIAS bepcon ON bepcon.CODNUM = bepsol.BEPCON_CODNUM"
				+ " WHERE bepale.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Alegacion alegacion = new Alegacion();
					alegacion.setCodNum(rs.getInt("CODNUM"));
					alegacion.setBepsboCodNum(rs.getInt("BEPSBO_CODNUM"));
					alegacion.setEstado(rs.getString("ESTADO"));
					alegacion.setFechaCreacion(rs.getTimestamp("FECHACREACION"));
					alegacion.setFechaConfirmacion(rs.getTimestamp("FECHACONFIRMACION"));
					alegacion.setFechaEnvioDepartamento(rs.getTimestamp("FECHAENVIODEPARTAMENTO"));
					alegacion.setFechaResolucionDepartamento(rs.getTimestamp("FECHARESOLUCIONDEPARTAMENTO"));
					alegacion.setFechaResolucionFinal(rs.getTimestamp("FECHARESOLUCIONFINAL"));
					alegacion.setDesResolucionDep(rs.getString("DESRESOLUCIONDEP"));
					alegacion.setDesResolucionFinal(rs.getString("DESRESOLUCIONFINAL"));

					Area area = new Area();
					area.setCodNum(rs.getInt("AREA_CODNUM"));
					area.setIdAreaExterno(rs.getString("AREA_ID"));
					area.setDescripcion(rs.getString("AREA_DESCRIPCION"));
					alegacion.setArea(area);

					Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(rs.getInt("CONVOCATORIA_CODNUM"));
					alegacion.setConvocatoria(convocatoria);

					UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("CANDIDATO_CODNUM"));
					alegacion.setCandidato(candidato);

					return alegacion;
				}
			}
		}

		throw new UVException("No se encontró la alegación con id " + codNum);
	}

	/**
	 * Carga el usuario que figura en UID_USUARIO de una alegación
	 * y devuelve null si no hay registro o no existe USUARIO.
	 */
	private UsuarioBolsaEmpleo cargarUsuarioModificador(String uidUsuario) {
		if (uidUsuario == null) {
			return null;
		}

		try {
			return ModeloUsuarioBolsaEmpleo.obtenerInstancia().compruebaUsuarioByCodCuenta(uidUsuario);
		} catch (SQLException | UVException ex) {
			LOGGER.log(Level.WARNING, "No se pudo cargar usuario modificador: " + uidUsuario, ex);
			return null;
		}
	}

	/**
	 * Actualiza el estado de una alegación.
	 */
	public boolean actualizarEstadoAlegacion(Integer codNum, String nuevoEstado, String uidUsuario) throws SQLException {
		if (nuevoEstado == null) {
			return false;
		}

		String consulta = "UPDATE TBEP_ALEGACIONES SET ESTADO = ?, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, nuevoEstado);
			stmt.setString(2, uidUsuario);
			stmt.setInt(3, codNum);
			return stmt.executeUpdate() > 0;
		}
	}

	/**
	 * Actualiza la fecha de resolución del departamento.
	 */
	public void actualizarFechaResolucionDepartamento(Integer codNum, String uidUsuario) throws SQLException {
		String consulta = "UPDATE TBEP_ALEGACIONES SET FECHARESOLUCIONDEPARTAMENTO = SYSDATE, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, uidUsuario);
			stmt.setInt(2, codNum);
			stmt.executeUpdate();
		}
	}

	/**
	 * Actualiza la fecha de resolución final.
	 */
	public void actualizarFechaResolucionfinal(Integer codNum, String uidUsuario) throws SQLException {
		String consulta = "UPDATE TBEP_ALEGACIONES SET FECHARESOLUCIONFINAL = SYSDATE, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, uidUsuario);
			stmt.setInt(2, codNum);
			stmt.executeUpdate();
		}
	}

	/**
	 * Actualiza la fecha de envío al departamento.
	 */
	public void actualizarFechaEnvioDepartamento(Integer codNum, String uidUsuario) throws SQLException {
		String consulta = "UPDATE TBEP_ALEGACIONES SET FECHAENVIODEPARTAMENTO = SYSDATE, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, uidUsuario);
			stmt.setInt(2, codNum);
			stmt.executeUpdate();
		}
	}

	/**
	 * Inserta la descripción de resolución del departamento.
	 */
	public void insertarDescripcionResolucionDepartamento(Integer codNum, String descripcion, String uidUsuario) throws SQLException {
		String consulta = "UPDATE TBEP_ALEGACIONES SET DESRESOLUCIONDEP = ?, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			if (descripcion != null) {
				stmt.setString(1, descripcion);
			} else {
				stmt.setNull(1, Types.CLOB);
			}
			stmt.setString(2, uidUsuario);
			stmt.setInt(3, codNum);
			stmt.executeUpdate();
		}
	}

	/**
	 * Inserta la descripción de resolución final.
	 */
	public void insertarDescripcionResolucionFinal(Integer codNum, String descripcion, String uidUsuario) throws SQLException {
		String consulta = "UPDATE TBEP_ALEGACIONES SET DESRESOLUCIONFINAL = ?, UID_USUARIO = ? WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			if (descripcion != null) {
				stmt.setString(1, descripcion);
			} else {
				stmt.setNull(1, Types.CLOB);
			}
			stmt.setString(2, uidUsuario);
			stmt.setInt(3, codNum);
			stmt.executeUpdate();
		}
	}

	/**
	 * Para cada mérito del resultado, establece la descripción de la alegación si existe.
	 */
	public void establecerDescripcionesSiExistenAlegacionesEnLosMeritos(VistaMisResultados beanResultados,
			Convocatoria convocatoria, UsuarioBolsaEmpleo candidato, int bolsaCodNum) throws SQLException {
		BolsaResultado bolsaResultado = beanResultados.getBolsaResultado();
		if (bolsaResultado == null) {
			return;
		}

		String consulta = "SELECT bepalm.CODNUM, bepalm.BEPSBM_CODNUM, bepalm.DESCRIPCION"
				+ " FROM TBEP_SOL_MER_BOL_ALEGACION bepalm"
				+ " INNER JOIN TBEP_ALEGACIONES bepale ON bepale.CODNUM = bepalm.BEPALE_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " WHERE bepsol.BEPUSU_CODNUM = ?"
				+ " AND bepsol.BEPCON_CODNUM = ?"
				+ " AND bepsbo.BEPBOL_CODNUM = ?"
				+ " AND bepalm.BEPSBM_CODNUM IS NOT NULL";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, candidato.getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, bolsaCodNum);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int bepalmCodNum = rs.getInt("CODNUM");
					int bepsbmCodNum = rs.getInt("BEPSBM_CODNUM");
					String descripcion = rs.getString("DESCRIPCION");
					List<ArchivoAlegacionMerito> archivos = obtenerArchivosAlegacionMerito(conexion, bepalmCodNum);
					establecerDatosAlegacionEnMerito(bolsaResultado.getListaMeritos(), bepsbmCodNum, descripcion, archivos);
					establecerDatosAlegacionEnMerito(bolsaResultado.getListaMeritosExcluidos(), bepsbmCodNum, descripcion, archivos);
					establecerDatosAlegacionEnMerito(bolsaResultado.getListaMeritosNoEvaluados(), bepsbmCodNum, descripcion, archivos);
				}
			}
		}
	}

	private List<ArchivoAlegacionMerito> obtenerArchivosAlegacionMerito(Connection conexion, int bepalmCodNum) throws SQLException {
		List<ArchivoAlegacionMerito> archivos = new ArrayList<>();
		String consulta = "SELECT CODNUM, NOMBRE_ARCHIVO FROM TBEP_SOL_MER_BOL_ALE_FILE WHERE BEPALM_CODNUM = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, bepalmCodNum);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					archivos.add(new ArchivoAlegacionMerito(rs.getInt("CODNUM"), rs.getString("NOMBRE_ARCHIVO")));
				}
			}
		}
		return archivos;
	}

	private void establecerDatosAlegacionEnMerito(List<MeritoResultado> meritos, int bepsbmCodNum, String descripcion, List<ArchivoAlegacionMerito> archivos) {
		if (meritos == null) {
			return;
		}
		for (MeritoResultado merito : meritos) {
			if (merito.getCodNumMeritoSolicitud() != null && merito.getCodNumMeritoSolicitud() == bepsbmCodNum) {
				merito.setDescripcionAlegacion(descripcion);
				merito.setArchivosAlegacion(archivos);
				merito.setTieneAlegacion(true);
			}
		}
	}

	/**
	 * Obtiene el SolMerBolAlegacion existente para la alegación general (meritoId=null) o un mérito concreto.
	 */
	public SolMerBolAlegacion obtenerSolMerBolAlegacionExistente(Bolsa bolsa, Convocatoria convocatoria,
			UsuarioBolsaEmpleo candidato, Integer solBolMerCodNum) throws SQLException {
		String consulta = "SELECT bepalm.CODNUM, bepalm.BEPSBM_CODNUM, bepalm.BEPALE_CODNUM, bepalm.DESCRIPCION, bepalm.UID_USUARIO"
				+ " FROM TBEP_SOL_MER_BOL_ALEGACION bepalm"
				+ " INNER JOIN TBEP_ALEGACIONES bepale ON bepale.CODNUM = bepalm.BEPALE_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " WHERE bepsol.BEPUSU_CODNUM = ? AND bepsol.BEPCON_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ?";

		if (solBolMerCodNum != null) {
			consulta += " AND bepalm.BEPSBM_CODNUM = ?";
		} else {
			consulta += " AND bepalm.BEPSBM_CODNUM IS NULL";
		}

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, candidato.getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			if (solBolMerCodNum != null) {
				stmt.setInt(paramIndex++, solBolMerCodNum);
			}

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					SolMerBolAlegacion sol = new SolMerBolAlegacion();
					sol.setCodNum(rs.getInt("CODNUM"));
					int bepsbm = rs.getInt("BEPSBM_CODNUM");
					sol.setBepsbmCodNum(rs.wasNull() ? null : bepsbm);
					sol.setBepaleCodNum(rs.getInt("BEPALE_CODNUM"));
					sol.setDescripcion(rs.getString("DESCRIPCION"));
					sol.setUidUsuario(rs.getString("UID_USUARIO"));
					return sol;
				}
			}
		}

		return null;
	}

	/**
	 * Asigna los archivos de alegación al SolMerBolAlegacion.
	 */
	public void asignarArchivosAlegacion(VistaMisResultados beanResultados, SolMerBolAlegacion solMerBolAlegacion) throws SQLException {
		if (solMerBolAlegacion == null || solMerBolAlegacion.getCodNum() == null) {
			return;
		}

		List<ArchivoAlegacionGeneral> archivos = new ArrayList<>();
		String consulta = "SELECT CODNUM, NOMBRE_ARCHIVO FROM TBEP_SOL_MER_BOL_ALE_FILE WHERE BEPALM_CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, solMerBolAlegacion.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ArchivoAlegacionGeneral archivo = new ArchivoAlegacionGeneral();
					archivo.setId(rs.getInt("CODNUM"));
					archivo.setNombre(rs.getString("NOMBRE_ARCHIVO"));
					archivos.add(archivo);
				}
			}
		}

		solMerBolAlegacion.setArchivosAlegacion(archivos);
	}

	/**
	 * Obtiene la alegación asociada a una bolsa del resultado del candidato.
	 */
	public Alegacion getAlegacionBySolicitudBolsa(VistaMisResultados beanResultados,
			Convocatoria convocatoria, UsuarioBolsaEmpleo candidato) throws SQLException {
		if (beanResultados.getBolsa() == null) {
			return null;
		}
		Bolsa bolsa = beanResultados.getBolsa();
		return getAlegacion(bolsa, candidato, convocatoria);
	}

	/**
	 * Notifica al candidato que su alegación ha sido resuelta.
	 *
	 * Utiliza la plantilla de parámetros:
	 *   - ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_RESOLUCION_ALEGACION
	 *
	 * Variables reemplazadas en plantilla (via reemplazaAlegacionResolucionEnPlantilla):
	 *   - idalegacion -> alegacion.getCodNum().toString()
	 *   - nombre_area -> alegacion.getArea().getDescripcion()
	 */
	public void notificarCandidatoResolucionAlegacion(UsuarioBolsaEmpleo usuarioLogeado, Alegacion alegacion) throws SQLException, UVException {
		try {
			String idPlantillaStr = ModeloParametrosConfiguracion.obtenerInstancia()
					.getParametroByNombre(ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_RESOLUCION_ALEGACION).getValor();
			Integer idPlantilla = Integer.parseInt(idPlantillaStr);
			Plantilla plantilla = ModeloPlantilla.obtenerInstancia().getPlantillaById(idPlantilla);

			String titulo = ModeloPlantilla.obtenerInstancia().reemplazaAlegacionResolucionEnPlantilla(alegacion, plantilla.getTitulo(), false);
			String cuerpo = ModeloPlantilla.obtenerInstancia().reemplazaAlegacionResolucionEnPlantilla(alegacion, plantilla.getCuerpo(), true);

			Mensaje mensaje = new Mensaje();
			mensaje.setTitulo(titulo);
			mensaje.setCuerpo(cuerpo);
			mensaje.setEstado(ModeloMensajes.MENSAJE_ESTADO_ENVIANDO);
			mensaje.setFechaCreacion(BolsaEmpleoUtils.getCurrentDateTime());

			Integer idMensaje = ModeloMensajes.obtenerInstancia().nuevoMensaje(mensaje, usuarioLogeado);
			mensaje.setCodNum(idMensaje);
			ModeloMensajes.obtenerInstancia().agregarDestinatarioUsuario(mensaje, alegacion.getCandidato(), usuarioLogeado);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al notificar resolución de alegación", e);
			throw new UVException("Error al enviar la notificación de resolución.");
		}
	}

	/**
	 * Notifica sobre una alegación enviada al departamento.
	 *
	 * Utiliza la plantilla de parámetros:
	 *   - ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_ENVIADA_DEPARTAMENTO_ALEGACION
	 *
	 * Variables reemplazadas en plantilla (via reemplazaAlegacionEnviadaADepartamentoEnPlantilla):
	 *   - idalegacion -> alegacion.getCodNum().toString()
	 *   - nombre_area -> alegacion.getArea().getDescripcion()
	 *   - nombre_candidato -> alegacion.getCandidato().getNombre()
	 *
	 * Envía el mensaje a los directores del área de la alegación.
	 */
	public void notificarAlegacionEnviadaADepartamento(UsuarioBolsaEmpleo usuarioLogeado, Alegacion alegacion) throws SQLException, UVException {
		try {
			String idPlantillaStr = ModeloParametrosConfiguracion.obtenerInstancia()
					.getParametroByNombre(ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_ENVIADA_DEPARTAMENTO_ALEGACION).getValor();
			Integer idPlantilla = Integer.parseInt(idPlantillaStr);
			Plantilla plantilla = ModeloPlantilla.obtenerInstancia().getPlantillaById(idPlantilla);

			String titulo = ModeloPlantilla.obtenerInstancia().reemplazaAlegacionEnviadaADepartamentoEnPlantilla(alegacion, plantilla.getTitulo(), false);
			String cuerpo = ModeloPlantilla.obtenerInstancia().reemplazaAlegacionEnviadaADepartamentoEnPlantilla(alegacion, plantilla.getCuerpo(), true);

			Mensaje mensaje = new Mensaje();
			mensaje.setTitulo(titulo);
			mensaje.setCuerpo(cuerpo);
			mensaje.setEstado(ModeloMensajes.MENSAJE_ESTADO_ENVIANDO);
			mensaje.setFechaCreacion(BolsaEmpleoUtils.getCurrentDateTime());

			ModeloMensajes modelo = ModeloMensajes.obtenerInstancia();
			Integer idMensaje = modelo.nuevoMensaje(mensaje, usuarioLogeado);
			mensaje.setCodNum(idMensaje);

			// Enviar notificación a los directores del área
			try {
				List<UsuarioBolsaEmpleo> directores = ModeloDepartamento.obtenerInstancia().getDirectoresByAlegacion(alegacion);
				for (UsuarioBolsaEmpleo director : directores) {
					modelo.agregarDestinatarioUsuario(mensaje, director, usuarioLogeado);
				}
			} catch (UVException e) {
				LOGGER.log(Level.WARNING, "No hay directores asignados al área de la alegación: " + e.getMessage());
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al notificar alegación enviada a departamento", e);
			throw new UVException("Error al enviar la notificación al departamento.");
		}
	}

	/**
	 * Obtiene el fichero de una alegación de mérito.
	 * @param idAlegacion ID de la alegación en TBEP_SOL_MER_BOL_ALE_FILE
	 * @return InputStream con el contenido del fichero
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException en caso de error de parametros
	 */
	public InputStream getFicheroAlegacionMerito(Integer idAlegacion) throws SQLException, UVException {
		if (idAlegacion == null) {
			throw new UVException("El identificador de la alegación es obligatorio");
		}

		// Obtener el archivo usando FileSystemUtils, similar a ModeloMerito
		byte[] bytes = FileSystemUtils.obtenerFichero(
			Long.valueOf((long) idAlegacion),
			ESQUEMA_TBEP_SOL_MER_BOL_ALE_FILE,
			TABLA_TBEP_SOL_MER_BOL_ALE_FILE,
			ID_TBEP_SOL_MER_BOL_ALE_FILE
		);

		if (bytes == null) {
			return null;
		}

		return new ByteArrayInputStream(bytes);
	}

	/**
	 * Obtiene la Alegación asociada a un archivo de alegación de mérito.
	 *
	 * @param idArchivo Identificador del archivo de alegación (TBEP_SOL_MER_BOL_ALE_FILE.CODNUM)
	 * @return Objeto Alegacion con todos sus datos completos
	 * @throws SQLException Si ocurre un error de acceso a la base de datos
	 * @throws UVException Si el archivo no existe o no se puede obtener la alegación
	 */
	public Alegacion getAlegacionByArchivoId(Integer idArchivo) throws SQLException, UVException {
		if (idArchivo == null) {
			throw new UVException("El identificador del archivo de alegación es obligatorio.");
		}

		String consulta = "SELECT bepale.*,"
				+ " bepare.CODNUM AS AREA_CODNUM,"
				+ " bepare.ID_AREA_CONOCIMIENTO AS AREA_ID,"
				+ " bepare.DES_AREA_CONOCIMIENTO AS AREA_DESCRIPCION,"
				+ " bepcon.CODNUM AS CONVOCATORIA_CODNUM,"
				+ " bepusu.CODNUM AS CANDIDATO_CODNUM"
				+ " FROM TBEP_SOL_MER_BOL_ALE_FILE alf"
				+ " INNER JOIN TBEP_SOL_MER_BOL_ALEGACION alm ON alm.CODNUM = alf.BEPALM_CODNUM"
				+ " INNER JOIN TBEP_ALEGACIONES bepale ON bepale.CODNUM = alm.BEPALE_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " INNER JOIN TBEP_CONVOCATORIAS bepcon ON bepcon.CODNUM = bepsol.BEPCON_CODNUM"
				+ " WHERE alf.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, idArchivo);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Alegacion alegacion = new Alegacion();
					alegacion.setCodNum(rs.getInt("CODNUM"));
					alegacion.setBepsboCodNum(rs.getInt("BEPSBO_CODNUM"));
					alegacion.setEstado(rs.getString("ESTADO"));
					alegacion.setFechaCreacion(rs.getTimestamp("FECHACREACION"));
					alegacion.setFechaConfirmacion(rs.getTimestamp("FECHACONFIRMACION"));
					alegacion.setFechaEnvioDepartamento(rs.getTimestamp("FECHAENVIODEPARTAMENTO"));
					alegacion.setFechaResolucionDepartamento(rs.getTimestamp("FECHARESOLUCIONDEPARTAMENTO"));
					alegacion.setFechaResolucionFinal(rs.getTimestamp("FECHARESOLUCIONFINAL"));
					alegacion.setDesResolucionDep(rs.getString("DESRESOLUCIONDEP"));
					alegacion.setDesResolucionFinal(rs.getString("DESRESOLUCIONFINAL"));

					Area area = new Area();
					area.setCodNum(rs.getInt("AREA_CODNUM"));
					area.setIdAreaExterno(rs.getString("AREA_ID"));
					area.setDescripcion(rs.getString("AREA_DESCRIPCION"));
					alegacion.setArea(area);

					Convocatoria convocatoria = ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(rs.getInt("CONVOCATORIA_CODNUM"));
					alegacion.setConvocatoria(convocatoria);

					UsuarioBolsaEmpleo candidato = ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("CANDIDATO_CODNUM"));
					alegacion.setCandidato(candidato);

					return alegacion;
				}
			}
		}

		throw new UVException("No se encontró la alegación asociada al archivo con id " + idArchivo);
	}

	/**
	 * Comprueba si el candidato tiene una alegación confirmada (presentada) para una bolsa concreta.
	 * Una alegación se considera confirmada cuando existe y su estado es distinto de PENDIENTE.
	 *
	 * @param bean  Vista con el candidato y la solicitud del candidato
	 * @param bolsa Bolsa de la solicitud a comprobar
	 * @return true si existe una alegación confirmada, false en caso contrario
	 * @throws SQLException Si ocurre un error de acceso a la base de datos
	 */
	public boolean estaAlegacionConfirmada(VistaCandidatos bean, BolsaSolicitud bolsa) throws SQLException {
		if (bean == null || bean.getCandidato() == null || bean.getSolicitud() == null
				|| bean.getSolicitud().getConvocatoria() == null || bolsa == null) {
			return false;
		}
		Alegacion alegacion = getAlegacion(bolsa, bean.getCandidato(), bean.getSolicitud().getConvocatoria());
		return alegacion != null && !ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado());
	}

	/**
	 * Indica si la bolsa sigue en estado ALEGACIONES consultando el estado actual en BD.
	 */
	public boolean esBolsaEnEstadoAlegaciones(Integer bolsaCodNum) throws SQLException {
		if (bolsaCodNum == null) {
			return false;
		}

		String estadoBolsa = ModeloBolsa.obtenerInstancia().getEstadoPorCodNum(bolsaCodNum);
		return ModeloBolsa.BOLSA_ESTADO_ALEGACIONES.equals(estadoBolsa);
	}

	/**
	 * Indica si la fecha de fin de alegaciones de la bolsa ya ha pasado.
	 */
	public boolean esFechaFinAlegacionesSuperada(Integer bolsaCodNum) throws SQLException {
		if (bolsaCodNum == null) {
			return false;
		}

		String consulta = "SELECT FECHAFINALEGACIONES FROM TBEP_BOLSAS WHERE CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, bolsaCodNum);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					java.sql.Timestamp fechaFin = rs.getTimestamp("FECHAFINALEGACIONES");
					if (fechaFin != null && fechaFin.before(new java.util.Date())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Valida que una alegación pueda ser modificada por el candidato indicado.
	 * Lanza UVException si la alegación no pertenece al candidato o no está en estado PENDIENTE.
	 */
	public void validarAlegacionEditablePorCandidato(Integer aleCodNum, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		if (aleCodNum == null) {
			throw new UVException("El identificador de la alegación es obligatorio.");
		}
		if (candidato == null || candidato.getCodNum() == null) {
			throw new UVException("Usuario candidato inválido.");
		}
		Alegacion alegacion = getAlegacionByCodNum(aleCodNum);
		if (alegacion.getCandidato() == null || !alegacion.getCandidato().getCodNum().equals(candidato.getCodNum())) {
			throw new UVException("No tienes permiso para modificar esta alegación.");
		}
		if (!ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado())) {
			throw new UVException("No se puede modificar una alegación que no está en estado pendiente.");
		}
		validarAlegacionEditableEnEstadoAlegaciones(alegacion);
	}

	/**
	 * Valida que una alegación pueda ser confirmada por el candidato indicado.
	 */
	public void validarAlegacionConfirmablePorCandidato(Integer aleCodNum, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		if (aleCodNum == null) {
			throw new UVException("El identificador de la alegación es obligatorio.");
		}
		if (candidato == null || candidato.getCodNum() == null) {
			throw new UVException("Usuario candidato inválido.");
		}

		Alegacion alegacion = getAlegacionByCodNum(aleCodNum);
		if (alegacion.getCandidato() == null || !alegacion.getCandidato().getCodNum().equals(candidato.getCodNum())) {
			throw new UVException("No tienes permiso para confirmar esta alegación.");
		}
		if (!ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado())) {
			throw new UVException("No se puede confirmar una alegación que no está en estado pendiente.");
		}
		validarAlegacionEditableEnEstadoAlegaciones(alegacion);
	}

	/**
	 * Verifica que la bolsa asociada a una alegación está en el estado ALEGACIONES.
	 * Y no supoera la fecha de alegación final de la bolsa.
	 */
	private void validarAlegacionEditableEnEstadoAlegaciones(Alegacion alegacion) throws SQLException, UVException {
		if (alegacion == null || alegacion.getCodNum() == null) {
			throw new UVException("Alegación inválida.");
		}

		String consulta = "SELECT bepbol.ESTADO, bepbol.FECHAFINALEGACIONES "
				+ "FROM TBEP_ALEGACIONES bepale "
				+ "INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM "
				+ "INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM "
				+ "WHERE bepale.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, alegacion.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No se encontró la bolsa asociada a la alegación.");
				}
				String estadoBolsa = rs.getString("ESTADO");
				if (!ModeloBolsa.BOLSA_ESTADO_ALEGACIONES.equals(estadoBolsa)) {
					throw new UVException("No se puede modificar una alegación: la bolsa no está en estado ALEGACIONES.");
				}
				java.sql.Timestamp fechaFin = rs.getTimestamp("FECHAFINALEGACIONES");
				if (fechaFin != null && fechaFin.before(new java.util.Date())) {
					throw new UVException("El plazo para presentar alegaciones ha finalizado.");
				}
			}
		}
	}

	/**
	 * Reabre una alegación de bolsa para que el candidato pueda volver a presentarla.
	 * Se actualiza el estado a PENDIENTE y se registra el usuario que realiza el cambio.
	 *
	 * @param candidato   Candidato que presentó la solicitud
	 * @param convocatoria Convocatoria de la solicitud
	 * @param bolsa       Bolsa para la que se debe reabrir la alegación
	 * @param uidUsuario  Usuario que realiza la acción (se guarda en UID_USUARIO)
	 * @throws SQLException En caso de errores en la base de datos
	 * @throws UVException  En caso de datos inválidos o si no existe la alegación
	 */
	public void reabrirAlegacionBolsaCandidato(UsuarioBolsaEmpleo candidato, Convocatoria convocatoria, Bolsa bolsa, String uidUsuario) throws SQLException, UVException {
		if (candidato == null || candidato.getCodNum() == null || convocatoria == null || convocatoria.getCodNum() == null || bolsa == null || bolsa.getCodNum() == null || uidUsuario == null) {
			throw new UVException("No se puede reabrir alegación sin candidato, convocatoria, bolsa o usuario.");
		}

		Alegacion alegacion = getAlegacion(bolsa, candidato, convocatoria);
		if (alegacion == null) {
			throw new UVException("No existe una alegación para esta bolsa.");
		}

		// Validar que la bolsa asociada sigue en estado ALEGACIONES
		validarAlegacionEditableEnEstadoAlegaciones(alegacion);

		// Si ya está pendiente no hay nada que hacer
		if (ESTADO_PENDIENTE_ALEGACION.equals(alegacion.getEstado())) {
			return;
		}

		String consulta = "UPDATE TBEP_ALEGACIONES SET FECHACONFIRMACION = NULL, ESTADO = ?, UID_USUARIO = ? WHERE CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, ESTADO_PENDIENTE_ALEGACION);
			stmt.setString(2, uidUsuario);
			stmt.setInt(3, alegacion.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Listado de alegaciones del candidato logueado (DataTable AJAX).
	 */
	public BolsaEmpleoDataTable<Alegacion> listaAlegacionesCandidatoDatatable(UsuarioBolsaEmpleo candidato, Map<String, String[]> params) throws SQLException, UVException {
		List<Alegacion> alegaciones = new ArrayList<>();
		BolsaEmpleoDataTable<Alegacion> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepale.CODNUM, bepale.ESTADO, bepale.FECHACREACION, bepale.FECHACONFIRMACION,"
				+ " bepare.DES_AREA_CONOCIMIENTO AS AREA_DESCRIPCION, bepare.CODNUM AS AREA_CODNUM,"
				+ " bepcon.CODNUM AS CONVOCATORIA_CODNUM, bepcon.DESCRIPCION AS CONVOCATORIA_DESCRIPCION"
				+ " FROM TBEP_ALEGACIONES bepale"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_CONVOCATORIAS bepcon ON bepcon.CODNUM = bepsol.BEPCON_CODNUM"
				+ " WHERE bepsol.BEPUSU_CODNUM = ?";

		dataTable.setColumn(MIS_ORDER_COLUMN_INDEX_CONVOCATORIA, "bepcon.DESCRIPCION");
		dataTable.setColumn(MIS_ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(MIS_ORDER_COLUMN_INDEX_ESTADO, "bepale.ESTADO", DataTableColumn.COLUMN_TYPE_OPTION);
		dataTable.setColumn(MIS_ORDER_COLUMN_INDEX_FECHA_CREACION, "bepale.FECHACREACION", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(MIS_ORDER_COLUMN_INDEX_FECHA_CONFIRMACION, "bepale.FECHACONFIRMACION", DataTableColumn.COLUMN_TYPE_DATE);
		dataTable.setColumn(MIS_ORDER_COLUMN_INDEX_ID, "bepale.CODNUM", DataTableColumn.COLUMN_TYPE_IGNORE);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmtCount.setInt(paramIndex, candidato.getCodNum());
			stmt.setInt(paramIndex++, candidato.getCodNum());
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Alegacion ale = new Alegacion();
					ale.setCodNum(rs.getInt("CODNUM"));
					ale.setEstado(rs.getString("ESTADO"));
					ale.setFechaCreacion(rs.getTimestamp("FECHACREACION"));
					ale.setFechaConfirmacion(rs.getTimestamp("FECHACONFIRMACION"));

					Area area = new Area();
					area.setCodNum(rs.getInt("AREA_CODNUM"));
					area.setDescripcion(rs.getString("AREA_DESCRIPCION"));
					ale.setArea(area);

					Convocatoria convocatoria = new Convocatoria();
					convocatoria.setCodNum(rs.getInt("CONVOCATORIA_CODNUM"));
					convocatoria.setDescripcion(rs.getString("CONVOCATORIA_DESCRIPCION"));
					ale.setConvocatoria(convocatoria);

					alegaciones.add(ale);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(alegaciones);
		}

		return dataTable;
	}

	/**
	 * Obtiene el detalle de una alegación verificando que pertenece al candidato.
	 */
	public Alegacion obtenerDetalleAlegacionCandidato(Integer codNumAlegacion, UsuarioBolsaEmpleo candidato)
			throws SQLException, UVException {
		if (codNumAlegacion == null || candidato == null || candidato.getCodNum() == null) {
			throw new UVException("Parámetros inválidos para obtener la alegación.");
		}

		Alegacion alegacion = this.getAlegacionByCodNum(codNumAlegacion);
		if (alegacion.getCandidato() == null || !alegacion.getCandidato().getCodNum().equals(candidato.getCodNum())) {
			throw new UVException("No tienes permiso para ver esta alegación.");
		}

		return alegacion;
	}

	/**
	 * Guarda o actualiza la descripción general de una alegación (sin mérito específico).
	 */
	public void agregarDescripcionAlegacionGeneral(Integer bolsaCodNum, UsuarioBolsaEmpleo candidato,
			Convocatoria convocatoria, String descripcion) throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		validarAlegacionEditablePorCandidato(aleCodNum, candidato);
		upsertSolMerBolAlegacion(aleCodNum, null, descripcion, candidato.getCodCuenta());
	}

	/**
	 * Guarda o actualiza la descripción de una alegación para un mérito concreto.
	 */
	public void agregarDescripcionAlegacionMerito(Integer bolsaCodNum, UsuarioBolsaEmpleo candidato,
			Convocatoria convocatoria, Integer solBolMeritoCodNum, String descripcion) throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		validarAlegacionEditablePorCandidato(aleCodNum, candidato);
		upsertSolMerBolAlegacion(aleCodNum, solBolMeritoCodNum, descripcion, candidato.getCodCuenta());
	}

	/**
	 * Sube un fichero PDF para la alegación general.
	 */
	public void agregarFicheroAlegacionGeneral(Integer bolsaCodNum, UsuarioBolsaEmpleo candidato,
			Convocatoria convocatoria, InputStream fichero, String nombreFichero) throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		validarAlegacionEditablePorCandidato(aleCodNum, candidato);
		Integer solMerCodNum = obtenerOCrearSolMerBolAlegacion(aleCodNum, null, candidato.getCodCuenta());
		insertarArchivoAlegacion(solMerCodNum, nombreFichero, fichero, candidato.getCodCuenta());
	}

	/**
	 * Sube un fichero PDF para la alegación de un mérito concreto.
	 */
	public void agregarFicheroAlegacionMerito(Integer bolsaCodNum, UsuarioBolsaEmpleo candidato,
			Convocatoria convocatoria, Integer solBolMeritoCodNum, InputStream fichero, String nombreFichero)
			throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		validarAlegacionEditablePorCandidato(aleCodNum, candidato);
		Integer solMerCodNum = obtenerOCrearSolMerBolAlegacion(aleCodNum, solBolMeritoCodNum, candidato.getCodCuenta());
		insertarArchivoAlegacion(solMerCodNum, nombreFichero, fichero, candidato.getCodCuenta());
	}

	/**
	 * Confirma la alegación (cambia estado a PRESENTADA y establece fecha).
	 */
	public void confirmarAlegacion(Integer bolsaCodNum, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria)
			throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		validarAlegacionConfirmablePorCandidato(aleCodNum, candidato);

		String consulta = "UPDATE TBEP_ALEGACIONES SET ESTADO = 'PRESENTADA', FECHACONFIRMACION = SYSDATE, UID_USUARIO = ? "
				+ "WHERE CODNUM = ? AND ESTADO = 'PENDIENTE'";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, candidato.getCodCuenta());
			stmt.setInt(2, aleCodNum);
			int updated = stmt.executeUpdate();
			if (updated == 0) {
				throw new UVException("No se pudo confirmar la alegación. Puede que ya esté presentada.");
			}
		}
	}

	/**
	 * Elimina la alegación de un mérito concreto.
	 * Sólo se permite cuando la alegación principal está en estado PENDIENTE.
	 */
	public void eliminarAlegacionMerito(Alegacion alegacion, Integer solBolMeritoId, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (alegacion == null || alegacion.getCodNum() == null) {
			throw new UVException("Alegación inválida.");
		}
		if (solBolMeritoId == null) {
			throw new UVException("El identificador del mérito es obligatorio.");
		}
		if (usuario == null || usuario.getCodCuenta() == null) {
			throw new UVException("Usuario inválido.");
		}

		validarAlegacionEditablePorCandidato(alegacion.getCodNum(), usuario);

		// Primero actualizar UID_USUARIO para dejar registro, luego eliminar ficheros y registro de alegación.
		String updateFiles = "UPDATE TBEP_SOL_MER_BOL_ALE_FILE SET UID_USUARIO = ? WHERE BEPALM_CODNUM IN"
				+ " (SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM = ?)";
		String updateAlegacion = "UPDATE TBEP_SOL_MER_BOL_ALEGACION SET UID_USUARIO = ? WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM = ?";
		String deleteFiles = "DELETE FROM TBEP_SOL_MER_BOL_ALE_FILE WHERE BEPALM_CODNUM IN"
				+ " (SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM = ?)";
		String deleteAlegacion = "DELETE FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			try (PreparedStatement stmt = conexion.prepareStatement(updateFiles)) {
				stmt.setString(1, usuario.getCodCuenta());
				stmt.setInt(2, alegacion.getCodNum());
				stmt.setInt(3, solBolMeritoId);
				stmt.executeUpdate();
			}

			try (PreparedStatement stmt = conexion.prepareStatement(updateAlegacion)) {
				stmt.setString(1, usuario.getCodCuenta());
				stmt.setInt(2, alegacion.getCodNum());
				stmt.setInt(3, solBolMeritoId);
				stmt.executeUpdate();
			}

			try (PreparedStatement stmt = conexion.prepareStatement(deleteFiles)) {
				stmt.setInt(1, alegacion.getCodNum());
				stmt.setInt(2, solBolMeritoId);
				stmt.executeUpdate();
			}

			try (PreparedStatement stmt = conexion.prepareStatement(deleteAlegacion)) {
				stmt.setInt(1, alegacion.getCodNum());
				stmt.setInt(2, solBolMeritoId);
				stmt.executeUpdate();
			}
		}
	}

	/**
	 * Elimina los archivos de alegación por sus IDs.
	 *
	 * Sólo se permite cuando la alegación principal asociada está en estado PENDIENTE.
	 */
	public void eliminarArchivoAlegacion(List<Integer> ids, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		if (candidato == null || candidato.getCodNum() == null) {
			throw new UVException("Usuario candidato inválido.");
		}

		StringBuilder placeholders = new StringBuilder();
		for (int i = 0; i < ids.size(); i++) {
			if (i > 0) {
				placeholders.append(",");
			}
			placeholders.append("?");
		}

		String consultaAlegaciones = "SELECT DISTINCT a.CODNUM"
				+ " FROM TBEP_ALEGACIONES a"
				+ " INNER JOIN TBEP_SOL_MER_BOL_ALEGACION m ON m.BEPALE_CODNUM = a.CODNUM"
				+ " INNER JOIN TBEP_SOL_MER_BOL_ALE_FILE f ON f.BEPALM_CODNUM = m.CODNUM"
				+ " WHERE f.CODNUM IN (" + placeholders + ")";
		boolean hayAlegaciones = false;
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consultaAlegaciones)) {
			for (int i = 0; i < ids.size(); i++) {
				stmt.setInt(i + 1, ids.get(i));
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					hayAlegaciones = true;
					validarAlegacionEditablePorCandidato(rs.getInt("CODNUM"), candidato);
				}
			}
		}

		if (!hayAlegaciones) {
			throw new UVException("No se encontraron archivos de alegación para eliminar.");
		}

		String consulta = "DELETE FROM TBEP_SOL_MER_BOL_ALE_FILE WHERE CODNUM IN (" + placeholders + ")";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			for (int i = 0; i < ids.size(); i++) {
				stmt.setInt(i + 1, ids.get(i));
			}
			stmt.executeUpdate();
		}
	}

	/**
	 * DataTable de ficheros de la alegación general.
	 */
	public BolsaEmpleoDataTable<ArchivoAlegacionGeneral> datatableFicherosAlegacionGeneral(Integer bolsaCodNum,
			UsuarioBolsaEmpleo candidato, Convocatoria convocatoria, Map<String, String[]> params)
			throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		return datatableFicheros(aleCodNum, null, params);
	}

	/**
	 * DataTable de ficheros de la alegación de un mérito.
	 */
	public BolsaEmpleoDataTable<ArchivoAlegacionGeneral> datatableFicherosAlegacionMerito(Integer bolsaCodNum,
			Integer solBolMeritoCodNum, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria,
			Map<String, String[]> params) throws SQLException, UVException {
		Integer aleCodNum = obtenerAleCodNum(bolsaCodNum, candidato, convocatoria);
		return datatableFicheros(aleCodNum, solBolMeritoCodNum, params);
	}

	// ======================== Métodos privados auxiliares ========================

	private Integer obtenerAleCodNum(Integer bolsaCodNum, UsuarioBolsaEmpleo candidato, Convocatoria convocatoria) throws SQLException, UVException {
		String consulta = "SELECT bepale.CODNUM"
				+ " FROM TBEP_ALEGACIONES bepale"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepale.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ " WHERE bepsol.BEPUSU_CODNUM = ? AND bepsol.BEPCON_CODNUM = ? AND bepsbo.BEPBOL_CODNUM = ?"
				+ " ORDER BY bepale.CODNUM DESC FETCH FIRST 1 ROW ONLY";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, candidato.getCodNum());
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, bolsaCodNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("CODNUM");
				}
			}
		}

		throw new UVException("No se encontró una alegación para esta solicitud de bolsa.");
	}

	/**
	 * Inserta o actualiza la descripción de la alegación (general o por mérito).
	 *
	 * Si ya existe un registro en TBEP_SOL_MER_BOL_ALEGACION para la combinación
	 * (BEPALE_CODNUM, BEPSBM_CODNUM) se actualiza su campo DESCRIPCION.
	 * En caso contrario, se crea un nuevo registro con la descripción y el usuario.
	 *
	 * @param aleCodNum    ID de la alegación (TBEP_ALEGACIONES.CODNUM)
	 * @param bepsbmCodNum ID del mérito (TBEP_SOL_MER_BOL_ALEGACION.BEPSBM_CODNUM),
	 *                     o {@code null} para la alegación general
	 * @param descripcion  Descripción a almacenar (puede ser {@code null} para limpiar)
	 * @param uidUsuario   Usuario que realiza la modificación
	 * @throws SQLException en caso de error en la base de datos
	 */
	private void upsertSolMerBolAlegacion(Integer aleCodNum, Integer bepsbmCodNum, String descripcion, String uidUsuario) throws SQLException, UVException {
		// Verificar estado de la alegación: sólo se permiten cambios en PENDIENTE
		String estadoSelect = "SELECT ESTADO FROM TBEP_ALEGACIONES WHERE CODNUM = ?";

		// Verificar si ya existe
		String select;
		if (bepsbmCodNum != null) {
			select = "SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM = ?";
		} else {
			select = "SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM IS NULL";
		}

		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			try (PreparedStatement stmtChk = conexion.prepareStatement(estadoSelect)) {
				stmtChk.setInt(1, aleCodNum);
				try (ResultSet rsChk = stmtChk.executeQuery()) {
					if (rsChk.next()) {
						String estado = rsChk.getString("ESTADO");
						if (!ESTADO_PENDIENTE_ALEGACION.equals(estado)) {
							throw new UVException("No se puede modificar una alegación que no está en estado pendiente.");
						}
					} else {
						throw new UVException("No se encontró la alegación con id " + aleCodNum);
					}
				}
			}

			Integer existingCodNum = null;
			try (PreparedStatement stmtSel = conexion.prepareStatement(select)) {
				stmtSel.setInt(1, aleCodNum);
				if (bepsbmCodNum != null) {
					stmtSel.setInt(2, bepsbmCodNum);
				}
				try (ResultSet rs = stmtSel.executeQuery()) {
					if (rs.next()) {
						existingCodNum = rs.getInt("CODNUM");
					}
				}
			}

			if (existingCodNum != null) {
				String update = "UPDATE TBEP_SOL_MER_BOL_ALEGACION SET DESCRIPCION = ?, UID_USUARIO = ? WHERE CODNUM = ?";
				try (PreparedStatement stmtUpd = conexion.prepareStatement(update)) {
					if (descripcion != null) {
						stmtUpd.setString(1, descripcion);
					} else {
						stmtUpd.setNull(1, Types.CLOB);
					}
					stmtUpd.setString(2, uidUsuario);
					stmtUpd.setInt(3, existingCodNum);
					stmtUpd.executeUpdate();
				}
			} else {
				String insert = "INSERT INTO TBEP_SOL_MER_BOL_ALEGACION (BEPALE_CODNUM, BEPSBM_CODNUM, DESCRIPCION, UID_USUARIO) VALUES (?, ?, ?, ?)";
				try (PreparedStatement stmtIns = conexion.prepareStatement(insert)) {
					stmtIns.setInt(1, aleCodNum);
					if (bepsbmCodNum != null) {
						stmtIns.setInt(2, bepsbmCodNum);
					} else {
						stmtIns.setNull(2, Types.INTEGER);
					}
					if (descripcion != null) {
						stmtIns.setString(3, descripcion);
					} else {
						stmtIns.setNull(3, Types.CLOB);
					}
					stmtIns.setString(4, uidUsuario);
					stmtIns.executeUpdate();
				}
			}
		}
	}

	private Integer obtenerOCrearSolMerBolAlegacion(Integer aleCodNum, Integer bepsbmCodNum, String uidUsuario) throws SQLException {
		String select;
		if (bepsbmCodNum != null) {
			select = "SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM = ?";
		} else {
			select = "SELECT CODNUM FROM TBEP_SOL_MER_BOL_ALEGACION WHERE BEPALE_CODNUM = ? AND BEPSBM_CODNUM IS NULL";
		}

		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			try (PreparedStatement stmtSel = conexion.prepareStatement(select)) {
				stmtSel.setInt(1, aleCodNum);
				if (bepsbmCodNum != null) {
					stmtSel.setInt(2, bepsbmCodNum);
				}
				try (ResultSet rs = stmtSel.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("CODNUM");
					}
				}
			}

			String insert = "INSERT INTO TBEP_SOL_MER_BOL_ALEGACION (BEPALE_CODNUM, BEPSBM_CODNUM, UID_USUARIO) VALUES (?, ?, ?)";
			try (PreparedStatement stmtIns = conexion.prepareStatement(insert, new String[]{"CODNUM"})) {
				stmtIns.setInt(1, aleCodNum);
				if (bepsbmCodNum != null) {
					stmtIns.setInt(2, bepsbmCodNum);
				} else {
					stmtIns.setNull(2, Types.INTEGER);
				}
				stmtIns.setString(3, uidUsuario);
				stmtIns.executeUpdate();
				try (ResultSet rs = stmtIns.getGeneratedKeys()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}

		return null;
	}

	/**
	 * Inserta el archivo de alegación en BD y lo guarda en disco.
	 * CORREGIDO: Ahora guarda el fichero en FileSystem usando FileSystemUtils.grabarFichero()
	 */
	private void insertarArchivoAlegacion(Integer solMerCodNum, String nombreFichero, InputStream fichero, String uidUsuario) throws SQLException, UVException {
		String insert = "INSERT INTO TBEP_SOL_MER_BOL_ALE_FILE (BEPALM_CODNUM, NOMBRE_ARCHIVO, UID_USUARIO) VALUES (?, ?, ?)";

		Connection conexion = null;
		PreparedStatement stmt = null;
		try {
			conexion = ConexionUvirtual.obtenerInstancia();
			conexion.setAutoCommit(false);

			// Comprobar que la alegación asociada a este BEPALM (solMerCodNum) está en estado PENDIENTE
			try (PreparedStatement stmtChk = conexion.prepareStatement(
					"SELECT a.ESTADO FROM TBEP_ALEGACIONES a INNER JOIN TBEP_SOL_MER_BOL_ALEGACION m ON m.BEPALE_CODNUM = a.CODNUM WHERE m.CODNUM = ?")) {
				stmtChk.setInt(1, solMerCodNum);
				try (ResultSet rsChk = stmtChk.executeQuery()) {
					if (rsChk.next()) {
						String estado = rsChk.getString("ESTADO");
						if (!ESTADO_PENDIENTE_ALEGACION.equals(estado)) {
							throw new UVException("No se puede añadir ficheros a una alegación que no está en estado pendiente");
						}
					} else {
						throw new UVException("No existe la entrada de alegación asociada al archivo");
					}
				}
			}
			stmt = conexion.prepareStatement(insert, new String[]{ID_TBEP_SOL_MER_BOL_ALE_FILE});

			stmt.setInt(1, solMerCodNum);
			stmt.setString(2, nombreFichero);
			stmt.setString(3, uidUsuario);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				Integer codnum = rs.getInt(1);
				FileSystemUtils.grabarFichero(Long.valueOf((long) codnum), ESQUEMA_TBEP_SOL_MER_BOL_ALE_FILE, TABLA_TBEP_SOL_MER_BOL_ALE_FILE, ID_TBEP_SOL_MER_BOL_ALE_FILE, FileSystemUtils.fromInputStreamToByteArray(fichero));
				conexion.commit();
			}
		} catch(IOException e) {
			LOGGER.log(Level.SEVERE, "insertarArchivoAlegacion - Error al grabar archivo: " + e.getMessage());
			if (conexion != null) try { conexion.rollback(); } catch(Exception e2) {};
			throw new UVException("Error al grabar el fichero.");
		} finally {
			if (stmt != null) try { stmt.close(); } catch (Exception e2) {};
			if (conexion != null) try { conexion.setAutoCommit(true); conexion.close(); } catch(Exception e2) {};
		}
	}

	private BolsaEmpleoDataTable<ArchivoAlegacionGeneral> datatableFicheros(Integer aleCodNum, Integer bepsbmCodNum, Map<String, String[]> params) throws SQLException, UVException {
		List<ArchivoAlegacionGeneral> archivos = new ArrayList<>();
		BolsaEmpleoDataTable<ArchivoAlegacionGeneral> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT f.CODNUM, f.NOMBRE_ARCHIVO"
				+ " FROM TBEP_SOL_MER_BOL_ALE_FILE f"
				+ " INNER JOIN TBEP_SOL_MER_BOL_ALEGACION bepalm ON bepalm.CODNUM = f.BEPALM_CODNUM"
				+ " WHERE bepalm.BEPALE_CODNUM = ?";

		if (bepsbmCodNum != null) {
			consulta += " AND bepalm.BEPSBM_CODNUM = ?";
		} else {
			consulta += " AND bepalm.BEPSBM_CODNUM IS NULL";
		}

		dataTable.setColumn(0, "f.NOMBRE_ARCHIVO");
		dataTable.setColumn(1, "f.CODNUM", DataTableColumn.COLUMN_TYPE_IGNORE);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmtCount.setInt(paramIndex, aleCodNum);
			stmt.setInt(paramIndex++, aleCodNum);
			if (bepsbmCodNum != null) {
				stmtCount.setInt(paramIndex, bepsbmCodNum);
				stmt.setInt(paramIndex++, bepsbmCodNum);
			}
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ArchivoAlegacionGeneral archivo = new ArchivoAlegacionGeneral();
					archivo.setId(rs.getInt("CODNUM"));
					archivo.setNombre(rs.getString("NOMBRE_ARCHIVO"));
					archivos.add(archivo);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(archivos);
		}

		return dataTable;
	}
}

