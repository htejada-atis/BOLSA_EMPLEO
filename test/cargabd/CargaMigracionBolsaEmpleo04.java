package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210713 .
 * 	- rol de miembros de comisión en menú resultados .
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo04 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/05-resultadosevaluadores.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}