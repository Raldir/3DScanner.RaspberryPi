package de.rami.polygonViewer.serverSystem;
import java.security.PublicKey;

import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public class NullHostKeyVerifier implements HostKeyVerifier {

   @Override
   public boolean verify(String arg0, int arg1, PublicKey arg2) {
      return true;
   }

}