package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210913 .
 *	- campo tipo para la dedicación .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo13 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/24-tipodedicacion.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
