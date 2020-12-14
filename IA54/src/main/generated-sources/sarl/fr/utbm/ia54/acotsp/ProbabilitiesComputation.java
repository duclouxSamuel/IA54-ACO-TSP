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
  public abstract ArrayList<Float> probabilitiesComputation(final Integer currentCity, final ArrayList<Float> probabilities, final ArrayList<Integer> visitedCities, final ArrayList<Integer> visitedClusterss, final ArrayList<ArrayList<Double>> pheromones);
  
  /**
   * @ExcludeFromApidoc
   */
  public static class ContextAwareCapacityWrapper<C extends ProbabilitiesComputation> extends Capacity.ContextAwareCapacityWrapper<C> implements ProbabilitiesComputation {
    public ContextAwareCapacityWrapper(final C capacity, final AgentTrait caller) {
      super(capacity, caller);
    }
    
    public ArrayList<Float> probabilitiesComputation(final Integer currentCity, final ArrayList<Float> probabilities, final ArrayList<Integer> visitedCities, final ArrayList<Integer> visitedClusterss, final ArrayList<ArrayList<Double>> pheromones) {
      try {
        ensureCallerInLocalThread();
        return this.capacity.probabilitiesComputation(currentCity, probabilities, visitedCities, visitedClusterss, pheromones);
      } finally {
        resetCallerInLocalThread();
      }
    }
  }
}
