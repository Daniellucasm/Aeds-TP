import java.util.*;
import java.io.*;

public class Huffman {

    public void compress(String text) {
        Map<Character, Integer> frequencyMap = buildFrequencyMap(text);
        HuffmanNode root = buildHuffmanTree(frequencyMap);
        Map<Character, String> huffmanCodes = generateHuffmanCodes(root);

        String encodedText = encode(text, huffmanCodes);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("compressed_huffman.txt"))) {
            writer.write(encodedText);
            System.out.println("Texto comprimido foi escrito no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void decompress() {
        StringBuilder encodedText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("compressed_huffman.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                encodedText.append(line);
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        Map<Character, Integer> frequencyMap = buildFrequencyMap(encodedText.toString());
        HuffmanNode root = buildHuffmanTree(frequencyMap);
        String decodedText = decode(encodedText.toString(), root);

        System.out.println("Texto descomprimido: " + decodedText);
    }

    private Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    private HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode newNode = new HuffmanNode('\0', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.offer(newNode);
        }

        return priorityQueue.poll();
    }

    private void generateHuffmanCodesHelper(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.data, code);
        }

        generateHuffmanCodesHelper(root.left, code + "0", huffmanCodes);
        generateHuffmanCodesHelper(root.right, code + "1", huffmanCodes);
    }

    private Map<Character, String> generateHuffmanCodes(HuffmanNode root) {
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodesHelper(root, "", huffmanCodes);
        return huffmanCodes;
    }

    private String encode(String text, Map<Character, String> huffmanCodes) {
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(c));
        }
        return encodedText.toString();
    }

    private String decode(String encodedText, HuffmanNode root) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode current = root;

        for (int i = 0; i < encodedText.length(); i++) {
            if (encodedText.charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                decodedText.append(current.data);
                current = root;
            }
        }

        return decodedText.toString();
    }
}
