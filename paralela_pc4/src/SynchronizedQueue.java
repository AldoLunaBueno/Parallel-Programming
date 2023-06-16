import java.util.AbstractMap;
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
        BlockingQueue<Map.Entry<Double[],Double[]>> REQUEST  = new LinkedBlockingQueue<>();
        BlockingQueue<Result>  RESPONSE = new LinkedBlockingQueue<>();

        cargarDatos();
        visualizarDatos();


        //Ejemplo de perturbacion a los datos
        //---------------------------------------
        for(int i=1;i<=N;i++)  {
            DATASET[i][1] = DATASET[i][2] +
                    (int)(Math.random()*10)*0.1  *  ((int)(Math.random()*2)==0?1:-1);
        }
        //---------------------------------------

        Operation OBJ = new Operation(REQUEST,RESPONSE);
        OBJ.start();

        REQUEST.put(new AbstractMap.SimpleEntry<>(getColumn(DATASET, 1), getColumn(DATASET, 2)));
        RESPONSE.take();

        /*
        try {
            for (int i = 0; i < 10; i++) {
                REQUEST.put(123458);
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(RESPONSE.take());
            }
        }
        catch (InterruptedException ERROR) {
            ERROR.printStackTrace();
            System.out.println(ERROR);
        }
        */
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
            DATASET[i][n+m] = 1 + 1.0*(int)(Math.random()*3); break; //CLASE
        }
    }
    public static double[] getColumn(double[][] array, int index){
        double[] column = new double[array[0].length];
        for(int i=0; i<column.length; i++){
            column[i] = array[i][index];
        }
        return column;
    }
}
//====================================================================
class Operation {
    private final BlockingQueue<Map.Entry<Double[],Double[]>> IN;
    private final BlockingQueue<Result>  OUT;


    private static final int N=100; // Tamaño de la muestra
    private static final int n=5; // Tamaño de la entrada
    private static final int m=1; // Tamaño de la salida

    private static double DATASET[][] = new double[N+1][n+m+1];

    //----------------------------------------------------------------
    public Operation(BlockingQueue<Map.Entry<Double[],Double[]>> REQUEST, BlockingQueue<Result> RESPONSE) {
        this.IN  = REQUEST;
        this.OUT = RESPONSE;
    }
    public void start() {
        /*
        new Thread(new Runnable() {
            public void run() {
            int A,B;
                while (true) {
                    try {
                        A  = IN.take();
                        B = SumDivisors (A);
                        OUT.put(new Result(A,B));
                    }
                    catch (InterruptedException ERROR) {
                        ERROR.printStackTrace();
                    }
                }
            }
        }).start();
        */
        new Thread(new Runnable() {
            public void run() {
                Map.Entry<Double[],Double[]> A;
                Double B;
                while (true) {
                    try {
                        A = IN.take();
                        B = dependenciaLineal(A);
                        OUT.put(new Result(A,B));
                    }
                    catch (InterruptedException ERROR) {
                        ERROR.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public static Double dependenciaLineal(Map.Entry<Double[],Double[]> cols) {
        double T;

        for(int h=1;h<=n-1;h++)  {
            for(int k=h+1;k<=n;k++)  {
                T = pearson(h,k);
                System.out.println("Col " + h + " ~ Col " + k + " ==>  Pearson: " + T + "   Error: " + (1-T) );
            }
        }
    }
    public static double pearson(int h, int k) {
        return innerProd(h,k)/(norma(h)* norma(k));
    }
    //------------------------------------------------
    public static double norma(int j) {
        double S;
        S = 0;
        for(int i=1;i<=N;i++)  {
            S = S + Math.pow(DATASET[i][j],2);
        }
        return Math.sqrt(S);
    }
    //------------------------------------------------
    public static double innerProd(int h, int k) {
        double S;
        S = 0;
        for(int i=1;i<=N;i++)  {
            S = S + DATASET[i][h]*DATASET[i][k];
        }
        return S;
    }
    //------------------------------------------------
}
//====================================================================
class Result {
    private final int IN;
    private final int OUT;
    //----------------------------------------------------------------
    public Result(int A, int B) {
        this.IN  = A;
        this.OUT = B;
    }
    //----------------------------------------------------------------
    @Override public String toString() {
        return "\nSuma de Divisores de " + IN + ": " + OUT;
    }
}
//====================================================================
