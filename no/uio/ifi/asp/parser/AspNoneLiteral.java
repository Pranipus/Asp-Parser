package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNoneLiteral extends AspAtom{
  Object value;

  AspNoneLiteral(int n){
    super(n);
  }

  static AspNoneLiteral parse(Scanner s){
    enterParser("none literal");

    AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());
    anl.value = null;
    skip(s, noneToken);

    leaveParser("none literal");
    return anl;
  }

  @Override
  public void prettyPrint() {
     prettyWrite("None");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     //-- Must be changed in part 3:
      return new RuntimeNoneValue();
  }

}
