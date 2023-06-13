package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialTableMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialTableSolicitudBolsaMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.HistorialTableSolicitudBolsaMeritoValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para gestionar los historiales .
 * 
 * @author ATISoluciones
 */
public class ModeloHistorial {
	protected static ModeloHistorial eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloHistorial();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloHistorial obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Listado de historial de tabla de méritos.
	 * 
	 * @param params .
	 * @param merito .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public BolsaEmpleoDataTable<HistorialTableMerito> listaTablaMeritos(Map<String, String[]> params, Merito merito)
			throws SQLException, UVException {
		List<HistorialTableMerito> data = new ArrayList<>();
		BolsaEmpleoDataTable<HistorialTableMerito> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT his_bepmer.* FROM TBEP_HTO_MERITOS his_bepmer WHERE his_bepmer.CODNUM = ? ORDER BY his_bepmer.CODCAMBIO desc";

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			int param = 1;
			stmt.setInt(param, merito.getCodNum());
			stmtCount.setInt(param++, merito.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = this.getUsuarioBolsa(rs.getString("UID_USUARIO"));

					HistorialTableMerito historial = new HistorialTableMerito();
					historial.setCodCambio(rs.getInt("CODCAMBIO"));
					historial.setLog(rs.getString("LOG"));
					historial.setFechaLog(rs.getDate("FECHALOG"));
					historial.setUidUsuario(usuario.getCodCuenta());
					historial.setRolUsuario(usuario.getRol().getValor());

					historial.setCodNum(rs.getInt("CODNUM"));

					historial.setBepIteCodNum(rs.getInt("BEPITE_CODNUM"));
					historial.setItem(
							ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));

					historial.setBepEsuCodNum(rs.getInt("BEPUSU_CODNUM"));
					historial.setUsuario(
							ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));

