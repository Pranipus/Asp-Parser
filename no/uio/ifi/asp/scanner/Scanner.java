package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
	     curFileName = fileName;
	      indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
	String m = "Asp scanner error";
	if (curLineNum() > 0)
	    m += " on line " + curLineNum();
	m += ": " + message;

	Main.error(m);
    }


    public Token curToken() {
	while (curLineTokens.isEmpty()) {
	    readNextLine();
	}
	return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }


    private void readNextLine() {
	curLineTokens.clear();

	// Read the next line:
	String line = null;
	try {
	    line = sourceFile.readLine();
	    if (line == null) {
			while (indents.peek() > 0){
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
			Token eof = new Token(eofToken);
			curLineTokens.add(eof);
			Main.log.noteToken(eof);

			sourceFile.close();
			sourceFile = null;
			return;
	    } else {
			Main.log.noteSourceLine(curLineNum(), line);
	    }
	} catch (IOException e) {
	    sourceFile = null;
	    scannerError("Unspecified I/O error!");
	}

	//-- Must be changed in part 1:
	if(line.trim().isEmpty()) return;
	if(line.charAt(0) == '#') return;
	line = expandLeadingTabs(line);
	int curIndent = findIndent(line);

	if(curIndent > indents.peek()){
		indents.push(curIndent);
		curLineTokens.add(new Token(indentToken, curLineNum()));
	}

	while(curIndent < indents.peek()){
		indents.pop();
		curLineTokens.add(new Token(dedentToken, curLineNum()));
	}

	if(curIndent != indents.peek()) scannerError("Indentation error");


	for (int i=0; i<line.length(); i++){
		char c = line.charAt(i);

		switch(c){

      case '#':
        if (!curLineTokens.isEmpty()) curLineTokens.add(new Token (newLineToken, curLineNum()));
        return;

			case ' ':
				break;

			case '*':
				curLineTokens.add(new Token(astToken, curLineNum()));
				break;

			case '(':
				curLineTokens.add(new Token(leftParToken, curLineNum()));
				break;

			case ')':
				curLineTokens.add(new Token(rightParToken, curLineNum()));
				break;

			case '{':
				curLineTokens.add(new Token(leftBraceToken, curLineNum()));
				break;

			case '}':
				curLineTokens.add(new Token(rightBraceToken, curLineNum()));
				break;

			case '[':
				curLineTokens.add(new Token(leftBracketToken, curLineNum()));
				break;

			case ']':
				curLineTokens.add(new Token(rightBracketToken, curLineNum()));
				break;

			case ':':
				curLineTokens.add(new Token(colonToken, curLineNum()));
				break;

			case ';':
				curLineTokens.add(new Token(semicolonToken, curLineNum()));
				break;

			case ',':
				curLineTokens.add(new Token(commaToken, curLineNum()));
				break;

			case '+':
				curLineTokens.add(new Token(plusToken, curLineNum()));
				break;

			case '%':
				curLineTokens.add(new Token(percentToken, curLineNum()));
				break;

			case '-':
				curLineTokens.add(new Token(minusToken, curLineNum()));
				break;


			case '>':
				Token greaTok = null;
				if (i+1 < line.length() && line.charAt(i+1) == '='){
					greaTok = new Token(greaterEqualToken, curLineNum());
					i++;
				}else{
					greaTok = new Token(greaterToken, curLineNum());
				}
				curLineTokens.add(greaTok);
				break;

			case '<':
				Token lessTok = null;
				if (i+1 < line.length() && line.charAt(i+1) == '='){
					lessTok = new Token(lessEqualToken, curLineNum());
					i++;
				}else{
					lessTok = new Token(lessToken, curLineNum());
				}
				curLineTokens.add(lessTok);
				break;

			case '/':
				Token slashTok = null;
				if (i+1 < line.length() && line.charAt(i+1) == '/'){
					slashTok = new Token(doubleSlashToken, curLineNum());
					i++;
				}else{
					slashTok = new Token(slashToken, curLineNum());
				}
				curLineTokens.add(slashTok);
				break;

			case '=':
				Token eqToken = null;
				if (i+1 < line.length() && line.charAt(i+1) == '='){
					eqToken = new Token(doubleEqualToken, curLineNum());
					i++;
				}else{
					eqToken = new Token(equalToken, curLineNum());
				}
				curLineTokens.add(eqToken);
				break;

			case '!':

				if (i+1 < line.length() && line.charAt(i+1) == '='){
					curLineTokens.add(new Token(notEqualToken, curLineNum()));
					break;
				} else scannerError("single '!'' detected");

			case '\'':
				String stringLiteral = "";
				i++;
				while (i<line.length() && line.charAt(i) != '\''){

					stringLiteral = stringLiteral + line.charAt(i);
					i++;
				}
				Token stringT = new Token(stringToken, curLineNum());
				stringT.stringLit = stringLiteral;
				curLineTokens.add(stringT);
				break;

				case '\"':
				String stringLiteral2 = "";
				i++;
				while (i<line.length() && line.charAt(i) != '\"'){

					stringLiteral2 = stringLiteral2 + line.charAt(i);
					i++;
				}
				Token stringT2 = new Token(stringToken, curLineNum());
				stringT2.stringLit = stringLiteral2;
				curLineTokens.add(stringT2);
				break;

			default:
			if(isLetterAZ(c)){
				String nameLiteral = "";
				while(i<line.length() && (isLetterAZ(line.charAt(i)) || isDigit(line.charAt(i)))){
					nameLiteral = nameLiteral + line.charAt(i);
					i++;
				}
				i--;
				Token t = new Token(nameToken, curLineNum());
				t.name = nameLiteral;
				t.checkResWords();
				curLineTokens.add(t);

			} else if(isDigit(c)){
				String numberLiteral = "";
				while(i<line.length() && (isDigit(line.charAt(i)) || line.charAt(i) == '.')){
					numberLiteral = numberLiteral + line.charAt(i);
					i++;
				}
					i--;
				boolean dotFound = false;
				for (int j=0; j<numberLiteral.length(); j++){
					char cur = numberLiteral.charAt(j);
					if (cur == '.'){
						if(!dotFound){
							dotFound = true;
						} else {
							scannerError("invalid float");
							return;
						}
					}
				}
				if(dotFound){
					Token t = new Token(floatToken, curLineNum());
					t.floatLit = Double.parseDouble(numberLiteral);
					curLineTokens.add(t);
				} else{
					Token t = new Token(integerToken, curLineNum());
					t.integerLit = Long.parseLong(numberLiteral);
					curLineTokens.add(t);
				}

			}
		}
	}



	// Terminate line:
	curLineTokens.add(new Token(newLineToken,curLineNum()));

	for (Token t: curLineTokens)
	    Main.log.noteToken(t);
    }

    public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

    private String expandLeadingTabs(String s) {
	String newS = "";
	for (int i = 0;  i < s.length();  i++) {
	    char c = s.charAt(i);
	    if (c == '\t') {
		do {
		    newS += " ";
		} while (newS.length()%TABDIST > 0);
	    } else if (c == ' ') {
		newS += " ";
	    } else {
		newS += s.substring(i);
		break;
	    }
	}
	return newS;
    }


    private boolean isLetterAZ(char c) {
	return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
	     TokenKind k = curToken().kind;
       if(k == lessToken || k == greaterToken ||
       k == doubleEqualToken || k == greaterEqualToken ||
       k == lessEqualToken || k == notEqualToken) {
         return true;
       } else return false;
     }


    public boolean isFactorPrefix() {
	     TokenKind k = curToken().kind;
       if(k == plusToken || k == minusToken){
         return true;
       }else return false;
     }


    public boolean isFactorOpr() {
      TokenKind k = curToken().kind;
      if(k == astToken || k == slashToken || k == percentToken || k == doubleSlashToken){
        return true;
      }else return false;
    }


    public boolean isTermOpr() {
	     TokenKind k = curToken().kind;
       if(k == plusToken || k == minusToken){
         return true;
       }else return false;
     }


    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
