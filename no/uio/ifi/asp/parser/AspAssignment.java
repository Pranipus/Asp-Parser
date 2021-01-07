package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt{
  AspName name;
  ArrayList<AspSubscription> subscriptions = new ArrayList<>();
  AspExpr expr;

  AspAssignment(int n){
    super(n);
  }

  static AspAssignment parse(Scanner s){
    enterParser("assignment");
    AspAssignment aa = new AspAssignment(s.curLineNum());

    aa.name = AspName.parse(s);

    while(s.curToken().kind != equalToken){
      aa.subscriptions.add(AspSubscription.parse(s));
    }

    skip(s, equalToken);
    aa.expr = AspExpr.parse(s);

    leaveParser("assignment");
    return aa;
  }

  @Override
  public void prettyPrint() {
     name.prettyPrint();
     for (AspSubscription as : subscriptions){
       as.prettyPrint();
     }
     prettyWrite(" = ");
     expr.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    String varName = name.name;
    RuntimeValue to = expr.eval(curScope);
    if (subscriptions.isEmpty()){
      trace(varName + " = " + to);
      curScope.assign(varName, to);
    }
    else if (subscriptions.size() == 1){

      RuntimeValue v = curScope.find(name.name, this);
      RuntimeValue sub = subscriptions.get(0).eval(curScope);

      trace(varName + "[" + sub + "]" + " = " + to.showInfo());
      v.evalAssignElem(sub, to, this);

    }else if (subscriptions.size() > 1){
      RuntimeValue v = curScope.find(name.name, this);
      for (int i=0; i<subscriptions.size()-1; i++){
        RuntimeValue sub = subscriptions.get(i).eval(curScope);
        v = v.evalSubscription(sub, this);
      }
      RuntimeValue lastSub = subscriptions.get(subscriptions.size()-1).eval(curScope);
      trace("multilength subscription: " + varName + " to " + to);
      v.evalAssignElem(lastSub, to, this);

    }else RuntimeValue.runtimeError("Subscription error", this);
    return null;
  }

}
