package Lista;

public class Parser {

    private Cinta cinta;
    private Analex analex;

    private int error;          //Si error=0, la expresión es correcta
    private int suma;
    private boolean hayQueSumar;

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
            hayQueSumar = false;
            Lista();     //Llamar al símbolo inicial.
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
                default:
                    setError(Error.FATAL_ERROR);
            }
            return 0;
        }
    }

    private void Lista() throws Exception {  //Lista ->... Símbolo inicial. Devuelve el resultado de la Lista.
        match(Token.CA);
        hayQueSumar=!hayQueSumar;
        elem();
        match(Token.CC);
        hayQueSumar = !hayQueSumar;
        masLista();
    }

    private void masLista() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.COMA) {
            match(Token.COMA);
            hayQueSumar = !hayQueSumar;
            otroElem();
        }
    }

    private void elem() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.CA) {
            Lista();
        } else if (p == Token.NUM) {
            expr();
        }
    }

    private void otroElem() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.CA) {
            hayQueSumar = !hayQueSumar;
            Lista();
        } else {
            expr();
        }
    }

    private void expr() throws Exception {
        int num = analex.Preanalisis().getAtr();
        if (hayQueSumar) {
            suma += num;
        } else {
            suma -= num;
        }
        match(Token.NUM);
        expr1();
    }

    private void expr1() throws Exception {
        int p = analex.Preanalisis().getNom();
        if (p == Token.MAS || p == Token.MENOS) {
            op();
            match(Token.NUM);
            expr1();
        } else if (p == Token.COMA) {
            match(Token.COMA);
            otroElem();
        }
    }

    private void op() throws Exception {
        int p = analex.Preanalisis().getNom();
        int num = analex.Preanalisis().getAtr();
        if (p == Token.MAS) {
            if (hayQueSumar) {
                suma += num;
            } else {
                suma -= num;
            }
            match(Token.MAS);
        } else {
            if (hayQueSumar) {
                suma -= num;
            } else {
                suma += num;
            }
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
