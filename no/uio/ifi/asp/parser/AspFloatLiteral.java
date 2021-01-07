package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom{
  double value;

  AspFloatLiteral(int n){
    super(n);
  }

  static AspFloatLiteral parse(Scanner s){
    enterParser("float literal");
    AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());
    afl.value = s.curToken().floatLit;
    skip(s, floatToken);

    leaveParser("float literal");
    return afl;
  }

  @Override
  public void prettyPrint() {
     prettyWrite(String.valueOf(value));
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     //-- Must be changed in part 3:
      return new RuntimeFloatValue(value);
  }
}
