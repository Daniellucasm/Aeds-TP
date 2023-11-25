import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LZW {

    public void compress(String uncompressed) {
        Map<String, Integer> dictionary = new HashMap<>();
        int dictSize = 256;
        // criando um dicionario inicial
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char) i, i);
        }

        String current = "";
        List<Integer> result = new ArrayList<>();
        for (char c : uncompressed.toCharArray()) {
            String temp = current + c;
            if (dictionary.containsKey(temp)) {
                current = temp;
            } else {
                result.add(dictionary.get(current));
                dictionary.put(temp, dictSize++);
                current = "" + c;
            }
        }

        if (!current.equals("")) {
            result.add(dictionary.get(current));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arquivoLZWCompressao.txt"))) {
            for (int i = 0; i < result.size(); i++) {
                writer.write(result.get(i) + ", ");
            }
            System.out.println("Texto foi escrito no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void decompress() {
        List<Integer> compressed = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("arquivoLZWCompressao.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(", "); // Supondo que os inteiros estejam separados por ", "
                for (String value : values) {
                    if (!value.equalsIgnoreCase("null")) {
                        compressed.add(Integer.parseInt(value.trim()));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
            return; // Se houver erro na leitura, sai do método
        }
        // Faça a descompressão aqui usando a List de Integer
        Map<Integer, String> dictionary = new HashMap<>();
        int dictSize = 256;
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);
        }

        StringBuilder decompressed = new StringBuilder();
        if (!compressed.isEmpty()) {
            int prevCode = compressed.remove(0);
            decompressed.append(dictionary.get(prevCode));

            for (int code : compressed) {
                String entry;
                if (dictionary.containsKey(code)) {
                    entry = dictionary.get(code);
                } else {
                    if (code == dictSize) {
                        entry = dictionary.get(prevCode) + dictionary.get(prevCode).charAt(0);
                    } else {
                        //throw new IllegalArgumentException("Bad compressed k: " + code);
                        entry = " ";
                    }
                }

                decompressed.append(entry);
                dictionary.put(dictSize++, dictionary.get(prevCode) + entry.charAt(0));
                prevCode = code;
            }
        }

        // Output da string descomprimida
        System.out.println("Texto descomprimido: " + decompressed.toString());
    }
}
