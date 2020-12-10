package fr.utbm.ia54.acotsp;

import com.google.common.base.Objects;
import fr.utbm.ia54.acotsp.ACOParameters;
import fr.utbm.ia54.acotsp.IterationFinished;
import fr.utbm.ia54.acotsp.NewIteration;
import fr.utbm.ia54.acotsp.OptimizationFinished;
import fr.utbm.ia54.acotsp.ProbabilitiesComputation;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.core.Destroy;
import io.sarl.core.Initialize;
import io.sarl.core.Lifecycle;
import io.sarl.core.Logging;
import io.sarl.lang.annotation.ImportedCapacityFeature;
import io.sarl.lang.annotation.PerceptGuardEvaluator;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.BuiltinCapacitiesProvider;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Scope;
import io.sarl.lang.util.SerializableProxy;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.inject.Inject;
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
  
  private Integer startingCity;
  
  private ACOParameters acoParameters;
  
  private Double[][] pheromones;
  
  private Integer currentCity;
  
  private Integer currentPathLength;
  
  private ArrayList<Integer> visitedCities;
  
  private ArrayList<Integer> visitedClusters;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    throw new Error("Unresolved compilation problems:"
      + "\nBounds mismatch: The type argument <ProbabilitiesComputationWithGroupInfluenceSkill> is not a valid substitute for the bounded type parameter <S extends Skill> of the method setSkill(S, Class<? extends Capacity>[])"
      + "\nType mismatch: cannot convert from Class<ProbabilitiesComputation> to Class<? extends Capacity>[]");
  }
  
  private void $behaviorUnit$NewIteration$1(final NewIteration occurrence) {
    this.pheromones = occurrence.pheromones;
    this.buildPath();
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    IterationFinished _iterationFinished = new IterationFinished(this.visitedCities, this.currentPathLength);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_environment;
      
      public $SerializableClosureProxy(final UUID $_environment) {
        this.$_environment = $_environment;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _uUID = it.getUUID();
        return Objects.equal(_uUID, $_environment);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _uUID = it.getUUID();
        return Objects.equal(_uUID, ACOAgent.this.environment);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, ACOAgent.this.environment);
      }
    };
    _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_iterationFinished, _function);
  }
  
  private void $behaviorUnit$OptimizationFinished$2(final OptimizationFinished occurrence) {
    Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.killMe();
  }
  
  protected void buildPath() {
    ArrayList<Integer> _arrayList = new ArrayList<Integer>();
    this.visitedCities = _arrayList;
    ArrayList<Integer> _arrayList_1 = new ArrayList<Integer>();
    this.visitedClusters = _arrayList_1;
    this.currentPathLength = Integer.valueOf(0);
    this.currentCity = this.startingCity;
    this.visitedCities.add(this.startingCity);
    this.visitedClusters.add(this.acoParameters.getAttachedCluster()[((this.startingCity) == null ? 0 : (this.startingCity).intValue())]);
    while ((this.visitedClusters.size() < this.acoParameters.getNumberOfClusters().doubleValue())) {
      {
        ArrayList<Float> probabilities = new ArrayList<Float>();
        ProbabilitiesComputation _$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER = this.$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER();
        probabilities = _$CAPACITY_USE$FR_UTBM_IA54_ACOTSP_PROBABILITIESCOMPUTATION$CALLER.probabilitiesComputation(this.currentCity, probabilities, this.visitedCities, this.visitedClusters, 
          this.pheromones);
        int nextVisitedCity = probabilities.indexOf(IterableExtensions.<Float>max(probabilities));
        Integer _get = this.acoParameters.getDistances()[((this.currentCity) == null ? 0 : (this.currentCity).intValue())][nextVisitedCity];
        this.currentPathLength = Integer.valueOf((((this.currentPathLength) == null ? 0 : (this.currentPathLength).intValue()) + ((_get) == null ? 0 : (_get).intValue())));
        this.currentCity = Integer.valueOf(nextVisitedCity);
        this.visitedCities.add(Integer.valueOf(nextVisitedCity));
        this.visitedClusters.add(this.acoParameters.getAttachedCluster()[nextVisitedCity]);
      }
    }
    Integer _get = this.acoParameters.getDistances()[((this.currentCity) == null ? 0 : (this.currentCity).intValue())][((this.startingCity) == null ? 0 : (this.startingCity).intValue())];
    this.currentPathLength = Integer.valueOf((((this.currentPathLength) == null ? 0 : (this.currentPathLength).intValue()) + ((_get) == null ? 0 : (_get).intValue())));
  }
  
  private void $behaviorUnit$Destroy$3(final Destroy occurrence) {
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
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$3(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$OptimizationFinished(final OptimizationFinished occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$OptimizationFinished$2(occurrence));
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
    if (!java.util.Objects.equals(this.environment, other.environment))
      return false;
    if (other.startingCity == null) {
      if (this.startingCity != null)
        return false;
    } else if (this.startingCity == null)
      return false;
    if (other.startingCity != null && other.startingCity.intValue() != this.startingCity.intValue())
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
    result = prime * result + java.util.Objects.hashCode(this.environment);
    result = prime * result + java.util.Objects.hashCode(this.startingCity);
    result = prime * result + java.util.Objects.hashCode(this.currentCity);
    result = prime * result + java.util.Objects.hashCode(this.currentPathLength);
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
