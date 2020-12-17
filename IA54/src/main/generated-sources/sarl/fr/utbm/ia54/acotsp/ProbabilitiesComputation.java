package fr.utbm.ia54.acotsp;

import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.core.AgentTrait;
import io.sarl.lang.core.Capacity;
import java.util.ArrayList;

/**
 * @author yaben
 */
@FunctionalInterface
@SarlSpecification("0.11")
@SarlElementType(20)
@SuppressWarnings("all")
public interface ProbabilitiesComputation extends Capacity {
  public abstract ArrayList<Double> probabilitiesComputation(final Integer currentCity, final ArrayList<Double> probabilities, final ArrayList<Integer> visitedCities, final ArrayList<Integer> visitedClusterss, final ArrayList<ArrayList<Float>> pheromones);
  
  /**
   * @ExcludeFromApidoc
   */
  public static class ContextAwareCapacityWrapper<C extends ProbabilitiesComputation> extends Capacity.ContextAwareCapacityWrapper<C> implements ProbabilitiesComputation {
    public ContextAwareCapacityWrapper(final C capacity, final AgentTrait caller) {
      super(capacity, caller);
    }
    
    public ArrayList<Double> probabilitiesComputation(final Integer currentCity, final ArrayList<Double> probabilities, final ArrayList<Integer> visitedCities, final ArrayList<Integer> visitedClusterss, final ArrayList<ArrayList<Float>> pheromones) {
      try {
        ensureCallerInLocalThread();
        return this.capacity.probabilitiesComputation(currentCity, probabilities, visitedCities, visitedClusterss, pheromones);
      } finally {
        resetCallerInLocalThread();
      }
    }
  }
}
