package fr.utbm.ia54.acotsp.gui;

import fr.utbm.ia54.acotsp.gui.GuiPanel;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import javax.swing.JFrame;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class GUI extends JFrame {
  private GuiPanel panel;
  
  public GUI() {
    super();
    GuiPanel _guiPanel = new GuiPanel();
    this.panel = _guiPanel;
    this.setTitle("ACO TSP");
    this.panel.setVisible(true);
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
  
  @SyntheticMember
  private static final long serialVersionUID = -1867115221L;
}
