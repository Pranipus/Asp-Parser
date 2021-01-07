package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFactorPrefix extends AspSyntax{
  TokenKind token;

  AspFactorPrefix(int n){
    super(n);
  }

  static AspFactorPrefix parse(Scanner s){
    enterParser("factor prefix");

    AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
    afp.token = s.curToken().kind;
    skip(s, s.curToken().kind);

    leaveParser("factor prefix");
    return afp;

  }

  @Override
  public void prettyPrint() {
     prettyWrite(token.toString());
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     //-- Must be changed in part 3:
      return null;
  }
}
