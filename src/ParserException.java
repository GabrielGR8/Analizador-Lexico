public class ParserException extends Exception{

    public ParserException(String m){
        System.out.println(m);
    }

    public ParserException(Token tt, int estado){
        String message =
                ". No se esperaba " + tt.getTipo()+" en el estado: "+estado;
        System.out.println(message);
    }
}
