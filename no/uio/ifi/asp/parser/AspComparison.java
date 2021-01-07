package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.main.*;

public class AspComparison extends AspSyntax{
  ArrayList<AspTerm> terms = new ArrayList<>();
  ArrayList<AspCompOpr> comps = new ArrayList<>();

  AspComparison(int n){
    super(n);
  }

  static AspComparison parse(Scanner s){
    enterParser("comparison");
    AspComparison ac = new AspComparison(s.curLineNum());
    ac.terms.add(AspTerm.parse(s));
    while(true){
      if(s.isCompOpr()){
        ac.comps.add(AspCompOpr.parse(s));
        ac.terms.add(AspTerm.parse(s));
      } else break;
    }


    leaveParser("comparison");
    return ac;
  }

  @Override
  public void prettyPrint() {
    terms.get(0).prettyPrint();

    for (int i=0; i < comps.size(); i++){
      comps.get(i).prettyPrint();
      terms.get(i+1).prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeValue v = terms.get(0).eval(curScope);
     RuntimeValue res = v;

     for (int i=1; i<terms.size(); i++){
       TokenKind k = comps.get(i-1).op;
       RuntimeValue nextTerm = terms.get(i).eval(curScope);
       switch(k){
         case lessToken:
           res = v.evalLess(nextTerm, this);
           v = nextTerm; break;
         case greaterToken:
           res = v.evalGreater(nextTerm, this);
           v = nextTerm; break;
         case doubleEqualToken:
           res = v.evalEqual(nextTerm, this);
           v = nextTerm; break;
         case greaterEqualToken:
           res = v.evalGreaterEqual(nextTerm, this);
           v = nextTerm; break;
         case lessEqualToken:
           res = v.evalLessEqual(nextTerm, this);
           v = nextTerm; break;
         case notEqualToken:
           res = v.evalNotEqual(nextTerm, this);
           v = nextTerm; break;
         default:
           Main.panic("Illegal comparison operator: " + k + "!");
       }
     }
     return res;
  }
}
