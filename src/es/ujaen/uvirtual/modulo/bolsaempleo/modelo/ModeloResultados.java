package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudValoracion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para los resultados de las solicitudes .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloResultados {
	
	public static final String MENSAJE_ERROR_BOLSA_NULL = "Bolsa no puede estar vacía";
	public static final String MENSAJE_ERROR_MERITO_NULL = "Mérito no puede estar vacío";
	
	public static final String SOLICITUD_ESTADO_ABIERTA = "ABIERTA";
	public static final String SOLICITUD_ESTADO_CERRADA = "CERRADA";
	public static final String BEPMER_CODNUM = "BEPMER_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String BEPBOL_CODNUM = "BEPBOL_CODNUM";
	
		
	protected static ModeloResultados eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloResultados();
		}
	}

    /**
     * Obtiene una instancia de la conexión .
     * @return instancia .
     */
	public static ModeloResultados obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
        }
		return eInstancia;
    }
	
	/** Calculo total de una solicitud para un área .
	 * @param solicitud .
	 * @param bolsa .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public void calcularSolicitud(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		ModeloSolicitud modeloSolicitud = ModeloSolicitud.obtenerInstancia();
		
		Double total = 0.0;
		Double totalSinAplicar = 0.0;
		
		List<MeritoSolicitudTable> meritos = modeloSolicitud.getMeritosValoracionesSolicitudBolsa(solicitud, bolsa);
		for (MeritoSolicitudTable merito: meritos) {
			totalSinAplicar += calcularMerito(merito);
		}
		
		total = totalSinAplicar;
		
		guardarResultadoSolicitudBolsa(bolsa, solicitud, total, totalSinAplicar, "", null, null);
	}
	
	private Double calcularMerito(MeritoSolicitudTable merito) throws SQLException, UVException {
		Double puntuacion = 0.0;
		
		boolean tieneAfinidad = merito.getMerito().getItemBaremacion().getAfinidad() != null;
		
		Double valor = tieneAfinidad ? merito.getMerito().getValor() : merito.getMeritoSolicitud().getValor();
		Double afinidad = tieneAfinidad ? merito.getMeritoSolicitud().getValoraciones().get(0).getAfinidad().getModulacion() : 1.0;
		Double pesoCategoria = merito.getMerito().getItemBaremacion().getValor();
		Double pesoBloque = merito.getMerito().getItemBaremacion().getBloqueBaremacion().getApartadoBaremacion().getPorcentajeMaximo();
		
		if (!merito.getMerito().getItemBaremacion().getIndividualizado()) {
			valor = 0.0;
			afinidad = 0.0;
			for (int i = 0; i < merito.getMeritoSolicitud().getValoraciones().size(); i++) {
				valor += merito.getMeritoSolicitud().getValoraciones().get(i).getValor();
				afinidad += merito.getMeritoSolicitud().getValoraciones().get(i).getAfinidad().getModulacion();
			}
		}
		
		puntuacion = valor * afinidad * pesoCategoria * pesoBloque;
		String desglose = valor + " * " + afinidad + " * " + pesoCategoria + " * " + pesoBloque;
		
		this.guardarResultadoSolicitudBolsaMerito(merito, puntuacion, desglose, null);
		
		return puntuacion;
	}
	
	/** Lista de solicitudes asociadas a una bolsa .
	 * @param bolsa .
	 * @param convocatoria .
	 * @return solicitudes .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	public List<Solicitud> listaSolicitudesBolsa(Bolsa bolsa, Convocatoria convocatoria) throws SQLException, UVException {
		List<Solicitud> listaSolicitudes = new ArrayList<>();
		
		String consulta = "SELECT bepsol.* FROM TBEP_SOLICITUD_BOLSAS bepsob"
				+ "	INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsob.BEPSOL_CODNUM"
				+ "	WHERE bepsol.BEPCON_CODNUM = ? AND bepsob.BEPBOL_CODNUM = ? AND bepsol.ESTADO = " + SOLICITUD_ESTADO_CERRADA;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int paramIndex = 1;
			stmt.setInt(paramIndex++, convocatoria.getCodNum());
			stmt.setInt(paramIndex++, bolsa.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ModeloSolicitud.obtenerInstancia().createSolicitudFromResultSet(rs, false);
				}
			}
		}
		
		return listaSolicitudes;
	}
	
	/**
	 * Devuelve los méritos con valoraciones de la bolsa en una solicitud .
	 * @param bolsa .
	 * @param solicitud .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public List<MeritoSolicitudTable> getMeritosValoracionesSolicitudBolsa(Solicitud solicitud, Bolsa bolsa) throws SQLException, UVException {
		List<MeritoSolicitudTable> meritos = new ArrayList<>();
		
		String consulta = ""
				+ " SELECT bepsbm.BEPMER_CODNUM, bepsbm.CODNUM"
				+ " FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM  = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM"
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM"
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM"
				+ "	WHERE "
				+ "			bepsbo.BEPBOL_CODNUM = ?"
				+ "		AND bepsbo.BEPSOL_CODNUM = ?"
				+ "		AND bepsbm.FLGVALIDADO = 'S'"
				+ "		AND bepsbm.FLGEXCLUIDO = 'N'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int indexParam = 1;
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito merito = ModeloMerito.obtenerInstancia().getMeritoById(rs.getInt(BEPMER_CODNUM), false, false);
					Integer idMeritoSolicitud = rs.getInt(CODNUM);
					MeritoSolicitud ms = idMeritoSolicitud != 0 ? ModeloSolicitud.obtenerInstancia().getMeritoSolicitudById(idMeritoSolicitud) : null;
					List<MeritoSolicitudValoracion> valoraciones = idMeritoSolicitud != null 
							? ModeloSolicitud.obtenerInstancia().getValoracionesMeritoSolicitud(idMeritoSolicitud, false) : new ArrayList<>();
					
					MeritoSolicitudTable row = new MeritoSolicitudTable(merito, ms, valoraciones, 
							ModeloSolicitud.obtenerInstancia().isMeritoExcluido(merito, ms, bolsa));
					meritos.add(row);
				}
			}
		}
		
		return meritos;
	}
	
	/** Guardar resultado de la solicitud para una bolsa .
	 * @param bolsa .
	 * @param solicitud .
	 * @param total .
	 * @param totalSinAplicar .
	 * @param desgloseTotal .
	 * @param archivo .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void guardarResultadoSolicitudBolsa(Bolsa bolsa, Solicitud solicitud, Double total, Double totalSinAplicar, String desgloseTotal, InputStream archivo,
			UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (bolsa == null) {
			throw new UVException(MENSAJE_ERROR_BOLSA_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_SOLICITUD_BOLSAS SET RESULTADOTOTAL = ?, TOTALSINAPLICAR = ?, DESGLOSETOTAL = ?"
				+ " ARCHIVO = ?, UID_USUARIO = ? WHERE BEPBOL_CODNUM = ? AND BEPSOL_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDouble(indexParam++, total);
			stmt.setDouble(indexParam++, totalSinAplicar);
			stmt.setString(indexParam++, desgloseTotal);
			stmt.setBlob(indexParam++, archivo);
			stmt.setString(indexParam++, usuarioUp);
			stmt.setInt(indexParam++, bolsa.getCodNum());
			stmt.setInt(indexParam++, solicitud.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Guardar resultado de un mérito en una solicitud para una bolsa .
	 * @param merito .
	 * @param puntuacion .
	 * @param desglose .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void guardarResultadoSolicitudBolsaMerito(MeritoSolicitudTable merito, Double puntuacion, String desglose, 
			UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException(MENSAJE_ERROR_MERITO_NULL);
		}
		
		String usuarioUp = usuarioUpdate != null ? usuarioUpdate.getCodCuenta() : "TAREA_PROGRAMADA";
		
		String query = "UPDATE TBEP_SOL_BOL_MERITOS"
				+ "	SET RESULTAD = ?, DESGLOSE = ?, UID_USUARIO = ?"
				+ "	WHERE BEPMER_CODNUM = ? AND BEPSBO_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setDouble(indexParam++, puntuacion);
			stmt.setString(indexParam++, desglose);
			stmt.setString(indexParam++, usuarioUp);
			stmt.setInt(indexParam++, merito.getMerito().getCodNum());
			stmt.setInt(indexParam++, merito.getCodNum());
			stmt.executeUpdate();
		}
	}
	
}
