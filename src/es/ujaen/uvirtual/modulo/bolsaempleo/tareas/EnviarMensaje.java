package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Destinatario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloParametrosConfiguracion;
import es.ujaen.uvirtual.utilidades.EnviaCorreo;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Tarea para la gestión de mensajes.
 * 
 * @author ATISoluciones 2021
 */
public final class EnviarMensaje {
	private static final String NOMBREDEESTACLASE = EnviarMensaje.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	private EnviarMensaje() {
	}

	/**
	 * Ejecuta la tarea, si hay mensajes con estado enviando los envía .
	 */
	public static void run() {
		try {
			ModeloMensajes modelo = ModeloMensajes.obtenerInstancia();
			
			// cache de mensajes (en principio solo 1 o máximo 100 en el peor de los casos)
			Map<Integer, Mensaje> mensajes = new HashMap<>();
			
			// leemos destinatarios pendientes de envio (max. 100)
			List<Destinatario> destinatarios = modelo.listaDestinatariosAEnviarEnBloques();
			for (Destinatario destinatario : destinatarios) {
				// leemos mensaje
				if (!mensajes.containsKey(destinatario.getMensaje().getCodNum())) {
					mensajes.put(destinatario.getMensaje().getCodNum(), modelo.getMensajeById(destinatario.getMensaje().getCodNum()));
				}
				Mensaje mensaje = mensajes.get(destinatario.getMensaje().getCodNum());
				
				// enviamos mensajes
				List<String> emails = new ArrayList<>();
				emails.add(destinatario.getUsuario() != null ? destinatario.getUsuario().getEmail() : destinatario.getEmail());
				
				boolean enviado = false;
				
				if (mensaje.getAdjunto() != null) {
					DataSource source = new ByteArrayDataSource(mensaje.getAdjunto(), "application/pdf");
					
					String remitente = ModeloParametrosConfiguracion.obtenerInstancia().
							getParametroByNombre(ModeloParametrosConfiguracion.PARAMETRO_REMITENTE).getValor();
					enviado = EnviaCorreo.enviaCorreoAdjuntoHtml(remitente, emails, null, mensaje.getTitulo(), mensaje.getCuerpo(), "adjunto.pdf", source);
				} else {
					enviado = EnviaCorreo.enviaCorreoHTML(emails, mensaje.getTitulo(), mensaje.getCuerpo());
				}
				
				if (enviado) {
					modelo.actualizaEstadoDestinatarioComoEnviado(destinatario, null);
				} else {
					modelo.actualizaEstadoDestinatarioComoFallo(destinatario, null);
				}
			}
			
			// actualizamos estados de mensajes
			for (Mensaje msg : mensajes.values()) {
				modelo.actualizaEstadoMensajePorDestinatarios(msg);
			}
		} catch (SQLException | UVException | IOException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
}
