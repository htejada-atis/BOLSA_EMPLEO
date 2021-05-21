package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;

/** Carga inicial para uv.
 */
public class CargaInicialUvIntegracion {

	private CargaInicialUvIntegracion() {
		
	}
	
	/** main.
	 * @param args argumentos
	 */
	public static void main(String[] args) {
		try {
			BbddRunner.inicializaBd();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

}
