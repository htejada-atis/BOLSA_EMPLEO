package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20211121 .
 *	- creador de la plaza .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo17 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/29-convocatoriafinalizada.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
