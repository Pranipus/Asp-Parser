package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;
import javafx.util.Pair;
import no.uio.ifi.asp.main.*;

public class AspFactor extends AspSyntax{
  ArrayList <Pair<AspFactorPrefix, AspPrimary> > prefixNPrimary = new ArrayList<>();
  ArrayList <AspFactorOpr> operators = new ArrayList<>();



  AspFactor(int n){
    super(n);
  }

  static AspFactor parse(Scanner s){
    enterParser("factor");

    AspFactor af = new AspFactor(s.curLineNum());

    while(true){
      AspFactorPrefix afp = null;
      if(s.isFactorPrefix()) afp = AspFactorPrefix.parse(s);

      af.prefixNPrimary.add(new Pair(afp, AspPrimary.parse(s)));

      if(s.isFactorOpr()) af.operators.add(AspFactorOpr.parse(s));
      else break;
    }


    leaveParser("factor");
    return af;

  }

  @Override
  public void prettyPrint() {
     int nPrinted = 0;

     for (Pair<AspFactorPrefix, AspPrimary> ap : prefixNPrimary){
       if(nPrinted > 0) operators.get(nPrinted-1).prettyPrint();
       if(ap.getKey() != null) ap.getKey().prettyPrint();
       ap.getValue().prettyPrint();
       ++nPrinted;
     }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    //getting primary
     RuntimeValue v = prefixNPrimary.get(0).getValue().eval(curScope);
     if (prefixNPrimary.get(0).getKey() != null){
       TokenKind k = prefixNPrimary.get(0).getKey().token;
       switch(k){
         case plusToken:
           v = v.evalPositive(this); break;
         case minusToken:
           v = v.evalNegate(this); break;
         default:
           Main.panic("Illegal prefix: " + k + "!");
       }
     }
     for (int i=1; i<prefixNPrimary.size(); i++){
       TokenKind k = operators.get(i-1).op;
       switch (k){
         case astToken:
           v = v.evalMultiply(prefixNPrimary.get(i).getValue().eval(curScope), this); break;
         case slashToken:
           v = v.evalDivide(prefixNPrimary.get(i).getValue().eval(curScope), this); break;
         case percentToken:
           v = v.evalModulo(prefixNPrimary.get(i).getValue().eval(curScope), this); break;
         case doubleSlashToken:
           v = v.evalIntDivide(prefixNPrimary.get(i).getValue().eval(curScope), this); break;
         default:
           Main.panic("Illegal factor operator: " + k + "!");
       }
     }
     return v;
  }


}
