package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue{
  String stringValue;

  public RuntimeStringValue(String s){
    stringValue = s;
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){

    if (v instanceof RuntimeStringValue)
      return new RuntimeStringValue(stringValue + v.getStringValue("+ operand", where));

    runtimeError("Type error for +", where);
    return null;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue){
      long i = v.getIntValue("* operand", where);
      String toRet = "'";
      while(i > 0){
        toRet += stringValue;
        i--;
      }
      toRet += "'";
      this.stringValue = toRet;
      return this;
    }




    runtimeError("Type error for *.", where);
    return null;
  }

  @Override
  public String typeName(){
    return "String";
  }


  @Override
  public String showInfo(){
    String toRet = "'";
    toRet += stringValue;

    return toRet+="'";
  }

@Override
public String toString(){
  return stringValue;
}
  @Override
  public String getStringValue(String what, AspSyntax where) {
    return stringValue;
    }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeStringValue)
      if (stringValue.equals(v.toString()))
        return new RuntimeBoolValue(true);

    return new RuntimeBoolValue(false);
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where){
    if (stringValue == "") return false;
    else return true;
  }

  @Override
  public long getIntValue(String what, AspSyntax where){
    try{
      return Long.parseLong(stringValue);
    } catch(NumberFormatException e){
      runtimeError("String doesn't contain a legal integer value.", where);
      return 0;
    }
  }

  @Override
  public double getFloatValue(String what, AspSyntax where){
    try{
      return Double.parseDouble(stringValue);
    } catch(NumberFormatException e){
      runtimeError("String doesn't contain a legal float value.", where);
      return 0.0;
    }
  }
}
