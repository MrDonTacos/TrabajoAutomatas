package analizadorLexico
import static analizadorLexico.Tokens.*;
%%
%class Analizador
%type Tokens
L=[a-zA-Z_]+
D=[0-9]+
espacio=[ ,\t,\r,\n]+
%{
	public String lexeme;
%}
%%
"%".* {/*Ignore*/}
{"_" | [A-Z]+} ({L})* {return Variable;}
"=" {return Igualdad;}
"=:=" {return IgualdadAritmetica;}
"+" {return Suma;}
"*" {return Multiplicacion;}
"-" {return Resta;}
"/" {return Division;}
"=\=" {return Desigualdad;}
"<" {return Menor;}
">" {return Mayor;}
{L}(("(")({L}|{D})(")")({"."}))*(".") {return Oracion}

 