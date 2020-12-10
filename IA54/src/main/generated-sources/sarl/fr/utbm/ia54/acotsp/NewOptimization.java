package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.ACOParameters;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * @author yaben
 */
@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class NewOptimization extends Event {
  public ACOParameters acoParameters;
  
  public NewOptimization(final ACOParameters p) {
    this.acoParameters = p;
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    return result;
  }
  
  /**
   * Returns a String representation of the NewOptimization event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("acoParameters", this.acoParameters);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = 1357598012L;
}
