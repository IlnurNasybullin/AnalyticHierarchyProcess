package io.github.ilnurnasybullin.ahp.service;

import io.github.ilnurnasybullin.ahp.domain.NamedMatrix;
import io.github.ilnurnasybullin.ahp.domain.QualitativeComparison;
import org.springframework.stereotype.Service;

@Service
public class AlternativeWeightsCalculationServiceImpl implements AlternativeWeightsCalculationService {

    private final MathAlternativeWeightsCalculationService mathService;

    public AlternativeWeightsCalculationServiceImpl(MathAlternativeWeightsCalculationService mathService) {
        this.mathService = mathService;
    }

    @Override
    public NamedMatrix<Double> weights(NamedMatrix<QualitativeComparison> criteria, NamedMatrix<QualitativeComparison>... alternatives) {
        checkLengths(criteria, alternatives);

        double[][] criteriaValues = toDoubleMatrix(criteria);
        double[][][] alternativeValues = new double[alternatives.length][][];
        for (int i = 0; i < alternatives.length; i++) {
            alternativeValues[i] = toDoubleMatrix(alternatives[i]);
        }

        double[][] weights = mathService.weights(criteriaValues, alternativeValues);
        Double[][] boxedWeights = toBoxedMatrix(weights);

        String[] rows = alternatives[0].rowNames();
        // It's correct and working!
        String[] columns = {"weights"};

        return new NamedMatrix<>(rows, columns, boxedWeights);
    }

    private Double[][] toBoxedMatrix(double[][] matrix) {
        int rows = matrix.length;
        Double[][] boxedMatrix = new Double[rows][];

        for (int i = 0; i < rows; i++) {
            int columns = matrix[i].length;
            boxedMatrix[i] = new Double[columns];

            for (int j = 0; j < columns; j++) {
                boxedMatrix[i][j] = matrix[i][j];
            }
        }

        return boxedMatrix;
    }

    private double[][] toDoubleMatrix(NamedMatrix<QualitativeComparison> criteria) {
        int rows = criteria.rows();
        int columns = criteria.columns();
        double[][] matrix = new double[rows][columns];

        QualitativeComparison[][] qcMatrix = criteria.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = qcMatrix[i][j].quantitativeAnalogue();
            }
        }

        return matrix;
    }

    private void checkLengths(NamedMatrix<QualitativeComparison> criteria, NamedMatrix<QualitativeComparison>[] alternatives) {
        int rows = criteria.rows();
        int columns = criteria.columns();

        for (NamedMatrix<QualitativeComparison> alternative: alternatives) {
            if (alternative.rows() != rows || columns != alternative.columns()) {
                throw new IllegalArgumentException("Illegal matrix dimension!");
            }
        }
    }
}
