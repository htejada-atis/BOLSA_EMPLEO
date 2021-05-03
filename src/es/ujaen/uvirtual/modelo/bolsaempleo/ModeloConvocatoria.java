package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Convocatoria;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de convocatorias 
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloConvocatoria {	
	public static final int ORDER_COLUMN_INDEX_ID = 0;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 1;
	public static final int ORDER_COLUMN_INDEX_FECHACIERRE = 2;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 3;
	
	public static final String CONVOCATORIA_ESTADO_ABIERTA = "ABIERTA";
	public static final String CONVOCATORIA_ESTADO_CERRADA = "CERRADA";
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 150;
	
	public static final String MENSAJE_ERROR_NO_EXISTE_CONVOCATORIA = "No existe la convocatoria";
		
    protected static ModeloConvocatoria eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloConvocatoria();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloConvocatoria obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	/**
	 * Listado de bolsas de convocatorias. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Convocatoria> listaConvocatoriasDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Convocatoria> data = new ArrayList<>();
		BolsaEmpleoDataTable<Convocatoria> dataTable = new BolsaEmpleoDataTable<Convocatoria>(params);
		
		String consulta =
			"SELECT bepcon.* "
		  + "FROM TBEP_CONVOCATORIAS bepcon "		  
		  + "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepcon.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepcon.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_FECHACIERRE, "bepcon.FECHACIERRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepcon.ESTADO");		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Convocatoria convocatoria = new Convocatoria();
					convocatoria.setCodNum(rs.getInt("CODNUM"));
					convocatoria.setDescripcion(rs.getString("DESCRIPCION"));
					convocatoria.setFechaCierre(rs.getDate("FECHACIERRE"));
					convocatoria.setEstado(rs.getString("ESTADO"));					
					data.add(convocatoria);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(data);
		}
		
		return dataTable;
	}	
	
	/**
	 * Comprueba si hay convocatorias abiertas.
	 * @return .
	 * @throws SQLException .
	 */
	public boolean hayConvocatoriaAbierta() throws SQLException {
		String consulta =
				"SELECT COUNT(1) numero_convocatorias_abiertas "
			  + "FROM TBEP_CONVOCATORIAS bepcon "		  
			  + "WHERE bepcon.ESTADO = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setString(1, CONVOCATORIA_ESTADO_ABIERTA);
    		try (ResultSet rs = stmt.executeQuery();) {
	    		if (rs.next()) {
	    			if (rs.getInt("numero_convocatorias_abiertas") > 0) {
	    				return true;
	    			}
	    		}
    		}
		}
		
		return false;
	}
	
	/** Consulta titulaciones en BBDD y las devuelve.
	 * @param clausula para filtrar las titulaciones de la bd
	 * @return todas las titulaciones de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 */
	public List<Convocatoria> listaConvocatorias() throws SQLException {
		List<Convocatoria> convocatorias = new ArrayList<>();
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						try {
							Convocatoria convocatoria = new Convocatoria();
							convocatoria.setCodNum(rs.getInt("CODNUM"));
							convocatoria.setDescripcion(rs.getString("DESCRIPCION"));
							convocatoria.setFechaCierre(rs.getDate("FECHACIERRE"));
							convocatoria.setEstado(rs.getString("ESTADO"));					
							convocatorias.add(convocatoria);	
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		
		return convocatorias;
	}
	
	/**
	 * Añade una convocatoria al sistema cerrada.
	 * @param convocatoria .	 
	 * @throws SQLException .
	 */
	public void nuevaConvocatoria(Convocatoria convocatoria) throws SQLException {
		String consulta =
			"INSERT INTO TBEP_CONVOCATORIAS (DESCRIPCION, FECHACIERRE, ESTADO, NUMBOLSASMAXIMO, NUMMERITOSPORBLOQUE) " 
			+ "VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, convocatoria.getDescripcion());
			stmt.setDate(parameterIndex++, new java.sql.Date(convocatoria.getFechaCierre().getTime()));
			stmt.setString(parameterIndex++, convocatoria.getEstado());
			stmt.setInt(parameterIndex++, convocatoria.getNumBolsasMaximo());
			stmt.setInt(parameterIndex++, convocatoria.getNumMeritosPorBloque());			
			stmt.executeUpdate();
		}		
	}
	
	/**
	 * Devuelve un convocatoria por su pk.
	 * @param codNum .
	 * @return Convocatoria o null si no existe
	 * @throws SQLException .
	 */
	public Convocatoria getConvocatoriaById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon WHERE bepcon.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_CONVOCATORIA);
				}
				
				Convocatoria convocatoria = new Convocatoria();
				convocatoria.setCodNum(rs.getInt("CODNUM"));
				convocatoria.setDescripcion(rs.getString("DESCRIPCION"));
				convocatoria.setEstado(rs.getString("ESTADO"));
				convocatoria.setFechaCierre(rs.getDate("FECHACIERRE"));
				convocatoria.setNumBolsasMaximo(rs.getInt("NUMBOLSASMAXIMO"));
				convocatoria.setNumMeritosPorBloque(rs.getInt("NUMMERITOSPORBLOQUE"));
				
				return convocatoria;
			}
		}
	}
	
	/**
	 * Devuelve numero de solicitudes asociadas a convocatoria .
	 * @param codNum .
	 * @return Convocatoria o null si no existe
	 * @throws SQLException .
	 */
	public Integer getNumSolicitudesByConvocatoriaId(int codNum) throws SQLException, UVException {
		String consulta = "SELECT COUNT(*) AS total "
				+ "FROM TBEP_SOLICITUDES bepsol WHERE bepsol.BEPCON_CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			
			stmt.setInt(1, codNum);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return rs.getInt("total");				
				}	
			}
		}
		return null;
	}
	
	
	/** Actualiza una convocatoria.
	 * @param conv Convocatoria con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaConvocatoria(Convocatoria conv) throws SQLException, UVException {
		if (conv == null) {
			throw new UVException("Convocatoria obligatorio");
		}
		if (conv.getCodNum() == null) {
			throw new UVException("id convocatoria no válido");
		}
		
		String consulta = "UPDATE tbep_convocatorias "
			+ " SET DESCRIPCION=?, FECHACIERRE=?, ESTADO=?, NUMBOLSASMAXIMO=?, NUMMERITOSPORBLOQUE=? "
			+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
		PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, conv.getDescripcion());
			stmt.setDate(parameterIndex++, new java.sql.Date(conv.getFechaCierre().getTime()));
			stmt.setString(parameterIndex++, conv.getEstado());
			stmt.setInt(parameterIndex++, conv.getNumBolsasMaximo());
			stmt.setInt(parameterIndex++, conv.getNumMeritosPorBloque());
			stmt.setInt(parameterIndex++, conv.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Cambia el estado de una convocatoria.
	 * @param conv Convocatoria con los datos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void cambiaEstadoConvocatoria(Convocatoria conv) throws SQLException, UVException {
		if (conv == null) {
			throw new UVException("Convocatoria obligatorio");
		}
		if (conv.getCodNum() == null) {
			throw new UVException("id convocatoria no válido");
		}
		
		String consulta = "UPDATE tbep_convocatorias "
			+ "SET ESTADO=? "
			+ "WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
		PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, conv.getEstado());
			stmt.setInt(parameterIndex++, conv.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Elimina una convocatoria.
	 * @param conv convocatoria a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si convocatoria no es valida
	 */
	public void borraConvocatoria(Convocatoria conv) throws SQLException, UVException {
		if (conv == null) {
			throw new UVException("No se puede eliminar una convocatoria vacía");
		}
		if (conv.getCodNum() == null) {
			throw new UVException("No se puede eliminar una convocatoria con id vacío");
		}
		String consulta = "DELETE FROM tbep_convocatorias WHERE codnum = ? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, conv.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Devuelve la última convocatoria o null si no hay.
	 * @return .
	 * @throws UVException . 
	 * @throws SQLException .
	 */
	public Convocatoria getUltimaConvocatoria() throws SQLException, UVException {
		String consulta = "SELECT bepcon.* FROM TBEP_CONVOCATORIAS bepcon WHERE ROWNUM = 1 ORDER BY bepcon.CODNUM";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createFromResultSet(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Crea una convocatoria a partir de un resulset.
	 * @param rs .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Convocatoria createFromResultSet(ResultSet rs) throws SQLException, UVException {
		Convocatoria convocatoria = new Convocatoria();
		convocatoria.setCodNum(rs.getInt("CODNUM"));
		convocatoria.setDescripcion(rs.getString("DESCRIPCION"));
		convocatoria.setFechaCierre(rs.getDate("FECHACIERRE"));
		convocatoria.setEstado(rs.getString("ESTADO"));
		return convocatoria;
	}
	
}

