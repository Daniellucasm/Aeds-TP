import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Cesar {

    // Método para criptografar uma mensagem usando a cifra de César
    public static String criptografar(String mensagem, int chave) {
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < mensagem.length(); i++) {
            char caractere = mensagem.charAt(i);

            // Verifica se o caractere é uma letra do alfabeto
            if (Character.isLetter(caractere)) {
                // Obtém o código ASCII do caractere atual
                int codigoAscii = (int) caractere;

                // Verifica se é maiúscula ou minúscula
                int base = Character.isUpperCase(caractere) ? 'A' : 'a';

                // Aplica a cifra de César considerando a chave
                codigoAscii = ((codigoAscii - base + chave) % 26) + base;

                // Converte o código ASCII de volta para caractere
                caractere = (char) codigoAscii;
            }

            // Adiciona o caractere ao resultado
            resultado.append(caractere);
        }

        // Gravar o texto Criptografado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arquivoCesarCifra.txt"))) {
            writer.write(resultado.toString());
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
        }

        return resultado.toString();
    }

    // Método para descriptografar uma mensagem usando a cifra de César
    public static String descriptografar(String arquivo, int chave) {
        String mensagem = "";

        try (BufferedReader reader = new BufferedReader(new FileReader("arquivoCesarCifra.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                mensagem += line;
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }
        // Para descriptografar, basta usar a chave negativa
        return criptografar(mensagem, -chave);
    }
}
