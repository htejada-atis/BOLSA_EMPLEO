package es.ujaen.uvirtual.modelo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Modelo para claves de arcos.
 *
 */
public class ModeloClaveArcos {
	private static String eNombreDeEstaClase = ModeloClaveArcos.class.getName();
	private static final Logger ELOGGER = Logger.getLogger(ModeloClaveArcos.class.getName());
	
    private static final int SALT_LENGTH = 4;
    private static final int TAM_HASH_SSHA512 = 64;
    
    private static final String CARACTER_TIPO_VOCAL = "vocal";
    private static final String CARACTER_TIPO_CONSONANTE = "consonante";
    private static final String CARACTER_TIPO_ESPECIAL = "especial";
    private static final String CARACTER_TIPO_NUMERO = "numero";
    private static final String CARACTER_TIPO_MAYUSCULA = "mayuscula";
    private static final String CARACTER_TIPO_MINUSCULA = "minuscula";
    private static final String CARACTER_TIPO_CUALQUIERA = "cualquiera";
	
	/** genera ssha512 de una clave y un salt.
	 * @param miClave clave en claro
	 * @param salt salt usada, si es null se usa una aleatoria
	 * @return ssha de la clave y la salt
	 * @throws NoSuchAlgorithmException si algorotmo de hash no soportado
	 * @throws IOException si error io
	 */
	public String generaSsha512(String miClave, byte[] salt) throws NoSuchAlgorithmException, IOException {
		byte[] saltUsada = new byte[SALT_LENGTH];
		if (salt == null) {
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.nextBytes(saltUsada);
		} else {
			saltUsada = salt;
		}
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.reset();
		md.update(miClave.getBytes());
		md.update(saltUsada);
		byte[] hash = md.digest();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(hash);
		outputStream.write(saltUsada);

		byte[] hashPlusSalt = outputStream.toByteArray();

		return new StringBuilder().append("{SSHA512}").
				append(Base64.getEncoder().encodeToString(hashPlusSalt)).toString();
	}
	
