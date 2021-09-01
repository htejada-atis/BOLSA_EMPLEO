package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Contratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de plantillas .
 * 
 * @author ATISoluciones
 */
public class ModeloPlantilla {
	
	public static final int PLANTILLAS_COLUMN_INDEX_CODNUM = 0;
	public static final int PLANTILLAS_COLUMN_INDEX_NOMBRE = 1;
	
	public static final int PLANTILLAS_COLUMN_NOMBRE_MAXLENGTH = 150;
	public static final int PLANTILLAS_COLUMN_TITULO_MAXLENGTH = 500;
	
	public static final String CUERPO = "CUERPO";
	public static final String CODNUM = "CODNUM";
	public static final String NOMBRE = "NOMBRE";
	public static final String TITULO = "TITULO";
	
	public static final String MENSAJE_ERROR_NO_EXISTE_PLANTILLA = "No existe la plantilla";
	public static final String MENSAJE_ERROR_PLANTILLA_NULL = "No se puede insertar una plantilla vacía";
	
	protected static ModeloPlantilla eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloPlantilla();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloPlantilla obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
		
	/** Listado de plantillas .
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de plantillas .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException  error si no existe la area .
	 * @throws IOException .
	 */
	public BolsaEmpleoDataTable<Plantilla> listaPlantillasDatatable(Map<String, String[]> params) throws SQLException, UVException, IOException {
		List<Plantilla> mensajes = new ArrayList<>();
		BolsaEmpleoDataTable<Plantilla> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls";

		dataTable.setColumn(PLANTILLAS_COLUMN_INDEX_CODNUM, CODNUM);
		dataTable.setColumn(PLANTILLAS_COLUMN_INDEX_NOMBRE, NOMBRE);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					mensajes.add(createPlantillaFromResultSet(rs));
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(mensajes);
		}
		
