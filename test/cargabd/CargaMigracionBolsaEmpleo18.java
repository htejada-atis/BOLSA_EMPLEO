package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20211124 .
 *	- estado finalizada convocatoria .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo18 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/30-convocatoriafinalizada.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
