/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author matias
 */
import java.lang.String;
import java.util.*;

public class nreinas {

    /**
     * @param args the command line arguments
     */
    public static void crearTablero(int[] cromosoma){
            String[][] tablero= new String[8][8];
            
            for (int i=0;i<8;i++){             
                for(int j=0;j<8;j++){
                    if (cromosoma[j]==i){
                           tablero[i][j]=" R ";
                    }else
                        tablero[i][j]=" . ";
                    System.out.print(tablero[i][j]); 
                } 
                System.out.println();
            }
            
    }
    
    public static int[][] boardBinario(int[] cromosoma){
        int[][] board=new int[8][8];
        
        for (int i=0;i<8;i++){
            for(int j=0;j<8;j++){
               if(cromosoma[j]==i)
                   board[i][j]=1;
               else
                   board[i][j]=0;
               //System.out.print(board[i][j]);
            }
            //System.out.println();
        }
        return board;
    }

    static int N = 8;
    static int num_poblacion = 2000;
    static int cant_mutaciones=0;
    static double costo = 0;
    static int costoMin = 20;
    static int idCostoMin = 0;
    static int genContador = 1; //numero de generaciones
    static int genMax = 400;
    
    
    static int num_generaciones=1;
    static int max_generaciones=500;
    static Random rnd = new Random();
    
    static class Cromosoma{
        public int[] genes;
        public int fitness;
        public Cromosoma(){}
        public Cromosoma(int[] genes,int fitness){this.genes = genes ; this.fitness = fitness;}
        
        public int[] getGenes(){return genes;}
        public int getFitness(){return fitness;}
        
        public void setFitness(int fit) {
		this.fitness = fit;
	}
        
        public int retornaGen(int k){
            return genes[k];
        }
        
        public void printGenes(){
            System.out.print("[");
            for (int i=0;i<genes.length;i++){
                System.out.print(" "+genes[i]+",");
            }
            System.out.print("] Fitness: "+fitness+"\n");
        }
        
        
    }
    
    static ArrayList<Cromosoma> poblacion = new ArrayList<Cromosoma>();
    static ArrayList<Cromosoma> seleccion = new ArrayList<Cromosoma>();
    static ArrayList<Cromosoma> siguiente_generacion = new ArrayList<Cromosoma>();
    static ArrayList<Cromosoma> soluciones = new ArrayList<Cromosoma>();
    static ArrayList<String> cromosoma = new ArrayList<String>();
    
    public static void iniciar_poblacion(){
        int [] genes;
        for (int i=0; i<num_poblacion;i++){
            genes = new int [N];
            for (int j=0; j<N;j++){
                int num=rnd.nextInt(8);
                genes[j]=num;
                //System.out.print(" "+genes[j]+" ");
            }
            //System.out.println();
            int [][] tablero= boardBinario(genes);
            int costo = getCosto(tablero);
            Cromosoma tmp = new Cromosoma(genes,costo);
            
            poblacion.add(tmp);
            
        }
        
    }
    
    /*
    public static void setFitness(ArrayList<Cromosoma> poblacion){
        
        for (int i=0;i<poblacion.size();i++){
            int fitness=0;
            Cromosoma actual = poblacion.get(i);
            //int [][] solucion = boardBinario(actual.genes);
            
            for (int gen=0;gen<8;gen++){
                //int fila = actual.genes[gen];
                for (int m=gen+1;m<8;m++){
                    if( (Math.abs(actual.genes[gen] - actual.genes[m]) == Math.abs(m-gen)))
                        fitness++;
                    
                }
            }
            if(fitness<costoMin){
                costoMin=fitness;
                idCostoMin=i;
            }
            actual.setFitness(fitness);
            
        }
    }
    */
    public static void poblacionIntermedia(ArrayList<Cromosoma> poblacion){
        for(int i=0;i<poblacion.size();i++){
            Cromosoma temp = poblacion.get(i);
            int valFitness=-1;
            valFitness=temp.fitness;
            if(valFitness<11){
                seleccion.add(temp);
                
            }
            
        }
    }
    
