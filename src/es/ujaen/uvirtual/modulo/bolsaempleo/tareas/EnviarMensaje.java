package es.ujaen.uvirtual.modulo.bolsaempleo.tareas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Destinatario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloMensajes;
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

			for (Mensaje mensaje : modelo.listaMensajesAEnviar()) {
				List<Destinatario> destinatarios = modelo.obtenerDestinatariosMensaje(mensaje);
				boolean enviado = false;
				
				for (Destinatario destinatario: destinatarios) {
					if (destinatario.getEstado().equals(ModeloMensajes.DESTINATARIO_ESTADO_SINENVIAR)) {
						List<String> emails = new ArrayList<>();
						emails.add(destinatario.getEmail());
						if (EnviaCorreo.enviaCorreo(emails, mensaje.getTitulo(), mensaje.getCuerpo())) {
							modelo.actualizaEstadoDestinatarioComoEnviado(mensaje, destinatario, null);
							enviado = true;
						} else {
							modelo.actualizaEstadoDestinatarioComoFallo(mensaje, destinatario, null);
						}
					}					
				}
				
				if (enviado) {
					modelo.actualizaEstadoMensajeComoEnviado(mensaje, null);
				}
			}
		} catch (SQLException | UVException | IOException e) {
			LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
}
