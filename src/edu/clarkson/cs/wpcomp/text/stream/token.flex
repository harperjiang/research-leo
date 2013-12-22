package edu.clarkson.cs.wpcomp.text.stream;

import org.apache.commons.lang3.StringUtils;
import edu.clarkson.cs.wpcomp.text.Token;

%%

%class Parser
%public
%type Token

%eofval{ 
	return Token.EOF;
%eofval}
%eofclose

%{
java.lang.StringBuilder buffer;

public void append(java.lang.String data) {
	if(buffer == null) 
		buffer = new java.lang.StringBuilder();
	buffer.append(data);
}

public Token token() {
	if(buffer == null)
		return null;
	java.lang.String data = buffer.toString();
	buffer = null;
	return StringUtils.isEmpty(data.trim())?null:new Token(data.trim());
}

%}

%%

[,\.\":;]+			{ return token();	}
[\ \t\b\f\r\n]+ 	{ append(" ");		}
.       			{ append(yytext()); }