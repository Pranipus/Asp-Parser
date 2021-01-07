package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax{
  AspAtom atom;
  ArrayList<AspPrimarySuffix> suffixs = new ArrayList<>();

  AspPrimary(int n){
    super(n);
  }

  static AspPrimary parse(Scanner s){
    enterParser("primary");

    AspPrimary ap = new AspPrimary(s.curLineNum());
    ap.atom = AspAtom.parse(s);

    while(true){
      if(s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken){
        ap.suffixs.add(AspPrimarySuffix.parse(s));
      }else break;
    }

    leaveParser("primary");
    return ap;

  }

  @Override
  public void prettyPrint() {
     atom.prettyPrint();

     for (AspPrimarySuffix aps : suffixs){
       aps.prettyPrint();
     }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeValue v = atom.eval(curScope);
     if(suffixs.isEmpty()) return v;

     if(suffixs.get(0) instanceof AspArgument){
       RuntimeListValue args = (RuntimeListValue) suffixs.get(0).eval(curScope);
       RuntimeFunc func = (RuntimeFunc) v;
       trace("Call function " + ((AspName) atom).name + " with params " + args.toString());
       v = func.evalFuncCall(args.getValues(), this);
     }else{
       for (AspPrimarySuffix aps : suffixs){
         if (aps instanceof AspSubscription){
           RuntimeValue sub = aps.eval(curScope);
           v = v.evalSubscription(sub, this);
         }else RuntimeValue.runtimeError("Subscription error for: " + aps.toString(), this);
       }
     }
     return v;
  }
}
