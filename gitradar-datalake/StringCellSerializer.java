package Serializers;

import Biomolecules.Base;
import Biomolecules.Codon;
import Mitosis.Cell;
import Mitosis.Chromatid;
import Mitosis.Chromosome;
import Mitosis.KaryotypeElement;

import java.util.List;

public class StringCellSerializer {
    public String serialize(Cell cell) {
        StringBuilder genome = new StringBuilder();

        for (KaryotypeElement element : cell.getKaryotype()) {
            Chromosome chromosome = element.getChromosome(0);
            Chromatid chromatid = chromosome.getChromatid(0);
            List<Codon> strand = chromatid.getDna().getCodons();
            for (Codon codon : strand) {
                genome.append(baseToChar(codon.getFirstBase()));
                genome.append(baseToChar(codon.getSecondBase()));
                genome.append(baseToChar(codon.getThirdBase()));
            }
        }

        return genome.toString();
    }


    private char baseToChar(Base base) {
        switch (base) {
            case ADENINE: return 'A';
            case THYMINE: return 'T';
            case CYTOSINE: return 'C';
            case GUANINE: return 'G';
            case URACIL: return 'U';  // In case you decide to include RNA bases later
            default: throw new IllegalArgumentException("Unknown base: " + base);
        }
    }
}
