package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de méritos preferentes de un candidato.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloMeritosPreferentesCandidato {
	// ordenación 
	public static final int ORDER_COLUMN_INDEX_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 1;
	public static final int ORDER_COLUMN_INDEX_TIPO = 2;
	public static final int ORDER_COLUMN_INDEX_APLICABLE = 3;
	public static final int ORDER_COLUMN_INDEX_FACTOR = 4;
	public static final int ORDER_COLUMN_INDEX_ACTIVO = 5;
	
	public static final int MAX_LENGTH_COLUMN_DESCRIPCION = 1000;
	public static final int MAX_LENGTH_COLUMN_FACTOR = 20;
	public static final int MAX_LENGTH_COLUMN_CODIGO = 10;
	
	// tipo de meritos preferentes
	public static final String TIPO_TITULACION_PREFERENTE = "TITULACION_PREFERENTE";
	public static final String TIPO_MERITO = "MERITO";
	public static final String TIPO_POSESION = "POSESION";
		
	// tipo de cálculo
	public static final String TIPO_CALCULO_FACTOR = "FACTOR";
	public static final String TIPO_CALCULO_VALOR_MERITO_FACTOR = "VALOR_MERITO_FACTOR";
	
	// tipos de aplicable
	public static final String APLICABLE_BLOQUE = "BLOQUE";
	public static final String APLICABLE_APARTADO = "APARTADO";
	public static final String APLICABLE_ITEM = "ITEM"; 
	public static final String APLICABLE_TOTAL = "TOTAL";
		
	public static final String ERROR_MERITO_NOEXITE = "El mérito no existe";

	protected static ModeloMeritosPreferentesCandidato eInstancia;

	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMeritosPreferentesCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloMeritosPreferentesCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/**
	 * Listado. 
	 * @param params .
	 * @param usuario .
	 * @return . 
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<MeritoPreferenteUsuario> listaMeritosCandidatoDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) 
			throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("El usuario es requerido");
		}
		
		List<MeritoPreferenteUsuario> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepmpu.* FROM TBEP_MERITOS_PREFERENTES_USUARIO bepmpu WHERE bepmpu.BEPUSU_CODNUM = ? ";
		
//		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepmep.CODIGO");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmep.DESCRIPCION");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_TIPO, "bepmep.TIPO");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_APLICABLE, "bepmep.APLICABLE");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_FACTOR, "bepmep.FACTOR");
//		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTIVO, "bepmep.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
				
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			stmt.setInt(1, usuario.getCodNum());
			stmtCount.setInt(1, usuario.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferenteUsuario row = this.createMeritoFromResultSet(rs);
					meritos.add(row);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
		
	private MeritoPreferenteUsuario createMeritoFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferenteUsuario obj = new MeritoPreferenteUsuario();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(rs.getInt("BEPMEP_CODNUM")));
		obj.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		obj.setDescripcion(rs.getString("DESCRIPCION"));
		obj.setArchivo(rs.getBinaryStream("ARCHIVO"));
		obj.setBorrado(rs.getBoolean("FLGBORRADO"));
		obj.setFechaBorrado(rs.getDate("FECHA_BORRADO"));
		obj.setValidado(rs.getBoolean("FLGVALIDADO"));
		obj.setFechaValidado(rs.getDate("FECHA_VALIDADO"));
		return obj;
	}
}
