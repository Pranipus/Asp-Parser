package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
public abstract class AspPrimarySuffix extends AspSyntax{

  AspPrimarySuffix(int n){
    super(n);
  }

  static AspPrimarySuffix parse(Scanner s){
    enterParser("primary suffix");
    AspPrimarySuffix aps;

    if(s.curToken().kind == leftParToken) aps = AspArgument.parse(s);
    else aps = AspSubscription.parse(s);

    leaveParser("primary suffix");
    return aps;
  }
}
