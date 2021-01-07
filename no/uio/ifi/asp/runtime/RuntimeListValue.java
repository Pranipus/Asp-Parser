package no.uio.ifi.asp.runtime;
import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue{
  ArrayList<RuntimeValue> listValues;

  public RuntimeListValue(){
    listValues = new ArrayList<>();
  }

  public String toString(){
    String toRet = "[";
    int nPrinted = 0;

    for(RuntimeValue v : listValues){
      if(nPrinted > 0) toRet += ", ";
      toRet += v.toString(); nPrinted++;
    }

    return toRet+="]";
  }
  @Override
  public String typeName(){
    return "list";
  }

  public void addValue(RuntimeValue v){
    listValues.add(v);
  }

  public ArrayList<RuntimeValue> getValues(){
    return listValues;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue){
      ArrayList<RuntimeValue> toRet = new ArrayList<>();

      for (int i=0; i<v.getIntValue("* operand", where); i++){
        for (RuntimeValue e : listValues){
          toRet.add(e);
        }
      }
      listValues = toRet;
      return this;
    }else runtimeError("Unexpected value", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){

    if (v instanceof RuntimeIntValue){
      int i = (int) v.getIntValue("subscription", where);
      if(i >= listValues.size()) runtimeError("Index out of bounds", where);
      else return listValues.get(i);
    }
    runtimeError("Subscription must contain int", where);
    return null;
  }


  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where){
    int index = (int) inx.getIntValue("evalAssignElem", where);
    listValues.set(index, val);
  }


  @Override
  public RuntimeValue evalLen(AspSyntax where){
    return new RuntimeIntValue(listValues.size());
  }

}
