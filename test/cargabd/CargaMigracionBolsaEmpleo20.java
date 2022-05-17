package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20220516 .
 *	- fecha final de baremación bolsas y fecha de habilitación de contratos .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo20 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/32-fechasbolsas.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
