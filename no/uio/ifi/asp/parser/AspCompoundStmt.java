package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.*;

public abstract class AspCompoundStmt extends AspStmt{
  static AspCompoundStmt acs = null;

    AspCompoundStmt(int n){
        super(n);
    }

    static AspCompoundStmt parse(Scanner s){
      enterParser("compound stmt");
      switch (s.curToken().kind){
        case forToken:
          acs = AspForStmt.parse(s); break;
        case defToken:
          acs = AspFuncDef.parse(s); break;
        case ifToken:
          acs = AspIfStmt.parse(s); break;
        case whileToken:
          acs = AspWhileStmt.parse(s); break;
      }

      leaveParser("compoundt stmt");
      return acs;
    }

}
