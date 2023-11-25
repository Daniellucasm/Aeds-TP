/*
 * Classe abstrata da arvore 
 * De forma objetiva uma classe abstrata serve como modelo para uma classe concreta, neste caso para as classes NODE e LEAF
 */
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // Frequencia da arvore
    //
    public HuffmanTree(int freq) { 
    	frequency = freq; 
    }
    
    // Compara as frequ�ncias - Implementação da Interface Comparable para a ordenação na fila
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}