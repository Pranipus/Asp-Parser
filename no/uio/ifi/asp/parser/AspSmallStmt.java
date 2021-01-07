package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspSmallStmt extends AspSyntax{
  static AspSmallStmt as = null;
    AspSmallStmt(int n){
      super(n);
    }

    static AspSmallStmt parse (Scanner s){
      enterParser("small stmt");

      switch(s.curToken().kind){
        case passToken:
          as = AspPassStmt.parse(s); break;
        case returnToken:
          as = AspReturnStmt.parse(s); break;
        case nameToken:
          if(s.anyEqualToken()){
            as = AspAssignment.parse(s); break;
          }else{
            as = AspExprStmt.parse(s); break;
          }
      }

      leaveParser("small stmt");
      return as;
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
      return as.eval(curScope);
    }
}
