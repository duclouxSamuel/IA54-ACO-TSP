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
public class ProbabilitiesComputationWithGroupInfluenceSkill extends Skill implements ProbabilitiesComputation {
  private Integer numberOfCities;
  
  private Integer[][] distances;
  
  private Integer[] attachedCluster;
  
  private Float pheromoneRegulationFactor;
  
  private Float visibilityRegulationFactor;
  
  public ProbabilitiesComputationWithGroupInfluenceSkill(final Integer inumberOfCities, final Integer[][] idistances, final Integer[] iattachedCluster, final Float ipheromoneRegulationFactor, final Float ivisibilityRegulationFactor) {
    this.numberOfCities = inumberOfCities;
    this.distances = idistances;
    this.attachedCluster = iattachedCluster;
    this.pheromoneRegulationFactor = ipheromoneRegulationFactor;
    this.visibilityRegulationFactor = ivisibilityRegulationFactor;
  }
  
  public ArrayList<Float> probabilitiesComputation(final Integer currentCity, final ArrayList<Float> probabilities, final ArrayList<Integer> visitedCities, final ArrayList<Integer> visitedClusters, final Double[][] pheromones) {
    double sumOfAllowedCities = 0d;
    double sumOfAllowedCitiesWithGroupInfluence = 0d;
    ArrayList<Double> factorOfAllowedCitiesGroup = new ArrayList<Double>();
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      boolean _contains = visitedClusters.contains(this.attachedCluster[i]);
      if ((!_contains)) {
        Double _get = pheromones[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow = Math.pow(((_get) == null ? 0 : (double) _get) , ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
        Integer _get_1 = this.distances[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (int) _get_1) ), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
        sumOfAllowedCities = (sumOfAllowedCities + 
          (_pow * _pow_1));
      }
    }
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      {
        double sumOfClusterCities = 0d;
        boolean _contains = visitedClusters.contains(this.attachedCluster[i]);
        if ((!_contains)) {
          Double _get = pheromones[((currentCity) == null ? 0 : (currentCity).intValue())][i];
          double _pow = Math.pow(((_get) == null ? 0 : (double) _get) , ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
          Integer _get_1 = this.distances[((currentCity) == null ? 0 : (currentCity).intValue())][i];
          double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (int) _get_1) ), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
          sumOfClusterCities = (sumOfClusterCities + 
            (_pow * _pow_1));
        }
        factorOfAllowedCitiesGroup.add(Double.valueOf((sumOfClusterCities / sumOfAllowedCities)));
      }
    }
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      boolean _contains = visitedClusters.contains(this.attachedCluster[i]);
      if ((!_contains)) {
        Double _get = pheromones[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow = Math.pow(((_get) == null ? 0 : (double) _get) , ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
        Integer _get_1 = this.distances[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (int) _get_1) ), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
        double _multiply = (_pow * _pow_1);
        Double _get_2 = factorOfAllowedCitiesGroup.get(i);
        sumOfAllowedCitiesWithGroupInfluence = (sumOfAllowedCitiesWithGroupInfluence + 
          (_multiply * ((_get_2) == null ? 0 : (_get_2).doubleValue())));
      }
    }
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      {
        Double _get = pheromones[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow = Math.pow(((_get) == null ? 0 : (double) _get) , ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
        Integer _get_1 = this.distances[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (int) _get_1) ), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
        double _multiply = (_pow * _pow_1);
        Double _get_2 = factorOfAllowedCitiesGroup.get(i);
        double probability = ((_multiply * ((_get_2) == null ? 0 : (_get_2).doubleValue())) / sumOfAllowedCitiesWithGroupInfluence);
        probabilities.add(Float.valueOf((float) probability));
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
    ProbabilitiesComputationWithGroupInfluenceSkill other = (ProbabilitiesComputationWithGroupInfluenceSkill) obj;
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
