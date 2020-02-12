public class Generator{

    public static final boolean DEBUG   = false;
    public static final String count    = "1";
    public static final int LT          = 1;
    public static final int LTOREQ      = 2;
    public static final int GT          = 3;
    public static final int GTOREQ      = 4;
    public static final int EQ          = 5;
    public static final int NOTEQ       = 6;
    public static final int AND         = 7;
    public static final int OR          = 8;
    public static final int NOT         = 9;
    public static final int PLUS        = 10;
    public static final int MINUS       = 11;
    public static final int MUL         = 12;
    public static final int DIV         = 13;
    public static final int MIN         = 14;
    public static final int MOD         = 15;
    public static final int INCR        = 16;
    public static final int DECR        = 17;
    public static final int INCL        = 18;
    public static final int DECL        = 19;
    public static int numTempVars       = -1;
    public static int numLabels         = -1;

    public static String newTempVar(){
        numTempVars++;
        return "t" + numTempVars;
    }

    public static String newLabel(){
        numLabels++;
        return "L" + numLabels;
    }

    public static void label(String label){
        System.out.println(label + ":");
    }

    public static void gotoLabel(String label){
        System.out.println("\tgoto " + label + ";");
    }

    public static String print(String value){
        System.out.println("\tprint " + value + ";");
        return value;
    }

    public static void halt(){
        System.out.println("\thalt;");
    }

    public static void error(){
        System.out.println("\terror;");
    }

    public static void debug(){
        if(DEBUG){
            SymbolTable.showTable();
        }
    }

    public static void varNotDeclared(String variable){
        System.out.println("\t# variable " + variable + " not declared.");
        error();
        halt();
        System.exit(0);
    }

    public static void varDeclared(String variable){
        System.out.println("\t# variable " + variable + " has been declared already.");
        error();
        halt();
        System.exit(0);
    }

    public static void comment(String msg) {
        System.out.println("\t# " + msg);
    }

    public static String assignment(String var, String number){
        System.out.println("\t" + var + " = " + number + ";");
        return var;
    }

    public static void loopCounter(int op, String var, String step){
        switch(op){
            case PLUS:
                System.out.println("\t" + var + " = " + var + " + " + step + ";");
                break;
            case MINUS:
                System.out.println("\t" + var + " = " + var + " - " + step + ";");
                break;
        }
        
    }

    public static String increment(int op, String var){
        String temp = newTempVar(); 
        switch(op){
            case INCL:
                System.out.println("\t" + var + " = " + var + " + " + count + ";");
                temp = var;
                break;
            case DECL:
                System.out.println("\t" + var + " = " + var + " - " + count + ";");
                temp = var;
                break;
            case INCR:
                Generator.assignment(temp, var);
                System.out.println("\t" + var + " = " + var + " + " + count + ";");
                break;
            case DECR:
                Generator.assignment(temp, var);
                System.out.println("\t" + var + " = " + var + " - " + count + ";");
                break;   
            default:
                System.out.println("Error: code generation failed with arguments: \toperation:" + op + "\tvar: " + var + "\ttemp: " + temp);
                break;
        }
        return temp;
    }

    public static String arithmetic(int op, String number1, String number2){
        String temp = newTempVar(); 
        switch(op){
            case PLUS:
                System.out.println("\t" + temp + " = " + number1 + " + " + number2 + ";");
                break;
            case MINUS:
                System.out.println("\t" + temp + " = " + number1 + " - " + number2 + ";");
                break;
            case MUL:
                System.out.println("\t" + temp + " = " + number1 + " * " + number2 + ";");
                break;
            case DIV:
                System.out.println("\t" + temp + " = " + number1 + " / " + number2 + ";");
                break;
            case MIN:
                System.out.println("\t" + temp + " = -" + number1 + ";");
                break;
            case MOD:
                String t0 = newTempVar();
                String t1 = newTempVar();
                System.out.println("\t" + t0 + " = " + number1 + " / " + number2 + ";");
                System.out.println("\t" + t1 + " = " + t0 + " * " + number2 + ";");
                System.out.println("\t" + temp + " = " + number1 + " - " + t1 + ";");
                break;
            default:
                System.out.println("Error: code generation failed with arguments: \toperation:" + op + "\tnumber 1: " + number1 + "\tnumber 2: " + number2);
                break;
        }
        return temp;
    }

    public static Tag condition(int cond, String arg1, String arg2){
        Tag result = new Tag(newLabel(), newLabel());
        switch(cond){
            case EQ: 
                System.out.println("\tif (" + arg1 + " == " + arg2 + ") goto " + result.getT() + ";");
                System.out.println("\tgoto " + result.getF() + ";");
                break;
            case NOTEQ:
                System.out.println("\tif (" + arg1 + " == " + arg2 + ") goto " + result.getF() + ";");
                System.out.println("\tgoto " + result.getT() + ";");
                break;
            case LT:
                System.out.println("\tif (" + arg1 + " < " + arg2 + ") goto " + result.getT() + ";");
                System.out.println("\tgoto " + result.getF() + ";");
                break;
            case LTOREQ:
                System.out.println("\tif (" + arg2 + " < " + arg1 + ") goto " + result.getF() + ";");
                System.out.println("\tgoto " + result.getT() + ";");
                break;
            case GT:
                System.out.println("\tif (" + arg2 + " < " + arg1 + ") goto " + result.getT() + ";");
                System.out.println("\tgoto " + result.getF() + ";");
                break;
            case GTOREQ:
                System.out.println("\tif (" + arg1 + " < " + arg2 + ") goto " + result.getF() + ";");
                System.out.println("\tgoto " + result.getT() + ";");
                break;
            default:
                System.out.println("Error: code generation failed with arguments: \tcondition:" + cond + "\targ1: " + arg1 + "\targ2: " + arg2);
                break;
        }
        return result;
    }

    public static Tag operator(int cond, Tag c1, Tag c2){
        Tag result = c2;
        switch(cond){
            case NOT:
                result = new Tag(c1.getF(), c1.getT());
                break;
            case AND:
                label(c1.getF()); 
                gotoLabel(c2.getT());
                break;
            case OR:
                label(c1.getT()); 
                gotoLabel(c2.getF());
                break;
            default:
                break;
        }
        return result;
    }

}