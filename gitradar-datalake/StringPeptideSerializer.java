package Serializers;

import ProteinSynthesis.Aminoacid;
import ProteinSynthesis.Peptide;
import ProteinSynthesis.PeptideSerializer;

public class StringPeptideSerializer implements PeptideSerializer {
    @Override
    public String serialize(Peptide peptide) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < peptide.getChain().size(); i++) {
            Aminoacid aminoAcid = peptide.getChain().get(i);
            if (aminoAcid == null) {
                throw new IllegalArgumentException("Amino acid at position " + i + " is null.");
            }
            sb.append(aminoAcid.name());
            if (i < peptide.getChain().size() - 1) {
                sb.append('-');
            }
        }

        return sb.toString();
    }
}
