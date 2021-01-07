package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspStmt extends AspSyntax{
    static AspStmt as = null;
    AspStmt(int n){
        super(n);
    }

    static AspStmt parse(Scanner s){
      enterParser("stmt");


      TokenKind k = s.curToken().kind;

      if(k==forToken || k==ifToken || k==whileToken || k==defToken){
        as = AspCompoundStmt.parse(s);
      }else{
        as = AspSmallStmtList.parse(s);
      }
      leaveParser("stmt");
      return as;
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
      return as.eval(curScope);
    }
}
