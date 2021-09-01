package es.ujaen.uvirtual.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;

/** utilidades para rechaptcha.
 */
public class RecaptchaUtils {
	private RecaptchaUtils() {
		//no se puede instanciar
	}
	
	private static final String NOMBRE_ESTA_CLASE = RecaptchaUtils.class.getSimpleName();
	/** valida el recaptcha de la pagina.
	 * @param request peticion
	 * @throws UVException si no se valida correctemente
	 * @throws IOException si falla al conectarse para la validacion
	 */
	public static void validaRechaptcha(HttpServletRequest request) throws UVException, IOException {
		String nombreDeEsteMetodo = "validaRechaptcha";
		String claveSecreta = ConfiguracionGlobal.getParametroCadena("administracion.recaptcha.claveSecreta");
		String urlVerificaRecaptcha = "https://www.google.com/recaptcha/api/siteverify";
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		String mensajeLog = "";
		if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
			String mensajeUsuario = "verificación vacía";
			mensajeLog = mensajeUsuario;
			throw new UVException(Level.INFO, NOMBRE_ESTA_CLASE, nombreDeEsteMetodo, mensajeLog, mensajeUsuario, false);
		}
		URL verifyUrl = new URL(urlVerificaRecaptcha);
		// Open a Connection to URL above.
		HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
		// Add the Header informations to the Request to prepare send to the server.
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		// Data will be sent to the server.
		String postParams = "secret=" + claveSecreta + "&response=" + gRecaptchaResponse;
		// Send Request
		conn.setDoOutput(true);
		// Get the output stream of Connection.
		// Write data in this stream, which means to send data to Server.
		try (OutputStream outStream = conn.getOutputStream();) {
			outStream.write(postParams.getBytes());
			outStream.flush();
		}

		String jsonCaptcha = "";
		String regexIsSuccess = "(?s).*\"success\".*:.*true.*";
		String encoding = StandardCharsets.UTF_8.name();
		StringWriter writer = new StringWriter();
		//Get the Input Stream of Connection to read data sent from the Server.
		try (InputStream is = conn.getInputStream()) {
			IOUtils.copy(is, writer, encoding);
			jsonCaptcha = writer.toString(); 
		}
		
		if (!jsonCaptcha.matches(regexIsSuccess)) {
			String mensajeUsuario = "verificación incorrecta";
			mensajeLog = mensajeUsuario;
			throw new UVException(Level.INFO, NOMBRE_ESTA_CLASE, nombreDeEsteMetodo, mensajeLog, mensajeUsuario, false);
		}
	}

}
