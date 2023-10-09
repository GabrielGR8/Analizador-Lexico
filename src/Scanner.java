import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    private static final Map<String, TipoToken> caracteresReservados;

    private static final Map<String, TipoToken> doblesCaracteres;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);

        caracteresReservados = new HashMap<>();
        caracteresReservados.put("<", TipoToken.LESS);
        caracteresReservados.put(">", TipoToken.GREATER);
        caracteresReservados.put("!", TipoToken.BANG);
        caracteresReservados.put("=", TipoToken.EQUAL);
        caracteresReservados.put("+", TipoToken.PLUS);
        caracteresReservados.put("-", TipoToken.MINUS);
        caracteresReservados.put("*", TipoToken.STAR);
        caracteresReservados.put("/", TipoToken.SLASH);
        caracteresReservados.put("{", TipoToken.LEFT_BRACE);
        caracteresReservados.put("}", TipoToken.RIGHT_BRACE);
        caracteresReservados.put("(", TipoToken.LEFT_PAREN);
        caracteresReservados.put(")", TipoToken.RIGHT_PAREN);
        caracteresReservados.put(",", TipoToken.COMMA);
        caracteresReservados.put(".", TipoToken.DOT);
        caracteresReservados.put(";", TipoToken.SEMICOLON);

        doblesCaracteres = new HashMap<>();
        doblesCaracteres.put("<=", TipoToken.LESS_EQUAL);
        doblesCaracteres.put(">=", TipoToken.GREATER_EQUAL);
        doblesCaracteres.put("==", TipoToken.EQUAL_EQUAL);
        doblesCaracteres.put("!=", TipoToken.BANG_EQUAL);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        int estado = 0;
        String lexema = "";
        String aux = "";
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);

            switch (estado){
                case 0:
                    if(c == '<' || c == '>' || c == '=' || c == '!'){
                        estado = 1;
                        lexema += c;
                    }
                    else if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '"'){
                        estado = 24;
                        lexema+=c;
                    }
                    else if(c == '/'){
                        estado = 26;
                    }
                    else{
                        lexema+=c;
                        TipoToken tt = caracteresReservados.get(lexema);
                        if(tt != null){
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }
                        else if(c!= ' ' && c!= '\n' && c!= '\t' && c!= '\r'){
                            Interprete.error(0, "Se introdujo un caracter no valido: "+c);
                            return null;
                        }
                        lexema = "";
                    }
                    break;
                case 1:
                    if(c == '='){
                        lexema += c;
                        TipoToken tt = doblesCaracteres.get(lexema);
                        Token t = new Token(tt, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        TipoToken tt = caracteresReservados.get(lexema);
                        Token t = new Token(tt, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 13:
                    if(Character.isLetterOrDigit(c)){
                        lexema += c;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;

                    }
                    break;

                case 15:
                    if(Character.isDigit(c)){
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado = 16;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 16:
                    if(!Character.isDigit(c)){
                        Interprete.error(16, "No se acepta este numero porque no esta completo");
                        return null;
                    }
                    else{
                        lexema += c;
                        estado = 17;
                    }
                    break;
                case 17:
                    if(c == 'E'){
                        lexema+=c;
                        estado = 18;
                    }
                    else if(!Character.isDigit(c)){
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    else{
                        lexema += c;
                    }
                    break;
                case 18:
                    if(c == '+' || c == '-'){
                        lexema += c;
                        estado = 19;
                    }
                    else if(Character.isDigit(c)){
                        lexema += c;
                        estado = 20;
                    }
                    else{
                        Interprete.error(18, "No se introdujo un numero valido");
                        return null;
                    }
                    break;
                case 19:
                    if(Character.isDigit(c)){
                        lexema += c;
                        estado = 20;
                    }
                    else{
                        Interprete.error(19, "No se introdujo un numero valido");
                        return null;
                    }
                    break;
                case 20:
                    if(Character.isDigit(c)){
                        lexema += c;
                    }
                    else{
                        DecimalFormat formateador = new DecimalFormat("####0.###############");
                        double num = Double.parseDouble(lexema);
                        Token t = new Token(TipoToken.NUMBER, lexema, formateador.format(num));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 24:
                    if(c == '\n'  || c == '\r'){
                        Interprete.error(24, "No se admiten saltos de linea en los strings");
                        return null;
                    }
                    else if(c != '"'){
                        lexema += c;
                        aux += c;
                    }
                    else{
                        lexema +=c;
                        Token t = new Token(TipoToken.STRING, lexema, aux);
                        tokens.add(t);
                        lexema = "";
                        aux = "";
                        estado = 0;
                    }
                    break;
                case 26:
                    if(c == '*'){
                        estado = 27;
                    }
                    else if(c == '/'){
                        estado = 30;
                    }
                    else{
                        Token t = new Token(TipoToken.SLASH, "/");
                        tokens.add(t);
                        estado = 0;
                        i--;
                    }
                    break;
                case 27:
                    if(c == '*'){
                        estado = 28;
                    }
                    break;
                case 28:
                    if(c == '/'){
                        estado = 0;
                    }
                    else if(c != '*'){
                        estado = 27;
                    }
                    break;
                case 30:
                    if(c == '\n'){
                        estado = 0;
                    }
                    break;
            }


        }

        if(estado != 0){
            Interprete.error(-1, "Hay un error en la ultima linea, no se puede terminar de identificar el lexema");
            return null;
        }
        else{
            return tokens;
        }

    }
}
