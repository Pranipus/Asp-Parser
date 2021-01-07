package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom{
  boolean value;

  AspBooleanLiteral(int n){
    super(n);
  }

  static AspBooleanLiteral parse(Scanner s){
    enterParser("boolean literal");

    AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
    if(s.curToken().kind == trueToken) {
      abl.value = true;
      skip(s, trueToken);
    }else{
      abl.value = false;
      skip(s, falseToken);
    }

    leaveParser("boolean literal");
    return abl;
  }

  @Override
  public void prettyPrint() {
     if(value) prettyWrite("True");
     else prettyWrite("False");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     return new RuntimeBoolValue(value);
  }
}
