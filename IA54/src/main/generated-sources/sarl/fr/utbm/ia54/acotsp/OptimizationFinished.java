package fr.utbm.ia54.acotsp;

import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class OptimizationFinished extends Event {
  public Double[][] pheromones;
  
  public ArrayList<Integer> bestPath;
  
  public Integer bestPathLength;
  
  public OptimizationFinished(final Double[][] p, final ArrayList<Integer> bstPath, final Integer bstPathLength) {
    this.pheromones = p;
    this.bestPath = bstPath;
    this.bestPathLength = bstPathLength;
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
    OptimizationFinished other = (OptimizationFinished) obj;
    if (other.bestPathLength == null) {
      if (this.bestPathLength != null)
        return false;
    } else if (this.bestPathLength == null)
      return false;
    if (other.bestPathLength != null && other.bestPathLength.intValue() != this.bestPathLength.intValue())
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.bestPathLength);
    return result;
  }
  
  /**
   * Returns a String representation of the OptimizationFinished event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("pheromones", this.pheromones);
    builder.add("bestPath", this.bestPath);
    builder.add("bestPathLength", this.bestPathLength);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = 2637368863L;
}
