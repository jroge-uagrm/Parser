package Lista;

public class Main {

    public void main(String[] args) {

        Parser parser = new Parser();

        //int r = parser.evaluar("[13+15-10, 23, [15, 12-5+6], 18-20, 0]");
        
        int r = parser.evaluar("[,");

        int p = parser.getError();
        switch (p) {
            case 0:
                System.out.println("El valor de la Lista es " + r);
                break;
            case Error.FALTA_CA:
                System.out.println("Falta corchete de apertura");
                break;
            case Error.FALTA_CC:
                System.out.println("Falta corchete de cerradura");
                break;
            case Error.FALTA_COMA:
                System.out.println("Falta coma");
                break;
            default:
                System.out.println("Fatal error!");
                break;
        }
    }

}
