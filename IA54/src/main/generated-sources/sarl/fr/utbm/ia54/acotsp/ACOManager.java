package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.ACOAgent;
import fr.utbm.ia54.acotsp.ACOParameters;
import fr.utbm.ia54.acotsp.IterationFinished;
import fr.utbm.ia54.acotsp.NewIteration;
import fr.utbm.ia54.acotsp.NewOptimization;
import fr.utbm.ia54.acotsp.OptimizationFinished;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.core.Destroy;
import io.sarl.core.Initialize;
import io.sarl.core.InnerContextAccess;
import io.sarl.core.Lifecycle;
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
import java.util.Objects;
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
public class ACOManager extends Agent {
  private UUID myUUID = UUID.randomUUID();
  
  private ACOParameters acoParameters;
  
  private ArrayList<ArrayList<Integer>> paths;
  
  private ArrayList<Float> pathsLength;
  
  private ArrayList<Integer> currentBestPath;
  
  private Float currentBestPathLength;
  
  private ArrayList<ArrayList<Double>> pheromones = new ArrayList<ArrayList<Double>>();
  
  private Integer numberOfIterationsDone;
  
  private ArrayList<UUID> acoAgentsID;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was started.");
  }
  
  private void $behaviorUnit$Destroy$1(final Destroy occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was stopped.");
  }
  
  private void $behaviorUnit$NewOptimization$2(final NewOptimization occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("J\'ai recu l\'ordre de nouvelle optim");
    this.acoParameters = occurrence.acoParameters;
    ArrayList<UUID> _arrayList = new ArrayList<UUID>();
    this.acoAgentsID = _arrayList;
    this.numberOfIterationsDone = Integer.valueOf(0);
    Float _float = new Float(0);
    this.currentBestPathLength = _float;
    ArrayList<ArrayList<Integer>> _arrayList_1 = new ArrayList<ArrayList<Integer>>();
    this.paths = _arrayList_1;
    ArrayList<Float> _arrayList_2 = new ArrayList<Float>();
    this.pathsLength = _arrayList_2;
    this.initializePheromones(Double.valueOf(0d));
    this.launchACOAgents();
  }
  
  private void $behaviorUnit$IterationFinished$3(final IterationFinished occurrence) {
    this.paths.add(occurrence.path);
    this.pathsLength.add(occurrence.pathLength);
    int _size = this.paths.size();
    Integer _nunberOfAnts = this.acoParameters.getNunberOfAnts();
    if ((_size == ((_nunberOfAnts) == null ? 0 : (_nunberOfAnts).intValue()))) {
      this.pheromones = this.updatePheromones();
      this.numberOfIterationsDone++;
      Float _get = this.pathsLength.get(this.pathsLength.indexOf(IterableExtensions.<Float>max(this.pathsLength)));
      if ((_get.floatValue() > this.currentBestPathLength.doubleValue())) {
        this.currentBestPath = this.paths.get(this.pathsLength.indexOf(IterableExtensions.<Float>max(this.pathsLength)));
        this.currentBestPathLength = this.pathsLength.get(this.pathsLength.indexOf(IterableExtensions.<Float>max(this.pathsLength)));
      }
      Integer _numberOfIterations = this.acoParameters.getNumberOfIterations();
      if ((this.numberOfIterationsDone == _numberOfIterations)) {
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        OptimizationFinished _optimizationFinished = new OptimizationFinished(this.pheromones, this.currentBestPath, this.currentBestPathLength);
        _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_optimizationFinished);
      } else {
        this.launchACOAgents();
      }
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      String _string = this.currentBestPath.toString();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(((((("Itération :" + this.numberOfIterationsDone) + "meilleur chemin") + this.currentBestPathLength) + " parcours") + _string));
    }
  }
  
  protected void launchACOAgents() {
    for (int i = 0; (i < this.acoParameters.getNunberOfAnts().doubleValue()); i++) {
      {
        final UUID childID = UUID.randomUUID();
        Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
        InnerContextAccess _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER();
        _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.spawnInContextWithID(ACOAgent.class, childID, _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext(), this.myUUID, Integer.valueOf(i), this.acoParameters, this.pheromones);
        this.acoAgentsID.add(childID);
      }
    }
  }
  
  protected void launchIterations() {
    synchronized (this.acoAgentsID) {
      while ((this.acoAgentsID.size() < this.acoParameters.getNunberOfAnts().doubleValue())) {
        Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(Integer.valueOf(this.acoAgentsID.size()));
      }
      this.numberOfIterationsDone = Integer.valueOf(0);
      Float _float = new Float(0);
      this.currentBestPathLength = _float;
      this.initializePheromones(Double.valueOf(0d));
      ArrayList<ArrayList<Integer>> _arrayList = new ArrayList<ArrayList<Integer>>();
      this.paths = _arrayList;
      ArrayList<Float> _arrayList_1 = new ArrayList<Float>();
      this.pathsLength = _arrayList_1;
      InnerContextAccess _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER();
      NewIteration _newIteration = new NewIteration(this.pheromones);
      _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getDefaultSpace().emit(this.myUUID, _newIteration, null);
    }
  }
  
  protected void initializePheromones(final Double initialPheromoneValue) {
    for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
      {
        ArrayList<Double> temp = new ArrayList<Double>();
        for (int j = 0; (j < this.acoParameters.getNumberOfCities().doubleValue()); j++) {
          temp.add(initialPheromoneValue);
        }
        this.pheromones.add(temp);
      }
    }
  }
  
  protected ArrayList<ArrayList<Double>> updatePheromones() {
    ArrayList<ArrayList<Double>> newPheromones = new ArrayList<ArrayList<Double>>();
    double sumOfpheromoneDelta = this.pheromoneDeltaComputation();
    for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
      {
        ArrayList<Double> temp = new ArrayList<Double>();
        for (int j = 0; (j < this.acoParameters.getNumberOfCities().doubleValue()); j++) {
          {
            Float _pheromoneEvaporationFactor = this.acoParameters.getPheromoneEvaporationFactor();
            Double _get = this.pheromones.get(i).get(j);
            double newValue = ((((_pheromoneEvaporationFactor) == null ? 0 : (_pheromoneEvaporationFactor).floatValue()) * ((_get) == null ? 0 : (_get).doubleValue())) + sumOfpheromoneDelta);
            temp.add(Double.valueOf(newValue));
          }
        }
        newPheromones.add(temp);
      }
    }
    return newPheromones;
  }
  
  protected double pheromoneDeltaComputation() {
    double sumOfpheromoneDelta = 0d;
    for (int k = 0; (k < this.paths.size()); k++) {
      for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
        for (int j = 0; (j < this.acoParameters.getNumberOfCities().doubleValue()); j++) {
          if ((this.paths.get(k).contains(Integer.valueOf(i)) && this.paths.get(k).contains(Integer.valueOf(j)))) {
            Float _get = this.pathsLength.get(k);
            sumOfpheromoneDelta = (sumOfpheromoneDelta + (1 / ((_get) == null ? 0 : (_get).floatValue())));
          }
        }
      }
    }
    return sumOfpheromoneDelta;
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
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Destroy(final Destroy occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$1(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$NewOptimization(final NewOptimization occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$NewOptimization$2(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$IterationFinished(final IterationFinished occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$IterationFinished$3(occurrence));
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
    ACOManager other = (ACOManager) obj;
    if (!Objects.equals(this.myUUID, other.myUUID))
      return false;
    if (other.currentBestPathLength == null) {
      if (this.currentBestPathLength != null)
        return false;
    } else if (this.currentBestPathLength == null)
      return false;if (other.currentBestPathLength != null && Float.floatToIntBits(other.currentBestPathLength.floatValue()) != Float.floatToIntBits(this.currentBestPathLength.floatValue()))
      return false;
    if (other.numberOfIterationsDone == null) {
      if (this.numberOfIterationsDone != null)
        return false;
    } else if (this.numberOfIterationsDone == null)
      return false;
    if (other.numberOfIterationsDone != null && other.numberOfIterationsDone.intValue() != this.numberOfIterationsDone.intValue())
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.myUUID);
    result = prime * result + Objects.hashCode(this.currentBestPathLength);
    result = prime * result + Objects.hashCode(this.numberOfIterationsDone);
    return result;
  }
  
  @SyntheticMember
  public ACOManager(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public ACOManager(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public ACOManager(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
