package es.ujaen.uvirtual.modulo.autoregistrado.modelo;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.ModeloClaveArcos;
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
import es.ujaen.uvirtual.utilidades.EnviaCorreo;
import es.ujaen.uvirtual.utilidades.UVException;

/** Modelo para usuario autoregistrado.
 */
public class ModeloUsuarioAutoregistrado {
	
	private void insertaCuentaAutoregistradoBd(Connection conexion, Usuario usuario) throws SQLException, UVException {
		String consulta = " INSERT INTO arcos.ARG_CUENTA " 
				+ " (usuario, correo, clave, fecha_creacion) "
				+ " VALUES (?, ?, ?, sysdate)";
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			ModeloClaveArcos modelo = new ModeloClaveArcos();
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuario.getUid());
			stmt.setString(parameterIndex++, usuario.getEmailCuentaPersona());
			try {
				stmt.setString(parameterIndex++, modelo.generaSsha512(modelo.passwordAleatorio(), null));
			} catch (NoSuchAlgorithmException e) {
				throw new UVException("algoritmo no valido " + e.getMessage()); 
			} catch (IOException e) {
				throw new UVException("excepcion io " + e.getMessage()); 
			}
			stmt.executeUpdate();
		}
	}
	
	private void insertaUsuarioAutoregistradoBd(Connection conexion, Usuario usuario) throws SQLException {
		String consulta = " INSERT INTO arcos.ARG_USUARIO " 
				+ " (usuario, nombre, apellido1, apellido2, documento, tipodocumento, sexo) "
				+ " VALUES ('autoregistrado'||LPAD(sec_arg_usuario.nextval, 8, '0'), ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{"usuario"})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuario.getNombre());
			stmt.setString(parameterIndex++, usuario.getApellido1());
			stmt.setString(parameterIndex++, usuario.getApellido2());
			stmt.setString(parameterIndex++, usuario.getDocumentoNumero());
			stmt.setString(parameterIndex++, usuario.getDocumentoTipo());
			stmt.setString(parameterIndex++, usuario.getSexo());
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs != null && rs.next()) {
				    String usuarioGenerado = rs.getString(1);
				    usuario.setUid(usuarioGenerado);
				}
			}
		}
	}
	
	private void validaInsertaUsuarioAutoregistrado(Usuario usuario) throws UVException, SQLException {
		if (usuario == null) {
			throw new UVException("No se puede insertar un usuario vacio");
		}
		if (usuario.getNombre() == null || "".equals(usuario.getNombre())) {
			throw new UVException("No se puede insertar un usuario sin nombre");
		}
		if (usuario.getApellido1() == null || "".equals(usuario.getApellido1())) {
			throw new UVException("No se puede insertar un usuario sin primer apellido");
		}
		if (usuario.getDocumentoNumero() == null || "".equals(usuario.getDocumentoNumero())) {
			throw new UVException("No se puede insertar un usuario sin numero de documento");
		}
		if (isDocumentoRegistrado(usuario.getDocumentoNumero(), usuario.getDocumentoTipo())) {
			throw new UVException("Documento ya dado de alta, pongase en contracto con los gestores de la aplicacióna la que desea acceder");
		}
	}
	
	private void validaCorreoInsertaUsuario(Usuario usuario) throws SQLException, UVException {
		try {
			InternetAddress internetAddress = new InternetAddress(usuario.getEmailCuentaPersona());
	        internetAddress.validate();
		} catch (AddressException e) {
			throw new UVException("el correo debe ser válido");
		}
		if (usuario.getEmailCuentaPersona().contains("ujaen.es")) {
			throw new UVException("No se puede registrar una cuenta ujaen como autoregistrado");
		}
		if (isCorreoRegistrado(usuario.getEmailCuentaPersona())) {
			throw new UVException("Correo ya registrado");
		}
	}
	
	/** crea un usuario nuevo autoregistrado.
	 * @param usuario usuario a crear
	 * @throws SQLException si error en bbdd
	 * @throws UVException si error validacion datos
	 */
	public void insertaUsuarioAutoregistrado(Usuario usuario) throws SQLException, UVException {
		validaInsertaUsuarioAutoregistrado(usuario);
		validaCorreoInsertaUsuario(usuario);
		try (Connection conexion = ConexionArcos.obtenerInstancia()) {
			try {
				conexion.setAutoCommit(false);
				insertaUsuarioAutoregistradoBd(conexion, usuario);
				insertaCuentaAutoregistradoBd(conexion, usuario);
				conexion.commit();
			} catch (SQLException e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		} 
	}
	
	/** verifica si un correo ya esta registrado como usuario autoregistrado.
	 * @param correo correo a verificar
	 * @return si ya esta registrado previamente
	 * @throws SQLException si fallo en bd
	 */
	public boolean isCorreoRegistrado(String correo) throws SQLException {
		boolean salida = false;
		String consulta = " select correo from arcos.arg_cuenta c where c.correo = ? ";
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, correo);
			try (ResultSet rs = stmt.executeQuery();) {
				if (rs.next()) {
					salida = true;
				}
			}
		} 
		return salida;
	}
	
	/** verifica si un documento ya esta registrado como usuario autoregistrado.
	 * @param documento documento a verificar
	 * @param tipoDocumento tipo de documento a verificar
	 * @return si ya esta registrado previamente
	 * @throws SQLException si fallo en bd
	 */
	public boolean isDocumentoRegistrado(String documento, String tipoDocumento) throws SQLException {
		boolean salida = false;
		String consulta = " select 1 from arcos.arg_usuario u where u.documento = ? and u.tipodocumento = ? ";
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, documento);
			stmt.setString(parameterIndex++, tipoDocumento);
			try (ResultSet rs = stmt.executeQuery();) {
				if (rs.next()) {
					salida = true;
				}
			}
		} 
		return salida;
	}

	/** inserta una nueva petición de cambio de clave de usuario autoregistrado.
	 * @param correo correo del usuario externo
	 * @param idSolicitud identificador aleatorio de la solicitud
	 * @param codigoTemporal codigo temporal
	 * @param ip ip desde la que se solicita el cambio
	 * @throws SQLException si error en bbdd
	 * @throws UVException si error en parametros
	 */
	public void insertaPeticionCambio(String correo, String idSolicitud, String codigoTemporal, String ip) throws SQLException, UVException {
		if (correo == null || "".equals(correo)) {
			throw new UVException("No se puede insertar una peticion de clave sin correo");
		}
		if (codigoTemporal == null || "".equals(codigoTemporal)) {
			throw new UVException("No se puede insertar una peticion de clave sin codigo temporal");
		}
		
		String consulta = " INSERT INTO arcos.ARG_PETICION_CAMBIO_CLAVE " 
						+ " (correo, codigo_temporal, ip_solicitud, fecha_solicitud, identificador) "
						+ " VALUES (?, ?, ?, sysdate, ?)";
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, correo);
			stmt.setString(parameterIndex++, codigoTemporal);
			stmt.setString(parameterIndex++, ip);
			stmt.setString(parameterIndex++, idSolicitud);
			stmt.executeUpdate();
		}
	}

	/** verifica una petición de cambio de clave.
	 * @param correo correo del usuario externo
	 * @param idSolicitud identificador aleatorio de la solicitud
	 * @param codigoTemporal codigo temporal
	 * @param ip ip desde la que se solicita el cambio
	 * @return clave nueva del usuario
	 * @throws SQLException si error en bbdd
	 * @throws UVException si error en parametros
	 */
	public String verificaPeticionCambio(String correo, String idSolicitud, String codigoTemporal, String ip) throws SQLException, UVException {
		String salida = null;
		if (correo == null || "".equals(correo)) {
			throw new UVException("No se puede verificar una peticion de clave sin correo");
		}
		if (codigoTemporal == null || "".equals(codigoTemporal)) {
			throw new UVException("No se puede verificar una peticion de clave sin codigo temporal");
		}
		if (idSolicitud == null || "".equals(idSolicitud)) {
			throw new UVException("No se puede verificar una peticion de clave sin id");
		}
		
		String consulta = " UPDATE arcos.ARG_PETICION_CAMBIO_CLAVE " 
						+ " SET IP_VERIFICACION = ?, "
						+ "     FECHA_VERIFICACION = sysdate, "
						+ "     VERIFICADA = 'S' "
						+ " WHERE CORREO = ? "
						+ "   AND CODIGO_TEMPORAL = ? "
						+ "   AND IDENTIFICADOR = ? "
						+ "   AND FECHA_SOLICITUD >= sysdate - (15 / 24 / 60) ";
						
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, ip);
			stmt.setString(parameterIndex++, correo);
			stmt.setString(parameterIndex++, codigoTemporal);
			stmt.setString(parameterIndex++, idSolicitud);
			int actualizados = stmt.executeUpdate();
			if (actualizados == 1) {
				ModeloClaveArcos modeloClaveArcos = new ModeloClaveArcos();
				String passwordAleatorio = modeloClaveArcos.passwordAleatorio();
				actualizaClaveUsuario(correo, passwordAleatorio);
				salida = passwordAleatorio;
			} else {
				throw new UVException("No se puede ha podido verificar su clave. intentelo de nuevo");
			}
		}
		return salida;
	}
	
	/** obtiene el hash de la clave del usuario externo.
	 * @param correo correo del usuario externo
	 * @return hash de la clave del usuario externo
	 * @throws SQLException si error en bd
	 */
	private String listaClaveUsuarioExterno(String correo) throws SQLException {
		String salida = null;
		String consulta = " select clave from arcos.arg_cuenta c where c.correo = ? ";
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, correo);
			try (ResultSet rs = stmt.executeQuery();) {
				if (rs.next()) {
					salida = rs.getString("clave");
				}
			}
		} 
		return salida;
	}	
	
	/**
	 * Valida si la clave de un usuario externo es valida.
	 * @param correo correo del usuario externo
	 * @param clave clave del usuairo
	 * @return true en caso de ser valida, false en caso contrario
	 * @throws SQLException en caso de error en la base de datos
	 */
	public boolean validaClaveUsuario(String correo, String clave) throws SQLException {
		boolean salida = false;
		String ultimoHash = listaClaveUsuarioExterno(correo);
		ModeloClaveArcos modeloClaveArcos = new ModeloClaveArcos();
		if (modeloClaveArcos.isClaveIgualHash(clave, ultimoHash)) {
			salida = true;
		}
		return salida;
	}	
	
	/** Obtiene el usuario a partir del uid.
	 * @param uid identificador del usuario
	 * @return usuario usuario
	 */
	public Usuario cargaUsuarioExterno(String uid) {
		Usuario usuario = new Usuario();
		usuario.setApellido1(uid);
		usuario.setApellido2(uid);
		usuario.setDocumentoNumero(uid);
		usuario.setDocumentoTipo(uid);
		usuario.setNombre(uid);
		usuario.setUid(uid);
		ArrayList<String> roles = new ArrayList<>();
		roles.add("UsuarioNoVerificado");
		usuario.setRoles(roles);
		return usuario;
	}
	
	/** actualiza la clave del usuario.
	 * @param correo correo al que cambiar la clave
	 * @param clave nueva clave a poner
	 * @throws SQLException si se produce un error en la base de datos
	 * @throws UVException si error al cambiar clave
	 */
	private void actualizaClaveUsuario(String correo, String clave) throws SQLException, UVException {
		String consulta 
			= "update arcos.arg_cuenta " 
			+ "   set clave = ? "
			+ " where correo = ? ";
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			ModeloClaveArcos modeloClaveArcos = new ModeloClaveArcos();
			String claveHaseada = modeloClaveArcos.generaSsha512(clave, null);
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, claveHaseada);
			stmt.setString(parameterIndex++, correo);
			stmt.executeUpdate();
		} catch (NoSuchAlgorithmException | IOException e) {
			throw new UVException("Error al cambiar la clave " + e);
		}
	}
	
	/** manda correo con clave temporal.
	 * @param correo correo del destinatario
	 * @param ip ip desde la que hace la petición.
	 * @return idSolicitud de clave temporal
	 * @throws SQLException si error bd
	 * @throws UVException si error validacion
	 */
	public String mandarClaveTemporal(String correo, String ip) throws SQLException, UVException {
		ArrayList<String> destinatariosCorreo = new ArrayList<>();
		ModeloClaveArcos modeloClaveArcos = new ModeloClaveArcos();
		final int numeroBitsId = 512;
		final int moduloConversionACaracter = 32;
		String codigoPin = modeloClaveArcos.pinAleatorioTemporal();
		String idsolicitud = new BigInteger(numeroBitsId, new SecureRandom()).toString(moduloConversionACaracter);
		insertaPeticionCambio(correo, idsolicitud, codigoPin, ip);
		destinatariosCorreo.add(correo);
		String asunto = ConfiguracionGlobal.getParametroCadena("autoaprovisionado.mail.asunto");
		String cuerpo = ConfiguracionGlobal.getParametroCadena("autoaprovisionado.mail.cuerpo");
		cuerpo = aplicaPlantilla(cuerpo, codigoPin, idsolicitud);
		try {
			EnviaCorreo.enviaCorreoExcepcion(null, destinatariosCorreo, null, null, null, asunto, cuerpo);
		} catch (Exception e) {
			throw new UVException(e.getMessage());
		}
		return idsolicitud;

	}
	
	/** aplica a un texto con las marcas de plantilla los valores indicador y lo devuelva sustituido.
	 * @param plantilla plantilla con marcas
	 * @param codigoPin codigo de pin para aplicar a la plantilla
	 * @param idSolicitud idSolicitud para aplicar a la plantilla
	 * @return texto de la plantilla con los valores cambiados
	 */
	private String aplicaPlantilla(String plantilla, String codigoPin, String idSolicitud) {
		String salida = plantilla;
		salida = salida.replace("%%CODIGO%%", codigoPin);
		salida = salida.replace("%%IDSOLICITUD%%", idSolicitud);
		return salida;
	}
}