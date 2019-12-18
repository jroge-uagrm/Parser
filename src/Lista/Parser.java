package Lista;

public class Parser {

    private Cinta cinta;
    private Analex analex;

    private int error;          //Si error=0, la expresión es correcta
    private int suma;
    private boolean hayQueSumarListas,hayQueSumarElementos;

    public Parser() {
        cinta = new Cinta();
        analex = new Analex(cinta);
    }

    public int getError() {
        return error;
    }

    private void setError(int e) {
        if (error == 0) {
            error = e;      //Solo aceptar el 1er error.
        }
    }

    public int evaluar(String list) {
        try {
            cinta.init(list);
            analex.init();
            error = suma = 0;          //No hay error aún
            hayQueSumarListas = false;
            hayQueSumarElementos=true;
            Lista();     //Llamar al símbolo inicial.
            match(Token.FIN);
            return suma;
        } catch (Exception e) {
            int error = Integer.parseInt(e.getMessage());
            switch (error) {
                case Token.CA:
                    setError(Error.FALTA_CA);
                case Token.CC:
                    setError(Error.FALTA_CC);
                case Token.COMA:
                    setError(Error.FALTA_COMA);
                case Token.NUM:
                    setError(Error.FALTA_NUM);
                case Token.MAS:
                    setError(Error.FALTA_SIGNO);
                case Token.MENOS:
                    setError(Error.FALTA_SIGNO);
                default:
                    setError(Error.FATAL_ERROR);
            }
            return 0;
        }
    }

    private void Lista() throws Exception {  //Lista ->... Símbolo inicial. Devuelve el resultado de la Lista.
        match(Token.CA);
        hayQueSumarListas=!hayQueSumarListas;
        elem();
        match(Token.CC);
        hayQueSumarListas=!hayQueSumarListas;
    }

    private void elem() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.CA) {
            Lista();
            separar();
        } else if (p == Token.NUM)
        {
            expr();
            separar();
        }
    }

    private void separar() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.COMA) {
            match(Token.COMA);
            hayQueSumarElementos=true;
            elem();
        }
    }

    private void expr() throws Exception {
        int num = analex.Preanalisis().getAtr();
        if (hayQueSumarListas) {
            if(hayQueSumarElementos){
                suma += num;
            }else{
                suma -= num;
            }
        } else {
            if(hayQueSumarElementos){
                suma -= num;
            }else{
                suma += num;
            }
        }
        match(Token.NUM);
        masExpr();
    }

    private void masExpr() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.MAS || p == Token.MENOS) {
            op();
            expr();
        }
    }

    private void op() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.MAS) {
            hayQueSumarElementos=true;
            match(Token.MAS);
        } else {
            hayQueSumarElementos=false;
            match(Token.MENOS);
        }
    }

    private void match(int nomToken) throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == nomToken) {
            analex.avanzar();
        } else {
            throw new Exception(Integer.toString(nomToken));
        }
    }
}
