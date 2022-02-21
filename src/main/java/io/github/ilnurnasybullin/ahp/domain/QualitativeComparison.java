package io.github.ilnurnasybullin.ahp.domain;

import java.util.EnumMap;
import java.util.Map;

public enum QualitativeComparison {

    EQUAL(1, "равны"),
    EQUAL_OR_A_LITTLE_BETTER(2, "равны или немного лучше"),
    A_LITTLE_BETTER(3, "немного лучше"),
    A_LITTLE_BETTER_OR_BETTER(4, "немного лучше или лучше"),
    BETTER(5, "лучше"),
    BETTER_OR_MUCH_BETTER(6, "лучше или значительно лучше"),
    MUCH_BETTER(7, "значительно лучше"),
    MUCH_BETTER_OR_FUNDAMENTALLY_BETTER(8, "значительно лучше или принципиально лучше"),
    FUNDAMENTALLY_BETTER(9, "принципиально лучше"),

    EQUAL_OR_A_LITTLE_WORSE(1d/2, "равны или немного хуже"),
    A_LITTLE_WORSE(1d/3, "немного хуже"),
    A_LITTLE_WORSE_OR_WORSE(1d/4, "немного хуже или хуже"),
    WORSE(1d/5, "хуже"),
    WORSE_OR_MUCH_WORSE(1d/6, "хуже или значительно хуже"),
    MUCH_WORSE(1d/7, "значительно хуже"),
    MUCH_WORSE_OR_FUNDAMENTALLY_WORSE(1d/8, "значительно хуже или принципиально хуже"),
    FUNDAMENTALLY_WORSE(1d/9, "принципиально хуже");

    private static final Map<QualitativeComparison, QualitativeComparison> negs;

    final double quantitativeAnalogue;
    final String description;

    static {
        negs = new EnumMap<>(QualitativeComparison.class);
        negs.put(EQUAL, EQUAL);
        negs.put(EQUAL_OR_A_LITTLE_BETTER, EQUAL_OR_A_LITTLE_WORSE);
        negs.put(EQUAL_OR_A_LITTLE_WORSE, EQUAL_OR_A_LITTLE_BETTER);
        negs.put(A_LITTLE_BETTER, A_LITTLE_WORSE);
        negs.put(A_LITTLE_WORSE, A_LITTLE_BETTER);
        negs.put(A_LITTLE_WORSE_OR_WORSE, A_LITTLE_BETTER_OR_BETTER);
        negs.put(A_LITTLE_BETTER_OR_BETTER, A_LITTLE_WORSE_OR_WORSE);
        negs.put(BETTER, WORSE);
        negs.put(WORSE, BETTER);
        negs.put(BETTER_OR_MUCH_BETTER, WORSE_OR_MUCH_WORSE);
        negs.put(WORSE_OR_MUCH_WORSE, BETTER_OR_MUCH_BETTER);
        negs.put(MUCH_BETTER, MUCH_WORSE);
        negs.put(MUCH_WORSE, MUCH_BETTER);
        negs.put(MUCH_BETTER_OR_FUNDAMENTALLY_BETTER, MUCH_WORSE_OR_FUNDAMENTALLY_WORSE);
        negs.put(MUCH_WORSE_OR_FUNDAMENTALLY_WORSE, MUCH_BETTER_OR_FUNDAMENTALLY_BETTER);
        negs.put(FUNDAMENTALLY_BETTER, FUNDAMENTALLY_WORSE);
        negs.put(FUNDAMENTALLY_WORSE, FUNDAMENTALLY_WORSE);
    }

    QualitativeComparison(double quantitativeAnalogue, String description) {
        this.quantitativeAnalogue = quantitativeAnalogue;
        this.description = description;
    }

    public double quantitativeAnalogue() {
        return quantitativeAnalogue;
    }

    public QualitativeComparison neg() {
        return negs.get(this);
    }

    public String description() {
        String quantity = quantitativeAnalogue >= 1d ?
                Integer.toString((int) quantitativeAnalogue) :
                String.format("1/%d", (int) neg().quantitativeAnalogue());
        return String.format("%s (%s)", description, quantity);
    }

    public String toString() {
        return description();
    }
}
