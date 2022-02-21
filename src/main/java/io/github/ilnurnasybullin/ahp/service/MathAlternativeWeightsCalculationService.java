package io.github.ilnurnasybullin.ahp.service;

public interface MathAlternativeWeightsCalculationService {
    double[][] weights(double[][] criteria, double[][] ... alternatives);
}
