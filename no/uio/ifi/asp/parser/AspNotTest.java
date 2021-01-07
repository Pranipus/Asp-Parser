package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspNotTest extends AspSyntax{
  AspComparison aspCompr;
  boolean not = false;

  AspNotTest(int n){
    super(n);
  }

  static AspNotTest parse(Scanner s){
    enterParser("not test");

    AspNotTest ant = new AspNotTest(s.curLineNum());
    if(s.curToken().kind == notToken) {
      ant.not = true;
      skip(s, notToken);
    }

    ant.aspCompr = AspComparison.parse(s);
    leaveParser("not test");
    return ant;
  }

  @Override
  public void prettyPrint() {
     if (not) prettyWrite(" not ");
     aspCompr.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeValue v = aspCompr.eval(curScope);
     if(not) v = v.evalNot(this);
     return v;
  }
}
