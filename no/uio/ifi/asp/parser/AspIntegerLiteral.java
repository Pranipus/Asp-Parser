package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIntegerLiteral extends AspAtom{
  long value;

  AspIntegerLiteral (int n){
    super(n);
  }

  static AspIntegerLiteral parse(Scanner s){
    enterParser("integer literal");

    AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());
    ail.value = s.curToken().integerLit;
    skip(s, integerToken);

    leaveParser("integer literal");
    return ail;
  }

  @Override
  public void prettyPrint() {
     prettyWrite(String.valueOf(value));
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     return new RuntimeIntValue(value);
  }
}
