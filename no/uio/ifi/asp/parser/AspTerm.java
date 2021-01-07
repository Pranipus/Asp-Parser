package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.main.*;

public class AspTerm extends AspSyntax{
  ArrayList<AspFactor> factors = new ArrayList<>();
  ArrayList<AspTermOpr> termoprs = new ArrayList<>();
  AspTerm(int n){
    super(n);
  }

  static AspTerm parse(Scanner s){
    enterParser("term");

    AspTerm at = new AspTerm(s.curLineNum());

    while(true){
      at.factors.add(AspFactor.parse(s));

      if(s.isTermOpr()) at.termoprs.add(AspTermOpr.parse(s));
      else break;
    }

    leaveParser("term");
    return at;
  }

  @Override
  public void prettyPrint() {
     factors.get(0).prettyPrint();

     for(int i=0; i<termoprs.size(); i++){
       termoprs.get(i).prettyPrint();
       factors.get(i+1).prettyPrint();
     }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

     RuntimeValue v = factors.get(0).eval(curScope);

     for(int i=1; i<factors.size(); i++){
       TokenKind k = termoprs.get(i-1).op;
       switch(k){
         case minusToken:
           v = v.evalSubtract(factors.get(i).eval(curScope), this); break;
         case plusToken:
           v = v.evalAdd(factors.get(i).eval(curScope), this); break;
         default:
           Main.panic("Illegal term operator: " + k + "!");
       }
     }
     return v;
  }
}
