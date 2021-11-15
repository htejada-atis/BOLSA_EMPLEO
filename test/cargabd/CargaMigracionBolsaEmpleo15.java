package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20211019 .
 *	- plantilla aprobación plaza .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo15 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/27-parametroplantillaaprobacionplaza.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
