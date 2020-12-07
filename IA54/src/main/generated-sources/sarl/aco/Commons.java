package aco;

import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class Commons {
  private int[][] distances;
  
  private float[][] positions;
  
  private float[] paramRegulation;
  
  private int nFourmis;
  
  private int nIterations;
  
  private float fact_evaporation;
  
  private float[][] pheromones;
  
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
    Commons other = (Commons) obj;
    if (other.nFourmis != this.nFourmis)
      return false;
    if (other.nIterations != this.nIterations)
      return false;
    if (Float.floatToIntBits(other.fact_evaporation) != Float.floatToIntBits(this.fact_evaporation))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.nFourmis);
    result = prime * result + Integer.hashCode(this.nIterations);
    result = prime * result + Float.hashCode(this.fact_evaporation);
    return result;
  }
  
  @SyntheticMember
  public Commons() {
    super();
  }
}
