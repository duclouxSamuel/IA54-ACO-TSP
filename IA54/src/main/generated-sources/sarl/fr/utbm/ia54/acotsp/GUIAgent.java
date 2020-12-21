package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.ACOParameters;
import fr.utbm.ia54.acotsp.Gtsp;
import fr.utbm.ia54.acotsp.GuiRepaint;
import fr.utbm.ia54.acotsp.NewOptimization;
import fr.utbm.ia54.acotsp.OptimizationFinished;
import io.sarl.core.DefaultContextInteractions;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Omen
 */
@SarlSpecification("0.11")
@SarlElementType(19)
@SuppressWarnings("all")
public class GUIAgent extends Agent {
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    try {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was started.");
      File _file = new File("data/instances/20kroA100.txt");
      Gtsp gtsp = new Gtsp(_file);
      float pheromoneEvaporationFactor = 0.1f;
      float pheromoneRegulationFactor = 1f;
      float visibilityRegulationFactor = 1.25f;
      int nunberOfAnts = 70;
      int numberOfIterations = 100;
      int _dimension = gtsp.getDimension();
      int _groupDimension = gtsp.getGroupDimension();
      ArrayList<ArrayList<Float>> _convertToFloatArrayList = this.convertToFloatArrayList(gtsp.getNode_dist());
      ArrayList<ArrayList<Float>> _convertToFloatArrayList_1 = this.convertToFloatArrayList(gtsp.getNode_coord_section());
      ArrayList<Integer> _convertToIntegerArrayList = this.convertToIntegerArrayList(gtsp.getGroup_list());
      ACOParameters acoParameters = new ACOParameters(Integer.valueOf(_dimension), Integer.valueOf(_groupDimension), _convertToFloatArrayList, _convertToFloatArrayList_1, _convertToIntegerArrayList, Float.valueOf(pheromoneEvaporationFactor), Float.valueOf(pheromoneRegulationFactor), Float.valueOf(visibilityRegulationFactor), Integer.valueOf(nunberOfAnts), Integer.valueOf(numberOfIterations));
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      NewOptimization _newOptimization = new NewOptimization(acoParameters);
      _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_newOptimization);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void $behaviorUnit$Destroy$1(final Destroy occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The agent was stopped.");
  }
  
  private void $behaviorUnit$OptimizationFinished$2(final OptimizationFinished occurrence) {
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(new GuiRepaint(occurrence.pheromones, occurrence.bestPath, occurrence.bestPathLength));
  }
  
  protected ArrayList<ArrayList<Float>> convertToFloatArrayList(final float[][] array) {
    ArrayList<ArrayList<Float>> response = new ArrayList<ArrayList<Float>>();
    for (int i = 0; (i < array.length); i++) {
      {
        ArrayList<Float> temp = new ArrayList<Float>();
        for (int j = 0; (j < array[i].length); j++) {
          temp.add(Float.valueOf(array[i][j]));
        }
        response.add(temp);
      }
    }
    return response;
  }
  
  protected ArrayList<Integer> convertToIntegerArrayList(final int[] array) {
    ArrayList<Integer> response = new ArrayList<Integer>();
    for (int i = 0; (i < array.length); i++) {
      response.add(Integer.valueOf(array[i]));
    }
    return response;
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
  private void $guardEvaluator$OptimizationFinished(final OptimizationFinished occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$OptimizationFinished$2(occurrence));
  }
  
  @SyntheticMember
  public GUIAgent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public GUIAgent(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public GUIAgent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
