package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.NewIteration;
import fr.utbm.ia54.acotsp.ProbabilitiesComputation;
import fr.utbm.ia54.acotsp.ProbabilitiesComputationWithGroupInfluenceSkill;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.core.Destroy;
import io.sarl.core.Initialize;
import io.sarl.core.InnerContextAccess;
import io.sarl.core.Lifecycle;
import io.sarl.core.Logging;
import io.sarl.core.Schedules;
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
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
  
  private Integer numberOfClusters;
  
  private Integer startingCity;
  
  private Integer[][] distances;
  
  private Integer[] attachedCluster;
  
  private Double[][] pheromones;
  
  private Float pheromoneRegulationFactor;
  
  private Float visibilityRegulationFactor;
  
  private Integer currentCity;
  
  private Integer currentPathLength;
  
  private ArrayList<Integer> visitedCities;
  
  private ArrayList<Integer> visitedClusters;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    int _size = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).size();
    if ((_size > 8)) {
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
      if ((_get_12 instanceof Integer)) {
        Object _get_13 = occurrence.parameters[6];
        this.numberOfClusters = ((Integer) _get_13);
      }
      Object _get_14 = occurrence.parameters[7];
      if ((_get_14 instanceof Integer[])) {
        Object _get_15 = occurrence.parameters[7];
        this.attachedCluster = ((Integer[]) _get_15);
      }
      Object _get_16 = occurrence.parameters[8];
      if ((_get_16 instanceof String)) {
        Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
        Object _get_17 = occurrence.parameters[8];
        _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.setLoggingName((_get_17 == null ? null : _get_17.toString()));
      }
    }
    ProbabilitiesComputationWithGroupInfluenceSkill _probabilitiesComputationWithGroupInfluenceSkill = new ProbabilitiesComputationWithGroupInfluenceSkill(this.numberOfCities, this.distances, this.attachedCluster, 
      this.pheromoneRegulationFactor, this.visibilityRegulationFactor);
    this.<ProbabilitiesComputationWithGroupInfluenceSkill>setSkill(_probabilitiesComputationWithGroupInfluenceSkill, ProbabilitiesComputation.class);
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
    this.visitedClusters.add(this.attachedCluster[((this.startingCity) == null ? 0 : (this.startingCity).intValue())]);
    while ((this.visitedClusters.size() < this.numberOfClusters.doubleValue())) {
      {
        ArrayList<Float> probabilities = new ArrayList<Float>();
        ProbabilitiesComputation _$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER = this.$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER();
        probabilities = _$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER.probabilitiesComputation(this.currentCity, probabilities, this.visitedCities, this.visitedClusters, 
          this.pheromones);
        int nextVisitedCity = probabilities.indexOf(IterableExtensions.<Float>max(probabilities));
        this.visitedCities.add(Integer.valueOf(nextVisitedCity));
        this.visitedClusters.add(this.attachedCluster[nextVisitedCity]);
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
  
  @Extension
  @ImportedCapacityFeature(InnerContextAccess.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS;
  
  @SyntheticMember
  @Pure
  private InnerContextAccess $CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS == null || this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS = $getSkill(InnerContextAccess.class);
    }
    return $castSkill(InnerContextAccess.class, this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS);
  }
  
  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS;
  
  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS);
  }
  
  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_LIFECYCLE;
  
  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE);
  }
  
  @Extension
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_SCHEDULES;
  
  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES);
  }
  
  @Extension
  @ImportedCapacityFeature(ProbabilitiesComputation.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION;
  
  @SyntheticMember
  @Pure
  private ProbabilitiesComputation $CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER() {
    if (this.$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION == null || this.$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION.get() == null) {
      this.$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION = $getSkill(ProbabilitiesComputation.class);
    }
    return $castSkill(ProbabilitiesComputation.class, this.$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION);
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
    if (other.numberOfClusters == null) {
      if (this.numberOfClusters != null)
        return false;
    } else if (this.numberOfClusters == null)
      return false;
    if (other.numberOfClusters != null && other.numberOfClusters.intValue() != this.numberOfClusters.intValue())
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
    result = prime * result + Objects.hashCode(this.numberOfClusters);
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
