/*
 * Classe do n� folha da �rvore 
 */
class HuffmanLeaf extends HuffmanTree {
    public final char value; // A letra � atribuida a um n� folha 
 
    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}