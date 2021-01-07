package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom{
  String string;

  AspStringLiteral(int n){
    super(n);
  }

  static AspStringLiteral parse(Scanner s){
    enterParser("string literal");

    AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
    asl.string = s.curToken().stringLit;
    skip(s, stringToken);

    leaveParser("string literal");
    return asl;
  }

  @Override
  public void prettyPrint() {
     prettyWrite('"'+string+'"');
  }

  @Override
  public String toString(){
    return string;
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     //-- Must be changed in part 3:
      return new RuntimeStringValue(string);
  }
}
