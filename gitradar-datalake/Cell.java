package Mitosis;

import java.util.List;

public class Cell {
    List<KaryotypeElement> karyotpye;
    private final DNAPolymerase dnaPolymerase;
    private final DNALigase dnaLigase;

    public Cell(List<KaryotypeElement> karyotype, DNAPolymerase dnaPolymerase, DNALigase dnaLigase){
        this.karyotpye = karyotype;
        this.dnaPolymerase = dnaPolymerase;
        this.dnaLigase = dnaLigase;
    }

    public List<KaryotypeElement> getKaryotype() {
        return this.karyotpye;
    }

    public DNAPolymerase getDnaPolymerase() {
        return dnaPolymerase;
    }

    public DNALigase getDnaLigase() {
        return dnaLigase;
    }

}
