package ProteinSynthesis;

import java.util.List;

public class Peptide {
    private List<Aminoacid> chain;
    public Peptide(List<Aminoacid> chain){
        this.chain = chain;
    }

    public List<Aminoacid> getChain() {
        return chain;
    }
}
