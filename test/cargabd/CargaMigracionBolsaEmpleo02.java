package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;

/**
 * Migraciones 20210624 .
 * 	- valor en solicitud bolsas méritos para validar no afines
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo02 {

	/**
	 * main.
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/03-valorsolicitudbolsasmeritos.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
