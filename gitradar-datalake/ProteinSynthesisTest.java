import Biomolecules.DNAStrand;
import Deserializers.TxtDNADeserializer;
import ProteinSynthesis.Exceptions.GeneSequenceNotFoundException;
import ProteinSynthesis.*;
import Serializers.StringDNASerializer;
import Serializers.StringPeptideSerializer;
import Serializers.StringRNASerializer;
import org.junit.Test;

public class ProteinSynthesisTest {

    @Test
    public void name() throws GeneSequenceNotFoundException {

        TxtDNADeserializer dnaDeserializer = new TxtDNADeserializer();
        DNAStrand genome = dnaDeserializer.deserialize("genome.txt");
        DNAStrand gene = dnaDeserializer.deserialize("gene.txt");
        GeneLocator locator = new GeneLocator(genome);
        DNAStrand dnaStrand = locator.locate(gene);
        StringDNASerializer dnaSerializer = new StringDNASerializer();

        RNAPolymerase polymerase = new RNAPolymerase();
        MessengerRNA messengerRnaStrand = polymerase.transcription(dnaStrand);
        StringRNASerializer rnaSerializer = new StringRNASerializer();

        Ribosome ribosome = new Ribosome();
        Peptide peptidicChain = ribosome.translate(messengerRnaStrand);
        StringPeptideSerializer peptideSerializer = new StringPeptideSerializer();

        System.out.println("DNA Strand: " + dnaSerializer.serialize(dnaStrand));
        System.out.println("Genome: " + dnaSerializer.serialize(genome));
        System.out.println("Messenger RNA Strand: " + rnaSerializer.serialize(messengerRnaStrand));
        System.out.println("Peptidic Chain: " + peptideSerializer.serialize(peptidicChain));
    }
}