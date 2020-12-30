package fr.utbm.ia54.acotsp.gui;

import fr.utbm.ia54.acotsp.ACOParameters;
import fr.utbm.ia54.acotsp.IterationFinished;
import fr.utbm.ia54.acotsp.NewOptimization;
import fr.utbm.ia54.acotsp.OptimizationFinished;
import fr.utbm.ia54.acotsp.gui.GuiPanel;
import io.sarl.bootstrap.SRE;
import io.sarl.bootstrap.SREBootstrap;
import io.sarl.core.OpenEventSpace;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.AgentContext;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.EventListener;
import io.sarl.lang.core.EventSpace;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class GUI extends GuiPanel implements EventListener {
  public static final UUID id = UUID.randomUUID();
  
  /**
   * SRE Kernel instance
   */
  private SREBootstrap kernel;
  
  /**
   * The default SARL context where environment and boids are spawned
   */
  private AgentContext defaultSARLContext;
  
  /**
   * Identifier of the environment
   */
  private UUID environment;
  
  /**
   * the vent space used to establish communication between GUI and agents,
   * Especially enabling GUI to forward start event to the environment,
   * respectively the environment to send GUIRepain at each simulation step to the GUI
   */
  private OpenEventSpace space;
  
  public void repaint(final ArrayList<ArrayList<Float>> pheromones, final ArrayList<Integer> bestPath, final Float bestPathLength) {
    this.panel.setBestTour(bestPath);
    this.panel.setPheromones(pheromones);
    String _string = bestPathLength.toString();
    this.setStatus(("Best path length is : " + _string));
    this.panel.repaint();
  }
  
  @Override
  public void runOptimization() {
    System.out.println("runOptimization sarl");
    this.kernel = SRE.getBootstrap();
    this.defaultSARLContext = this.kernel.startWithoutAgent();
    this.environment = UUID.randomUUID();
    EventSpace _defaultSpace = this.defaultSARLContext.getDefaultSpace();
    this.space = ((OpenEventSpace) _defaultSpace);
    this.space.register(this);
    ACOParameters _acoParameters = this.getAcoParameters();
    NewOptimization _newOptimization = new NewOptimization(_acoParameters);
    this.space.emit(GUI.id, _newOptimization, null);
  }
  
  @Override
  public void stopOptimization() {
    this.setStatus("Stopped Optimization");
    System.out.println("stopOptimization sarl");
    this.kernel = SRE.getBootstrap();
    this.defaultSARLContext = this.kernel.startWithoutAgent();
    this.environment = UUID.randomUUID();
    EventSpace _defaultSpace = this.defaultSARLContext.getDefaultSpace();
    this.space = ((OpenEventSpace) _defaultSpace);
    OptimizationFinished _optimizationFinished = new OptimizationFinished(null, null, null);
    this.space.emit(GUI.id, _optimizationFinished, null);
  }
  
  public void receiveEvent(final Event event) {
    if ((event instanceof IterationFinished)) {
      this.setPath(((IterationFinished)event).path, ((IterationFinished)event).pathLength);
      this.repaint();
    } else {
      if ((event instanceof OptimizationFinished)) {
        this.setPath(((OptimizationFinished)event).bestPath, ((OptimizationFinished)event).bestPathLength);
        this.repaint();
        this.setStatus(("Optimization finished, best path length is " + ((OptimizationFinished)event).bestPath));
      }
    }
  }
  
  @Pure
  public UUID getID() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
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
    GUI other = (GUI) obj;
    if (!Objects.equals(this.environment, other.environment))
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
    return result;
  }
  
  @SyntheticMember
  public GUI() {
    super();
  }
  
  @SyntheticMember
  private static final long serialVersionUID = -2476165899L;
}
