package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210629 .
 * 	- fecha de baremación en bolsas .
 * 	- pendiente de baremación en bolsas .
 * 	- puntuación en solicitud bolsas .
 * 	- archivo en solicitud bolsas .
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo03 {

	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/04-resultados.sql.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}