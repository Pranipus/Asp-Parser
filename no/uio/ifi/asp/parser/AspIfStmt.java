package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import javafx.util.Pair;
import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt{
  Pair <AspExpr, AspSuite> ifStmt;
  ArrayList <Pair <AspExpr, AspSuite> > elifs = new ArrayList<>();
  AspSuite elseStmt;

  AspIfStmt(int n){
    super(n);
  }

  static AspIfStmt parse(Scanner s){
    enterParser("if stmt");

    AspIfStmt ais = new AspIfStmt(s.curLineNum());
    skip(s, ifToken);
    AspExpr expr = AspExpr.parse(s);
    skip(s, colonToken);
    AspSuite body = AspSuite.parse(s);

    ais.ifStmt = new Pair(expr, body);


    if(s.curToken().kind == elifToken){
      while(s.curToken().kind ==  elifToken){
        skip(s, elifToken);
        AspExpr expr2 = AspExpr.parse(s);
        skip(s, colonToken);
        AspSuite body2 = AspSuite.parse(s);
        ais.elifs.add(new Pair(expr2, body2));
      }
    }

    if(s.curToken().kind == elseToken){
      skip(s, elseToken);
      skip(s, colonToken);
      ais.elseStmt = AspSuite.parse(s);
    }

    leaveParser("if stmt");
    return ais;
  }

  @Override
  public void prettyPrint() {
    prettyWrite("if ");
    ifStmt.getKey().prettyPrint();
    prettyWrite(" : ");
    ifStmt.getValue().prettyPrint();

    for (Pair<AspExpr, AspSuite> p : elifs){
      prettyWrite("elif ");
      p.getKey().prettyPrint();
      prettyWrite(" : ");
      p.getValue().prettyPrint();
    }

    if(elseStmt != null){
      prettyWrite("else :");
      elseStmt.prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
      prettyPrint();
     if(ifStmt.getKey().eval(curScope).getBoolValue("if-stmt", this)){

       trace("if True alt #1: ...");
       ifStmt.getValue().eval(curScope);
       return null;
     }

     int nElif = 2;
     for (Pair <AspExpr,AspSuite> p : elifs){
       if(p.getKey().eval(curScope).getBoolValue("elif-stmt", this)){
         trace("if True alt #" + nElif + ": ...");
         p.getValue().eval(curScope);
         nElif++;
         return null;
       }
     }

     if(elseStmt != null){
       trace ("else: ...");
       elseStmt.eval(curScope);
     }
     return null;
  }

}
