package es.ujaen.uvirtual.utilidades;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;

/**
 * Clase para enviar correos electrónicos desde la aplicación.
 * 
 * @author julopez
 *     modificación: jalucena
 *     Se crea otra versión de Envía correo que permite adjuntar archivos
 *     20130424 - julopez - modifica código repetido
 */
public class EnviaCorreo {
	private static String nombreMetodoEnvioCorreo = "enviaCorreo";
	private static String fPrefijo = "[uvirtual]";
	
	private static String errorEnviarCorreo = "Error al enviar correo ";
	private static Properties fConfiguracion = new Properties(); 

	private static String eNombreDeEstaClase = EnviaCorreo.class.getName();
	private static final Logger ELOGGER = Logger.getLogger(eNombreDeEstaClase);
	
	
	private EnviaCorreo() { }

	private static String ajustaAsunto(String asunto) {
		if (asunto.startsWith(fPrefijo)) { 
			return fPrefijo + asunto.substring(fPrefijo.length());
		} else {
			return asunto;
		}
	}
	
	/** envia correo.
	 * @param destinatarios detinatarios
	 * @param conCopia con copia 
	 * @param conCopiaOculta con copia oculta
	 * @param replyTo reply to
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(List<String> destinatarios, List<String> conCopia, 
			List<String> conCopiaOculta, String replyTo, String asunto, String cuerpo) {
		boolean resultado = false;
		try {
			resultado = enviaCorreoExcepcion(null, destinatarios, conCopia, conCopiaOculta, replyTo, asunto, cuerpo);
		} catch (Exception e) {
			ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreMetodoEnvioCorreo, errorEnviarCorreo + e);
		}
		return resultado;
	}

	/** envia correo.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param conCopia con copia
	 * @param conCopiaOculta con copia oculta
	 * @param replyTo reply to
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(String remitente, List<String> destinatarios, List<String> conCopia, 
			List<String> conCopiaOculta, String replyTo, String asunto, String cuerpo) {
		boolean resultado = false;
		try {
			resultado = enviaCorreoExcepcion(remitente, destinatarios, conCopia, conCopiaOculta, replyTo, asunto, cuerpo);
		} catch (Exception e) {
			ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreMetodoEnvioCorreo, errorEnviarCorreo + e);
		}
		return resultado;
	}
	
	/** envia correo con excepcion.
	 * @param emisor emisor
	 * @param destinatarios destinatarios
	 * @param conCopia con copia
	 * @param conCopiaOculta con copia oculta
	 * @param replyTo reply to
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 * @throws Exception si error
	 */
	public static boolean enviaCorreoExcepcion(String emisor, List<String> destinatarios, List<String> conCopia, 
			List<String> conCopiaOculta, String replyTo, String asunto, String cuerpo) throws Exception {
		Session session = Session.getDefaultInstance(fConfiguracion, null);
		MimeMessage message = new MimeMessage(session);
		if (emisor == null) {
			message.setFrom(new InternetAddress(ConfiguracionGlobal.getParametroCadena("mail.from"), ConfiguracionGlobal.getParametroCadena("mail.from")));
		} else {
			message.setFrom(new InternetAddress(emisor, emisor));
		}
		for (String destinatario : destinatarios) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("<" + destinatario + ">"));
		}
		if (conCopia != null) {
			for (String destinatario : conCopia) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress("<" + destinatario + ">"));
			}
		}
		if (conCopiaOculta != null) {
			for (String destinatario : conCopiaOculta) {
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress("<" + destinatario + ">"));
			}
		}
		if (replyTo != null) {
			Address[] aReplyTo = new Address[1];
			aReplyTo[0] = new InternetAddress("<" + replyTo + ">");
			message.setReplyTo(aReplyTo);
		}
		String asuntoAjustado = ajustaAsunto(asunto);
		message.setSubject(asuntoAjustado, "utf-8");
		message.setText(cuerpo, "utf-8");
		Transport.send(message);
		return true;		
	}
	
	/** envia correo.
	 * @param destinatarios destinatarios
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(List<String> destinatarios, String asunto, String cuerpo) {
		return enviaCorreo(destinatarios, null, null, null, asunto, cuerpo);
	}

	/** envia correo.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(String remitente, List<String> destinatarios, String asunto, String cuerpo) {
		return enviaCorreo(remitente, destinatarios, null, null, null, asunto, cuerpo);
	}

	/** envia correo.
	 * @param destinatarios destinatarios
	 * @param conCopia con copia
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(List<String> destinatarios, List<String> conCopia, String asunto, String cuerpo) {
		return enviaCorreo(destinatarios, conCopia, null, null, asunto, cuerpo);
	}

	/** envia correo.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param conCopia con copia
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(String remitente, List<String> destinatarios, List<String> conCopia, String asunto, String cuerpo) {
		return enviaCorreo(remitente, destinatarios, conCopia, null, null, asunto, cuerpo);
	}

	/** envia correo.
	 * @param destinatarios destinatarios
	 * @param conCopia con copia
	 * @param conCopiaOculta con copia oculta
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(List<String> destinatarios, List<String> conCopia, List<String> conCopiaOculta, String asunto, String cuerpo) {
		return enviaCorreo(destinatarios, conCopia, conCopiaOculta, null, asunto, cuerpo);
	}

	/** envia correo.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param conCopia con copia
	 * @param conCopiaOculta con copia oculta
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(String remitente, List<String> destinatarios, List<String> conCopia, List<String> conCopiaOculta, String asunto, String cuerpo) {
		return enviaCorreo(remitente, destinatarios, conCopia, conCopiaOculta, null, asunto, cuerpo);
	}

	/** envia correo.
	 * @param destinatarios destinatarios
	 * @param conCopiaOculta con copia oculta
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreoCCO(List<String> destinatarios, List<String> conCopiaOculta, String asunto, String cuerpo) {
		return enviaCorreo(destinatarios, null, conCopiaOculta, null, asunto, cuerpo);
	}
	
	/** envia correo cco.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param conCopiaOculta con copia oculta
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreoCCO(String remitente, List<String> destinatarios, List<String> conCopiaOculta, String asunto, String cuerpo) {
		return enviaCorreo(remitente, destinatarios, null, conCopiaOculta, null, asunto, cuerpo);
	}
	
	
	/** envia correo html.
	 * @param destinatarios destinatarios
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreoHTML(List<String> destinatarios, String asunto, String cuerpo) {
		boolean resultado = false;
		Session session = Session.getDefaultInstance(fConfiguracion, null);
		MimeMessage message = new MimeMessage(session);
		try {
			for (String destinatario : destinatarios) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("<" + destinatario + ">"));
			}
			String asuntoAjustado = ajustaAsunto(asunto);
			message.setSubject(asuntoAjustado);
			message.setContent(cuerpo, "text/html");
			Transport.send(message);
			resultado = true;
		} catch (MessagingException ex) {
			ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreMetodoEnvioCorreo, errorEnviarCorreo + ex);
		}
		return resultado;
	}

	/** envia correo.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @param nombreArchivo nombre archivo
	 * @param dataSource datasource
	 * @return si se ha enviado correntamente
	 */
	public static boolean enviaCorreo(String remitente, List<String> destinatarios, String asunto, String cuerpo, String nombreArchivo, DataSource dataSource) {
		return enviaCorreo(remitente, destinatarios, null, asunto, cuerpo, nombreArchivo, dataSource);  
	}
	
	/** envia correo.
	 * @param remitente remitente
	 * @param destinatarios destinatarios
	 * @param replyTo reply to
	 * @param asunto asunto
	 * @param cuerpo cuerpo
	 * @param nombreArchivo nombre archivo
	 * @param dataSource datasource
	 * @return si se ha enviado correctamente
	 */
	public static boolean enviaCorreo(String remitente, List<String> destinatarios, String replyTo, 
			String asunto, String cuerpo, String nombreArchivo, DataSource dataSource) {
		boolean resultado = false;
		Session session = Session.getDefaultInstance(fConfiguracion, null);
		
		MimeMessage message = new MimeMessage(session);
		try {
			
			// Crear el cuerpo del mensaje
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(cuerpo);
			
			// Crear el archivo
			MimeBodyPart fileBodyPart = new MimeBodyPart();
			if (nombreArchivo != null && !"".equals(nombreArchivo)) {
				fileBodyPart.setDataHandler(new DataHandler(dataSource));
				fileBodyPart.setFileName(nombreArchivo);
			}
			
			// Construir el contenido del mensaje (cuerpo y fichero adjunto)
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			
			if (nombreArchivo != null && !"".equals(nombreArchivo)) {
				mimeMultipart.addBodyPart(fileBodyPart);
			}				
			
			// Añadir remitente
			message.setSender(new InternetAddress("<" + remitente + ">"));
			
			// Añadir los destinatarios
			
			for (String destinatario : destinatarios) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("<" + destinatario + ">"));
			}

			// Añadir replyTo
			if (replyTo != null) {
				Address[] aReplyTo = new Address[1];
				aReplyTo[0] = new InternetAddress("<" + replyTo + ">");
				message.setReplyTo(aReplyTo);
			}
			
			// Añadir el asunto
			String asuntoAjustado = ajustaAsunto(asunto);
			message.setSubject(asuntoAjustado);
			
			// Añadir el cuerpo y el archivo
			message.setContent(mimeMultipart);
			
			// Enviar el mensaje
			Transport.send(message);
			resultado = true;
			
		} catch (MessagingException ex) {
			ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreMetodoEnvioCorreo, errorEnviarCorreo + ex);
		}
		
		return resultado;
	}
	
	/** recarga configuracion.
	 * 
	 */
	public static void recargarConfiguracion() {
	    fConfiguracion.clear();
	    cargarConfiguracion();
	  }

	static {
		cargarConfiguracion();
	}

	/**
	 * Primero carga la configuración de la base de datos, en caso de error.. de fichero
	 */
	private static void cargarConfiguracion() {
		try {
			fPrefijo = ConfiguracionGlobal.getParametroCadena("mail.prefijo");
			String parametros = ConfiguracionGlobal.getParametroCadena("mail.parametros");
			String[] datos = parametros.split(" ");
			for (String clave : datos) {
				fConfiguracion.put(clave, ConfiguracionGlobal.getParametroCadena(clave));
			}
	    } catch (Exception ex) {
	    	ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreMetodoEnvioCorreo, 
					"No se pudo cargar la información de configuración del servidor de correo, recuperando información de fichero.");
	    	try {
				ResourceBundle rb = ResourceBundle.getBundle("mail");
				Enumeration<String> e = rb.getKeys();
				while (e.hasMoreElements()) {
					String clave = e.nextElement();
					fConfiguracion.put(clave, rb.getObject(clave));
				}
	    	} catch (Exception exFichero) {
				ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreMetodoEnvioCorreo, 
						"No se pudo cargar la información de configuración del servidor de correo ni en BD ni en fichero.");
	    	}
	    }
	}
}