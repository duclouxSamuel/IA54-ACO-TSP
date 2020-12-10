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
public class IterationFinished extends Event {
  public ArrayList<Integer> path;
  
  public Integer pathLength;
  
  public IterationFinished(final ArrayList<Integer> p, final Integer pl) {
    this.path = p;
    this.pathLength = pl;
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
    IterationFinished other = (IterationFinished) obj;
    if (other.pathLength == null) {
      if (this.pathLength != null)
        return false;
    } else if (this.pathLength == null)
      return false;
    if (other.pathLength != null && other.pathLength.intValue() != this.pathLength.intValue())
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.pathLength);
    return result;
  }
  
  /**
   * Returns a String representation of the IterationFinished event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("path", this.path);
    builder.add("pathLength", this.pathLength);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = -3449026555L;
}
