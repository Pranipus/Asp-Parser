package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspListDisplay extends AspAtom{
  ArrayList<AspExpr> expressions = new ArrayList<>();;

  AspListDisplay(int n){
    super(n);
  }

  static AspListDisplay parse(Scanner s){
    enterParser("list display");
    AspListDisplay ald = new AspListDisplay(s.curLineNum());

    skip(s, leftBracketToken);
    while(true){
      if(s.curToken().kind != rightBracketToken) ald.expressions.add(AspExpr.parse(s));
      if(s.curToken().kind != rightBracketToken) skip(s, commaToken);
      else if (s.curToken().kind == rightBracketToken) break;
    }
    skip(s, rightBracketToken);
    leaveParser("list display");
    return ald;
  }

  @Override
  public void prettyPrint() {
    int nPrinted = 0;
     prettyWrite("[");
     for (AspExpr ex : expressions){
       if (nPrinted > 0) prettyWrite(", ");
       ex.prettyPrint(); ++nPrinted;
     }
     prettyWrite("]");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeListValue v = new RuntimeListValue();
     for (AspExpr ae : expressions){
       v.addValue(ae.eval(curScope));
     }
     return v;
  }
}
