import java.io.FileNotFoundException;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int i = 0;
    private Token preanalisis;

    private boolean hayErrores = false;
    private Tabla tabla;

    // ...
    // ...
    // ...

    public Parser(List<Token> tokens){
        this.tokens = tokens;
        this.tokens.add(new Token(TipoToken.EOF, "$"));
        preanalisis = this.tokens.get(i);
    }

    public boolean parse() throws ParserException, FileNotFoundException {
        tabla = new Tabla();
        program();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

   private void program() throws ParserException {
        switch (tabla.BuscarEnTabla(0, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                declaration();
                break;
        }
   }

    private void declaration() throws ParserException {
        switch (tabla.BuscarEnTabla(1, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                fun_decl();
                declaration();
                break;
            case 2:
                var_decl();
                declaration();
                break;
            case 3:
                statement();
                declaration();
                break;
            case 4:
                break;
        }
    }

    private void fun_decl() throws ParserException {
        switch (tabla.BuscarEnTabla(2, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.FUN);
                func();
                break;
        }
    }

    private void var_decl() throws ParserException {
        switch (tabla.BuscarEnTabla(3, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.VAR);
                match(TipoToken.IDENTIFIER);
                var_init();
                match(TipoToken.SEMICOLON);
                break;
        }
    }

    private void var_init() throws ParserException {
        switch (tabla.BuscarEnTabla(4, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.EQUAL);
                expression();
                break;
            case 2:
                break;
        }
    }

    private void statement() throws ParserException {
        switch (tabla.BuscarEnTabla(5, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                expr_stmt();
                break;
            case 2:
                for_stmt();
                break;
            case 3:
                if_stmt();
                break;
            case 4:
                print_stmt();
                break;
            case 5:
                return_stmt();
                break;
            case 6:
                while_stmt();
                break;
            case 7:
                block();
                break;
        }
    }

    private void expr_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(6, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                expression();
                match(TipoToken.SEMICOLON);
                break;
        }
    }

    private void for_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(7, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.FOR);
                match(TipoToken.LEFT_PAREN);
                for_stmt_1();
                for_stmt_2();
                for_stmt_3();
                match(TipoToken.RIGHT_PAREN);
                statement();
                break;
        }
    }

    private void for_stmt_1() throws ParserException {
        switch (tabla.BuscarEnTabla(8, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                var_decl();
                break;
            case 2:
                expr_stmt();
                break;
            case 3:
                match(TipoToken.SEMICOLON);
                break;
        }
    }

    private void for_stmt_2() throws ParserException {
        switch (tabla.BuscarEnTabla(9, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                expression();
                match(TipoToken.SEMICOLON);
                break;
            case 2:
                match(TipoToken.SEMICOLON);
                break;
        }
    }

    private void for_stmt_3() throws ParserException {
        switch (tabla.BuscarEnTabla(10, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                expression();
                break;
            case 2:
                break;
        }
    }

    private void if_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(11, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.IF);
                match(TipoToken.LEFT_PAREN);
                expression();
                match(TipoToken.RIGHT_PAREN);
                statement();
                else_stmt();
                break;
        }
    }

    private void else_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(12, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.ELSE);
                statement();
                break;
            case 2:
                break;
        }
    }

    private void print_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(13, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.PRINT);
                expression();
                match(TipoToken.SEMICOLON);
                break;
        }
    }

    private void return_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(14, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.RETURN);
                return_exp_opc();
                match(TipoToken.SEMICOLON);
                break;
        }
    }

    private void return_exp_opc() throws ParserException {
        switch (tabla.BuscarEnTabla(15, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                expression();
                break;
            case 2:
                break;
        }
    }

    private void while_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(16, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.WHILE);
                match(TipoToken.LEFT_PAREN);
                expression();
                match(TipoToken.RIGHT_PAREN);
                statement();
                break;
        }
    }

    private void block() throws ParserException {
        switch (tabla.BuscarEnTabla(17, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.LEFT_BRACE);
                declaration();
                match(TipoToken.RIGHT_BRACE);
                break;
        }
    }

    private void expression() throws ParserException {
        switch (tabla.BuscarEnTabla(18, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                assignment();
                break;
        }
    }

    private void assignment() throws ParserException {
        switch (tabla.BuscarEnTabla(19, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                logic_or();
                assignment_opc();
                break;
        }
    }

    private void logic_or() throws ParserException {
        switch (tabla.BuscarEnTabla(20, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                logic_and();
                logic_or_2();
                break;
        }
    }

    private void logic_and() throws ParserException {
        switch (tabla.BuscarEnTabla(21, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                equality();
                logic_and_2();
                break;
        }
    }

    private void logic_or_2() throws ParserException {
        switch (tabla.BuscarEnTabla(22, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.OR);
                logic_and();
                logic_or_2();
                break;
            case 2:
                break;
        }
    }

    private void assignment_opc() throws ParserException {
        switch (tabla.BuscarEnTabla(23, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.EQUAL);
                expression();
                break;
            case 2:
                break;
        }
    }

    private void logic_and_2() throws ParserException {
        switch (tabla.BuscarEnTabla(24, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.AND);
                equality();
                logic_and_2();
                break;
            case 2:
                break;
        }
    }

    private void equality() throws ParserException {
        switch (tabla.BuscarEnTabla(25, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                comparison();
                equality_2();
                break;
        }
    }

    private void equality_2() throws ParserException {
        switch (tabla.BuscarEnTabla(26, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.BANG_EQUAL);
                comparison();
                equality_2();
                break;
            case 2:
                match(TipoToken.EQUAL_EQUAL);
                comparison();
                equality_2();
                break;
            case 3:
                break;
        }
    }

    private void comparison() throws ParserException {
        switch (tabla.BuscarEnTabla(27, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                term();
                comparison_2();
                break;
        }
    }

    private void comparison_2() throws ParserException {
        switch (tabla.BuscarEnTabla(28, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.GREATER);
                term();
                comparison_2();
                break;
            case 2:
                match(TipoToken.GREATER_EQUAL);
                term();
                comparison_2();
                break;
            case 3:
                match(TipoToken.LESS);
                term();
                comparison_2();
                break;
            case 4:
                match(TipoToken.LESS_EQUAL);
                term();
                comparison_2();
                break;
            case 5:
                break;
        }
    }

    private void term() throws ParserException {
        switch (tabla.BuscarEnTabla(29, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                factor();
                term_2();
                break;
        }
    }

    private void term_2() throws ParserException {
        switch (tabla.BuscarEnTabla(30, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.MINUS);
                factor();
                term_2();
                break;
            case 2:
                match(TipoToken.PLUS);
                factor();
                term_2();
                break;
            case 3:
                break;
        }
    }

    private void factor() throws ParserException {
        switch (tabla.BuscarEnTabla(31, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                unary();
                factor_2();
                break;
        }
    }

    private void factor_2() throws ParserException {
        switch (tabla.BuscarEnTabla(32, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.SLASH);
                unary();
                factor_2();
                break;
            case 2:
                match(TipoToken.STAR);
                unary();
                factor_2();
                break;
            case 3:
                break;
        }
    }

    private void unary() throws ParserException {
        switch (tabla.BuscarEnTabla(33, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.BANG);
                unary();
                break;
            case 2:
                match(TipoToken.MINUS);
                unary();
                break;
            case 3:
                cal();
                break;
        }
    }

    private void cal() throws ParserException {
        switch (tabla.BuscarEnTabla(34, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                primary();
                cal_2();
                break;
        }
    }

    private void cal_2() throws ParserException {
        switch (tabla.BuscarEnTabla(35, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.LEFT_PAREN);
                arguments_opc();
                match(TipoToken.RIGHT_PAREN);
                cal_2();
                break;
            case 2:
                break;
        }
    }

    private void primary() throws ParserException {
        switch (tabla.BuscarEnTabla(36, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.TRUE);
                break;
            case 2:
                match(TipoToken.FALSE);
                break;
            case 3:
                match(TipoToken.NULL);
                break;
            case 4:
                match(TipoToken.NUMBER);
                break;
            case 5:
                match(TipoToken.STRING);
                break;
            case 6:
                match(TipoToken.IDENTIFIER);
                break;
            case 7:
                match(TipoToken.LEFT_PAREN);
                expression();
                match(TipoToken.RIGHT_PAREN);
                break;
        }
    }

    private void func() throws ParserException {
        switch (tabla.BuscarEnTabla(37, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.IDENTIFIER);
                match(TipoToken.LEFT_PAREN);
                parameters_opc();
                match(TipoToken.RIGHT_PAREN);
                block();
                break;
        }
    }

    private void parameters_opc() throws ParserException {
        switch (tabla.BuscarEnTabla(39, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                parameters();
                break;
            case 2:
                break;
        }
    }

    private void parameters() throws ParserException {
        switch (tabla.BuscarEnTabla(40, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.IDENTIFIER);
                parameters_2();
                break;
        }
    }

    private void parameters_2() throws ParserException {
        switch (tabla.BuscarEnTabla(41, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.COMMA);
                match(TipoToken.IDENTIFIER);
                parameters_2();
                break;
            case 2:
                break;
        }
    }

    private void arguments_opc() throws ParserException {
        switch (tabla.BuscarEnTabla(42, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                expression();
                arguments();
                break;
            case 2:
                break;
        }
    }

    private void arguments() throws ParserException {
        switch (tabla.BuscarEnTabla(43, preanalisis)){
            case 0:
                throw new ParserException(preanalisis);
            case 1:
                match(TipoToken.COMMA);
                expression();
                arguments();
                break;
            case 2:
                break;
        }
    }


    private void match(TipoToken tt) throws ParserException {
        if(preanalisis.getTipo() ==  tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message =
                    ". Se esperaba " + preanalisis.getTipo() +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }


    private Token previous() {
        return this.tokens.get(i - 1);
    }
}