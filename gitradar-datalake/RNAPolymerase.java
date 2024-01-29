package ProteinSynthesis;

import Biomolecules.Base;
import Biomolecules.Codon;
import Biomolecules.DNAStrand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RNAPolymerase {

    private static final Map<Base, Base> TRANSCRIPTION_MAP = Map.of(
            Base.ADENINE, Base.URACIL,
            Base.THYMINE, Base.ADENINE,
            Base.CYTOSINE, Base.GUANINE,
            Base.GUANINE, Base.CYTOSINE
    );

    public MessengerRNA transcription(DNAStrand dnaStrand){
        List<Codon> codons = dnaStrand.codons;
        List<Codon> anticodons = new ArrayList<Codon>();
        for (Codon codon : codons){
            Base firstBase = TRANSCRIPTION_MAP.get(codon.getFirstBase());
            Base secondBase = TRANSCRIPTION_MAP.get(codon.getSecondBase());
            Base thirdBase = TRANSCRIPTION_MAP.get(codon.getThirdBase());
            anticodons.add(new Codon(firstBase,secondBase,thirdBase));
        }
        return new MessengerRNA(anticodons);
    }
}
