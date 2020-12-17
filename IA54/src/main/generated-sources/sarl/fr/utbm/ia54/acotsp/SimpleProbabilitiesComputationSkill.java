package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.ProbabilitiesComputation;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Skill;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("potential_field_synchronization_problem")
@SarlSpecification("0.11")
@SarlElementType(22)
public class SimpleProbabilitiesComputationSkill extends Skill implements ProbabilitiesComputation {
  private Integer numberOfCities;
  
  private ArrayList<ArrayList<Float>> distances;
  
  private ArrayList<Integer> attachedCluster;
  
  private Float pheromoneRegulationFactor;
  
  private Float visibilityRegulationFactor;
  
  public SimpleProbabilitiesComputationSkill(final Integer inumberOfCities, final ArrayList<ArrayList<Float>> idistances, final ArrayList<Integer> iattachedCluster, final Float ipheromoneRegulationFactor, final Float ivisibilityRegulationFactor) {
    this.numberOfCities = inumberOfCities;
    this.distances = idistances;
    this.attachedCluster = iattachedCluster;
    this.pheromoneRegulationFactor = ipheromoneRegulationFactor;
    this.visibilityRegulationFactor = ivisibilityRegulationFactor;
  }
  
  public ArrayList<Double> probabilitiesComputation(final Integer currentCity, final ArrayList<Double> probabilities, final ArrayList<Integer> visitedCities, final ArrayList<Integer> visitedClusters, final ArrayList<ArrayList<Float>> pheromones) {
    double sumOfAllowedCities = 0d;
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      boolean _contains = visitedClusters.contains(this.attachedCluster.get(i));
      if ((!_contains)) {
        Float _get = pheromones.get(((currentCity) == null ? 0 : (currentCity).intValue())).get(i);
        double _pow = Math.pow(((_get) == null ? 0 : (_get).floatValue()), ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
        Float _get_1 = this.distances.get(((currentCity) == null ? 0 : (currentCity).intValue())).get(i);
        double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (_get_1).floatValue())), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
        sumOfAllowedCities = (sumOfAllowedCities + 
          (_pow * _pow_1));
      }
    }
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      {
        double probability = 0d;
        if (((!visitedClusters.contains(this.attachedCluster.get(i))) && (sumOfAllowedCities > 0))) {
          Float _get = pheromones.get(((currentCity) == null ? 0 : (currentCity).intValue())).get(i);
          double _pow = Math.pow(((_get) == null ? 0 : (_get).floatValue()), ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
          Float _get_1 = this.distances.get(((currentCity) == null ? 0 : (currentCity).intValue())).get(i);
          double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (_get_1).floatValue())), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
          probability = ((_pow * _pow_1) / sumOfAllowedCities);
        }
        if ((currentCity != null && (currentCity.intValue() == i))) {
          probabilities.add(Double.valueOf(0d));
        } else {
          probabilities.add(Double.valueOf(probability));
        }
      }
    }
    return probabilities;
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SimpleProbabilitiesComputationSkill other = (SimpleProbabilitiesComputationSkill) obj;
    if (other.numberOfCities == null) {
      if (this.numberOfCities != null)
        return false;
    } else if (this.numberOfCities == null)
      return false;
    if (other.numberOfCities != null && other.numberOfCities.intValue() != this.numberOfCities.intValue())
      return false;
    if (other.pheromoneRegulationFactor == null) {
      if (this.pheromoneRegulationFactor != null)
        return false;
    } else if (this.pheromoneRegulationFactor == null)
      return false;if (other.pheromoneRegulationFactor != null && Float.floatToIntBits(other.pheromoneRegulationFactor.floatValue()) != Float.floatToIntBits(this.pheromoneRegulationFactor.floatValue()))
      return false;
    if (other.visibilityRegulationFactor == null) {
      if (this.visibilityRegulationFactor != null)
        return false;
    } else if (this.visibilityRegulationFactor == null)
      return false;if (other.visibilityRegulationFactor != null && Float.floatToIntBits(other.visibilityRegulationFactor.floatValue()) != Float.floatToIntBits(this.visibilityRegulationFactor.floatValue()))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.numberOfCities);
    result = prime * result + Objects.hashCode(this.pheromoneRegulationFactor);
    result = prime * result + Objects.hashCode(this.visibilityRegulationFactor);
    return result;
  }
}
