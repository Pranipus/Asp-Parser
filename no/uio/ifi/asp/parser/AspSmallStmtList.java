package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspSmallStmtList extends AspStmt{
  ArrayList<AspSmallStmt> statements = new ArrayList<>();

  AspSmallStmtList (int n){
    super(n);
  }

  static AspSmallStmtList parse (Scanner s){
    enterParser("small stmt list");
    AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());

    while(s.curToken().kind != newLineToken){
      assl.statements.add(AspSmallStmt.parse(s));
      if(s.curToken().kind == semicolonToken) skip(s, semicolonToken);
    }

/*
    while(true){
      assl.statements.add(AspSmallStmt.parse(s));

      if (s.curToken().kind == semicolonToken){
        System.out.println("found semicolonToken");
        skip(s, semicolonToken);
      }
      if (s.curToken().kind == newLineToken) {
        System.out.println("found newLineToken");
        break;
      }
    }
*/
    skip(s, newLineToken);
    leaveParser("small stmt list");
    return assl;
  }

  @Override
  public void prettyPrint() {
     for (AspSmallStmt stmt : statements){
       stmt.prettyPrint();
     }
     prettyWriteLn();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     for(AspSmallStmt ass : statements){
       ass.eval(curScope);
     }
     return null;
  }
}
