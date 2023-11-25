import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class CaractereForaDoIntervaloException extends Exception {
    public CaractereForaDoIntervaloException(String message) {
        super(message);
    }
}

public class Huffman {
    HuffmanTree tree; 

    /*
     * Método para executar todas as funções para compactar e decodificar o texto
     * Parametro de Entrada: jogadores: Lista de Players
     */
    public void executar(List<Players> jogadores) {
        // Texto exemplo para Compactar usando o Algoritmo de Huffman
        String test = jogadores.toString();

        // Passo 1 - Percorre o texto contando os simbolos e montando um vetor de
        // frequencias.
        int[] charFreqs = new int[256];
        for (char c : test.toCharArray()) {
            if (c < 256) {
                charFreqs[c]++;
            } else {
                System.out.println(c);
            }
        }

        // Criar a arvore dos codigos para a Compactação
        tree = buildTree(charFreqs);

        // Resultados das quantidade e o codigo da Compactação
        System.out.println("TABELA DE CÓDIGOS");
        System.out.println("SÍMBOLO\tQUANTIDADE\tHUFFMAN CÓDIGO");
        printCodes(tree, new StringBuffer());

        // Compactar o texto
        String encode = encode(tree, test);
        // Gravar o texto Compactado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arquivoHuffmanCompressao.txt"))) {
            writer.write(encode);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
        }

        // Decodificar o texto
        System.out.println("\n\nTEXTO DECODIFICADO");
        System.out.println(decode(tree, encode));

    }

    /*
     * Criar a árvore de Codificação - A partir da quantidade de frequencias de cada
     * letra
     * cria-se uma arvore binaria para a compactação do texto
     * Parametro de Entrada: charFreqs: array com quantidade de frequ�ncias de cada
     * letra
     * Parametro de Saída: trees: arvore binaria para a compactação e decodificação
     */
    public HuffmanTree buildTree(int[] charFreqs) {
        // Cria uma Fila de Prioridade
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // Criar as Folhas da �rvore para cada letra
        for (int i = 0; i < charFreqs.length; i++) {
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char) i)); // Inser os elementos, nó folha, na fila de
                                                                      // prioridade
        }
        // Percorre todos os elementos da fila
        // Criando a arvore binaria de baixo para cima
        while (trees.size() > 1) {
            HuffmanTree a = trees.poll(); // poll - Retorna o proximo na da Fila ou NULL se não tem mais
            HuffmanTree b = trees.poll(); // poll - Retorna o proximo na da Fila ou NULL se não tem mais

            // Criar os nós da arvore binaria
            trees.offer(new HuffmanNode(a, b));
        }
        // Retorna o primeiro n� da �rvore
        return trees.poll();
    }

    /*
     * COMPACTAR a string
     * Parametro de Entrada: tree - Raiz da arvore de compactação
     * encode - Texto original
     * Parametro de Saida: encodeText- Texto Compactado
     */
    public String encode(HuffmanTree tree, String encode) {
        assert tree != null;

        String encodeText = "";
        for (char c : encode.toCharArray()) {
            encodeText += (getCodes(tree, new StringBuffer(), c));
        }
        return encodeText; // Retorna o texto Compactado
    }

    /*
     * DECODIFICAR a string
     * Parametro de Entrada: tree - Raiz da arvore de compactação
     * encode - Texto Compactado
     * Parametro de Saida: decodeText- Texto decodificado
     */
    public String decode(HuffmanTree tree, String encode) {
        assert tree != null;

        String decodeText = "";
        HuffmanNode node = (HuffmanNode) tree;
        for (char code : encode.toCharArray()) {
            if (code == '0') { // Quando for igual a 0 é o Lado Esquerdo
                if (node.left instanceof HuffmanLeaf) {
                    decodeText += ((HuffmanLeaf) node.left).value; // Retorna o valor do nó folha, pelo lado Esquerdo
                    node = (HuffmanNode) tree; // Retorna para a Raiz da arvore
                } else {
                    node = (HuffmanNode) node.left; // Continua percorrendo a arvore pelo lado Esquerdo
                }
            } else if (code == '1') { // Quando for igual a 1 é o Lado Direito
                if (node.right instanceof HuffmanLeaf) {
                    decodeText += ((HuffmanLeaf) node.right).value; // Retorna o valor do nó folha, pelo lado Direito
                    node = (HuffmanNode) tree; // Retorna para a Raiz da arvore
                } else {
                    node = (HuffmanNode) node.right; // Continua percorrendo a arvore pelo lado Direito
                }
            }
        } // End for
        return decodeText; // Retorna o texto Decodificado
    }

    /*
     * Metodo para percorrer a arvore e mostra a tabela de compactação
     * Parametros de Entrada: tree - Raiz da arvore de compactação
     * prefix - texto codificado com 0 e/ou 1
     */
    public void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;

        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            // Imprime na tela a Lista
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t\t" + prefix);

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /*
     * Metodo para retornar o codigo compactado de uma letra (w)
     * Parametros de Entrada: tree - Raiz da arvore de compactação
     * prefix - texto codificado com 0 e/ou 1
     * w - Letra
     * Parametros de Saida: prefix- Letra codificada
     */
    public String getCodes(HuffmanTree tree, StringBuffer prefix, char w) {
        assert tree != null;

        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            // Retorna o texto compactado da letra
            if (leaf.value == w) {
                return prefix.toString();
            }

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // Percorre a esquerda
            prefix.append('0');
            String left = getCodes(node.left, prefix, w);
            prefix.deleteCharAt(prefix.length() - 1);

            // Percorre a direita
            prefix.append('1');
            String right = getCodes(node.right, prefix, w);
            prefix.deleteCharAt(prefix.length() - 1);

            if (left == null)
                return right;
            else
                return left;
        }
        return null;
    }

}
