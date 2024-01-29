package Mitosis;

import java.util.ArrayList;
import java.util.List;

public class Microtubule {

    public Microtubule() {
    }

    public List<Cell> divideCell(Cell cell){
        List<Cell> daughterCells = new ArrayList<>();
        List<KaryotypeElement> karyotypeCell1 = new ArrayList<>();
        List<KaryotypeElement> karyotypeCell2 = new ArrayList<>();
        for(KaryotypeElement element: cell.getKaryotype()){
            karyotypeCell1.add(new KaryotypeElement(element.getChromosome(0),element.getChromosome(0).getId()));
            karyotypeCell2.add(new KaryotypeElement(element.getChromosome(1),element.getChromosome(1).getId()));
        }
        Cell daughter1 = new Cell(karyotypeCell1,cell.getDnaPolymerase(),cell.getDnaLigase());
        daughterCells.add(daughter1);
        Cell daughter2 = new Cell(karyotypeCell2,cell.getDnaPolymerase(),cell.getDnaLigase());
        daughterCells.add(daughter2);
        return daughterCells;
    }
}
