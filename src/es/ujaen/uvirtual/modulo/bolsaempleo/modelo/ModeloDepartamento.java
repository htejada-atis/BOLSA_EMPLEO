package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modelo.conexion.ConexionUxxiRrhh;
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
				+ " WHERE PRSNIF = ?";
		
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

	private Departamento createDepartamentoFromResultSet(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setCodNum(rs.getInt("CODNUM"));
		dep.setIdDepartamentoExterno(rs.getString("ID_DEPARTAMENTO"));
		dep.setDescripcion(rs.getString("DES_DEPARTAMENTO"));
		return dep;
	}
}
