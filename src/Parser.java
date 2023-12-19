import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int i = 0;
    private Token preanalisis;

    private boolean hayErrores = false;
    List<Statement> programa;
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
                throw new ParserException(preanalisis, 0);
            case 1:
                programa = declaration();
                break;
        }
   }

    private List<Statement> declaration() throws ParserException {
        List<Statement> stmts = new ArrayList<>();
        switch (tabla.BuscarEnTabla(1, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 1);
            case 1:
                stmts.add(fun_decl());
                stmts.addAll(declaration());
                return stmts;
            case 2:
                stmts.add(var_decl());
                stmts.addAll(declaration());
                return stmts;
            case 3:
                stmts.add(statement());
                stmts.addAll(declaration());
                return stmts;
            case 4:
                break;
        }
        return stmts;
    }

    private Statement fun_decl() throws ParserException {
        switch (tabla.BuscarEnTabla(2, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 2);
            case 1:
                match(TipoToken.FUN);
                return func();
        }
        return null;
    }

    private Statement var_decl() throws ParserException {
        switch (tabla.BuscarEnTabla(3, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 3);
            case 1:
                match(TipoToken.VAR);
                match(TipoToken.IDENTIFIER);
                Statement stmt = var_init(previous());
                match(TipoToken.SEMICOLON);
                return stmt;
        }
        return null;
    }

    private Statement var_init(Token name) throws ParserException {
        switch (tabla.BuscarEnTabla(4, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 4);
            case 1:
                match(TipoToken.EQUAL);
                Expression expr = expression();
                return new StmtVar(name, expr);
            case 2:
                break;
        }
        return new StmtVar(name, null);
    }

    private Statement statement() throws ParserException {
        switch (tabla.BuscarEnTabla(5, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 5);
            case 1:
                return expr_stmt();
            case 2:
                return for_stmt();
            case 3:
                return if_stmt();
            case 4:
                return print_stmt();
            case 5:
                return return_stmt();
            case 6:
                return while_stmt();
            case 7:
                return block();
        }
        return null;
    }

    private Statement expr_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(6, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 6);
            case 1:
                Expression expr = expression();
                match(TipoToken.SEMICOLON);
                return new StmtExpression(expr);
        }
        return null;
    }

    private Statement for_stmt() throws ParserException {
        List<Statement> stmts = new ArrayList<>();
        switch (tabla.BuscarEnTabla(7, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 7);
            case 1:
                match(TipoToken.FOR);
                match(TipoToken.LEFT_PAREN);
                stmts.add(for_stmt_1());
                Expression expr = for_stmt_2();
                stmts.add(for_stmt_3());
                match(TipoToken.RIGHT_PAREN);
                stmts.add(statement());
                StmtBlock block = new StmtBlock(stmts);
                return new StmtLoop(expr, block);
        }
        return null;
    }

    private Statement for_stmt_1() throws ParserException {
        switch (tabla.BuscarEnTabla(8, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 8);
            case 1:
                return var_decl();
            case 2:
                return expr_stmt();
            case 3:
                match(TipoToken.SEMICOLON);
                return new StmtExpression(null);
        }
        return null;
    }

    private Expression for_stmt_2() throws ParserException {
        switch (tabla.BuscarEnTabla(9, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 9);
            case 1:
                Expression expr = expression();
                match(TipoToken.SEMICOLON);
                return expr;
            case 2:
                match(TipoToken.SEMICOLON);
                return null;
        }
        return null;
    }

    private Statement for_stmt_3() throws ParserException {
        switch (tabla.BuscarEnTabla(10, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 10);
            case 1:
                return new StmtExpression(expression());
            case 2:
                return new StmtExpression(null);
        }
        return null;
    }

    private Statement if_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(11, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 11);
            case 1:
                match(TipoToken.IF);
                match(TipoToken.LEFT_PAREN);
                Expression expr = expression();
                match(TipoToken.RIGHT_PAREN);
                Statement stmt1 = statement();
                Statement stmt2 = else_stmt();
                return new StmtIf(expr, stmt1, stmt2);
        }
        return null;
    }

    private Statement else_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(12, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 12);
            case 1:
                match(TipoToken.ELSE);
                return statement();
            case 2:
                break;
        }
        return null;
    }

    private Statement print_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(13, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 13);
            case 1:
                match(TipoToken.PRINT);
                StmtPrint stmt = new StmtPrint(expression());
                match(TipoToken.SEMICOLON);
                return stmt;
        }
        return null;
    }

    private Statement return_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(14, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 14);
            case 1:
                match(TipoToken.RETURN);
                Statement stmt = return_exp_opc();
                match(TipoToken.SEMICOLON);
                return stmt;
        }
        return null;
    }

    private Statement return_exp_opc() throws ParserException {
        switch (tabla.BuscarEnTabla(15, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 15);
            case 1:
                return  new StmtReturn(expression());
            case 2:
                break;
        }
        return new StmtReturn(null);
    }

    private Statement while_stmt() throws ParserException {
        switch (tabla.BuscarEnTabla(16, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 16);
            case 1:
                match(TipoToken.WHILE);
                match(TipoToken.LEFT_PAREN);
                Expression expr = expression();
                match(TipoToken.RIGHT_PAREN);
                Statement stmt = statement();
                return new StmtLoop(expr, stmt);
        }
        return null;
    }

    private Statement block() throws ParserException {
        switch (tabla.BuscarEnTabla(17, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 17);
            case 1:
                match(TipoToken.LEFT_BRACE);
                StmtBlock stmt = new StmtBlock(declaration());
                match(TipoToken.RIGHT_BRACE);
                return stmt;
        }
        return null;
    }

    private Expression expression() throws ParserException {
        switch (tabla.BuscarEnTabla(18, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 18);
            case 1:
                return assignment();
        }
        return null;
    }

    private Expression assignment() throws ParserException {
        switch (tabla.BuscarEnTabla(19, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 19);
            case 1:
                Expression expr = logic_or();
                expr = assignment_opc(expr);
                return expr;
        }
        return null;
    }

    private Expression logic_or() throws ParserException {
        switch (tabla.BuscarEnTabla(20, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 20);
            case 1:
                Expression expr = logic_and();
                expr = logic_or_2(expr);
                return expr;
        }
        return null;
    }

    private Expression logic_and() throws ParserException {
        switch (tabla.BuscarEnTabla(21, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 21);
            case 1:
                Expression expr = equality();
                expr = logic_and_2(expr);
                return expr;
        }
        return null;
    }

    private Expression logic_or_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(22, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 22);
            case 1:
                match(TipoToken.OR);
                Token operador = previous();
                Expression expr2 = logic_and();
                ExprLogical expb = new ExprLogical(expr, operador, expr2);
                return logic_or_2(expb);
            case 2:
                break;
        }
        return expr;
    }

    private Expression assignment_opc(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(23, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 23);
            case 1:
                match(TipoToken.EQUAL);
                Token operador = previous();
                Expression expr2 = expression();
                return new ExprBinary(expr, operador, expr2);
            case 2:
                break;
        }
        return expr;
    }

    private Expression logic_and_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(24, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 24);
            case 1:
                match(TipoToken.AND);
                Token operador = previous();
                Expression expr2 = equality();
                ExprLogical expb = new ExprLogical(expr, operador, expr2);
                return logic_and_2(expb);
            case 2:
                break;
        }
        return expr;
    }

    private Expression equality() throws ParserException {
        switch (tabla.BuscarEnTabla(25, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 25);
            case 1:
                Expression expr = comparison();
                expr = equality_2(expr);
                return expr;
        }
        return null;
    }

    private Expression equality_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(26, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 26);
            case 1:
                match(TipoToken.BANG_EQUAL);
                Token operador = previous();
                Expression expr2 = comparison();
                ExprLogical expb = new ExprLogical(expr, operador, expr2);
                return equality_2(expb);
            case 2:
                match(TipoToken.EQUAL_EQUAL);
                operador = previous();
                expr2 = comparison();
                expb = new ExprLogical(expr, operador, expr2);
                return equality_2(expb);
            case 3:
                break;
        }
        return expr;
    }

    private Expression comparison() throws ParserException {
        switch (tabla.BuscarEnTabla(27, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 27);
            case 1:
                Expression expr = term();
                expr = comparison_2(expr);
                return expr;
        }
        return null;
    }

    private Expression comparison_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(28, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 28);
            case 1:
                match(TipoToken.GREATER);
                Token operador = previous();
                Expression expr2 = term();
                ExprLogical expb = new ExprLogical(expr, operador, expr2);
                return comparison_2(expb);
            case 2:
                match(TipoToken.GREATER_EQUAL);
                operador = previous();
                expr2 = term();
                expb = new ExprLogical(expr, operador, expr2);
                return comparison_2(expb);
            case 3:
                match(TipoToken.LESS);
                operador = previous();
                expr2 = term();
                expb = new ExprLogical(expr, operador, expr2);
                return comparison_2(expb);
            case 4:
                match(TipoToken.LESS_EQUAL);
                operador = previous();
                expr2 = term();
                expb = new ExprLogical(expr, operador, expr2);
                return comparison_2(expb);
            case 5:
                break;
        }
        return expr;
    }

    private Expression term() throws ParserException {
        switch (tabla.BuscarEnTabla(29, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 29);
            case 1:
                Expression expr = factor();
                expr = term_2(expr);
                return expr;
        }
        return null;
    }

    private Expression term_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(30, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 30);
            case 1:
                match(TipoToken.MINUS);
                Token operador = previous();
                Expression expr2 = factor();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return term_2(expb);
            case 2:
                match(TipoToken.PLUS);
                operador = previous();
                expr2 = factor();
                expb = new ExprBinary(expr, operador, expr2);
                return term_2(expb);
            case 3:
                break;
        }
        return expr;
    }

    private Expression factor() throws ParserException {
        switch (tabla.BuscarEnTabla(31, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 31);
            case 1:
                Expression expr = unary();
                expr = factor_2(expr);
                return expr;
        }
        return null;
    }

    private Expression factor_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(32, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 32);
            case 1:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = unary();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return factor_2(expb);
            case 2:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = unary();
                expb = new ExprBinary(expr, operador, expr2);
                return factor_2(expb);
            case 3:
                break;
        }
        return expr;
    }

    private Expression unary() throws ParserException {
        switch (tabla.BuscarEnTabla(33, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 33);
            case 1:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = unary();
                return new ExprUnary(operador, expr);
            case 2:
                match(TipoToken.MINUS);
                operador = previous();
                expr = unary();
                return new ExprUnary(operador, expr);
            case 3:
                return cal();
        }
        return null;
    }

    private Expression cal() throws ParserException {
        switch (tabla.BuscarEnTabla(34, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 34);
            case 1:
                Expression expr = primary();
                expr = cal_2(expr);
                return expr;
        }
        return null;
    }

    private Expression cal_2(Expression expr) throws ParserException {
        switch (tabla.BuscarEnTabla(35, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 35);
            case 1:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = arguments_opc();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return cal_2(ecf);
            case 2:
                break;
        }
        return expr;
    }

    private Expression primary() throws ParserException {
        switch (tabla.BuscarEnTabla(36, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 36);
            case 1:
                match(TipoToken.TRUE);
                return new ExprLiteral(true);
            case 2:
                match(TipoToken.FALSE);
                return new ExprLiteral(false);
            case 3:
                match(TipoToken.NULL);
                return new ExprLiteral(null);
            case 4:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(numero.getLiteral());
            case 5:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.getLiteral());
            case 6:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new ExprVariable(id);
            case 7:
                match(TipoToken.LEFT_PAREN);
                Expression expr = expression();
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
        }
        return null;
    }

    private Statement func() throws ParserException {
        switch (tabla.BuscarEnTabla(37, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 37);
            case 1:
                match(TipoToken.IDENTIFIER);
                Token name = previous();
                match(TipoToken.LEFT_PAREN);
                List<Token> params = parameters_opc();
                match(TipoToken.RIGHT_PAREN);
                Statement block = block();
                return new StmtFunction(name, params, block);
        }
        return null;
    }

    private List<Token> parameters_opc() throws ParserException {
        List<Token> tokens = new ArrayList<>();
        switch (tabla.BuscarEnTabla(39, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 38);
            case 1:
                tokens.addAll(parameters());
                break;
            case 2:
                break;
        }
        return tokens;
    }

    private List<Token> parameters() throws ParserException {
        List<Token> tokens = new ArrayList<>();
        switch (tabla.BuscarEnTabla(40, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 39);
            case 1:
                match(TipoToken.IDENTIFIER);
                tokens.add(previous());
                tokens.addAll(parameters_2());
                return tokens;
        }
        return tokens;
    }

    private List<Token> parameters_2() throws ParserException {
        List<Token> tokens = new ArrayList<>();
        switch (tabla.BuscarEnTabla(41, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 41);
            case 1:
                match(TipoToken.COMMA);
                match(TipoToken.IDENTIFIER);
                tokens.add(previous());
                tokens.addAll(parameters_2());
                return tokens;
            case 2:
                break;
        }
        return tokens;
    }

    private List<Expression> arguments_opc() throws ParserException {
        List<Expression> exprs = new ArrayList<>();
        switch (tabla.BuscarEnTabla(42, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 42);
            case 1:
                exprs.add(expression());
                exprs.addAll(arguments());
                return exprs;
            case 2:
                break;
        }
        return exprs;
    }

    private List<Expression> arguments() throws ParserException {
        List<Expression> exprs = new ArrayList<>();
        switch (tabla.BuscarEnTabla(43, preanalisis)){
            case 0:
                throw new ParserException(preanalisis, 43);
            case 1:
                match(TipoToken.COMMA);
                exprs.add(expression());
                exprs.addAll(arguments());
                return exprs;
            case 2:
                break;
        }
        return exprs;
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