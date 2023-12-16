public class ParserException extends Exception{

    public ParserException(String m){
        System.out.println(m);
    }

    public ParserException(Token tt){
        String message =
                ". No se esperaba " + tt.getTipo();
        System.out.println(message);
    }
}
