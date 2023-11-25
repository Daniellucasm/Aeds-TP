/*
 * Classe do nó folha da arvore 
 */
class HuffmanLeaf extends HuffmanTree {
    public final char value; // A letra atribuida a um nó folha 
 
    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}