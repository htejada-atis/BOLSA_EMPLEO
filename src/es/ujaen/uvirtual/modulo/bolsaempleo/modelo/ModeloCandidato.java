package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Candidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL. Modelo -
 * Operaciones con nombres: lista, actualiza, borra, inserta Controlador -
 * Opers. con nombres: obtener, cambiar, eliminar, agregar
 * 
 * @author ATISoluciones 2021
 */
public class ModeloCandidato {

	public static final int ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO = 0;
	public static final int ORDER_COLUMN_INDEX_TITULACIONES_CANDIDATO = 1;
	public static final int ORDER_COLUMN_INDEX_TIT_VALIDADAS_CANDIDATO = 2;

	protected static ModeloCandidato eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de candidatos en función de un área .
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de candidatos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 */
	public BolsaEmpleoDataTable<Candidato> listaCandidatosDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Candidato> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<Candidato> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepusu.CODNUM, bepusu.PRSNIF, "
				+ "	(SELECT COUNT(*) FROM UVIRTUAL.TBEP_TITULACIONES_USUARIO beptus"
				+ "		WHERE beptus.BEPTUS_USU_CODNUM = bepusu.CODNUM" + "	) AS COUNT_TITULACIONES,"
				+ "	(SELECT COUNT(*) FROM UVIRTUAL.TBEP_TITULACIONES_USUARIO beptus"
				+ "		WHERE beptus.BEPTUS_USU_CODNUM = bepusu.CODNUM AND beptus.FLGVALIDADA = 'S'"
				+ "	) AS COUNT_VALIDADAS" + "	FROM UVIRTUAL.TBEP_USUARIOS bepusu" + "	WHERE bepusu.rol = "
				+ ModeloRol.ID_ROL_CANDIDATO + " ";

		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_CANDIDATO, "bepusu.CODCUENTA");

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

					try (Connection conexionArcos = ConexionArcos.obtenerInstancia();
							PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
						stmtArcos.setString(1, rs.getString("PRSNIF"));
						try (ResultSet rsArcos = stmtArcos.executeQuery()) {
							while (rsArcos.next()) {
								UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
								usuario.setCodNum(rs.getInt("CODNUM"));
								usuario.setNombre(rsArcos.getString("STRNOMBRE"));
								usuario.setPrimerApellido(rsArcos.getString("STRAPELLIDO1"));
								usuario.setSegundoApellido(rsArcos.getString("STRAPELLIDO2"));

								Integer titulaciones = rs.getInt("COUNT_TITULACIONES");
								Integer validadas = rs.getInt("COUNT_VALIDADAS");

								usuarios.add(new Candidato(usuario, titulaciones, validadas));
							}
						}
					}
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}

		return dataTable;
	}

}
