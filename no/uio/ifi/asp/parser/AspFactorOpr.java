package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;
public class AspFactorOpr extends AspSyntax{
  TokenKind op;

  AspFactorOpr(int n){
    super(n);
  }

  static AspFactorOpr parse(Scanner s){
    enterParser("factor operator");

    AspFactorOpr af = new AspFactorOpr(s.curLineNum());
    af.op = s.curToken().kind;
    skip(s, s.curToken().kind);

    leaveParser("factor operator");
    return af;

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