	/** Comprueba si una clave y un hash son iguales.
	 * @param claveUsuario clave en claro
	 * @param claveAlmacenada hash almacenado
	 * @return verdadero si son iguales
	 */
	public boolean isClaveIgualHash(String claveUsuario, String claveAlmacenada) {
		boolean salida = false;
		final String nombreEsteMetodo = "isClaveIgualHash";
		try {
			String formatoClave = claveAlmacenada.substring(0, claveAlmacenada.indexOf("}") + 1);
			ELOGGER.logp(Level.FINER, eNombreDeEstaClase, nombreEsteMetodo, "formatoClave {0}", formatoClave);
			String claveAlmacenadaSinformato = claveAlmacenada.substring(claveAlmacenada.indexOf("}") + 1);
			String hashUsuario = "";
			byte[] hashAlmacenado = new byte[TAM_HASH_SSHA512];
			byte[] saltAlmacenado = new byte[SALT_LENGTH];
			byte[] hashSaltAlmacenada;
			ByteBuffer bb;
			if ("{SSHA512}".equals(formatoClave)) {
				hashSaltAlmacenada = Base64.getDecoder().decode(claveAlmacenadaSinformato);
				bb = ByteBuffer.wrap(hashSaltAlmacenada);
				bb.get(hashAlmacenado, 0, TAM_HASH_SSHA512);
				bb.get(saltAlmacenado, 0, saltAlmacenado.length);
				hashUsuario = generaSsha512(claveUsuario, saltAlmacenado);
			} else {
				throw new NoSuchAlgorithmException("formato no soportado " + formatoClave);
			}
			ELOGGER.logp(Level.FINER, eNombreDeEstaClase, nombreEsteMetodo, "clave hash usuario {0}", hashUsuario);
			ELOGGER.logp(Level.FINER, eNombreDeEstaClase, nombreEsteMetodo, "clave almacenada {0}", claveAlmacenada);
			if (hashUsuario.equals(claveAlmacenada)) {
				salida = true;
			}
		} catch (Exception e) {
			ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, nombreEsteMetodo, "error al comprobar clave almacenada {0}", claveAlmacenada);
		}
		return salida;
	}

	/** verifica que la clave cumple con los requisitos de formato del sinf.
	 * @param usuario uid del usuario
	 * @param clave clave en claro
	 * @return true si la clave cumple con los requisitos
	 */
	public boolean validaFormatoClave(String usuario, String clave) {
		boolean salida = true;
		String sClaveRev = (new StringBuffer(clave)).reverse().toString().toLowerCase();
		
		// condiciones no vÃ¡lidas
		if ((usuario.equalsIgnoreCase(clave)) || (usuario.equals(sClaveRev)) || (clave.toLowerCase().contains(usuario))) {
			salida = false;
		}
		
		if (!validaFormatoCaracteresClave(clave)) {
			salida = false;
		}
		return salida;
	}
	
	private List<Integer> cuentaNumeroCaracteres(String clave) {
		ArrayList<Integer> salida = new ArrayList<>();
		int pcount = 0;
		int num = 0;
		int alfa = 0;
		int alfaMay = 0;
		int especial = 0;
		for (int i = 0; i < clave.length(); i++) {
			boolean contado = false;
			pcount++;
			if (clave.charAt(i) >= '0' && clave.charAt(i) <= '9') {
				num++;
				contado = true;
			}
			if (clave.charAt(i) >= 'a' && clave.charAt(i) <= 'z') {
				alfa++;
				contado = true;
			}
			if (clave.charAt(i) >= 'A' && clave.charAt(i) <= 'Z') {
				alfaMay++;
				contado = true;
			}
			if (!contado) {
				especial++;
			}
		}
		salida.add(pcount);
		salida.add(num);
		salida.add(alfa);
		salida.add(alfaMay);
		salida.add(especial);
		return salida;
	}
	
	private int cuentaNumeroCaracteres(String clave, String tipo) {
		List<Integer> cuenta = cuentaNumeroCaracteres(clave);
		int salida = 0;
		final int posicionMayuscula = 3;
		final int posicionEspecial = 4;
		switch (tipo) {
		case CARACTER_TIPO_NUMERO:
			salida = cuenta.get(1);
			break;
		case CARACTER_TIPO_MINUSCULA:
			salida = cuenta.get(2);
			break;
		case CARACTER_TIPO_MAYUSCULA:
			salida = cuenta.get(posicionMayuscula);
			break;
		case CARACTER_TIPO_ESPECIAL:
			salida = cuenta.get(posicionEspecial);
			break;
		default:
			salida = cuenta.get(0);
		}
		return salida;
	}
	
	private boolean validaFormatoCaracteresClave(String clave) {
		boolean salida = true;
		final int minimoTamClave = 8;
		int pcount = cuentaNumeroCaracteres(clave, CARACTER_TIPO_CUALQUIERA);
		int num = cuentaNumeroCaracteres(clave, CARACTER_TIPO_NUMERO);
		int alfa = cuentaNumeroCaracteres(clave, CARACTER_TIPO_MINUSCULA);
		int alfaMay = cuentaNumeroCaracteres(clave, CARACTER_TIPO_MAYUSCULA);
		int especial = cuentaNumeroCaracteres(clave, CARACTER_TIPO_ESPECIAL);
		if (pcount < minimoTamClave || num < 1 || alfa < 1 || alfaMay < 1 || especial < 1) {
			salida = false;
		}
		return salida;
	}

	/** obtiene un pin aleatorio temporal.
	 * @return pin aleatorio temporal
	 */
	public String pinAleatorioTemporal() {
		final int pinMinimo = 100000;
		final int pinMaximo = 999999;
		return String.valueOf(randInt(pinMinimo, pinMaximo));
	}	
	
	private static int randInt(int min, int max) {
		SecureRandom rand = new SecureRandom();
		return rand.nextInt((max - min) + 1) + min;
	}

	private String caracterAleatorio(String tipo) {
		String salida = "";
		switch (tipo) {
		case CARACTER_TIPO_VOCAL:
			String vocales = "aeiu"; //sin o
			int rand = randInt(0, vocales.length() - 1);
			salida = vocales.substring(rand, rand + 1);
			break;
		case CARACTER_TIPO_CONSONANTE:
			String consonantes = "bcdfghjkmnpqrstvwxyz"; //sin l
			rand = randInt(0, consonantes.length() - 1);
			salida = consonantes.substring(rand, rand + 1);
			break;
		case CARACTER_TIPO_ESPECIAL:
			String especiales = ",.:;(){}[]%$&?";
			rand = randInt(0, especiales.length() - 1);
			salida = especiales.substring(rand, rand + 1);
			break;
		default:
			salida = "";
		}
		return salida;
	}
	
	/** crea un password aleatorio.
	 * @return password aleatorio
	 */
	public String passwordAleatorio() {
		String password = "";
		final int numeroMinPass = 2;
		final int numeroMaxPass = 9;

		int subnum1 = randInt(numeroMinPass, numeroMaxPass); //sin 0 ni 1
		int subnum2 = randInt(numeroMinPass, numeroMaxPass);

		String substr1 = caracterAleatorio(CARACTER_TIPO_VOCAL);
		substr1 = substr1 + caracterAleatorio(CARACTER_TIPO_CONSONANTE);
		substr1 = substr1 + caracterAleatorio(CARACTER_TIPO_VOCAL);

		String substr2 = caracterAleatorio(CARACTER_TIPO_CONSONANTE);
		substr2 = substr2 + caracterAleatorio(CARACTER_TIPO_VOCAL);
		substr2 = substr2 + caracterAleatorio(CARACTER_TIPO_CONSONANTE);

		// Pasamos una de las letras a mayuscula
		int pos = randInt(0, 2);
		if ((randInt(0, 1)) >= 1) {
			substr1 = substr1.replace(substr1.charAt(pos), substr1.substring(pos, pos + 1).toUpperCase().charAt(0));
		} else {
			substr2 = substr2.replace(substr2.charAt(pos), substr2.substring(pos, pos + 1).toUpperCase().charAt(0));
		}

		// Concatenamos
		if ((randInt(0, 1)) >= 1) {
			password = substr1 + caracterAleatorio(CARACTER_TIPO_ESPECIAL) + substr2;
		} else {
			password = caracterAleatorio(CARACTER_TIPO_ESPECIAL) + substr2 + substr1;
		}
		// Unimos con los valores numericos
		if ((randInt(0, 1)) >= 1) {
			password = password + subnum1;
		} else {
			password = subnum1 + password;
		}
		if ((randInt(0, 1)) >= 1) {
			password = password + subnum2;
		} else {
			password = subnum2 + password;
		}
		return password;
	}
	
}
