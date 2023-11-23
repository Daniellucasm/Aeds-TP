/*
 * Classe abstrata da �rvore 
 * De forma objetiva uma classe abstrata serve como modelo para uma classe concreta, neste caso para as classes NODE e LEAF
 */
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // Frequ�ncia da �rvore
    //
    public HuffmanTree(int freq) { 
    	frequency = freq; 
    }
    
    // Compara as frequ�ncias - Implementa��o da Interface Comparable para a ordena��o na fila
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}