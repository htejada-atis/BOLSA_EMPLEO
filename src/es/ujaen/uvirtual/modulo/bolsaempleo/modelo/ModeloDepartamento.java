package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modelo.conexion.ConexionUxxiRrhh;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de departamentos.
 *
 * @author ATISoluciones 2021
 */
public class ModeloDepartamento {

	public static final String MENSAJE_DEPARTAMENTO_NO_EXISTE = "No existe el departamento";
	public static final String MENSAJE_ERROR_DEPARTAMENTO_REQUERIDO = "El departamento es requerido";

	protected static ModeloDepartamento eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloDepartamento();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 *
	 * @return instancia
	 */
	public static ModeloDepartamento obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Consulta departamentos en BBDD y los devuelve.
	 *
	 * @return departamentos de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  .
	 */
	public List<Departamento> listaDepartamentos() throws SQLException {
		List<Departamento> departamentos = new ArrayList<>();
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep ";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Departamento dep = this.createDepartamentoFromResultSet(rs);
					departamentos.add(dep);
				}
			}
		}
		return departamentos;
	}

	/**
	 * Consulta departamentos en BBDD y los devuelve ordenados por descripción .
	 *
	 * @return departamentos de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  .
	 */
	public List<Departamento> listaDepartamentosOrderByDesc() throws SQLException {
		List<Departamento> departamentos = new ArrayList<>();
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep ORDER BY DES_DEPARTAMENTO ";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Departamento dep = new Departamento();
					dep.setCodNum(rs.getInt("CODNUM"));
					dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
					dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
					departamentos.add(dep);
				}
			}
		}
		return departamentos;
	}

	/** Listado de departamentos .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param director .
	 * @return listado de departamentos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<Departamento> listaDepartamentosDirectorDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo director) throws SQLException, UVException {
		List<Departamento> departamentos = new ArrayList<>();
		BolsaEmpleoDataTable<Departamento> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT * FROM UXXIRRHH.VUJA_NET_BEP_RH_DEP_DIR"
				+ " WHERE PRSNIF = ? ORDER BY ID_DEPARTAMENTO ";

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUxxiRrhh.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setString(indexParam, director.getPrsNif());
			stmtCount.setString(indexParam++, director.getPrsNif());

			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Departamento departamento = this.getDepartamentoByIdExterno(rs.getString("ID_DEPARTAMENTO"));
					departamentos.add(departamento);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(departamentos);
		}

		return dataTable;
	}

	/**
	 * Devuelve la lista de departamentos de un director .
	 * @param director .
	 * @return departamentos .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si area no es existe .
	 */
	public List<Departamento> listaDepartamentosDirector(UsuarioBolsaEmpleo director) throws SQLException {
		List<Departamento> departamentos = new ArrayList<>();
		String consulta = "SELECT * FROM UXXIRRHH.VUJA_NET_BEP_RH_DEP_DIR"
				+ " WHERE PRSNIF = ?";

		try (Connection conexion = ConexionUxxiRrhh.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int indexParam = 1;
			stmt.setString(indexParam, director.getPrsNif());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Departamento departamento = this.getDepartamentoByIdExterno(rs.getString("ID_DEPARTAMENTO"));
					departamentos.add(departamento);
				}
			}
		}

		return departamentos;
	}

	/**
	 * Devuelve un departamento por su id.
	 *
	 * @param idDepartamento id del departamento .
	 * @return departamento .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si area no es existe .
	 */
	public Departamento getDepartamentoByCodNum(Integer idDepartamento) throws SQLException, UVException {
		if (idDepartamento == null) {
			throw new UVException(MENSAJE_ERROR_DEPARTAMENTO_REQUERIDO);
		}

		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, idDepartamento);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_DEPARTAMENTO_NO_EXISTE);
				}
				return createDepartamentoFromResultSet(rs);
			}
		}
	}

	/**
	 * Devuelve un departamento por su id externo.
	 *
	 * @param idDepartamento id del departamento .
	 * @return departamento .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si area no es existe .
	 */
	public Departamento getDepartamentoByIdExterno(String idDepartamento) throws SQLException {
		String consulta = "SELECT bepdep.* FROM TBEP_DEPARTAMENTOS bepdep WHERE bepdep.ID_DEPARTAMENTO = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, idDepartamento);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				return createDepartamentoFromResultSet(rs);
			}
		}
	}

