package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210722 .
 * 	- acreditaciones y titulaciones validadas guardadas con solicitud bolsas para mostrar en resultados .
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo07 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/09-titulacionesacreditacionesresultados.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
