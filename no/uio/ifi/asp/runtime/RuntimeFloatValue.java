package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue{
  double floatValue;

  public RuntimeFloatValue(double v){
    floatValue = v;
  }

  public String toString(){
    return Double.toString(floatValue);
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeFloatValue)
      return new RuntimeFloatValue(floatValue + v.getFloatValue("+ operand", where));
    else if (v instanceof RuntimeIntValue)
      return new RuntimeFloatValue(floatValue + v.getIntValue("+ operand", where));

    runtimeError("Type error for '+'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue)
      return new RuntimeFloatValue(floatValue - v.getIntValue("- operand", where));
    else if (v instanceof RuntimeFloatValue)
      return new RuntimeFloatValue(floatValue - v.getFloatValue("- operand", where));

    runtimeError("Type error for '-'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue)
      return new RuntimeFloatValue(floatValue * (double)v.getIntValue("* operand", where));
    else if (v instanceof RuntimeFloatValue)
      return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));

    runtimeError("Type error for '*'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue)
      return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
    runtimeError("Type error for '/'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue)
      return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
    runtimeError("Type error for '//'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){

    if (v instanceof RuntimeIntValue){
      return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)
        * Math.floor(floatValue / v.getFloatValue("% operand", where)));

    } else if (v instanceof RuntimeFloatValue){
      return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)
        * Math.floor(floatValue / v.getFloatValue("% operand", where)));
    }
    runtimeError("Type error for '%'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeFloatValue || v instanceof RuntimeIntValue){
      if (floatValue == v.getFloatValue("== operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    runtimeError("Type error for '=='.", where);
    return null;
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue){
      if (floatValue < v.getFloatValue("< operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    runtimeError("Type error for '<'.", where);
    return null;
  }

  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue){
      if (floatValue <= v.getFloatValue("<= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    runtimeError("Type error for '<='.", where); return null;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue){
      if (floatValue > v.getFloatValue("> operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    runtimeError("Type error for '>'.", where); return null;
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
    if (v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue){
      if (floatValue >= v.getFloatValue(">= operand", where)) return new RuntimeBoolValue(true);
      else return new RuntimeBoolValue(false);
    }
    runtimeError("Type error for '>='.", where); return null;
  }


  @Override
  public RuntimeValue evalNegate(AspSyntax where){
    floatValue = floatValue * -1;
    return this;
  }

  @Override
  public RuntimeValue evalPositive(AspSyntax where){
    floatValue = Math.abs(floatValue);
    return this;
  }
  @Override
  public String typeName(){
    return "Float";
  }

  @Override
  public double getFloatValue(String what, AspSyntax where){
    return floatValue;
  }

  @Override
  public long getIntValue(String what, AspSyntax where){
    return (long) floatValue;
  }

  @Override
  public String getStringValue(String what, AspSyntax where){
    return Double.toString(floatValue);
  }
}
