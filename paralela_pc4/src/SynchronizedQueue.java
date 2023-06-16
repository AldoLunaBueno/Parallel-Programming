import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//====================================================================
public class SynchronizedQueue {
    private static final int N=100; // Tamaño de la muestra
    private static final int n=5; // Tamaño de la entrada
    private static final int m=1; // Tamaño de la salida

    private static Double DATASET[][] = new Double[N+1][n+m+1];
    //----------------------------------------------------------------
    public static void main(String[] args) {
        BlockingQueue<Map.Entry<Integer[],Double[][]>> REQUEST = new LinkedBlockingQueue<>();
        BlockingQueue<Result> RESPONSE = new LinkedBlockingQueue<>();

        cargarDatos();
        visualizarDatos();


        //Ejemplo de perturbación a los datos
        //---------------------------------------
        for(int i=1;i<=N;i++)  {
            DATASET[i][1] = DATASET[i][2] +
                    (int)(Math.random()*10)*0.1  *  ((int)(Math.random()*2)==0?1:-1);
        }
        //---------------------------------------

        Operation OBJ = new Operation(REQUEST,RESPONSE);
        OBJ.start();

        try {

            int count = 1;
            for(int h=1;h<=n-1;h++)  {
                for(int k=h+1;k<=n;k++)  {
                    REQUEST.put(new AbstractMap.SimpleEntry<>(new Integer[]{h,k}, getTwoColumns(DATASET, h, k)));
                    count++;
                }
            }
            for (int i = 0; i < count; i++) {
                System.out.println(RESPONSE.take());
            }
        }
        catch (InterruptedException ERROR) {
            ERROR.printStackTrace();
            System.out.println(ERROR);
        }
    }
    public static void visualizarDatos() {
        for(int i=1;i<=N;i++)  {
            for(int j=1;j<=n;j++)  {
                System.out.print(DATASET[i][j] + "\t");
            }
            System.out.println(" | " + DATASET[i][n+m]);
        }
        System.out.println();
    }
    //------------------------------------------------
    public static void cargarDatos() {
        for(int j=1;j<=n;j++)  {
            for(int i=1;i<=N;i++)  {
                switch(j) {
                    case 1: DATASET[i][j] = 1.0*500*(1 + (int)(Math.random()*18)); break; //SUELDO
                    case 2: DATASET[i][j] = 1.0*500*(1 + (int)(Math.random()*18)); break; //GASTO
                    case 3: DATASET[i][j] =  1 + 1.0*(int)(Math.random()*9); break; //TOTAL PERSONAS
                    case 4: DATASET[i][j] =  1.0*(int)(Math.random()*2); break; //ESTADO 1
                    case 5: DATASET[i][j] =  1.0*(int)(Math.random()*2); break; //ESTADO 2
                    default: break;
                }
            }
        }
        for(int i=1;i<=N;i++)  {
            DATASET[i][n+m] = 1 + 1.0*(int)(Math.random()*3); //CLASE
        }
    }
    public static Double[][] getTwoColumns(Double[][] array, int index0, int index1){
        Double[][] columns = new Double[array[0].length][2];
        for(int i=0; i<columns.length; i++){
            columns[i][0] = array[i][index0];
            columns[i][1] = array[i][index1];
        }
        return columns;
    }
}
//====================================================================
class Operation {
    private final BlockingQueue<Map.Entry<Integer[],Double[][]>> IN;
    private final BlockingQueue<Result>  OUT;

    //----------------------------------------------------------------
    public Operation(BlockingQueue<Map.Entry<Integer[],Double[][]>> REQUEST, BlockingQueue<Result> RESPONSE) {
        this.IN  = REQUEST;
        this.OUT = RESPONSE;
    }
    public void start() {
        new Thread(new Runnable() {
            public void run() {
                Map.Entry<Integer[],Double[][]> A;
                Double B;
                while (true) {
                    try {
                        A = IN.take();
                        B = dependenciaLineal(A.getValue());
                        OUT.put(new Result(A.getKey(),B));
                    }
                    catch (InterruptedException ERROR) {
                        ERROR.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public static Double dependenciaLineal(Double[][] twoColsReceived) {
        double T;
        T = pearson(twoColsReceived);
        return T;
    }
    public static double pearson(Double[][] twoColsReceived) {
        return innerProd(twoColsReceived)/(norma(twoColsReceived, 0)* norma(twoColsReceived, 1));
    }
    //------------------------------------------------
    public static double norma(Double[][] twoColsReceived, int j) {
        double S;
        S = 0;
        int N = twoColsReceived[0].length;
        for(int i=1;i<=N;i++)  {
            S = S + Math.pow(twoColsReceived[i][j],2);
        }
        return Math.sqrt(S);
    }
    //------------------------------------------------
    public static double innerProd(Double[][] twoColsReceived) {
        double S;
        S = 0;
        int N = twoColsReceived[0].length;
        for(int i=1;i<=N;i++)  {
            S = S + twoColsReceived[i][0]*twoColsReceived[i][1];
        }
        return S;
    }
    //------------------------------------------------
}
//====================================================================
class Result {
    private final Integer[] IN;
    private final Double OUT;
    //----------------------------------------------------------------
    public Result(Integer[] A, Double B) {
        this.IN  = A;
        this.OUT = B;
    }
    //----------------------------------------------------------------
    @Override public String toString() {
        return "Col " + IN[0] + " ~ Col " + IN[1] + " ==>  Pearson: " + OUT + "   Error: " + (1-OUT);
    }
}
//====================================================================
