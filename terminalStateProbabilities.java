/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author bryant
 */
public class terminalStateProbabilities {
    
    private static Map<Integer,int[]> terminalProbabilities = new HashMap<>();
    private static int[][] matrix = new int[][]{ {0,2,1,0,0},{0,0,0,3,4},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};
//    private static int[][] matrix = new int[][]{{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
    
    public static void main(String[] args) {
        int[] solution = doomsday_fuel(matrix);

        for(int i = 0; i < solution.length; i++){
            System.out.println("\n" + solution[i]);
        }
    }
    private static int[] doomsday_fuel(int[][] matrix){
        int[] solution;
        int[] fraction = new int[]{0,0};
        
        pursuePath(matrix[0], 0, fraction);
        
        List<Integer> terminalStates = new ArrayList<>();
        for(int key : terminalProbabilities.keySet()){
            terminalStates.add(key);
        }
        Collections.sort(terminalStates);
        solution = new int[terminalStates.size() + 1];
        int[]denoms = new int[terminalStates.size()];
        for(int i = 0; i < terminalStates.size(); i++){
            int[] probs = terminalProbabilities.get(terminalStates.get(i));
            solution[i] = probs[0];
            denoms[i] = probs[1];
        }
        printArray(denoms);
        int lcm = lcm(denoms);
        solution[solution.length-1] = lcm;
        for(int i = 0; i < terminalStates.size(); i++){
            int[] probs = terminalProbabilities.get(terminalStates.get(i));
            int conversion = lcm/probs[1];
            solution[i] = probs[0] * conversion;
        }
        return solution;
        
    }
    private static void pursuePath(int[] state, int currentState, int[]probabilityFraction){
        if(sumOfState(state) == 0){
            if(terminalProbabilities.containsKey(currentState)){
                terminalProbabilities.put(currentState, addFractions(terminalProbabilities.get(currentState), probabilityFraction));
            }else{
                terminalProbabilities.put(currentState, probabilityFraction);
            }
            return;
        }
        int denominator = sumOfState(state);
        for(int i = 0; i < state.length; i++){
            if(state[i] != 0){
                int[] stateChangeProbability = new int[]{state[i],denominator};
                if(probabilityFraction[0] != 0){
                   stateChangeProbability = multiplyFractions(probabilityFraction,stateChangeProbability);
                }
                pursuePath(matrix[i], i , stateChangeProbability);
            }
        }
    }
    private static int sumOfState(int[] state){
        int sum = 0;
        for(int i = 0; i < state.length; i++){
            sum += state[i];
        }
        return sum;
    }
    private static int[] addFractions(int[] a,int[] b){
        int[] fraction = new int[2];
        fraction[1] = b[1]*a[1];
        b[0] = b[0]*a[1];
        a[0] = a[0]*b[1];
        fraction[0] = b[0] + a[0];
        if(fraction[1] != 0){
            fraction = simplify(fraction);
        }
        return fraction;
    }
    private static int[] multiplyFractions(int[] a, int[] b){
        int[] fraction = new int[]{
        a[0] * b[0],
        a[1] * b[1]};
        if(fraction[1] != 0){
            fraction = simplify(fraction);
        }
        return fraction;
    }
    private static int[] simplify(int[] fraction){
        int common = gcd(fraction[1],fraction[0]);
        fraction[0] /= common;
        fraction[1] /= common;
        return fraction;
    }
    private static int gcd(int a, int b){
        if(b == 0){
            return a;
        }else{
            return gcd(b,a%b);
        }
    }
    private static int gcd(int[] input){
        int result = input[0];
        for(int i = 1; i < input.length; i++){
            result = gcd(result, input[i]);
        }
        return result;
    }
    private static int lcm(int a, int b){
        return a * (b / gcd(a, b));
    }

    private static int lcm(int[] input){
        int result = input[0];
        for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
    private static void printArray(int[] solution){
        for(int i = 0; i < solution.length; i++){
            System.out.println(solution[i]);
        }
    }
}
    