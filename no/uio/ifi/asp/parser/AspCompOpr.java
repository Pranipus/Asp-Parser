package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.TokenKind.*;
public class AspCompOpr extends AspSyntax{
  TokenKind op;

  AspCompOpr(int n){
    super(n);
  }

  static AspCompOpr parse(Scanner s){
    enterParser("comparison operator");

    AspCompOpr aco = new AspCompOpr(s.curLineNum());
    aco.op = s.curToken().kind;
    skip(s, s.curToken().kind);

    leaveParser("comparison operator");
    return aco;

  }

  @Override
  public void prettyPrint() {
     prettyWrite(" " + op.toString() + " ");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     RuntimeValue v = null;
     return v;
  }
}
