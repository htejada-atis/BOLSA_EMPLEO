package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210723 .
 * 	- cambiado el peso de bloque .
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo08 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/10-pesoapartados.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
