package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspAtom extends AspSyntax{
  static AspAtom aa = null;
  AspAtom(int n){
    super(n);
  }

  static AspAtom parse(Scanner s){
    enterParser("atom");

    switch(s.curToken().kind){
      case nameToken:
        aa = AspName.parse(s); break;
      case stringToken:
        aa = AspStringLiteral.parse(s); break;
      case integerToken:
        aa = AspIntegerLiteral.parse(s); break;
      case floatToken:
        aa = AspFloatLiteral.parse(s); break;
      case noneToken:
        aa = AspNoneLiteral.parse(s); break;
      case trueToken:
        aa = AspBooleanLiteral.parse(s); break;
      case falseToken:
        aa = AspBooleanLiteral.parse(s); break;
      case leftParToken:
        aa = AspInnerExpr.parse(s); break;
      case leftBracketToken:
        aa = AspListDisplay.parse(s); break;
      case leftBraceToken:
        aa = AspDictDisplay.parse(s); break;
    }

    leaveParser("atom");
    return aa;
  }
}
