package no.uio.ifi.asp.runtime;
import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import java.util.HashMap;
import java.util.Map;

public class RuntimeDictValue extends RuntimeValue{
  HashMap<String, RuntimeValue> dictValues;

  public RuntimeDictValue(){
    dictValues = new HashMap<>();
  }

  @Override
  public String typeName(){
    return "dictionary";
  }

  public void addValue(RuntimeValue key, RuntimeValue value, AspSyntax where){
    if(key instanceof RuntimeStringValue) dictValues.put(key.getStringValue("adding to dict", where), value);
    else Main.panic("invalid key to dictionary");
  }

  public String toString(){
    String toRet = "{";
    int nPrinted = 0;
    for (Map.Entry<String, RuntimeValue> entry : dictValues.entrySet()){
      if (nPrinted > 0) toRet += ", ";
      toRet += entry.getKey();
      toRet += " : ";
      toRet += entry.getValue().toString();
      nPrinted++;
    }
    return toRet+="}";
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){
    if(v instanceof RuntimeStringValue){
      String sub = v.getStringValue("dict subscription", where);

      if(dictValues.get(sub) == null) runtimeError("key:" +sub+ " not in dictionary", where);
      else return dictValues.get(sub);
    }
    runtimeError("Unexpected value for subscription", where);
    return null;
  }

  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue v, AspSyntax where){
    if (inx instanceof RuntimeStringValue){
      if (dictValues.containsKey(inx.toString()))
        dictValues.replace(inx.toString(), v);
      else addValue(inx, v, where);
    } else runtimeError("Expected String but found " + inx.typeName(), where);
  }


}
