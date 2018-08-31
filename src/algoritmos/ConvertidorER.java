package algoritmos;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class ConvertidorER {

    /** Mapa de precedencia de los operadores. */
    private static Map<Character, Integer> PRECEDENCIA;

    //constructor

    static {
        Map<Character, Integer> map = new HashMap<>();
        map.put('(', 1); // parentesis
        map.put('|', 2); // Union o or
        map.put('.', 3); // explicit concatenation operator
        map.put('?', 4); // | €
        map.put('*', 4); // kleene
        map.put('+', 4); // positivo
        PRECEDENCIA = Collections.unmodifiableMap(map);

    };

    /**
     * Obtener la precedencia del caracter
     *
     * @param c character
     * @return corresponding precedence
     */
    //Obtiene la presedencia del caracter
    private static Integer getPrecedencia(Character c) {
        Integer precedencia = PRECEDENCIA.get(c);
        return precedencia == null ? 6 : precedencia;
    }



    /**
     * Insertar caracter en una posicion deseada
     * @param s string deseado
     * @param pos indice del caracter
     * @param ch caracter  o String deseado
     * @return nuevo string con el caracter deseado
     */
    private static String insertCharAt(String s, int pos, Object ch){
        return s.substring(0,pos)+ch+s.substring(pos+1);
    }
    /**
     * Agregar caracter en la posicion deseada (no elimina el caracter anterior)
     * @param s string deseado
     * @param pos posicion del caracter
     * @param ch caracter deseado
     * @return nuevo string con el caracter agregado
     */
    private static String appendCharAt(String s, int pos, Object ch){
        String val = s.substring(pos,pos+1);
        return s.substring(0,pos)+val+ch+s.substring(pos+1);

    }

    /**
     * Metodo para abreviar el operador ?
     * equivalente a |€
     * @param regex expresion regular
     * @return expresion regular modificada sin el operador ?
     */
    private static String abreviaturaInterrogacion(String regex)
    {
        for (int i = 0; i<regex.length();i++){
            Character ch = regex.charAt(i);

            if (ch.equals('?'))
            {
                if (regex.charAt(i-1) == ')')
                {
                    regex = insertCharAt(regex,i,"|"+")");

                    int j =i;
                    while (j!=0)
                    {
                        if (regex.charAt(j)=='(')
                        {
                            break;
                        }

                        j--;

                    }

                    regex=appendCharAt(regex,j,"(");

                }
                else
                {
                    regex = insertCharAt(regex,i,"|"+")");
                    regex = insertCharAt(regex,i-1,"("+regex.charAt(i-1));
                }
            }
        }
        regex = balancearParentesis(regex);
        return regex;
    }

    /**
     * Método para contar los parentesis izquierdos '('
     * @param regex String expresion regular
     * @return int contador
     */
    private static int parentesisIzq(String regex){
        int P1=0;
        for (int i = 0;i<regex.length();i++){
            Character ch = regex.charAt(i);
            if (ch.equals('(')){
                P1++;
            }

        }
        return P1;
    }
    /**
     * Método para contar los parentesis derechos ')'
     * @param regex String expresion regular
     * @return int contador
     */
    private static int parentesisDer(String regex){
        int P1=0;
        for (int i = 0;i<regex.length();i++){
            Character ch = regex.charAt(i);
            if (ch.equals(')')){
                P1++;
            }
        }
        return P1;
    }
    /**
     * Método para balancear parentesis en caso de que esté mal ingresada
     * la expresión regular
     * @param regex String expresión regular
     * @return String expresion regular modificada
     */
    private static String balancearParentesis(String regex){
        //corregir parentesis de la expresion en caso que no esten balanceados
        int P1 = parentesisIzq(regex);
        int P2 = parentesisDer(regex);


        while(P1 != P2){
            if (P1>P2)
                regex +=")";
            if (P2>P1)
                regex ="(" + regex;
            P1 = parentesisIzq(regex);
            P2 = parentesisDer(regex);
        }
        return regex;
    }

    /**
     * Método para abreviar el operador de cerradura positiva
     * @param regex expresion regular (string)
     * @return expresion regular modificada sin el operador +
     */
    private static String abreviaturaCerraduraPositiva(String regex){
        //sirve para buscar el '(' correcto cuando  hay () en medio
        // de la cerradura positiva
        int compare = 0;

        for (int i = 0; i<regex.length();i++){
            Character ch = regex.charAt(i);

            if (ch.equals('+'))
            {
                //si hay un ')' antes de un operador
                //significa que hay que buscar el '(' correspondiente
                if (regex.charAt(i-1) == ')'){

                    int fixPosicion = i;

                    while (fixPosicion != -1)
                    {
                        if (regex.charAt(fixPosicion)==')')
                        {
                            compare++;

                        }

                        if (regex.charAt(fixPosicion)=='(')
                        {

                            compare--;
                            if (compare ==0)
                                break;
                        }


                        fixPosicion--;

                    }

                    String regexAb = regex.substring(fixPosicion,i);
                    regex = insertCharAt(regex,i,regexAb+"*");


                }
                //si no hay parentesis, simplemente se inserta el caracter
                else
                {
                    regex = insertCharAt(regex,i,regex.charAt(i-1)+"*");
                }


            }

        }

        regex = balancearParentesis(regex);

        return regex;
    }
    /**
     *
     * Transformar una expresión regular insertando un punto '.' explicitamente
     * como operador de concatenación.
     * @param regex String
     * @return regexExplicit String
     */
    private static String formatRegEx(String regex) {
        regex = regex.trim();
        regex = abreviaturaInterrogacion(regex);
        regex = abreviaturaCerraduraPositiva(regex);
        String  regexExplicit = new String();
        List<Character> operadores = Arrays.asList('|', '?', '+', '*');
        List<Character> operadoresBinarios = Arrays.asList('|');


        //recorrer la cadena
        for (int i = 0; i < regex.length(); i++)
        {
            Character c1 = regex.charAt(i);

            if (i + 1 < regex.length())
            {

                Character c2 = regex.charAt(i + 1);

                regexExplicit += c1;

                //mientras la cadena no incluya operadores definidos, será una concatenación implicita
                if (!c1.equals('(') && !c2.equals(')') && !operadores.contains(c2) && !operadoresBinarios.contains(c1))
                {
                    regexExplicit += '.';

                }

            }
        }
        regexExplicit += regex.charAt(regex.length() - 1);


        return regexExplicit;
    }

    private static String abreviacionOr(String regex){
        String resultado = new String();
        try{
            for (int i=0;i<regex.length();i++){
                Character ch = regex.charAt(i);
                if (ch =='[' ){
                    if (regex.charAt(i+2)=='-'){
                        int inicio = regex.charAt(i+1);
                        int fin = regex.charAt(i+3);
                        resultado +="(";
                        for (int j = 0;j<=fin-inicio;j++)
                        {
                            if (j==(fin-inicio))
                                resultado+= Character.toString((char)(inicio+j));
                            else
                                resultado+= Character.toString((char)(inicio+j))+'|';
                        }
                        resultado +=")";
                        i=i+4;
                    }
                    else{
                        resultado +=ch;
                    }
                }
                else{
                    resultado+=ch;
                }

            }
        } catch (Exception e){
            System.out.println("Error en la conversión " + regex);
            resultado = " ";
        }

        return resultado;
    }

    private static String abreviacionAnd(String regex){
        String resultado = new String();
        try{
            for (int i=0;i<regex.length();i++){
                Character ch = regex.charAt(i);
                if (ch =='[' ){
                    if (regex.charAt(i+2)=='.'){
                        int inicio = regex.charAt(i+1);
                        int fin = regex.charAt(i+3);
                        resultado +="(";
                        for (int j = 0;j<=fin-inicio;j++)
                        {

                            resultado+= Character.toString((char)(inicio+j));
                        }
                        resultado +=")";
                        i=i+4;
                    }
                }
                else{
                    resultado+=ch;
                }
                //System.out.println(resultado);
            }
        }catch (Exception e){
            System.out.println("Error en la conversion "+regex);
            resultado = "(a|b)*abb";
        }
        return resultado;
    }


    /**
     * Convertir una expresión regular de notación infix a postfix
     * con el algoritmo de Shunting-yard.
     *
     * @param regex notacion infix
     * @return notacion postfix
     */
    public static String infijoAPostfijo(String regex) {
        String postfix = new String();
        regex = abreviacionOr(regex);
        regex = abreviacionAnd(regex);
        Stack<Character> stack = new Stack<>();

        String formattedRegEx = formatRegEx(regex);
        for (Character c : formattedRegEx.toCharArray()) {
            switch (c) {
                case '(':
                    stack.push(c);
                    break;

                case ')':
                    while (!stack.peek().equals('(')) {
                        postfix += stack.pop();
                    }
                    stack.pop();
                    break;

                default:
                    while (stack.size() > 0)
                    {
                        Character peekedChar = stack.peek();

                        Integer peekedCharPrecedence = getPrecedencia(peekedChar);
                        Integer currentCharPrecedence = getPrecedencia(c);

                        if (peekedCharPrecedence >= currentCharPrecedence)
                        {
                            postfix += stack.pop();

                        }
                        else
                        {
                            break;
                        }
                    }
                    stack.push(c);
                    break;
            }

        }

        while (stack.size() > 0)
            postfix += stack.pop();
        return postfix;
    }

}