package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.*;
import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt{
  AspName funcName;
  public ArrayList<AspName> args = new ArrayList<>();
  public AspSuite body;

  AspFuncDef(int n){
    super(n);
  }


  static AspFuncDef parse(Scanner s){
    enterParser("func def");

    AspFuncDef afd = new AspFuncDef(s.curLineNum());

    skip(s, defToken);
    afd.funcName = AspName.parse(s);
    skip(s, leftParToken);

    while(true){
      if (s.curToken().kind != rightParToken) afd.args.add(AspName.parse(s));
      if (s.curToken().kind != rightParToken) skip(s, commaToken);
      else if (s.curToken().kind == rightParToken) break;
    }
    skip(s, rightParToken);
    skip(s, colonToken);

    afd.body = AspSuite.parse(s);

    leaveParser("func def");
    return afd;
  }

  @Override
  public void prettyPrint() {
     prettyWrite("def ");
     funcName.prettyPrint();
     prettyWrite("(");

     int nPrinted = 0;
     for (AspName arg : args){
       if (nPrinted > 0) prettyWrite(", ");
       arg.prettyPrint(); ++nPrinted;
     }
     prettyWrite(")");

     prettyWrite(": ");
     body.prettyPrint();

  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

      trace ("def " + funcName.name);
      RuntimeFunc func = new RuntimeFunc(this, curScope);
      curScope.assign(funcName.name, func);

      return null;
    }

}
