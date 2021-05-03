package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Candidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Evaluador;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * @author ATISoluciones
 */
public class ModeloCandidato {
	
	public static final int ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES = 1;
	public static final int ORDER_COLUMN_INDEX_ACTIVO_EVALUADORES = 2;
	
	protected static ModeloCandidato eInstancia = null;
	
	public static final Integer PARAM_ROL_CANDIDATO_ID = 1052;
	
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloCandidato();
		}
	}
	
	/**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloCandidato obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	/** Listado de candidatos en función de un área . 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de candidatos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<Candidato> listaCandidatosDatatable(Map<String, String[]> params) throws SQLException, UVException {		
		List<Candidato> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<Candidato> dataTable = new BolsaEmpleoDataTable<Candidato>(params);
		
		String consulta =
				"SELECT * "
				+ "FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
				+ "WHERE bepusu.rol = 1052";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO_EVALUADORES, "uvpersona.IDNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS_EVALUADORES, "uvpersona.STRAPELLIDO1");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVO_EVALUADORES, "bepeva.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {		
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
					usuarios.add(usuario);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}
	
}
