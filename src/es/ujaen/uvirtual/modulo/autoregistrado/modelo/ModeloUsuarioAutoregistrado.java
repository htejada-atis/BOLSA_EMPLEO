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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.ModeloClaveArcos;
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;
import es.ujaen.uvirtual.utilidades.EnviaCorreo;
import es.ujaen.uvirtual.utilidades.UVException;

/** Modelo para usuario autoregistrado.
 */
public class ModeloUsuarioAutoregistrado {
	private static final String NOMBREDEESTACLASE = ModeloUsuarioAutoregistrado.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private void insertaCuentaAutoregistradoBd(Connection conexion, Usuario usuario) throws SQLException, UVException {
		String consulta = " INSERT INTO arcos.ARG_CUENTA " 
				+ " (usuario, correo, clave, fecha_creacion) "
				+ " VALUES (?, ?, ?, sysdate)";
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			ModeloClaveArcos modelo = ModeloClaveArcos.obtenerInstancia();
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
				+ " (usuario, nombre, apellido1, apellido2, documento, tipodocumento) "
				+ " VALUES ('autoregistrado'||LPAD(arcos.sec_arg_usuario.nextval, 8, '0'), ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{"usuario"})) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuario.getNombre());
			stmt.setString(parameterIndex++, usuario.getApellido1());
			stmt.setString(parameterIndex++, usuario.getApellido2());
			stmt.setString(parameterIndex++, usuario.getDocumentoNumero());
			stmt.setString(parameterIndex++, usuario.getDocumentoTipo());
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs != null && rs.next()) {
				    String usuarioGenerado = rs.getString(1);
				    usuario.setUid(usuarioGenerado);
				}
			}
		}
	}
	
	private void validaTipoDocumento(Usuario usuario) throws UVException {
		if ("NIF".equals(usuario.getDocumentoTipo())) {
			final int numeroCaracteresNif = 9;
			if (usuario.getDocumentoNumero().length() != numeroCaracteresNif) {
				throw new UVException("el nif debe tener 9 caracteres incluyendo la letra");
			}
			String numeroNif = AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuario);
			String letraNif = AdaptadorDocumentoIdentidad.letraNIF(usuario.getDocumentoTipo(), numeroNif);
			String letraNifCalculada = calculaLetraNIFJava(numeroNif);
			if (!letraNif.equals(letraNifCalculada)) {
				throw new UVException("letra de NIF no correcta");
			}
		}
	}
	
	private String calculaLetraNIFJava(String nif) {
		final int modulo = 23;
		String caracteres = "TRWAGMYFPDXBNJZSQVHLCKE";
		String nifTratado = nif.replace("X", "0").replace("Y", "1").replace("Z", "2");
		int resto = Integer.parseInt(nifTratado) % modulo;
		return Character.toString(caracteres.charAt(resto));
	}
	
	private void validaCamposUsuarioAutoregistrado(Usuario usuario) throws UVException {
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
	}
	
	private void validaInsertaUsuarioAutoregistrado(Usuario usuario) throws UVException, SQLException {
		validaCamposUsuarioAutoregistrado(usuario);
		if (isDocumentoRegistrado(usuario.getDocumentoNumero(), usuario.getDocumentoTipo())) {
			throw new UVException("Documento ya dado de alta, pongase en contracto con los gestores de la aplicacióna la que desea acceder");
		}
		boolean validarDocumento = false;
		try {
			validarDocumento = ConfiguracionGlobal.getParametroLogico("autoregistrado.validaNif");
		} catch (UVException e) {
			LOGGER.log(Level.WARNING, e.toString());
		}
		if (validarDocumento) {
			validaTipoDocumento(usuario);
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
			throw new UVException("Correo ya registrado. Intente recuperar su clave");
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

	/** inserta un registro de intento de validacion de usuario.
	 * @param correo correo del usuario externo
	 * @param valido si ha accedido correctamente
	 * @param ip ip desde la que se solicita el acceso
	 * @throws SQLException si error en bbdd
	 * @throws UVException si error en parametros
	 */
	public void insertaLogAcceso(String correo, boolean valido, String ip) throws SQLException {
		String consulta = " INSERT INTO arcos.ARG_LOG_ACCESO " 
						+ " (correo, valido, ip, fecha) "
						+ " VALUES (?, ?, ?, sysdate)";
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, correo);
			stmt.setString(parameterIndex++, valido ? "S" : "N");
			stmt.setString(parameterIndex++, ip);
			stmt.executeUpdate();
		}
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
				ModeloClaveArcos modeloClaveArcos = ModeloClaveArcos.obtenerInstancia();
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
	 * @param ip ip desde la que se valida el usuario
	 * @return true en caso de ser valida, false en caso contrario
	 * @throws SQLException en caso de error en la base de datos
	 */
	public boolean validaClaveUsuario(String correo, String clave, String ip) throws SQLException {
		boolean salida = false;
		String ultimoHash = listaClaveUsuarioExterno(correo);
		ModeloClaveArcos modeloClaveArcos = ModeloClaveArcos.obtenerInstancia();
		if (modeloClaveArcos.isClaveIgualHash(clave, ultimoHash)) {
			salida = true;
		}
		insertaLogAcceso(correo, salida, ip);
		return salida;
	}	
	
	private Usuario listaDatosUsuario(String correo) throws SQLException {
		String consulta = "select * "
						+ "  from ARCOS.ARG_CUENTA c, arcos.arg_usuario u "
						+ " where c.correo = ?"
						+ "   and c.usuario = u.usuario ";
		Usuario salida = null;
		try (Connection conexion = ConexionArcos.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1; 
			stmt.setString(parameterIndex++, correo);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					salida = new Usuario();
					salida.setUid(rs.getString("correo"));
					salida.setCuentaInstitucional(false);
					salida.setCuentaBloqueada(false);
					salida.setDominio("autoregistrado.ujaen.es");
					salida.setCuentaDN(null);
					salida.setCodigoCuentaArcos(null);
					salida.setCorreoRuta(null);
					salida.setCuentaGoogle(false);
					salida.setCuentaGoogleSuspendida(false);
					salida.setNombre(rs.getString("nombre"));
					salida.setApellido1(rs.getString("apellido1"));
					salida.setApellido2(rs.getString("apellido2"));
					salida.setDocumentoNumero(rs.getString("documento"));
					salida.setDocumentoTipo(rs.getString("tipodocumento"));
					salida.setCodigoPersonaArcos(null);
					salida.setCodigoRRHH(null);
					salida.setCodigoUXXIAC(null);
					salida.setPersonaManual(false);
					salida.setSexo(null);
				}
			}
		}
		if (salida == null) {
			throw new SQLException("no existe cuenta del usuario" + correo);
		}
		return salida;
	}	
	
	/** Obtiene el usuario a partir del correo autoregistrado.
	 * @param correo correo del usuario autoregistrado
	 * @return usuario usuario
	 * @throws SQLException si fallo bd
	 */
	public Usuario cargaUsuarioExterno(String correo) throws SQLException {
		Usuario usuario = listaDatosUsuario(correo);
		ArrayList<String> roles = new ArrayList<>();
		roles.add("UsuarioAutoregistrado");
		roles.add("publico");
		HashMap<String, ArrayList<String>> rolesDominio = new HashMap<>();
		rolesDominio.put(usuario.getDominio(), roles);
		usuario.setRolesPorDominio(rolesDominio);
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
			ModeloClaveArcos modeloClaveArcos = ModeloClaveArcos.obtenerInstancia();
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
		List<String> destinatariosCorreo = new ArrayList<String>();
		ModeloClaveArcos modeloClaveArcos = ModeloClaveArcos.obtenerInstancia();
		final int numeroBitsId = 512;
		final int moduloConversionACaracter = 32;
		String codigoPin = modeloClaveArcos.pinAleatorioTemporal();
		String idsolicitud = new BigInteger(numeroBitsId, new SecureRandom()).toString(moduloConversionACaracter);
		if (!isCorreoRegistrado(correo)) {
			throw new UVException("primero debe registrar su usuario");
		}
		insertaPeticionCambio(correo, idsolicitud, codigoPin, ip);
		destinatariosCorreo.add(correo);
		String asunto = ConfiguracionGlobal.getParametroCadena("autoaprovisionado.mail.asunto");
		String cuerpo = ConfiguracionGlobal.getParametroCadena("autoaprovisionado.mail.cuerpo");
		cuerpo = aplicaPlantilla(cuerpo, codigoPin, idsolicitud, correo);
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
	 * @param correo para aplicar a la plantilla
	 * @return texto de la plantilla con los valores cambiados
	 */
	private String aplicaPlantilla(String plantilla, String codigoPin, String idSolicitud, String correo) {
		String salida = plantilla;
		salida = salida.replace("%%CODIGO%%", codigoPin);
		salida = salida.replace("%%IDSOLICITUD%%", idSolicitud);
		salida = salida.replace("%%CORREO%%", correo);
		return salida;
	}
}