package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.HashMap;
import java.util.Map;
import no.uio.ifi.asp.runtime.*;

public class AspDictDisplay extends AspAtom{
  HashMap<AspStringLiteral, AspExpr> map;

  AspDictDisplay(int n){
    super(n);
  }

  static AspDictDisplay parse(Scanner s){
    enterParser("dict display");

    AspDictDisplay add = new AspDictDisplay(s.curLineNum());
    add.map = new HashMap<>();
    skip(s, leftBraceToken);

    while(s.curToken().kind != rightBraceToken){
      AspStringLiteral asl = AspStringLiteral.parse(s);
      skip(s, colonToken);
      AspExpr ae = AspExpr.parse(s);
      add.map.put(asl, ae);
      if(s.curToken().kind == commaToken) skip(s, commaToken);
    }
    skip(s, rightBraceToken);
    leaveParser("dict display");
    return add;
  }

  @Override
  public void prettyPrint() {
     prettyWrite("{");

     int nPrinted = 0;
     for (Map.Entry<AspStringLiteral, AspExpr> entry : map.entrySet()){
       entry.getKey().prettyPrint();
       prettyWrite(" : ");
       entry.getValue().prettyPrint();
       if(nPrinted < map.size() - 1) prettyWrite(", "); ++nPrinted;
     }
     prettyWrite("}");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeDictValue v = new RuntimeDictValue();
     for (Map.Entry<AspStringLiteral, AspExpr> entry : map.entrySet()){
       v.addValue(entry.getKey().eval(curScope), entry.getValue().eval(curScope), this);
     }
     return v;
  }
}
