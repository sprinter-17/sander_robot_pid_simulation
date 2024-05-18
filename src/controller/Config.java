package controller;

import java.util.OptionalDouble;

public record Config(double proportional, double derivative, double integral, OptionalDouble integralCap) {
}