					historial.setValor(rs.getDouble("VALOR"));
					historial.setDescripcion(rs.getString("DESCRIPCION"));
					historial.setObservacion(rs.getString("OBSERVACION"));
					data.add(historial);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}

		return dataTable;
	}

	/**
	 * Devuelve el historico de solicitud bolsa mérito.
	 * 
	 * @param params          .
	 * @param meritoSolicitud .
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public BolsaEmpleoDataTable<HistorialTableSolicitudBolsaMerito> listaTablaSolicitudBolsaMerito(
			Map<String, String[]> params, MeritoSolicitud meritoSolicitud) throws SQLException, UVException {
		List<HistorialTableSolicitudBolsaMerito> data = new ArrayList<>();
		BolsaEmpleoDataTable<HistorialTableSolicitudBolsaMerito> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT his_bepsbm.* FROM TBEP_HTO_SOL_BOL_MERITOS his_bepsbm WHERE his_bepsbm.CODNUM = ? ORDER BY his_bepsbm.CODCAMBIO desc";

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			int param = 1;
			stmt.setInt(param, meritoSolicitud.getCodNum());
			stmtCount.setInt(param++, meritoSolicitud.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					data.add(this.getHistorialTableSolicitudBolsaMeritoFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}

		return dataTable;
	}

	/**
	 * Devuleve el historial de la tabla: TBEP_SOL_BOL_MER_VALORACION.
	 * 
	 * @param params          .
	 * @param meritoSolicitud .
	 * @return .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public BolsaEmpleoDataTable<HistorialTableSolicitudBolsaMeritoValoracion> listaTablaSolicitudBolsaMeritoValoracion(
			Map<String, String[]> params, MeritoSolicitud meritoSolicitud) throws SQLException, UVException {
		List<HistorialTableSolicitudBolsaMeritoValoracion> data = new ArrayList<>();
		BolsaEmpleoDataTable<HistorialTableSolicitudBolsaMeritoValoracion> dataTable = new BolsaEmpleoDataTable<>(
				params);

		String consulta = "SELECT his_bepsbv.* FROM TBEP_HTO_SOL_BOL_MER_VALORA his_bepsbv WHERE his_bepsbv.BEPSBM_CODNUM = ? ORDER BY his_bepsbv.CODCAMBIO desc";

		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			int param = 1;
			stmt.setInt(param, meritoSolicitud.getCodNum());
			stmtCount.setInt(param++, meritoSolicitud.getCodNum());

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					data.add(this.getHistorialTableSolicitudBolsaMeritoValoracionFromResulset(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}

		return dataTable;
	}

	private UsuarioBolsaEmpleo getUsuarioBolsa(String codUsuario) throws SQLException, UVException {
		Integer uidUsuario = Formateador.leeParametroInteger(codUsuario);

		if (uidUsuario != null) {
			return ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(uidUsuario);
		} else {
			Rol rol = new Rol();
			rol.setValor("");

			UsuarioBolsaEmpleo noUser = new UsuarioBolsaEmpleo();
			noUser.setCodCuenta(codUsuario);
			noUser.setRol(rol);

			return noUser;
		}
	}

	private HistorialTableSolicitudBolsaMerito getHistorialTableSolicitudBolsaMeritoFromResultSet(ResultSet rs)
			throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = this.getUsuarioBolsa(rs.getString("UID_USUARIO"));

		HistorialTableSolicitudBolsaMerito historial = new HistorialTableSolicitudBolsaMerito();
		historial.setCodCambio(rs.getInt("CODCAMBIO"));
		historial.setLog(rs.getString("LOG"));
		historial.setFechaLog(rs.getDate("FECHALOG"));
		historial.setUidUsuario(usuario.getCodCuenta());
		historial.setRolUsuario(usuario.getRol().getValor());

		historial.setCodNum(rs.getInt("CODNUM"));
		historial.setBepsboCodNum(rs.getInt("BEPSBO_CODNUM"));

		historial.setBepmerCodNum(rs.getInt("BEPMER_CODNUM"));
		historial.setMerito(ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt("BEPMER_CODNUM"), false, false));

		historial.setFlgExcluido(rs.getString("FLGEXCLUIDO").equals("S"));
		historial.setFlgValidado(rs.getString("FLGVALIDADO").equals("S"));
		historial.setObservacionCandidato(rs.getString("OBSERVACION_CANDIDATO"));
		historial.setValor(rs.getDouble("VALOR"));
		historial.setDesglose(rs.getString("DESGLOSE"));
		historial.setResultado(rs.getDouble("RESULTADO"));

		historial.setBepiteCodNum(rs.getInt("BEPITE_CODNUM"));
		historial.setItemSolicitudBolsa(
				ModeloBaremacionItems.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));

		historial.setCodigo(rs.getString("CODIGO"));
		historial.setNombre(rs.getString("NOMBRE"));

		return historial;
	}

	private HistorialTableSolicitudBolsaMeritoValoracion getHistorialTableSolicitudBolsaMeritoValoracionFromResulset(
			ResultSet rs) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = this.getUsuarioBolsa(rs.getString("UID_USUARIO"));

		HistorialTableSolicitudBolsaMeritoValoracion historial = new HistorialTableSolicitudBolsaMeritoValoracion();
		historial.setCodCambio(rs.getInt("CODCAMBIO"));
		historial.setLog(rs.getString("LOG"));
		historial.setFechaLog(rs.getDate("FECHALOG"));
		historial.setUidUsuario(usuario.getCodCuenta());
		historial.setRolUsuario(usuario.getRol().getValor());

		historial.setCodNum(rs.getInt("CODNUM"));
		historial.setBepsbmCodNum(rs.getInt("BEPSBM_CODNUM"));
		historial.setBepafiCodNum(rs.getInt("BEPAFI_CODNUM"));
		historial.setValor(rs.getDouble("VALOR"));
		historial.setAfinidad(ModeloAfinidad.obtenerInstancia().getAfinidadById(rs.getInt("BEPAFI_CODNUM")));

		return historial;
	}
}
