package fr.utbm.ia54.acotsp;

import com.google.common.base.Objects;
import fr.utbm.ia54.acotsp.ACOAgent;
import fr.utbm.ia54.acotsp.ACOParameters;
import fr.utbm.ia54.acotsp.AgentIsReady;
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
import java.util.Random;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
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
  
  private ArrayList<ArrayList<Float>> pheromones = new ArrayList<ArrayList<Float>>();
  
  private Integer numberOfIterationsDone;
  
  private Integer numberOfIterationsWithoutChanges;
  
  private ArrayList<UUID> acoAgentsReady;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was started.");
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.setLoggingName("ACOManager");
  }
  
  private void $behaviorUnit$Destroy$1(final Destroy occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was stopped.");
  }
  
  private void $behaviorUnit$NewOptimization$2(final NewOptimization occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("J\'ai recu l\'ordre d\'une nouvelle optim");
    this.acoParameters = occurrence.acoParameters;
    this.numberOfIterationsDone = Integer.valueOf(0);
    this.numberOfIterationsWithoutChanges = Integer.valueOf(0);
    Float _float = new Float(999999);
    this.currentBestPathLength = _float;
    this.initializePheromones();
    this.launchACOAgents();
  }
  
  private void $behaviorUnit$IterationFinished$3(final IterationFinished occurrence) {
    synchronized (this.paths) {
      synchronized (this.pathsLength) {
        ArrayList<Integer> path = occurrence.path;
        Float pathLength = occurrence.pathLength;
        this.paths.add(path);
        this.pathsLength.add(pathLength);
        int _size = this.paths.size();
        Integer _numberOfAnts = this.acoParameters.getNumberOfAnts();
        if ((_size == ((_numberOfAnts) == null ? 0 : (_numberOfAnts).intValue()))) {
          this.pheromones = this.updatePheromones();
          this.numberOfIterationsDone++;
          if ((this.numberOfIterationsWithoutChanges.intValue() > 15)) {
            ArrayList<ArrayList<Integer>> newPaths = new ArrayList<ArrayList<Integer>>();
            ArrayList<Float> newPathsLength = new ArrayList<Float>();
            Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
            _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("Mutation");
            for (final ArrayList<Integer> p : this.paths) {
              {
                Random generator = new Random();
                ArrayList<Integer> newPath = this.mutation(p);
                float newPathLength = this.computePathLength(newPath);
                newPaths.add(newPath);
                newPathsLength.add(Float.valueOf(newPathLength));
              }
            }
            this.paths = newPaths;
            this.pathsLength = newPathsLength;
            this.initializePheromones();
          }
          Float _get = this.pathsLength.get(this.pathsLength.indexOf(IterableExtensions.<Float>min(this.pathsLength)));
          if ((_get.floatValue() < this.currentBestPathLength.doubleValue())) {
            this.currentBestPath = this.paths.get(this.pathsLength.indexOf(IterableExtensions.<Float>min(this.pathsLength)));
            this.currentBestPathLength = this.pathsLength.get(this.pathsLength.indexOf(IterableExtensions.<Float>min(this.pathsLength)));
            this.numberOfIterationsWithoutChanges = Integer.valueOf(0);
            Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
            String _string = this.currentBestPath.toString();
            _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info(
              ((((("Itération : " + this.numberOfIterationsDone) + " Meilleur chemin : ") + this.currentBestPathLength) + 
                " Parcours : ") + _string));
          } else {
            this.numberOfIterationsWithoutChanges++;
          }
          Integer _numberOfIterations = this.acoParameters.getNumberOfIterations();
          if ((this.numberOfIterationsDone == _numberOfIterations)) {
            DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
            OptimizationFinished _optimizationFinished = new OptimizationFinished(this.pheromones, this.currentBestPath, this.currentBestPathLength);
            _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_optimizationFinished);
            Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
            String _string_1 = this.currentBestPath.toString();
            _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2.info((((("Fin d\'optimisation  " + "meilleur chemin : ") + this.currentBestPathLength) + " parcours") + _string_1));
          } else {
            this.launchIteration();
          }
        }
      }
    }
  }
  
  private void $behaviorUnit$AgentIsReady$4(final AgentIsReady occurrence) {
    this.acoAgentsReady.add(occurrence.getSource().getUUID());
    int _size = this.acoAgentsReady.size();
    Integer _numberOfAnts = this.acoParameters.getNumberOfAnts();
    if ((_numberOfAnts != null && _size == _numberOfAnts.doubleValue())) {
      this.launchIteration();
    }
  }
  
  protected void launchACOAgents() {
    ArrayList<UUID> _arrayList = new ArrayList<UUID>();
    this.acoAgentsReady = _arrayList;
    for (int i = 0; (i < this.acoParameters.getNumberOfAnts().doubleValue()); i++) {
      {
        final UUID childID = UUID.randomUUID();
        double _random = Math.random();
        Integer _numberOfCities = this.acoParameters.getNumberOfCities();
        Integer startingCity = Integer.valueOf((int) (_random * ((_numberOfCities) == null ? 0 : (_numberOfCities).intValue())));
        Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
        InnerContextAccess _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER();
        _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.spawnInContextWithID(ACOAgent.class, childID, _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext(), this.myUUID, startingCity, this.acoParameters);
      }
    }
  }
  
  protected void launchIteration() {
    ArrayList<ArrayList<Integer>> _arrayList = new ArrayList<ArrayList<Integer>>();
    this.paths = _arrayList;
    ArrayList<Float> _arrayList_1 = new ArrayList<Float>();
    this.pathsLength = _arrayList_1;
    for (final UUID acoAgent : this.acoAgentsReady) {
      InnerContextAccess _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER();
      NewIteration _newIteration = new NewIteration(this.pheromones);
      class $SerializableClosureProxy implements Scope<Address> {
        
        private final UUID acoAgent;
        
        public $SerializableClosureProxy(final UUID acoAgent) {
          this.acoAgent = acoAgent;
        }
        
        @Override
        public boolean matches(final Address it) {
          UUID _uUID = it.getUUID();
          return Objects.equal(_uUID, acoAgent);
        }
      }
      final Scope<Address> _function = new Scope<Address>() {
        @Override
        public boolean matches(final Address it) {
          UUID _uUID = it.getUUID();
          return Objects.equal(_uUID, acoAgent);
        }
        private Object writeReplace() throws ObjectStreamException {
          return new SerializableProxy($SerializableClosureProxy.class, acoAgent);
        }
      };
      _$CAPACITY_USE$IO_SARL_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getDefaultSpace().emit(this.myUUID, _newIteration, _function);
    }
  }
  
  protected void initializePheromones() {
    for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
      {
        ArrayList<Float> temp = new ArrayList<Float>();
        for (int j = 0; (j < this.acoParameters.getNumberOfCities().doubleValue()); j++) {
          if ((i != j)) {
            temp.add(Float.valueOf(1f));
          } else {
            temp.add(Float.valueOf(0f));
          }
        }
        this.pheromones.add(temp);
      }
    }
  }
  
  protected ArrayList<ArrayList<Float>> updatePheromones() {
    ArrayList<ArrayList<Float>> newPheromones = new ArrayList<ArrayList<Float>>();
    for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
      {
        ArrayList<Float> temp = new ArrayList<Float>();
        for (int j = 0; (j < this.acoParameters.getNumberOfCities().doubleValue()); j++) {
          {
            Float _pheromoneEvaporationFactor = this.acoParameters.getPheromoneEvaporationFactor();
            Float _get = this.pheromones.get(i).get(j);
            float _sumOfPheromoneDeltaComputation = this.sumOfPheromoneDeltaComputation(Integer.valueOf(i), Integer.valueOf(j));
            float newValue = ((((_pheromoneEvaporationFactor) == null ? 0 : (_pheromoneEvaporationFactor).floatValue()) * ((_get) == null ? 0 : (_get).floatValue())) + _sumOfPheromoneDeltaComputation);
            if (((newValue < 1e-32) || (i == j))) {
              newValue = 0f;
            }
            temp.add(Float.valueOf(newValue));
          }
        }
        newPheromones.add(temp);
      }
    }
    return newPheromones;
  }
  
  protected ArrayList<ArrayList<Float>> updatePheromonesWithMutation() {
    ArrayList<ArrayList<Float>> newPheromones = new ArrayList<ArrayList<Float>>();
    float minPheromones = 99999f;
    Pair<Integer, Integer> minPheromonesIndex = new Pair<Integer, Integer>(Integer.valueOf(0), Integer.valueOf(0));
    float maxPheromones = 0f;
    Pair<Integer, Integer> maxPheromonesIndex = new Pair<Integer, Integer>(Integer.valueOf(0), Integer.valueOf(0));
    for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
      {
        ArrayList<Float> temp = new ArrayList<Float>();
        for (int j = 0; (j < this.acoParameters.getNumberOfCities().doubleValue()); j++) {
          {
            Float _pheromoneEvaporationFactor = this.acoParameters.getPheromoneEvaporationFactor();
            Float _get = this.pheromones.get(i).get(j);
            float _sumOfPheromoneDeltaComputation = this.sumOfPheromoneDeltaComputation(Integer.valueOf(i), Integer.valueOf(j));
            float newValue = ((((_pheromoneEvaporationFactor) == null ? 0 : (_pheromoneEvaporationFactor).floatValue()) * ((_get) == null ? 0 : (_get).floatValue())) + _sumOfPheromoneDeltaComputation);
            if (((newValue < 1e-32) || (i == j))) {
              newValue = 0f;
            }
            temp.add(Float.valueOf(newValue));
            if (((newValue < minPheromones) && (newValue > 0f))) {
              minPheromones = newValue;
              Pair<Integer, Integer> _pair = new Pair<Integer, Integer>(Integer.valueOf(i), Integer.valueOf(j));
              minPheromonesIndex = _pair;
            } else {
              if ((newValue > maxPheromones)) {
                maxPheromones = newValue;
                Pair<Integer, Integer> _pair_1 = new Pair<Integer, Integer>(Integer.valueOf(i), Integer.valueOf(j));
                maxPheromonesIndex = _pair_1;
              }
            }
          }
        }
        newPheromones.add(temp);
      }
    }
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    String _string = minPheromonesIndex.toString();
    String _string_1 = maxPheromonesIndex.toString();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(
      ((((((("Mutation de " + _string) + " value : ") + Float.valueOf(minPheromones)) + " à ") + _string_1) + " value : ") + Float.valueOf(maxPheromones)));
    if (((minPheromonesIndex.getKey() == null ? (minPheromonesIndex.getValue() != null) : (minPheromonesIndex.getValue() == null || minPheromonesIndex.getKey().intValue() != minPheromonesIndex.getValue().doubleValue())) && 
      (maxPheromonesIndex.getKey() == null ? (maxPheromonesIndex.getValue() != null) : (maxPheromonesIndex.getValue() == null || maxPheromonesIndex.getKey().intValue() != maxPheromonesIndex.getValue().doubleValue())))) {
      newPheromones.get(((minPheromonesIndex.getKey()) == null ? 0 : (minPheromonesIndex.getKey()).intValue())).set(((minPheromonesIndex.getValue()) == null ? 0 : (minPheromonesIndex.getValue()).intValue()), Float.valueOf(maxPheromones));
      newPheromones.get(((maxPheromonesIndex.getKey()) == null ? 0 : (maxPheromonesIndex.getKey()).intValue())).set(((maxPheromonesIndex.getValue()) == null ? 0 : (maxPheromonesIndex.getValue()).intValue()), Float.valueOf(minPheromones));
    }
    return newPheromones;
  }
  
  protected float sumOfPheromoneDeltaComputation(final Integer i, final Integer j) {
    float sumOfpheromoneDelta = 0f;
    synchronized (this.paths) {
      synchronized (this.pathsLength) {
        for (int k = 0; (k < this.paths.size()); k++) {
          if (((i == null ? (j != null) : (j == null || i.intValue() != j.doubleValue())) && this.isEdgeVisited(this.paths.get(k), i, j))) {
            Float _get = this.pathsLength.get(k);
            sumOfpheromoneDelta = (sumOfpheromoneDelta + (1 / ((_get) == null ? 0 : (_get).floatValue())));
          }
        }
      }
    }
    return sumOfpheromoneDelta;
  }
  
  @Pure
  protected boolean isEdgeVisited(final ArrayList<Integer> path, final Integer i, final Integer j) {
    boolean response = false;
    if (((path.get(0) == null ? (i == null) : (i != null && path.get(0).intValue() == i.doubleValue())) && (path.get((path.size() - 1)) == null ? (j == null) : (j != null && path.get((path.size() - 1)).intValue() == j.doubleValue())))) {
      response = true;
    } else {
      if (((path.get(0) == null ? (j == null) : (j != null && path.get(0).intValue() == j.doubleValue())) && (path.get((path.size() - 1)) == null ? (i == null) : (i != null && path.get((path.size() - 1)).intValue() == i.doubleValue())))) {
        response = true;
      } else {
        for (int n = 0; (n < (path.size() - 1)); n++) {
          if (((path.get(n) == null ? (i == null) : (i != null && path.get(n).intValue() == i.doubleValue())) && (path.get((n + 1)) == null ? (j == null) : (j != null && path.get((n + 1)).intValue() == j.doubleValue())))) {
            response = true;
          } else {
            if (((path.get(n) == null ? (j == null) : (j != null && path.get(n).intValue() == j.doubleValue())) && (path.get((n + 1)) == null ? (i == null) : (i != null && path.get((n + 1)).intValue() == i.doubleValue())))) {
              response = true;
            }
          }
        }
      }
    }
    return response;
  }
  
  protected float computePathLength(final ArrayList<Integer> path) {
    float length = 0f;
    for (int i = 0; (i < (path.size() - 1)); i++) {
      Float _get = this.acoParameters.getDistances().get(((path.get(i)) == null ? 0 : (path.get(i)).intValue())).get(((path.get((i + 1))) == null ? 0 : (path.get((i + 1))).intValue()));
      length = (length + ((_get) == null ? 0 : (_get).floatValue()));
    }
    int _size = path.size();
    Float _get = this.acoParameters.getDistances().get(((path.get((_size - 1))) == null ? 0 : (path.get((_size - 1))).intValue())).get(((path.get(0)) == null ? 0 : (path.get(0)).intValue()));
    length = (length + ((_get) == null ? 0 : (_get).floatValue()));
    return length;
  }
  
  protected ArrayList<Integer> mutation(final ArrayList<Integer> path) {
    Random generator = new Random();
    Integer t = path.get(generator.nextInt(path.size()));
    ArrayList<Integer> temp = new ArrayList<Integer>();
    for (int i = 0; (i < this.acoParameters.getNumberOfCities().doubleValue()); i++) {
      Integer _get = this.acoParameters.getAttachedCluster().get(i);
      Integer _get_1 = this.acoParameters.getAttachedCluster().get(((t) == null ? 0 : (t).intValue()));
      if ((_get == null ? (_get_1 == null) : (_get_1 != null && _get.intValue() == _get_1.doubleValue()))) {
        temp.add(Integer.valueOf(i));
      }
    }
    Integer s = temp.get(generator.nextInt(temp.size()));
    path.set(path.indexOf(t), s);
    return path;
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
  private void $guardEvaluator$AgentIsReady(final AgentIsReady occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$AgentIsReady$4(occurrence));
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
    if (!java.util.Objects.equals(this.myUUID, other.myUUID))
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
    if (other.numberOfIterationsWithoutChanges == null) {
      if (this.numberOfIterationsWithoutChanges != null)
        return false;
    } else if (this.numberOfIterationsWithoutChanges == null)
      return false;
    if (other.numberOfIterationsWithoutChanges != null && other.numberOfIterationsWithoutChanges.intValue() != this.numberOfIterationsWithoutChanges.intValue())
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.myUUID);
    result = prime * result + java.util.Objects.hashCode(this.currentBestPathLength);
    result = prime * result + java.util.Objects.hashCode(this.numberOfIterationsDone);
    result = prime * result + java.util.Objects.hashCode(this.numberOfIterationsWithoutChanges);
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
