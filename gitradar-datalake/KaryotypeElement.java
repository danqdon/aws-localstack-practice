package Mitosis;

import java.util.ArrayList;
import java.util.List;

public class KaryotypeElement {
    private final List<Chromosome> element = new ArrayList<>();
    private final int id;
    public KaryotypeElement(Chromosome chromosome, int id) {
        this.element.add(chromosome);
        this.id = id;
    }

    public KaryotypeElement(Chromosome chromosome, Chromosome chromosome2, int id) {
        this.id = id;
        this.element.add(chromosome);
        this.element.add(chromosome2);
    }

    public Chromosome getChromosome(int index) {
        if (index < 0 || index >= element.size()) {
            throw new IndexOutOfBoundsException("Chromatid index " + index + " is out of bounds for chromosome " + this.id + " with " + element.size() + " chromatids.");
        }
        return this.element.get(index);
    }

    public List<Chromosome> getElement() {
        return element;
    }
}
