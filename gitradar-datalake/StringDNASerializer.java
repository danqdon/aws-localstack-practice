package Serializers;

import Biomolecules.Codon;
import Biomolecules.DNAStrand;
import ProteinSynthesis.DNASerializer;

public class StringDNASerializer implements DNASerializer {

    public String serialize(DNAStrand dnaStrand) {
        StringBuilder sb = new StringBuilder();
        for (Codon codon : dnaStrand.codons) {
            sb.append(codon.toString()).append(" ");
        }
        return sb.toString();
    }
}


