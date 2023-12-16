import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.util.Scanner;

public class Tabla {

    private int[][] tabla;
    private List<TipoToken> terminales;

    public Tabla() throws FileNotFoundException {
        tabla = new int[44][35];
        String[][] texto = new String[44][];
        File archivo = new File("C:\\Users\\gabri\\Documents\\ESCOM\\Compiladores\\Scanner\\Scanner\\src\\TablaAnalizador.csv");
        java.util.Scanner scanner = new java.util.Scanner(archivo);
        for (int i = 0; i < 44; i++){
            texto[i] = scanner.nextLine().split(",");
        }

        for (int i = 0; i < texto.length; i++){
            for(int j = 0; j < texto[i].length; j++){
                tabla[i][j] = Integer.parseInt(texto[i][j]);
            }
        }
        terminales = new ArrayList<>();
        AgregarTerminales();
    }

    public int BuscarEnTabla(int prod, Token t){
        return tabla[prod][terminales.indexOf(t.getTipo())];
    }

    public void AgregarTerminales(){
        terminales.add(TipoToken.COMMA);
        terminales.add(TipoToken.FUN);
        terminales.add(TipoToken.VAR);
        terminales.add(TipoToken.BANG);
        terminales.add(TipoToken.BANG_EQUAL);
        terminales.add(TipoToken.ELSE);
        terminales.add(TipoToken.TRUE);
        terminales.add(TipoToken.FALSE);
        terminales.add(TipoToken.PLUS);
        terminales.add(TipoToken.MINUS);
        terminales.add(TipoToken.STAR);
        terminales.add(TipoToken.SLASH);
        terminales.add(TipoToken.PRINT);
        terminales.add(TipoToken.WHILE);
        terminales.add(TipoToken.IF);
        terminales.add(TipoToken.FOR);
        terminales.add(TipoToken.NULL);
        terminales.add(TipoToken.EOF);
        terminales.add(TipoToken.NUMBER);
        terminales.add(TipoToken.RETURN);
        terminales.add(TipoToken.STRING);
        terminales.add(TipoToken.EQUAL);
        terminales.add(TipoToken.OR);
        terminales.add(TipoToken.AND);
        terminales.add(TipoToken.EQUAL_EQUAL);
        terminales.add(TipoToken.GREATER);
        terminales.add(TipoToken.GREATER_EQUAL);
        terminales.add(TipoToken.LESS_EQUAL);
        terminales.add(TipoToken.LESS);
        terminales.add(TipoToken.LEFT_PAREN);
        terminales.add(TipoToken.RIGHT_PAREN);
        terminales.add(TipoToken.SEMICOLON);
        terminales.add(TipoToken.LEFT_BRACE);
        terminales.add(TipoToken.RIGHT_BRACE);
        terminales.add(TipoToken.IDENTIFIER);
    }

}
