package algoritmos;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/*Clase que transforma una expresion regular de notacion infija a la
  notacion postfija*/
public class ConvertidorER {

    //Precedencia de los operadores
    private static final Map<Character, Integer> PRECEDENCIA;

    static {
        Map<Character, Integer> map = new HashMap();
        map.put('(', 1);
        map.put('|', 2);
        map.put('.', 3);
        map.put('?', 4);
        map.put('*', 4);
        map.put('+', 4);
        map.put('^', 5);
        PRECEDENCIA = Collections.unmodifiableMap(map);
    };

    //Obtiene la presedencia del caracter
    private static Integer getPrecedencia(Character c) {
        Integer precedencia = PRECEDENCIA.get(c);
        return precedencia == null ? 6 : precedencia;
    }

    //Trasnforma la expresion regular insertando '.' como concatenacion
    private static String formatoER(String regex) {
        String res = new String();
        List<Character> allOperators = Arrays.asList('|', '?', '+',
                '*', '^', '.');
        List<Character> binaryOperators = Arrays.asList('^', '|');

        for (int i = 0; i < regex.length(); i++) {
            Character c1 = regex.charAt(i);

            if (i + 1 < regex.length()) {
                Character c2 = regex.charAt(i + 1);

                res += c1;

                if (!c1.equals('(') && !c1.equals('.') && !c2.equals(')')
                        && !allOperators.contains(c2)
                        && !binaryOperators.contains(c1)) {
                    res += '.';
                }
            }
        }
        res += regex.charAt(regex.length() - 1);

        return res;
    }

    /*Convierte una expresion regular de infijo a postfijo utilizando el
          algoritmo shunting yard*/
    public static String infijoAPostfijo(String regex) {
        String postfijo = new String();

        Stack<Character> pila = new Stack();

        String expRegFormateada = formatoER(regex);

        for (Character c : expRegFormateada.toCharArray()) {
            switch (c) {
                case '(':
                    pila.push(c);
                    break;

                case ')':
                    while (!pila.peek().equals('(')) {
                        postfijo += pila.pop();
                    }
                    pila.pop();
                    break;

                default:
                    while (pila.size() > 0) {
                        Character peekedChar = pila.peek();

                        Integer peekedCharPrecedence = getPrecedencia(peekedChar);
                        Integer currentCharPrecedence = getPrecedencia(c);

                        if (peekedCharPrecedence >= currentCharPrecedence) {
                            postfijo += pila.pop();
                        } else {
                            break;
                        }
                    }
                    pila.push(c);
                    break;
            }
        }
        while (pila.size() > 0) {
            postfijo += pila.pop();
        }

        return postfijo;
    }
}
