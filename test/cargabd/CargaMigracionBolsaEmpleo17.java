package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20211121 .
 *	- parámetro plantilla creación de la plaza .
 *	- parámetro email creación de la plaza .
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
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/29-parametrosplantillaemailcreacionplaza.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
