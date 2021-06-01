package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de ficheros Modelo - Operaciones con nombres:
 * lista, inserta Controlador - Opers. con nombres: obtener, agregar
 * 
 * @author ATISoluciones 
 */
public class ModeloFichero {

	public static final int ORDER_COLUMN_INDEX_ID = 0;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_TITULO = 2;
	public static final int ORDER_COLUMN_INDEX_PUBLICO = 4;

	public static final int COLUMN_TITULO_MAXLENGTH = 300;
	
	private static final String PUBLICO = "S";
	private static final String PRIVADO = "N";

	protected static ModeloFichero eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloFichero();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloFichero obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**********************************************
	 * METODOS PÚBLICOS PARA CONSULTAS
	 ********************************************/

	/**
	 * Consulta ficheros en BBDD y los devuelve .
	 * 
	 * @param clausula para filtrar los ficheros de la bd .
	 * @param id       para filtrar ficheros por id .
	 * @return lista todos los ficheros de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 */
	private List<Fichero> listaFicheros(String clausula, Integer id) throws SQLException {
		List<Fichero> ficheros = new ArrayList<>();
		String consulta = "SELECT bepfich.* FROM tbep_ficheros bepfich " + clausula;

		if (id != null) {
			consulta += "WHERE codnum = ?";
		}

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			if (id != null) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, id);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Fichero fich = new Fichero();
					fich.setCodNum(rs.getInt("CODNUM"));
					fich.setNombre(rs.getString("NOMBRE"));
					fich.setTitulo(rs.getString("TITULO"));
					fich.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
					fich.setPublico(rs.getString("FLGPUBLICO").equals(PUBLICO));
					ficheros.add(fich);
				}
			}
		}
		return ficheros;
	}

	/**
	 * lista todos los ficheros de inicio .
	 * 
	 * @param anonimo .
	 * @return lista de todos los ficheros .
	 * @throws SQLException si hay un error en la base de datos .
	 */
	public List<Fichero> listaFicherosInicio(boolean anonimo) throws SQLException {
		return listaFicheros(anonimo ? "WHERE flgpublico = 'S' " : "", null);
	}

	/**
	 * lista todos los ficheros .
	 * 
	 * @return lista de todos los ficheros .
	 * @throws SQLException si hay un error en la base de datos .
	 */
	public List<Fichero> listaFicheros() throws SQLException {
		return listaFicheros("", null);
	}

	/**
	 * obtiene un archivo a partir de su id.
	 * 
	 * @param id codigo del fichero .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  en caso de error de parametros .
	 */
	public Fichero listaFichero(Integer id) throws SQLException, UVException {
		List<Fichero> ficheros = listaFicheros("", id);
		if (ficheros.isEmpty()) {
			throw new UVException("No existe fichero");
		}
		return ficheros.get(0);
	}

	/**
	 * Elimina un fichero .
	 * 
	 * @param fichero a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si fichero no es válido
	 */
	public void borraFichero(Fichero fichero) throws SQLException, UVException {
		if (fichero == null) {
			throw new UVException("No se puede eliminar un fichero vacío");
		}
		if (fichero.getCodNum() == null) {
			throw new UVException("No se puede eliminar un fichero con id vacío");
		}
		String consulta = "DELETE FROM tbep_ficheros WHERE codnum = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, fichero.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Función que inserta un fichero .
	 * 
	 * @param fichero fichero a insertar .
	 * @param usuario .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  en caso de error de parametros .
	 */
	public void insertaFichero(Fichero fichero, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (fichero == null) {
			throw new UVException("No se puede insertar un fichero vacío");
		}
		if (fichero.getNombre() == null || fichero.getNombre().equals("")) {
			throw new UVException("No se puede insertar un fichero sin nombre");
		}
		if (fichero.getArchivo() == null) {
			throw new UVException("No se puede insertar un fichero sin archivo");
		}
		if (fichero.isPublico() == null) {
			throw new UVException("No se puede insertar un fichero sin parámetro público");
		}

		String consulta = "INSERT INTO tbep_ficheros (NOMBRE,TITULO,ARCHIVO,FLGPUBLICO,UID_USUARIO) VALUES (?,?,?,?,?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, fichero.getNombre());
			stmt.setString(parameterIndex++, fichero.getTitulo());
			stmt.setBinaryStream(parameterIndex++, fichero.getArchivo());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(fichero.isPublico()) ? PUBLICO : PRIVADO);
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.executeUpdate();
		}
	}

	/**
	 * Cambia booleano publico de un fichero .
	 * 
	 * @param fichero a modificar .
	 * @param usuario .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si fichero no es válido .
	 */
	public void modificarPublicoFichero(Fichero fichero, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (fichero == null) {
			throw new UVException("No se puede editar un fichero vacío");
		}
		if (fichero.getCodNum() == null) {
			throw new UVException("No se puede editar un fichero con id vacío");
		}
		if (fichero.isPublico() == null) {
			throw new UVException("No se puede cambiar un publico vacío");
		}

		String consulta = "UPDATE tbep_ficheros SET flgpublico=?,UID_USUARIO=? WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(fichero.isPublico()) ? PUBLICO : PRIVADO);
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.setInt(parameterIndex++, fichero.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * Listado de ficheros .
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @return listado de ficheros .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  si el fichero no es valido .
	 */
	public BolsaEmpleoDataTable<Fichero> listaFicherosDatatable(Map<String, String[]> params)
			throws SQLException, UVException {
		List<Fichero> ficheros = new ArrayList<>();
		BolsaEmpleoDataTable<Fichero> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT bepfich.codnum, bepfich.nombre, bepfich.titulo, bepfich.FLGPUBLICO FROM tbep_ficheros bepfich WHERE 1=1 ";

		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepfich.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE, "bepfich.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_TITULO, "bepfich.TITULO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_PUBLICO, "bepfich.FLGPUBLICO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Fichero fich = new Fichero();
					fich.setCodNum(rs.getInt("CODNUM"));
					fich.setNombre(rs.getString("NOMBRE"));
					fich.setTitulo(rs.getString("TITULO"));
					fich.setPublico(rs.getString("FLGPUBLICO").equals(PUBLICO));
					ficheros.add(fich);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(ficheros);
		}

		return dataTable;
	}

}
