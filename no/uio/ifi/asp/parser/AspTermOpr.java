package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;

public class AspTermOpr extends AspSyntax{
  TokenKind op;

  AspTermOpr(int n){
    super(n);
  }

  static AspTermOpr parse(Scanner s){
    enterParser("term operator");

    AspTermOpr ato = new AspTermOpr(s.curLineNum());
    ato.op = s.curToken().kind;
    skip(s, s.curToken().kind);

    leaveParser("term operator");
    return ato;

  }

  @Override
  public void prettyPrint() {
     prettyWrite(" " + op.toString() + " ");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     //-- Must be changed in part 3:
      return null;
  }
}