/**
 	* Devuelve la lista de departamentos asociados a un área.
 	*
 	* @param area área cuyos departamentos se desean obtener.
 	* @return lista de departamentos asociados al área.
 	* @throws SQLException en caso de error en la BD.
 	* @throws UVException si no existen departamentos asociados al área.
 	*/
	public List<Departamento> getDepartamentosByArea(Area area) throws SQLException, UVException {
		String consulta = "SELECT dep.* FROM TBEP_DEPARTAMENTOS dep "
				+ "INNER JOIN TBEP_AREAS_DEPARTAMENTOS ad ON ad.BEPDEP_CODNUM = dep.CODNUM "
				+ "WHERE ad.BEPARE_CODNUM = ?";
		List<Departamento> departamentos = new ArrayList<>();
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, area.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Departamento departamento = createDepartamentoFromResultSet(rs);
					departamentos.add(departamento);
				}
			}
		}
		if (departamentos.isEmpty()) {
			throw new UVException("No se encontraron departamentos asociados al área especificada.");
		}
		return departamentos;
	}
	
	/**
 	 * Obtiene los directores (usuarios con rol de director de departamento) asignados
 	 * al área asociada a una alegación.
 	 *
 	 * @param alegacion alegación que contiene el área de interés.
 	 * @return lista de usuarios que son directores para el área de la alegación.
 	 * @throws SQLException en caso de error de acceso a la base de datos.
 	 * @throws UVException si la alegación no tiene área o no se encuentran directores para el área.
 	 */
	public List<UsuarioBolsaEmpleo> getDirectoresByAlegacion(Alegacion alegacion) throws SQLException, UVException {
		Area area = alegacion.getArea();
		if (area == null) {
			throw new UVException("La alegación no tiene un área asociada.");
		}
		ModeloUsuarioBolsaEmpleo modeloUsuarioBolsaEmpleo = ModeloUsuarioBolsaEmpleo.obtenerInstancia(); 
		Integer areaCodNum = area.getCodNum();
		List<UsuarioBolsaEmpleo> directores = new ArrayList<>();
		String consultaEvaluadores = "SELECT u.* " 
				+ "FROM TBEP_EVALUADORES bepeva "
				+ "JOIN TBEP_USUARIOS u ON bepeva.BEPUSU_CODNUM = u.CODNUM " 
				+ "WHERE bepeva.BEPARE_CODNUM = ? "
				+ "AND bepeva.FLGACTIVO = 'S' " +
				"AND u.ROL = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consultaEvaluadores)) {			
			stmt.setInt(1, areaCodNum);
			stmt.setInt(2, ModeloRol.ID_ROL_DIRECTOR_DEPARTAMENTO);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo director = modeloUsuarioBolsaEmpleo.getUsuarioFromResultSet(rs);
					directores.add(director);
				}
			}
		}
		
		if (directores.isEmpty()) {
			throw new UVException("Al intentar enviar la notificación no se encontraron directores para el área de la alegación.");
		}
		
		return directores;
	}
	
	/**
	 * Obtiene los directores (UsuarioBolsaEmpleo) asociados a una lista de departamentos.
	 * 
	 * @param departamentos Lista de departamentos de los cuales obtener los directores.
	 * @return Lista de UsuarioBolsaEmpleo correspondiente a los directores asociados.
	 * @throws SQLException Si ocurre algún problema de acceso a la base de datos.
	 * @throws UVException Si no se encuentra ningún director asociado.
	 */
	public List<UsuarioBolsaEmpleo> getDirectoresByDepartamentos(List<Departamento> departamentos)
			throws SQLException, UVException {
		if (departamentos == null || departamentos.isEmpty()) {
			throw new UVException("La lista de departamentos está vacía.");
		}
		
		ModeloUsuarioBolsaEmpleo modeloUsuarioBolsaEmpleo = ModeloUsuarioBolsaEmpleo.obtenerInstancia();
		List<UsuarioBolsaEmpleo> directores = new ArrayList<>();

		String consultaDepartamentos = "SELECT depDir.PRSNIF FROM UXXIRRHH.VUJA_NET_BEP_RH_DEP_DIR depDir "
				+ "WHERE depDir.ID_DEPARTAMENTO IN ("
				+ departamentos.stream().map(d -> "?").collect(Collectors.joining(", ")) + ")";

		// Usamos una conexión para el esquema UXXIRRHH
		try (Connection conexionUxxiRrhh = ConexionUxxiRrhh.obtenerInstancia();
				PreparedStatement stmtDept = conexionUxxiRrhh.prepareStatement(consultaDepartamentos)) {
			// Establecer los parámetros de la consulta de departamentos
			int index = 1;
			for (Departamento departamento : departamentos) {
				stmtDept.setString(index++, departamento.getIdDepartamentoExterno());
			}
			// Ejecutar la consulta y obtener los resultados de los `PRSNIF`
			List<String> prsnifs = new ArrayList<>();
			try (ResultSet rsDept = stmtDept.executeQuery()) {
				while (rsDept.next()) {
					prsnifs.add(rsDept.getString("PRSNIF"));
				}
			}
			if (prsnifs.isEmpty()) {
				throw new UVException("No se encontraron PRSNIF para los departamentos especificados.");
			}
			// Consulta para obtener los directores de la tabla TBEP_USUARIOS
			String consultaDirectores = "SELECT dir.* FROM uvirtual.TBEP_USUARIOS dir " + "WHERE dir.VUAJA_PRSNIF IN ("
					+ prsnifs.stream().map(p -> "?").collect(Collectors.joining(", ")) + ")";
			// Usamos una conexión para el esquema uvirtual
			try (Connection conexionUvirtual = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmtDirectores = conexionUvirtual.prepareStatement(consultaDirectores)) {
				// Establecer los parámetros de la consulta de directores
				int index2 = 1;
				for (String prsnif : prsnifs) {
					stmtDirectores.setString(index2++, prsnif);
				}
				// Ejecutar la consulta para obtener los directores
				try (ResultSet rsDirectores = stmtDirectores.executeQuery()) {
					while (rsDirectores.next()) {
						UsuarioBolsaEmpleo director = modeloUsuarioBolsaEmpleo.getUsuarioFromResultSet(rsDirectores);
						directores.add(director);
					}
				}
			}
		}
		if (directores.isEmpty()) {
			throw new UVException("No se encontraron directores asociados a los departamentos especificados.");
		}
		return directores;
	}

	/**
	 * Función que inserta un departamento en la BD.
	 *
	 * @param conexion     .
	 * @param departamento a insertar en la BD .
	 * @param usuario .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  en caso de error de parametros .
	 */
	public void insertaDepartamento(Connection conexion, Departamento departamento, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (departamento == null) {
			throw new UVException("No se puede insertar un departamento vacio");
		}
		if (departamento.getIdDepartamentoExterno() == null || "".equals(departamento.getIdDepartamentoExterno())) {
			throw new UVException("No se puede insertar un departamento sin id externo");
		}

		String consulta = "INSERT INTO TBEP_DEPARTAMENTOS (ID_DEPARTAMENTO, DES_DEPARTAMENTO, UID_USUARIO) VALUES (?, ?, ?)";

		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, departamento.getIdDepartamentoExterno());
			stmt.setString(parameterIndex++, departamento.getDescripcion());
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.executeUpdate();
		}
	}

	/**
	 * Verifica si un director de departamento tiene acceso a un área específica.
	 * El director tiene acceso si el área está asignada a uno de sus departamentos.
	 *
	 * @param director Usuario director a verificar.
	 * @param area Área para la cual verificar el acceso.
	 * @return true si el director tiene acceso al área, false en caso contrario.
	 * @throws SQLException En caso de error al acceder a la base de datos.
	 */
	public boolean tieneAccesoDirectorAArea(UsuarioBolsaEmpleo director, Area area) throws SQLException {
		if (director == null || area == null) {
			return false;
		}
		
		List<Departamento> departamentosDirector = listaDepartamentosDirector(director);
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();
		
		for (Departamento departamento : departamentosDirector) {
			List<Area> areasDelDepartamento = modeloArea.getAreasByDepartamento(departamento);
			for (Area areaDelDepartamento : areasDelDepartamento) {
				if (areaDelDepartamento.getCodNum().equals(area.getCodNum())) {
					return true;
				}
			}
		}
		
		return false;
	}

	private Departamento createDepartamentoFromResultSet(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setCodNum(rs.getInt("CODNUM"));
		dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
		dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
		return dep;
	}
}
