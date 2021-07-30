package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloPlazaOfertada {
	
	
	protected static ModeloPlazaOfertada eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloPlazaOfertada();
		}
	}

	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloPlazaOfertada obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/**
	 * Lista de plazas ofertadas .
	 * @param params .
	 * @return datatable de plazas ofertadas .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<PlazaOfertada> listadoPlazasOfertadas(Map<String, String[]> params) throws SQLException, UVException {
		List<PlazaOfertada> rows = new ArrayList<>();
		BolsaEmpleoDataTable<PlazaOfertada> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT * FROM TBEP_PLAZAS_OFERTADAS";
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					rows.add(createPlazaOfertadaFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
	}
	
	private PlazaOfertada createPlazaOfertadaFromResultSet(ResultSet rs) throws SQLException, UVException {
		PlazaOfertada plaza = new PlazaOfertada();
		
		plaza.setCodNum(rs.getInt("CODNUM"));
		plaza.setEstado(rs.getString("ESTADO"));
		plaza.setArea(ModeloArea.obtenerInstancia().getAreaById(rs.getInt("BEPARE_CODNUM")));
		plaza.setFechaCreacion(rs.getDate("FECHA_CREACION"));
		plaza.setFechaAbierta(rs.getDate("FECHA_ABIERTA"));
		plaza.setFechaCerrada(rs.getDate("FECHA_CERRADA"));
		
		return plaza;
	}
	
}
