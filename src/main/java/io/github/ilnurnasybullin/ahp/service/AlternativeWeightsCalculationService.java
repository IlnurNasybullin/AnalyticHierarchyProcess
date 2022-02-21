package io.github.ilnurnasybullin.ahp.service;

import io.github.ilnurnasybullin.ahp.domain.NamedMatrix;
import io.github.ilnurnasybullin.ahp.domain.QualitativeComparison;

public interface AlternativeWeightsCalculationService {
    NamedMatrix<Double> weights(NamedMatrix<QualitativeComparison> criteria, NamedMatrix<QualitativeComparison>... alternatives);
}
