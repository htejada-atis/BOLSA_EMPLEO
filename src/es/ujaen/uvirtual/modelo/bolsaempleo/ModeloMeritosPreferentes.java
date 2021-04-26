package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.MeritoPreferente;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;

/**
 * Clase de modelo para la gestión de méritos preferentes.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloMeritosPreferentes {
	// ordenación 
	public static final int ORDER_COLUMN_INDEX_APARTADOS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PUNTUACIONMAXIMA = 2;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO = 3;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_MERITOSPREFERENTES = 4;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_ACTIVO = 5;
	
	// tipo de meritos preferentes
	public static final String TIPO_TITULACION = "TITULACION";
	public static final String TIPO_MERITO = "MERITO";
	
	// tipos de aplicable
	public static final String APLICABLE_BLOQUE = "BLOQUE";
	public static final String APLICABLE_APARTADO = "APARTADO";
	public static final String APLICABLE_ITEM = "ITEM"; 
	
	public static final String ERROR_MERITO_NOEXITE = "El mérito no existe";
	
    protected static ModeloMeritosPreferentes eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMeritosPreferentes();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloMeritosPreferentes obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
		
    /**
	 * Devuelve un mérito preferente por su id.
	 * @param codNum .
	 * @return merito o excepción si no existe
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException .
	 */
	public MeritoPreferente getMeritoPreferenteById(Integer codNum) throws SQLException, UVException {
		String sql = "SELECT bepmep.* FROM TBEP_MERITOSPREFERENTES bepmep WHERE bepmep.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createApartadoFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(ERROR_MERITO_NOEXITE);		
	}
    
	/**
	 * Listado. 
	 * @param params .
	 * @return . 
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<MeritoPreferente> listadoMeritosPreferentes(Map<String, String[]> params) throws SQLException, UVException {
		List<MeritoPreferente> apartados = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoPreferente> dataTable = new BolsaEmpleoDataTable<MeritoPreferente>(params);
		
		String consulta =
			"SELECT bepmep.* "
		  + "FROM TBEP_MERITOSPREFERENTES bepmep "		  
		  + "WHERE 1=1 ";
		
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_CODIGO, "bepapa.CODIGO");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_NOMBRE, "bepapa.NOMBRE");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_PUNTUACIONMAXIMA, "bepapa.PUNTUACIONMAXIMA");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO, "bepapa.PORCENTAJEMAXIMO");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_MERITOSPREFERENTES, "bepapa.MERITOS_PREFERENTES", DataTableColumn.COLUMN_TYPE_BOOLEAN);
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_ACTIVO, "bepapa.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferente row = this.createApartadoFromResultSet(rs);
					apartados.add(row);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(apartados);
		}
		
		return dataTable;
	}

	private MeritoPreferente createApartadoFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferente obj = new MeritoPreferente();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setDescripcion(rs.getString("DESCRIPCION"));
		obj.setTipo(rs.getString("TIPO"));
		obj.setAplicable(rs.getString("APLICABLE"));
		obj.setFactor(rs.getString("FACTOR"));
		obj.setValorMaximo(rs.getFloat("VALOR_MAXIMO") == 0 ? null : rs.getFloat("VALOR_MAXIMO"));
		obj.setTipoTitulacion(rs.getInt("BEPTIT_CODNUM") == 0 ? null : ModeloTitulacion.obtenerInstancia().getTitulacionById(rs.getInt("BEPTIT_CODNUM")));
		obj.setTipoItemBaremacion(rs.getInt("BEPITE_CODNUM") == 0 ? null : ModeloBaremacion.obtenerInstancia().getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));
		return obj;
	}		
}
