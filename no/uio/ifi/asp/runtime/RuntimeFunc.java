package no.uio.ifi.asp.runtime;
import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;

public class RuntimeFunc extends RuntimeValue{
  AspFuncDef def;
  RuntimeScope defScope;

  public RuntimeFunc(AspSyntax where, RuntimeScope scope){
    def = (AspFuncDef) where;
    defScope = scope;
  }

  public RuntimeFunc(String libraryfunc){

  }


  @Override
  public String typeName(){
    return "func";
  }

  @Override
  public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> args, AspSyntax where){
    if (args.size() != def.args.size()) {
      RuntimeValue.runtimeError("Inaccurate number of parameters.", where);
      return null;
    }
    RuntimeScope nScope = new RuntimeScope(defScope);
    for (int i=0; i<args.size(); i++){
      String formalName = def.args.get(i).name;
      nScope.assign(formalName, args.get(i));
    }

    try{
      def.body.eval(nScope);
    } catch (RuntimeReturnValue rrv){
      return rrv.value;
    }
    return new RuntimeNoneValue();
  }

}
