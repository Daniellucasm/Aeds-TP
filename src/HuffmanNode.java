/*
 * Classe de um nó da arvore 
 */
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // sub-arvores
 
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}