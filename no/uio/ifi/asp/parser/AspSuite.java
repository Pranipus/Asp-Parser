package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspSuite extends AspSyntax{
  ArrayList<AspStmt> stmts = new ArrayList<>();
  AspSmallStmtList smallStmtList;

  AspSuite(int n){
    super(n);
  }

  static AspSuite parse(Scanner s){
    enterParser("suite");

    AspSuite as = new AspSuite(s.curLineNum());
    if (s.curToken().kind == newLineToken){
      skip(s, newLineToken);
      skip(s, indentToken);

      while(s.curToken().kind != dedentToken){
        as.stmts.add(AspStmt.parse(s));
      }
      skip(s, dedentToken);
    }else{
      as.smallStmtList = AspSmallStmtList.parse(s);
    }

    leaveParser("suite");
    return as;
  }

  @Override
  public void prettyPrint() {

     if(smallStmtList != null){
       smallStmtList.prettyPrint();
     }else{
       prettyWriteLn();
       prettyIndent();
       for (AspStmt as : stmts){
         as.prettyPrint();
       }
       prettyDedent();
     }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     if (stmts.size()>0){
       RuntimeValue v = stmts.get(0).eval(curScope);

       for (int i=1; i<stmts.size(); i++){
         v = stmts.get(i).eval(curScope);
       }
       return v;
     }else{
       RuntimeValue v = smallStmtList.eval(curScope);
       return v;
     }
  }
}
