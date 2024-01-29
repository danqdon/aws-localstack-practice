import Biomolecules.DNAStrand;
import Deserializers.TxtDNADeserializer;
import Deserializers.TxtStringDeserializer;
import Mitosis.*;
import Serializers.StringCellSerializer;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MitosisTest {

    @Test
    public void should_Create_Two_Identical_Daughter_Cells_From_One() throws IOException {
        TxtDNADeserializer dnaDeserializer = new TxtDNADeserializer();
        DNAStrand dnaStrand = dnaDeserializer.deserialize("genome.txt");
        GenomeOrganizer organizer = new GenomeOrganizer();
        List<Chromatid> chromatidList = organizer.organizeIntoChromatids(dnaStrand);
        DNAPolymerase dnaPolymerase = new DNAPolymerase();
        List<KaryotypeElement> karyotypeElements = new ArrayList<>();
        for (Chromatid chromatid : chromatidList) {
            Chromatid complementaryChromatid = new Chromatid(dnaPolymerase.getComplementary(chromatid).getDna(), chromatid.getId());
            Chromosome chromosome = new Chromosome(chromatid, complementaryChromatid, chromatid.getId());
            karyotypeElements.add(new KaryotypeElement(chromosome,chromosome.getId()));
        }
        DNALigase dnaLigase = new DNALigase(dnaPolymerase);
        Cell cell = new Cell(karyotypeElements, dnaPolymerase, dnaLigase);
        StringCellSerializer genomeSerializer = new StringCellSerializer();
        cell = cell.getDnaLigase().replicate(cell);
        Microtubule microtubule = new Microtubule();
        List<Cell> daughterCells = microtubule.divideCell(cell);
        Cell daughterCell1 = daughterCells.get(0);
        Cell daughterCell2 = daughterCells.get(1);
        TxtStringDeserializer stringDeserializer = new TxtStringDeserializer();
        String expected = stringDeserializer.deserialize("genome.txt");
        assertThat(genomeSerializer.serialize(cell)).isEqualTo(expected);
        assertThat(genomeSerializer.serialize(daughterCell1)).isEqualTo(expected);
        assertThat(genomeSerializer.serialize(daughterCell2)).isEqualTo(expected);
    }


}
