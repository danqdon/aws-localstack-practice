package ProteinSynthesis;

import Biomolecules.Codon;

import java.util.List;

public class MessengerRNA {

    private final List<Codon> anticodons;

    public MessengerRNA(List<Codon> anticodons){
        this.anticodons=anticodons;
    }

    public List<Codon> getAnticodons() {
        return anticodons;
    }
}
