package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.DataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de titulaciones. 
 * @author ATISoluciones
 */
public class ModeloMerito {
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 2;
	public static final int ORDER_COLUMN_INDEX_REQUERIDA = 3;
	
	/**
	 * Listado de titulaciones en una tabla excluyendo las de un área .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param area id del area por el que se va a filtrar
	 * @return listado de titulaciones
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe titulación
	 */
	public DataTable<Merito> listaMeritosDatatable(Map<String, String[]> params, Integer usuario) throws SQLException, UVException {
		
		if (usuario == null) {
			throw new UVException("No se pueden listar méritos sin el id del usuario");
		}
		
		List<Merito> meritos = new ArrayList<>();
		DataTable<Merito> dataTable = new DataTable<Merito>(params);
		
		String consultaNotIn = "SELECT beptit.CODNUM FROM tbep_titulaciones beptit "
				+ "INNER JOIN tbep_titulacionespreferentesarea beptpa ON beptit.codnum = beptpa.beptit_codnum "
				+ "INNER JOIN TBEP_AREAS bepare ON bepare.codnum = beptpa.bepare_codnum "
				+ "WHERE bepare.CODNUM = ? ";
		String consulta = "SELECT bepusu.* FROM tbep_meritos bepmer INNER JOIN tbep_usuarios bepusu ON bepusu.CODNUM = bepmer. WHERE bepusu.CODNUM NOT IN (" + consultaNotIn + ")";
		
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_ID, "beptit.CODNUM");
		dataTable.setOrderColumn(ORDER_COLUMN_INDEX_NOMBRE, "beptit.NOMBRE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, usuario);
			stmtCount.setInt(indexParam++, usuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito mer = new Merito();
					mer.setCodNum(rs.getInt("CODNUM"));
					mer.setDescripcion(rs.getString("DESCRIPCION"));
					meritos.add(mer);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
}