		return dataTable;
	}
	
	/** Devuelve una plantilla por su id.
	 * @param codNum .
	 * @return plantilla o null si no existe
	 * @throws IOException .
	 * @throws SQLException .
	 */
	public Plantilla getPlantillaById(Integer codNum) throws SQLException, UVException, IOException {
		if (codNum == null) {
			throw new UVException(MENSAJE_ERROR_NO_EXISTE_PLANTILLA);
		}
		String consulta = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls WHERE beppls.CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_NO_EXISTE_PLANTILLA);
				}
				
				return this.createPlantillaFromResultSet(rs);
			}
		}
	}

	/** Añade una plantilla .
	 * @param plantilla .
	 * @param usuarioUpdate .
	 * @return id plantilla creada .
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public Integer nuevaPlantilla(Plantilla plantilla, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plantilla == null) {
			throw new UVException(MENSAJE_ERROR_PLANTILLA_NULL);
		}
		
		String consulta = String.format("INSERT INTO TBEP_PLANTILLAS (%s,%s,%s,%s) VALUES (?,?,?,?)", TITULO, CUERPO, NOMBRE, "UID_USUARIO");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[] {CODNUM})) {
			int indexParam = 1;
			
			stmt.setString(indexParam++, plantilla.getTitulo());
			stmt.setClob(indexParam++, BolsaEmpleoUtils.stringToClob(plantilla.getCuerpo(), conexion));
			stmt.setString(indexParam++, plantilla.getNombre());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		}
	}
	
	/** Actualiza una plantilla .
	 * @param plantilla .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void actualizarPlantilla(Plantilla plantilla, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plantilla == null) {
			throw new UVException(MENSAJE_ERROR_PLANTILLA_NULL);
		}
		
		String consulta = String.format("UPDATE TBEP_PLANTILLAS SET %s=?, %s=?, %s=?, %s=? WHERE %s=?", TITULO, CUERPO, NOMBRE, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setString(indexParam++, plantilla.getTitulo());
			stmt.setClob(indexParam++, BolsaEmpleoUtils.stringToClob(plantilla.getCuerpo(), conexion));
			stmt.setString(indexParam++, plantilla.getNombre());
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setInt(indexParam++, plantilla.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Elimina una plantilla .
	 * @param plantilla a borrar .
	 * @param usuarioDelete .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException  si fichero no es válido .
	 */
	public void borraPlantilla(Plantilla plantilla, UsuarioBolsaEmpleo usuarioDelete) throws SQLException, UVException {
		if (plantilla == null) {
			throw new UVException(MENSAJE_ERROR_PLANTILLA_NULL);
		}
		if (plantilla.getCodNum() == null) {
			throw new UVException("No se puede eliminar una plantilla con id vacío");
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			String consultaUpdate = "UPDATE TBEP_PLANTILLAS SET UID_USUARIO=? WHERE CODNUM = ?";
			try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
				int parameterIndex = 1;
				stmt.setString(parameterIndex++, usuarioDelete.getCodCuenta());
				stmt.setInt(parameterIndex++, plantilla.getCodNum());
				stmt.executeUpdate();
			}
			
			String consulta = "DELETE FROM TBEP_PLANTILLAS WHERE CODNUM = ? ";
			try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, plantilla.getCodNum());
				stmt.executeUpdate();
			}
		}
	}
	
	public Integer insertarMensajeDePlantilla(Mensaje mensaje, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException {
		String consultaInsertMsg = String.format("INSERT INTO TBEP_MENSAJES (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)", 
				"TITULO", "CUERPO", "FECHA_CREACION", "ESTADO", "UID_USUARIO");
		
		try (PreparedStatement stmt = conexion.prepareStatement(consultaInsertMsg, new String[] {CODNUM})) {
			int parameterIndex = 1;

			stmt.setString(parameterIndex++, mensaje.getTitulo());
			stmt.setClob(parameterIndex++, BolsaEmpleoUtils.stringToClob(mensaje.getCuerpo(), conexion));
			stmt.setDate(parameterIndex++, new java.sql.Date(mensaje.getFechaCreacion().getTime()));
			stmt.setString(parameterIndex++, mensaje.getEstado());
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);
		}
	}
	
	public String reemplazaPlazaEnPlantilla(PlazaOfertada plaza, String texto) {
		texto = texto.replaceAll("<<idplaza>>", plaza.getCodNum().toString());
		texto = texto.replaceAll("<<area>>", plaza.getArea().getDescripcion());
		texto = texto.replaceAll("<<justificacion>>", plaza.getJustificacion());
		texto = texto.replaceAll("<<duracion_prevista>>", plaza.getDuracionPrevista());
		texto = texto.replaceAll("<<dedicacion>>", plaza.getDedicacion().getTexto());
		texto = texto.replaceAll("<<cuatrimestre>>", plaza.getCuatrimestre());
		texto = texto.replaceAll("<<sueldo>>", plaza.getDedicacion().getSueldo().toString());
		texto = texto.replaceAll("<<centro_destino>>", plaza.getCentroDestino());
		texto = texto.replaceAll("<<hora_fin_oferta>>", Formateador.formatoFecha(plaza.getFechaFinOferta(), Formateador.FORMATO_FECHA_HORA_MINUTOS));
		texto = texto.replaceAll("<<fecha_fin_oferta>>", Formateador.formatoFecha(plaza.getFechaFinOferta(), Formateador.FORMATO_FECHA_DDMMYYYY));
		return texto;
	}
	
	public String reemplazaPlazaYContratacionEnPlantilla(PlazaOfertada plaza, Contratacion contratacion, String texto) {
		texto = reemplazaPlazaEnPlantilla(plaza, texto);
		texto = texto.replaceAll("<<hora_cita>>", Formateador.formatoFecha(contratacion.getFechaCita(), Formateador.FORMATO_FECHA_HORA_MINUTOS));
		texto = texto.replaceAll("<<fecha_cita>>", Formateador.formatoFecha(contratacion.getFechaCita(), Formateador.FORMATO_FECHA_DDMMYYYY));
		texto = texto.replaceAll("<<apellidos_candidato>>", contratacion.getCandidato().getPrimerApellido() + " " + contratacion.getCandidato().getSegundoApellido());
		texto = texto.replaceAll("<<nombre_candidato>>", contratacion.getCandidato().getNombre());
		texto = texto.replaceAll("<<NIF_candidato>>", contratacion.getCandidato().getPrsNif());
		texto = texto.replaceAll("<<email_candidato>>", contratacion.getCandidato().getEmail());
		return texto;
	}

	public Plantilla createPlantillaFromResultSet(ResultSet rs) throws SQLException, IOException {
		Plantilla plantilla = new Plantilla();
		plantilla.setCodNum(rs.getInt(CODNUM));
		plantilla.setNombre(rs.getString(NOMBRE));
		plantilla.setTitulo(rs.getString(TITULO));
		plantilla.setCuerpo(BolsaEmpleoUtils.clobToString(rs.getClob(CUERPO)));
		return plantilla;
	}
	
}
