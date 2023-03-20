package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;

/**
 * Migraciones 20230316 . 
 * - nombre y codigo de los méritos . 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo23 {
	/**
	 * Main .
	 * 
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/35-solicitudbolsasmeritosnombrecodigo.sql");
			
			UtilsTestBolsaEmpleo.updateNombreCodigoEnSolicitudBolsaMeritos();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
