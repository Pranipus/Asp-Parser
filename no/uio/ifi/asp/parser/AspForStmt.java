package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspForStmt extends AspCompoundStmt{
  AspName name;
  AspExpr expr;
  AspSuite suite;

  AspForStmt(int n){
    super(n);
  }

  static AspForStmt parse(Scanner s){
    enterParser("for stmt");

    AspForStmt afs = new AspForStmt(s.curLineNum());
    skip(s, forToken);
    afs.name = AspName.parse(s);
    skip(s, inToken);
    afs.expr = AspExpr.parse(s);
    skip(s, colonToken);
    afs.suite = AspSuite.parse(s);

    leaveParser("for stmt");
    return afs;
  }

  @Override
  public void prettyPrint() {
     prettyWrite("for ");
     name.prettyPrint();
     prettyWrite(" in ");
     expr.prettyPrint();
     prettyWrite(":");
     suite.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeValue t = expr.eval(curScope);
     if (t instanceof RuntimeListValue){
       RuntimeListValue list = (RuntimeListValue) t;
       ArrayList<RuntimeValue> loop = list.getValues();

       for(int i=0; i<loop.size(); i++){
         trace("for #" + (i+1) + ": " + name.name + " = " + loop.get(i));
         curScope.assign(name.name, loop.get(i));
         suite.eval(curScope);
       }

     }else{
       t.runtimeError(t.showInfo() + "is not iterable", this);
     }
     return null;
  }

}
