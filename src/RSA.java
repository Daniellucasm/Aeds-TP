import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
    public static void startRSA(String mensagem, int bits){
        // Encontrar números primos grandes p e q
		BigInteger p = largePrime(bits);
		BigInteger q = largePrime(bits);

        // Calculo de n a partir de p e q
		BigInteger n = n(p, q);

        // Calculo de z
		// z = (p-1)(q-1)
		BigInteger phi = getPhi(p, q);

        // Calculo de e
		BigInteger e = genE(phi);

		// Calculo de d
		BigInteger d = extEuclid(e, phi)[1];

        // salvar chaves para referencia
		salvaChaves(p, q, n, phi, e, d);

        // Converter string em números usando uma cifra
		BigInteger cipherMessage = stringCipher(mensagem);
		// Criptografe a mensagem cifrada
		BigInteger encrypted = encrypt(cipherMessage, e, n);
		// Descriptografe a mensagem criptografada
		BigInteger decrypted = decrypt(encrypted, d, n);
		// Descifrar a mensagem descriptografada em texto
		String restoredMessage = cipherToString(decrypted);

		System.out.println("Original message: " + mensagem);
		System.out.println("Ciphered: " + cipherMessage);
		// Gravar o texto Criptografado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arquivoRSACifra.txt"))) {
            writer.write(cipherMessage.toString());
        } catch (IOException a) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + a.getMessage());
        }
		System.out.println("Encrypted: " + encrypted);
		System.out.println("Decrypted: " + decrypted);
		System.out.println("Restored: " + restoredMessage);
    }

	public static void salvaChaves(BigInteger p, BigInteger q, BigInteger n, BigInteger phi, BigInteger e, BigInteger d){
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("arquivoRSAChave.txt"))) {
            writer.write(p.toString() + "\n");
			writer.write(q.toString() + "\n");
			writer.write(n.toString() + "\n");
			writer.write(phi.toString() + "\n");
			writer.write(e.toString() + "\n");
			writer.write(d.toString() + "\n");
        } catch (IOException a) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + a.getMessage());
        }
	}

    /**
	 * Pega uma string e converte cada caractere em um valor decimal ASCII
	 * Retorna BigInteger
	 */
	public static BigInteger stringCipher(String message) {
		message = message.toUpperCase();
		String cipherString = "";
		int i = 0;
		while (i < message.length()) {
			int ch = (int) message.charAt(i);
			cipherString = cipherString + ch;
			i++;
		}
		BigInteger cipherBig = new BigInteger(String.valueOf(cipherString));
		return cipherBig;
	}

    /**
	 * Pega um BigInteger que está cifrado e o converte novamente em texto simples
	 *	retorna String
	 */
	public static String cipherToString(BigInteger message) {
		String cipherString = message.toString();
		String output = "";
		int i = 0;
		while (i < cipherString.length()) {
			int temp = Integer.parseInt(cipherString.substring(i, i + 2));
			char ch = (char) temp;
			output = output + ch;
			i = i + 2;
		}
		return output;
	}

    /** Calculo de z
	 *	z = (p-1)(q-1)
	 */
	public static BigInteger getPhi(BigInteger p, BigInteger q) {
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		return phi;
	}

	/**
	 * Gera um grande número primo aleatório de comprimento de bits especificado
	 *
	 */
	public static BigInteger largePrime(int bits) {
		Random randomInteger = new Random();
		BigInteger largePrime = BigInteger.probablePrime(bits, randomInteger);
		return largePrime;
	}


	/**
	 * Implementação recursiva do algoritmo euclidiano para encontrar o maior denominador comum
	 * Usa operações BigInteger
	 */
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO)) {
			return a;
		} else {
			return gcd(b, a.mod(b));
		}
	}


	/** Algoritmo euclidiano recursivo EXTENDED, resolve a identidade de Bezout (ax + by = gcd(a,b))
	 * e encontra o inverso multiplicativo que é a solução para ax ≡ 1 (mod m)
	 * retorna [d, p, q] onde d = mdc(a,b) e ap + bq = d
	 * Usa operações BigInteger
	 */
	public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO)) return new BigInteger[] {
			a, BigInteger.ONE, BigInteger.ZERO
		}; // { a, 1, 0 }
		BigInteger[] vals = extEuclid(b, a.mod(b));
		BigInteger d = vals[0];
		BigInteger p = vals[2];
		BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));
		return new BigInteger[] {
			d, p, q
		};
	}


	/**
	 * Gera e encontra um Phi tal que sejam coprimos (mdc = 1)
	 *
	 */
	public static BigInteger genE(BigInteger phi) {
		Random rand = new Random();
		BigInteger e = new BigInteger(1024, rand);
		do {
			e = new BigInteger(1024, rand);
			while (e.min(phi).equals(phi)) { // enquanto phi é menor que e, procure um novo e
				e = new BigInteger(1024, rand);
			}
		} while (!gcd(e, phi).equals(BigInteger.ONE)); // se gcd(e,phi) não for 1 então permaneça no loop
		return e;
	}

	public static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
		return message.modPow(e, n);
	}

	public static BigInteger decrypt(BigInteger message, BigInteger d, BigInteger n) {
		return message.modPow(d, n);
	}

	public static BigInteger n(BigInteger p, BigInteger q) {
		return p.multiply(q);

	}
}
