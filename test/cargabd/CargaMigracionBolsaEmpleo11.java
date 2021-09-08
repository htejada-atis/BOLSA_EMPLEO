package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210907 .
 *	- parámetro remitente .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo11 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/22-parametroremitente.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
