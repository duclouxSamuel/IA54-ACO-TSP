package fr.utbm.ia54.acotsp;

import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class ACOParameters {
  @Accessors
  private Integer numberOfCities;
  
  @Accessors
  private Integer numberOfClusters;
  
  @Accessors
  private ArrayList<ArrayList<Float>> distances;
  
  @Accessors
  private ArrayList<ArrayList<Float>> positions;
  
  @Accessors
  private ArrayList<Integer> attachedCluster;
  
  @Accessors
  private Float pheromoneEvaporationFactor;
  
  @Accessors
  private Float pheromoneRegulationFactor;
  
  @Accessors
  private Float visibilityRegulationFactor;
  
  @Accessors
  private Integer numberOfAnts;
  
  @Accessors
  private Integer numberOfIterations;
  
  public ACOParameters(final Integer inumberOfCities, final Integer inumberOfClusters, final ArrayList<ArrayList<Float>> idistances, final ArrayList<ArrayList<Float>> ipositions, final ArrayList<Integer> iattachedCluster, final Float ipheromoneEvaporationFactor, final Float ipheromoneRegulationFactor, final Float ivisibilityRegulationFactor, final Integer inumberOfAnts, final Integer inumberOfIterations) {
    this.numberOfCities = inumberOfCities;
    this.numberOfClusters = inumberOfClusters;
    this.distances = idistances;
    this.positions = ipositions;
    this.attachedCluster = iattachedCluster;
    this.pheromoneEvaporationFactor = ipheromoneEvaporationFactor;
    this.pheromoneRegulationFactor = ipheromoneRegulationFactor;
    this.visibilityRegulationFactor = ivisibilityRegulationFactor;
    this.numberOfAnts = inumberOfAnts;
    this.numberOfIterations = inumberOfIterations;
  }
  
  public ACOParameters(final ACOParameters acoParameters) {
    this.numberOfCities = acoParameters.numberOfCities;
    this.numberOfClusters = acoParameters.numberOfClusters;
    this.distances = acoParameters.distances;
    this.positions = acoParameters.positions;
    this.attachedCluster = acoParameters.attachedCluster;
    this.pheromoneEvaporationFactor = acoParameters.pheromoneEvaporationFactor;
    this.pheromoneRegulationFactor = acoParameters.pheromoneRegulationFactor;
    this.visibilityRegulationFactor = acoParameters.visibilityRegulationFactor;
    this.numberOfAnts = acoParameters.numberOfAnts;
    this.numberOfIterations = acoParameters.numberOfIterations;
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
    ACOParameters other = (ACOParameters) obj;
    if (other.numberOfCities == null) {
      if (this.numberOfCities != null)
        return false;
    } else if (this.numberOfCities == null)
      return false;
    if (other.numberOfCities != null && other.numberOfCities.intValue() != this.numberOfCities.intValue())
      return false;
    if (other.numberOfClusters == null) {
      if (this.numberOfClusters != null)
        return false;
    } else if (this.numberOfClusters == null)
      return false;
    if (other.numberOfClusters != null && other.numberOfClusters.intValue() != this.numberOfClusters.intValue())
      return false;
    if (other.pheromoneEvaporationFactor == null) {
      if (this.pheromoneEvaporationFactor != null)
        return false;
    } else if (this.pheromoneEvaporationFactor == null)
      return false;if (other.pheromoneEvaporationFactor != null && Float.floatToIntBits(other.pheromoneEvaporationFactor.floatValue()) != Float.floatToIntBits(this.pheromoneEvaporationFactor.floatValue()))
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
    if (other.numberOfAnts == null) {
      if (this.numberOfAnts != null)
        return false;
    } else if (this.numberOfAnts == null)
      return false;
    if (other.numberOfAnts != null && other.numberOfAnts.intValue() != this.numberOfAnts.intValue())
      return false;
    if (other.numberOfIterations == null) {
      if (this.numberOfIterations != null)
        return false;
    } else if (this.numberOfIterations == null)
      return false;
    if (other.numberOfIterations != null && other.numberOfIterations.intValue() != this.numberOfIterations.intValue())
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
    result = prime * result + Objects.hashCode(this.numberOfClusters);
    result = prime * result + Objects.hashCode(this.pheromoneEvaporationFactor);
    result = prime * result + Objects.hashCode(this.pheromoneRegulationFactor);
    result = prime * result + Objects.hashCode(this.visibilityRegulationFactor);
    result = prime * result + Objects.hashCode(this.numberOfAnts);
    result = prime * result + Objects.hashCode(this.numberOfIterations);
    return result;
  }
  
  @Pure
  public Integer getNumberOfCities() {
    return this.numberOfCities;
  }
  
  public void setNumberOfCities(final Integer numberOfCities) {
    this.numberOfCities = numberOfCities;
  }
  
  @Pure
  public Integer getNumberOfClusters() {
    return this.numberOfClusters;
  }
  
  public void setNumberOfClusters(final Integer numberOfClusters) {
    this.numberOfClusters = numberOfClusters;
  }
  
  @Pure
  public ArrayList<ArrayList<Float>> getDistances() {
    return this.distances;
  }
  
  public void setDistances(final ArrayList<ArrayList<Float>> distances) {
    this.distances = distances;
  }
  
  @Pure
  public ArrayList<ArrayList<Float>> getPositions() {
    return this.positions;
  }
  
  public void setPositions(final ArrayList<ArrayList<Float>> positions) {
    this.positions = positions;
  }
  
  @Pure
  public ArrayList<Integer> getAttachedCluster() {
    return this.attachedCluster;
  }
  
  public void setAttachedCluster(final ArrayList<Integer> attachedCluster) {
    this.attachedCluster = attachedCluster;
  }
  
  @Pure
  public Float getPheromoneEvaporationFactor() {
    return this.pheromoneEvaporationFactor;
  }
  
  public void setPheromoneEvaporationFactor(final Float pheromoneEvaporationFactor) {
    this.pheromoneEvaporationFactor = pheromoneEvaporationFactor;
  }
  
  @Pure
  public Float getPheromoneRegulationFactor() {
    return this.pheromoneRegulationFactor;
  }
  
  public void setPheromoneRegulationFactor(final Float pheromoneRegulationFactor) {
    this.pheromoneRegulationFactor = pheromoneRegulationFactor;
  }
  
  @Pure
  public Float getVisibilityRegulationFactor() {
    return this.visibilityRegulationFactor;
  }
  
  public void setVisibilityRegulationFactor(final Float visibilityRegulationFactor) {
    this.visibilityRegulationFactor = visibilityRegulationFactor;
  }
  
  @Pure
  public Integer getNumberOfAnts() {
    return this.numberOfAnts;
  }
  
  public void setNumberOfAnts(final Integer numberOfAnts) {
    this.numberOfAnts = numberOfAnts;
  }
  
  @Pure
  public Integer getNumberOfIterations() {
    return this.numberOfIterations;
  }
  
  public void setNumberOfIterations(final Integer numberOfIterations) {
    this.numberOfIterations = numberOfIterations;
  }
}
