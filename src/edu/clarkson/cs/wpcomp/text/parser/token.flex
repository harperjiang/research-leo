package edu.clarkson.cs.wpcomp.text.parser;

import org.apache.commons.lang3.StringUtils;
import edu.clarkson.cs.wpcomp.text.model.Token;

%%

%class Lexer
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

public Token stopToken() {
	Token token = token();
	if(null!= token) {
		token.setType(Token.Type.STOP);
		return token;
	} else {
		return Token.STOP;
	}
}

%}

%%
"."					{ return stopToken();}
[,\":;”]+			{ return stopToken();}
[\ \t\b\f\r\n]+ 	{ return token();	}

.       			{ append(yytext()); }