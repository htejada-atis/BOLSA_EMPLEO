package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de bolsas 
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloBolsa {	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_AREA = 2;
	public static final int ORDER_COLUMN_INDEX_ESTADO = 3;
	public static final int ORDER_COLUMN_INDEX_ACTUALIZADA = 4;
	public static final int ORDER_COLUMN_INDEX_BLOQUEO = 5;
	public static final int ORDER_COLUMN_INDEX_DESBLOQUEO = 6;
	public static final int ORDER_COLUMN_INDEX_BAREMALE = 7;
	
	public static final String BOLSA_ESTADO_BLOQUEADA = "BLOQUEADA";
	public static final String BOLSA_ESTADO_REVISION = "REVISION";
	public static final String BOLSA_ESTADO_BAREMACION = "BAREMACION";
	public static final String BOLSA_ESTADO_ALEGACIONES = "ALEGACIONES";
	public static final String BOLSA_ESTADO_DESBLOQUEADA = "DESBLOQUEADA";
	
	public static final String BOLSA_BAREMABLE = "S";
	public static final String BOLSA_NO_BAREMABLE = "N";
		
	/**
	 * Listado de bolsas de empleo. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<Bolsa> listaBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = new ModeloArea();
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		
		String consulta =
			"SELECT bepbol.* "
		  + "FROM TBEP_BOLSAS bepbol "
		  + "INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		  + "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepbol.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ESTADO, "bepbol.ESTADO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ACTUALIZADA, "bepbol.FECHAACTUALIZACION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUEO, "bepbol.FECHABLOQUEO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESBLOQUEO, "bepbol.FECHADEBLOQUEO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt("CODNUM"));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt("BEPARE_CODNUM")));
					bolsa.setEstado(rs.getString("ESTADO"));
					bolsa.setBaremable(rs.getString("FLGBAREMABLE").equals("S"));					
					bolsa.setFechaActualizacion(rs.getTimestamp("FECHAACTUALIZACION"));
					bolsa.setFechaBloqueo(rs.getTimestamp("FECHABLOQUEO"));
					bolsa.setFechaDesBloqueo(rs.getTimestamp("FECHADEBLOQUEO"));
					
					bolsas.add(bolsa);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}
		
		return dataTable;
	}	
	
	/**
	 * Devuelve una bolsa por su id.
	 * @param codNum id de bolsa
	 * @return bolsa
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bolsa no es existe
	 */
	public Bolsa getBolsaById(int codNum) throws SQLException, UVException {
		ModeloArea modeloArea = new ModeloArea();
		String consulta = "SELECT bepbol.* FROM TBEP_BOLSAS bepbol WHERE bepbol.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe la bolsa con id " + codNum);
				}
				
				Bolsa bolsa = new Bolsa();
				bolsa.setCodNum(rs.getInt("CODNUM"));
				bolsa.setArea(modeloArea.getAreaById(rs.getInt("BEPARE_CODNUM")));
				bolsa.setEstado(rs.getString("ESTADO"));
				bolsa.setBaremable(rs.getString("FLGBAREMABLE").equals("S"));					
				bolsa.setFechaActualizacion(rs.getTimestamp("FECHAACTUALIZACION"));
				bolsa.setFechaBloqueo(rs.getTimestamp("FECHABLOQUEO"));
				bolsa.setFechaDesBloqueo(rs.getTimestamp("FECHADEBLOQUEO"));
				
				return bolsa;
			}
		}
	}
	
	/**
	 * Devuelve un listado de bolsas por su id.
	 * @param ids codnum de la bolsa
	 * @return listado de bolsas leidas
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Bolsa> getBolsasByIds(int[] ids) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		
		for (int i = 0; i < ids.length; i++) {
			bolsas.add(this.getBolsaById(ids[i]));
	    }
		
		return bolsas;
	}

	/**
	 * Bloquea bolsas.
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void bloquearBolsas(List<Bolsa> bolsas) throws SQLException {
		/*
		 * Estado por defecto de las bolsas. Los candidatos NO PUEDEN añadir nuevo méritos.
		 * La bolsa se puede baremas (si es baremable)
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_BLOQUEADA);			
	}
	
	/**
	 * Pone bolsas en revisión.
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void ponerBolsasEnRevision(List<Bolsa> bolsas) throws SQLException {
		/*
		 * Se está revisando la bolsa. Usuarios no pueden introducir méritos y los miembros de la comisión
		 * no pueden evaluar. Se utiliza para un primer filtrado de candidatos apuntados a las bolsas
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_REVISION);
	}
	
	/**
	 * Pone bolsa en baremación.
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void ponerBolsasEnBaremacion(List<Bolsa> bolsas) throws SQLException {
		/*
		 * Las comisiones pueden evaluar méritos (solo en este estado)
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_BAREMACION);		
	}
	
	/**
	 * Pone bolsa en alegaciones.
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void ponerBolsasEnAlegaciones(List<Bolsa> bolsas) throws SQLException {
		/*
		 * Evaluar méritos después del proceso de evaluación. Se resuelven las alegaciones.
		 * Las comisiones no pueden acceder a evaluar méritos.
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_ALEGACIONES);		
	}
	
	/**
	 * Desbloquea bolsas.
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void desbloquearBolsas(List<Bolsa> bolsas) throws SQLException {
		/*
		 * Se pueden introducir nuevos méritos por los candidatos. Para que esto ocurra,
		 * TODAS LAS BOLSAS DEBEN ESTAR EN ESTE ESTADO
		 */
		this.cambiarEstadoBolsas(bolsas, BOLSA_ESTADO_DESBLOQUEADA);		
	}
			
	/**
	 * Baremar bolsas.
	 * @param bolsas .
	 */
	public void baremarBolsas(List<Bolsa> bolsas) {
		/*
		 * Las bolsas se bareman con las evaluaciones realizadas por las comisiones
		 * y por el servicio de personal.
		 */
		for (int i = 0; i < bolsas.size(); i++) {
			this.baremarBolsa(bolsas.get(i));
	    }
	}
	
	/**
	 * Barema una bolsa.
	 * @param bolsa .
	 */
	public void baremarBolsa(Bolsa bolsa) {
		
	}
	
	/**
	 * Establece la area asociada a la bolsa como baremable. 
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void ponerAreaComoBaremable(List<Bolsa> bolsas) throws SQLException {
		this.cambiarFlagBaremableBolsas(bolsas, BOLSA_BAREMABLE);
	}

	/**
	 * Establece la area asociada a la bolsa como baremable. 
	 * @param bolsas .
	 * @throws SQLException .
	 */
	public void ponerAreaComoNoBaremable(List<Bolsa> bolsas) throws SQLException {
		this.cambiarFlagBaremableBolsas(bolsas, BOLSA_NO_BAREMABLE);		
	}
	
	private void cambiarEstadoBolsas(List<Bolsa> bolsas, String estado) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		String query = "UPDATE TBEP_BOLSAS SET ESTADO = ? WHERE CODNUM IN (" + params + ")";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, estado);
			for (Bolsa bolsa : bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum());
			}
			stmt.executeUpdate();
		}
	}
	
	private void cambiarFlagBaremableBolsas(List<Bolsa> bolsas, String baremable) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(bolsas.size());
		String query = "UPDATE TBEP_BOLSAS SET FLGBAREMABLE = ? WHERE CODNUM IN (" + params + ")";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, baremable);
			for (Bolsa bolsa : bolsas) {
				stmt.setInt(indexParam++, bolsa.getCodNum()); 				
			}
			stmt.executeUpdate();
		}
	}
}
