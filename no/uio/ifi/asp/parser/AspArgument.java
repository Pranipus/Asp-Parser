package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArgument extends AspPrimarySuffix{
  ArrayList<AspExpr> expressions = new ArrayList<>();

  AspArgument(int n){
    super(n);
  }

  static AspArgument parse(Scanner s){
    enterParser("arguments");

    AspArgument aa = new AspArgument(s.curLineNum());
    skip(s, leftParToken);
    while(true){
      if(s.curToken().kind == rightParToken) break;
      aa.expressions.add(AspExpr.parse(s));
      if (s.curToken().kind == commaToken) skip(s, commaToken);
      else break;

    }
    skip(s, rightParToken);

    leaveParser("arguments");
    return aa;
  }

  @Override
  public void prettyPrint() {
    int nPrinted = 0;
     prettyWrite("(");
     for (AspExpr ex : expressions){
       if (nPrinted > 0) prettyWrite(", ");
       ex.prettyPrint(); ++nPrinted;
     }
     prettyWrite(")");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeListValue v = new RuntimeListValue();

     for (int i=0; i<expressions.size(); i++){
       v.addValue(expressions.get(i).eval(curScope));
     }
     return v;
  }

}
