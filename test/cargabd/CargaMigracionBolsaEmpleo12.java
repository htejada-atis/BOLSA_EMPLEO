package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210909 .
 *	- campo id_plaza .
 *	- campo centro plaza actualizado .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo12 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/23-codigocentroactivaplaza.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
