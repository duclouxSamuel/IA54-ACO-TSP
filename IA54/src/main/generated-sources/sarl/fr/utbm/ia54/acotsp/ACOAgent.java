package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.NewIteration;
import io.sarl.core.Destroy;
import io.sarl.core.Initialize;
import io.sarl.core.Logging;
import io.sarl.lang.annotation.ImportedCapacityFeature;
import io.sarl.lang.annotation.PerceptGuardEvaluator;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.BuiltinCapacitiesProvider;
import io.sarl.lang.core.DynamicSkillProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Omen
 */
@SuppressWarnings("potential_field_synchronization_problem")
@SarlSpecification("0.11")
@SarlElementType(19)
public class ACOAgent extends Agent {
  private UUID environment;
  
  private Integer numberOfCities;
  
  private Integer startingCity;
  
  private Integer[][] distances;
  
  private Integer[] attachedCluster;
  
  private Double[][] pheromones;
  
  private Float pheromoneRegulationFactor;
  
  private Float visibilityRegulationFactor;
  
  private Integer currentCity;
  
  private Integer currentPathLength;
  
  private ArrayList<Integer> visitedCities;
  
  private Integer[] visitedClusters;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int _size = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).size();
    if ((_size > 7)) {
      Object _get = occurrence.parameters[0];
      if ((_get instanceof UUID)) {
        Object _get_1 = occurrence.parameters[0];
        this.environment = ((UUID) _get_1);
      }
      Object _get_2 = occurrence.parameters[1];
      if ((_get_2 instanceof Integer)) {
        Object _get_3 = occurrence.parameters[1];
        this.startingCity = ((Integer) _get_3);
      }
      Object _get_4 = occurrence.parameters[2];
      if ((_get_4 instanceof Integer[][])) {
        Object _get_5 = occurrence.parameters[2];
        this.distances = ((Integer[][]) _get_5);
      }
      Object _get_6 = occurrence.parameters[3];
      if ((_get_6 instanceof Float)) {
        Object _get_7 = occurrence.parameters[3];
        this.pheromoneRegulationFactor = ((Float) _get_7);
      }
      Object _get_8 = occurrence.parameters[4];
      if ((_get_8 instanceof Float)) {
        Object _get_9 = occurrence.parameters[4];
        this.visibilityRegulationFactor = ((Float) _get_9);
      }
      Object _get_10 = occurrence.parameters[5];
      if ((_get_10 instanceof Integer)) {
        Object _get_11 = occurrence.parameters[5];
        this.numberOfCities = ((Integer) _get_11);
      }
      Object _get_12 = occurrence.parameters[6];
      if ((_get_12 instanceof Integer[])) {
        Object _get_13 = occurrence.parameters[6];
        this.attachedCluster = ((Integer[]) _get_13);
      }
      Object _get_14 = occurrence.parameters[7];
      if ((_get_14 instanceof String)) {
        Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
        Object _get_15 = occurrence.parameters[7];
        _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.setLoggingName((_get_15 == null ? null : _get_15.toString()));
      }
    }
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info("The agent was started.");
  }
  
  private void $behaviorUnit$NewIteration$1(final NewIteration occurrence) {
    this.pheromones = occurrence.pheromones;
  }
  
  protected void contstructPath() {
    ArrayList<Integer> _arrayList = new ArrayList<Integer>();
    this.visitedCities = _arrayList;
    this.currentCity = this.startingCity;
    this.visitedCities.add(this.startingCity);
    while ((this.visitedCities.size() < this.numberOfCities.doubleValue())) {
      {
        ArrayList<Float> probabilites = new ArrayList<Float>();
        this.probabilitiesComputation(this.currentCity, probabilites);
      }
    }
  }
  
  protected void probabilitiesComputation(final Integer currentCity, final ArrayList<Float> probabilites) {
    double sum = 0d;
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      boolean _contains = this.visitedCities.contains(Integer.valueOf(i));
      if ((!_contains)) {
        Double _get = this.pheromones[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow = Math.pow(((_get) == null ? 0 : (double) _get) , ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
        Integer _get_1 = this.distances[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (int) _get_1) ), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
        sum = (sum + 
          (_pow * _pow_1));
      }
    }
    for (int i = 0; (i < this.numberOfCities.doubleValue()); i++) {
      {
        Double _get = this.pheromones[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow = Math.pow(((_get) == null ? 0 : (double) _get) , ((this.pheromoneRegulationFactor) == null ? 0 : (this.pheromoneRegulationFactor).floatValue()));
        Integer _get_1 = this.distances[((currentCity) == null ? 0 : (currentCity).intValue())][i];
        double _pow_1 = Math.pow((1 / ((_get_1) == null ? 0 : (int) _get_1) ), ((this.visibilityRegulationFactor) == null ? 0 : (this.visibilityRegulationFactor).floatValue()));
        double probability = ((_pow * _pow_1) / sum);
        probabilites.add(Float.valueOf((float) probability));
      }
    }
  }
  
  private void $behaviorUnit$Destroy$2(final Destroy occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was stopped.");
  }
  
  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_LOGGING;
  
  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$NewIteration(final NewIteration occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$NewIteration$1(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Destroy(final Destroy occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$2(occurrence));
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
    ACOAgent other = (ACOAgent) obj;
    if (!Objects.equals(this.environment, other.environment))
      return false;
    if (other.numberOfCities == null) {
      if (this.numberOfCities != null)
        return false;
    } else if (this.numberOfCities == null)
      return false;
    if (other.numberOfCities != null && other.numberOfCities.intValue() != this.numberOfCities.intValue())
      return false;
    if (other.startingCity == null) {
      if (this.startingCity != null)
        return false;
    } else if (this.startingCity == null)
      return false;
    if (other.startingCity != null && other.startingCity.intValue() != this.startingCity.intValue())
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
    if (other.currentCity == null) {
      if (this.currentCity != null)
        return false;
    } else if (this.currentCity == null)
      return false;
    if (other.currentCity != null && other.currentCity.intValue() != this.currentCity.intValue())
      return false;
    if (other.currentPathLength == null) {
      if (this.currentPathLength != null)
        return false;
    } else if (this.currentPathLength == null)
      return false;
    if (other.currentPathLength != null && other.currentPathLength.intValue() != this.currentPathLength.intValue())
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.environment);
    result = prime * result + Objects.hashCode(this.numberOfCities);
    result = prime * result + Objects.hashCode(this.startingCity);
    result = prime * result + Objects.hashCode(this.pheromoneRegulationFactor);
    result = prime * result + Objects.hashCode(this.visibilityRegulationFactor);
    result = prime * result + Objects.hashCode(this.currentCity);
    result = prime * result + Objects.hashCode(this.currentPathLength);
    return result;
  }
  
  @SyntheticMember
  public ACOAgent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public ACOAgent(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public ACOAgent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
