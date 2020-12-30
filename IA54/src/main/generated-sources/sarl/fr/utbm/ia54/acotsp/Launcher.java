package fr.utbm.ia54.acotsp;

import fr.utbm.ia54.acotsp.ACOManager;
import fr.utbm.ia54.acotsp.gui.GUI;
import io.sarl.bootstrap.SRE;
import io.sarl.bootstrap.SREBootstrap;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class Launcher {
  public static void main(final String... args) {
    try {
      GUI gui = new GUI();
      gui.setVisible(true);
      SREBootstrap kernel = SRE.getBootstrap();
      UUID environment = UUID.randomUUID();
      kernel.startAgentWithID(ACOManager.class, environment);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @SyntheticMember
  public Launcher() {
    super();
  }
}
