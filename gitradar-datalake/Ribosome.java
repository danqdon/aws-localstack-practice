package ProteinSynthesis;

import Biomolecules.Base;
import Biomolecules.Codon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ribosome {
    private static final Map<Codon, Aminoacid> TRANSLATION_MAP = new HashMap<>() {{
        // Phenylalanine
        put(new Codon(Base.URACIL, Base.URACIL, Base.URACIL), Aminoacid.PHE);
        put(new Codon(Base.URACIL, Base.URACIL, Base.CYTOSINE), Aminoacid.PHE);

        // Leucine
        put(new Codon(Base.URACIL, Base.URACIL, Base.ADENINE), Aminoacid.LEU);
        put(new Codon(Base.URACIL, Base.URACIL, Base.GUANINE), Aminoacid.LEU);
        put(new Codon(Base.CYTOSINE, Base.URACIL, Base.URACIL), Aminoacid.LEU);
        put(new Codon(Base.CYTOSINE, Base.URACIL, Base.CYTOSINE), Aminoacid.LEU);
        put(new Codon(Base.CYTOSINE, Base.URACIL, Base.ADENINE), Aminoacid.LEU);
        put(new Codon(Base.CYTOSINE, Base.URACIL, Base.GUANINE), Aminoacid.LEU);

        // Isoleucine
        put(new Codon(Base.ADENINE, Base.URACIL, Base.URACIL), Aminoacid.ISO);
        put(new Codon(Base.ADENINE, Base.URACIL, Base.CYTOSINE), Aminoacid.ISO);
        put(new Codon(Base.ADENINE, Base.URACIL, Base.ADENINE), Aminoacid.ISO);

        // Methionine (Start codon)
        put(new Codon(Base.ADENINE, Base.URACIL, Base.GUANINE), Aminoacid.MET);

        // Valine
        put(new Codon(Base.GUANINE, Base.URACIL, Base.URACIL), Aminoacid.VAL);
        put(new Codon(Base.GUANINE, Base.URACIL, Base.CYTOSINE), Aminoacid.VAL);
        put(new Codon(Base.GUANINE, Base.URACIL, Base.ADENINE), Aminoacid.VAL);
        put(new Codon(Base.GUANINE, Base.URACIL, Base.GUANINE), Aminoacid.VAL);

        // Serine
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.URACIL), Aminoacid.SER);
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.CYTOSINE), Aminoacid.SER);
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.ADENINE), Aminoacid.SER);
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.GUANINE), Aminoacid.SER);
        put(new Codon(Base.ADENINE, Base.GUANINE, Base.URACIL), Aminoacid.SER);
        put(new Codon(Base.ADENINE, Base.GUANINE, Base.CYTOSINE), Aminoacid.SER);

        // Proline
        put(new Codon(Base.CYTOSINE, Base.CYTOSINE, Base.URACIL), Aminoacid.PRO);
        put(new Codon(Base.CYTOSINE, Base.CYTOSINE, Base.CYTOSINE), Aminoacid.PRO);
        put(new Codon(Base.CYTOSINE, Base.CYTOSINE, Base.ADENINE), Aminoacid.PRO);
        put(new Codon(Base.CYTOSINE, Base.CYTOSINE, Base.GUANINE), Aminoacid.PRO);

        // Threonine
        put(new Codon(Base.ADENINE, Base.CYTOSINE, Base.URACIL), Aminoacid.THR);
        put(new Codon(Base.ADENINE, Base.CYTOSINE, Base.CYTOSINE), Aminoacid.THR);
        put(new Codon(Base.ADENINE, Base.CYTOSINE, Base.ADENINE), Aminoacid.THR);
        put(new Codon(Base.ADENINE, Base.CYTOSINE, Base.GUANINE), Aminoacid.THR);

        // Alanine
        put(new Codon(Base.GUANINE, Base.CYTOSINE, Base.URACIL), Aminoacid.ALA);
        put(new Codon(Base.GUANINE, Base.CYTOSINE, Base.CYTOSINE), Aminoacid.ALA);
        put(new Codon(Base.GUANINE, Base.CYTOSINE, Base.ADENINE), Aminoacid.ALA);
        put(new Codon(Base.GUANINE, Base.CYTOSINE, Base.GUANINE), Aminoacid.ALA);

        // Tyrosine
        put(new Codon(Base.URACIL, Base.ADENINE, Base.URACIL), Aminoacid.TYR);
        put(new Codon(Base.URACIL, Base.ADENINE, Base.CYTOSINE), Aminoacid.TYR);

        // Histidine
        put(new Codon(Base.CYTOSINE, Base.ADENINE, Base.URACIL), Aminoacid.HIS);
        put(new Codon(Base.CYTOSINE, Base.ADENINE, Base.CYTOSINE), Aminoacid.HIS);

        // Glutamine
        put(new Codon(Base.CYTOSINE, Base.ADENINE, Base.ADENINE), Aminoacid.GLN);
        put(new Codon(Base.CYTOSINE, Base.ADENINE, Base.GUANINE), Aminoacid.GLN);

        // Asparagine
        put(new Codon(Base.ADENINE, Base.URACIL, Base.URACIL), Aminoacid.ASN);
        put(new Codon(Base.ADENINE, Base.URACIL, Base.CYTOSINE), Aminoacid.ASN);

        // Lysine
        put(new Codon(Base.ADENINE, Base.ADENINE, Base.URACIL), Aminoacid.LYS);
        put(new Codon(Base.ADENINE, Base.ADENINE, Base.CYTOSINE), Aminoacid.LYS);

        // Aspartic Acid
        put(new Codon(Base.GUANINE, Base.ADENINE, Base.URACIL), Aminoacid.ASP);
        put(new Codon(Base.GUANINE, Base.ADENINE, Base.CYTOSINE), Aminoacid.ASP);

        // Glutamic Acid
        put(new Codon(Base.GUANINE, Base.ADENINE, Base.ADENINE), Aminoacid.GLU);
        put(new Codon(Base.GUANINE, Base.ADENINE, Base.GUANINE), Aminoacid.GLU);

        // Cysteine
        put(new Codon(Base.URACIL, Base.GUANINE, Base.URACIL), Aminoacid.CYS);
        put(new Codon(Base.URACIL, Base.GUANINE, Base.CYTOSINE), Aminoacid.CYS);

        // Tryptophan
        put(new Codon(Base.URACIL, Base.GUANINE, Base.GUANINE), Aminoacid.TRP);

        // Arginine
        put(new Codon(Base.CYTOSINE, Base.GUANINE, Base.URACIL), Aminoacid.ARG);
        put(new Codon(Base.CYTOSINE, Base.GUANINE, Base.CYTOSINE), Aminoacid.ARG);
        put(new Codon(Base.CYTOSINE, Base.GUANINE, Base.ADENINE), Aminoacid.ARG);
        put(new Codon(Base.CYTOSINE, Base.GUANINE, Base.GUANINE), Aminoacid.ARG);
        put(new Codon(Base.ADENINE, Base.GUANINE, Base.ADENINE), Aminoacid.ARG);
        put(new Codon(Base.ADENINE, Base.GUANINE, Base.GUANINE), Aminoacid.ARG);

        // Serine
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.URACIL), Aminoacid.SER);
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.CYTOSINE), Aminoacid.SER);
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.ADENINE), Aminoacid.SER);
        put(new Codon(Base.URACIL, Base.CYTOSINE, Base.GUANINE), Aminoacid.SER);

        // Glycine
        put(new Codon(Base.GUANINE, Base.GUANINE, Base.URACIL), Aminoacid.GLY);
        put(new Codon(Base.GUANINE, Base.GUANINE, Base.CYTOSINE), Aminoacid.GLY);
        put(new Codon(Base.GUANINE, Base.GUANINE, Base.ADENINE), Aminoacid.GLY);
        put(new Codon(Base.GUANINE, Base.GUANINE, Base.GUANINE), Aminoacid.GLY);
    }};

    public Peptide translate(MessengerRNA mRNA) {
        List<Aminoacid> chain = new ArrayList<>();
        for (Codon anticodon : mRNA.getAnticodons()) {
            Aminoacid aminoacid = TRANSLATION_MAP.get(anticodon);

            if (aminoacid != null) {  // only add if there's a valid mapping
                chain.add(aminoacid);
            }
        }
        return new Peptide(chain);
    }

}
