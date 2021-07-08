package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;

/**
 * Migraciones 20210623 .
 * 	- excluir solicitud
 * 	- ajustes mensajería
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo01 {

	/**
	 * main.
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/01-exclusionsolicitud.sql");	
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/02-mensajesdestinatarios.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