    public static void algoritmo(ArrayList<Cromosoma> poblacion){
        int cont=1;
        while(true){
            ArrayList<Cromosoma> sol = new ArrayList<Cromosoma>();
            for (int i=0; i<poblacion.size();i++){
                Cromosoma x = seleccion(poblacion);
                Cromosoma y = seleccion(poblacion);
                Cromosoma hijo = reproducir(x,y);
                siguiente_generacion.add(hijo);
                if(siguiente_generacion.get(i).fitness==0){
                    System.out.println("SOlucion encontrada");
                    crearTablero(siguiente_generacion.get(i).genes);
                    sol.add(siguiente_generacion.get(i));
                    siguiente_generacion.get(i).printGenes();
                }
            }
            poblacion=siguiente_generacion;
            System.out.println("Generacion:"+cont);
            cont++;
        }
    }
    
    
    public static void fitness(ArrayList<Cromosoma> lista){
        for (int i=0;i<lista.size();i++){
            int fit = lista.get(i).fitness;
            if(fit<costoMin){
                costoMin=fit;
            }
        }
    }
    
    
    public static int getCosto(int[][] cromosoma){
        int valor=0;
        int posFila=0;
        int posColumna=0;
        int tam=cromosoma.length;
        
        for (int i=0;i<tam;i++){
            for(int j=0;j<tam;j++){
                
                if(cromosoma[j][i]==1){
                    posFila=j;
                    posColumna=i;
                    
                    break;
                }
            }
            
            //calcular reinas en ataque
            
            for (int k=1; k<tam;k++){
                //diagonal superior
                if((((posFila-k) >= 0) && (posColumna+k)<tam) && cromosoma[posFila-k][posColumna+k] == 1){
            valor +=1;
                    }
                
                if((((posFila+k) < tam) && (posColumna+k)<tam) && cromosoma[posFila+k][posColumna+k] == 1){
            valor +=1;
                    }
                
                if (((posColumna+k) < tam)&& cromosoma[posFila][posColumna+k] == 1){
                    valor+=1;
                }
            }
        }
        return valor;
    }
    
    
    static Cromosoma reproducir(Cromosoma x, Cromosoma y){
        int c = rnd.nextInt(7);
        int[] genesX = x.getGenes();
        int[] genesY = y.getGenes();
        int [] hijo1 = new int [N];
        int [] hijo2 = new int [N];
        for (int i=0; i<N;i++){
            if(i<c){
                hijo1[i]=genesX[i];
                hijo2[i]=genesY[i];
            }else{
                hijo1[i]=genesY[i];
                hijo2[i]=genesX[i];
            }
        }
        Cromosoma child1 = new Cromosoma(hijo1,getCosto(boardBinario(hijo1)));
        Cromosoma child2 = new Cromosoma(hijo2,getCosto(boardBinario(hijo2)));
        
        seleccion.add(child1);
        seleccion.add(child2);
        
        siguiente_generacion.add(child1);
        siguiente_generacion.add(child2);
        
        return child1;
        
    }
    
    
   
    
    public static Cromosoma seleccion (ArrayList<Cromosoma> poblacion){
        Cromosoma elegido = new Cromosoma();
        if(poblacion.isEmpty()){
            System.out.println("No existen individuos");
            
        }else{
            
            int tamano = poblacion.size();
            int rand = rnd.nextInt(tamano);
            elegido = poblacion.get(rand);
            
        }
        
        return elegido;
    }
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        /*
        Vector<Integer> cromosoma = new Vector<Integer>();
        cromosoma.add(1);
        System.out.println(cromosoma.get(0));
        int gen[] = {0,2,7,2,3,1,5,4};
        ArrayList<String> prueba = new ArrayList<String>();
        prueba.add("23453");
        prueba.add("9");
        
        
        int num = Integer.parseInt(prueba.get(0));
        System.out.println("String to int : "+num);
        String numeros = prueba.get(0);
        
        // pasar string a array        
        int[] array = new int[numeros.length()];
        for (int i=0; i<numeros.length();i++){
            array[i]=numeros.charAt(i) -'0';
        }
        
        for (int i=0 ; i<numeros.length();i++){
            System.out.println("Numero en arreglo: " +array[i]);
        }
        
        
        System.out.println(prueba.get(1));
        */
        
        
        
        
        iniciar_poblacion();
                
       poblacionIntermedia(poblacion); //selecciona los individuos mas aptos
       
        System.out.println("Generacion intermedia n="+seleccion.size());
        
        algoritmo(seleccion); //inicia el algoritmo con la poblacion mas apta.
        
        /*
         int [] gen1 = {5,3,0,4,7,1,6,2};
        Cromosoma prueba = new Cromosoma(gen1,0);
        int [][] tab = new int[8][8];
        tab = boardBinario(gen1);
         int fit=-1;
         fit = getCosto(tab);
         crearTablero(gen1);
         System.out.println("fitnes: "+fit);
        */
        
        /*
        for(int i=0;i<seleccion.size();i++){
            
            int[][] binario = new int[8][8];
             binario = boardBinario(seleccion.get(i).genes);
            
            seleccion.get(i).setFitness(getCosto(binario));
            
            
        }  
        */
        //generar();
        //setFitness(seleccion);
        //System.out.println("costo minimo: "+costoMin+"en pos: "+idCostoMin);
        //seleccion.get(idCostoMin).printGenes();
        //Cromosoma hijo = reproducir(poblacion.get(0),poblacion.get(1));
        
        
        //int [] gen = {6,4,2,0,5,7,1,3};
        
        /*
        seleccion = new ArrayList<Cromosoma>();
        seleccion.add(prueba);
        setFitness(seleccion);
        
        
        boardBinario(gen);
        System.out.println("FItnees:: "+seleccion.get(0).fitness);
        
        //crearTablero(hijo.genes);
        //hijo.printGenes();
        //boardBinario(hijo.genes);
        
*/

    }
    
}
