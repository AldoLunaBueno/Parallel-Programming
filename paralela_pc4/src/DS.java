public class DS {
private static final int N=100;
private static final int n=5;
private static final int m=1;

private static double DATASET[][] = new double[N+1][n+m+1];
	//------------------------------------------------
	public static void DependenciaLineal() {
	double T;

	   //Ejemplo de perturbacion a los datos
	   //---------------------------------------
	   for(int i=1;i<=N;i++)  {
	   	   DATASET[i][1] = DATASET[i][2] +
	   	                   (int)(Math.random()*10)*0.1  *  ((int)(Math.random()*2)==0?1:-1);
	   }
	   //---------------------------------------

	   for(int h=1;h<=n-1;h++)  {
	       for(int k=h+1;k<=n;k++)  {
	           T = Pearson(h,k);
	       	   System.out.println("Col " + h + " ~ Col " + k + " ==>  Pearson: " + T + "   Error: " + (1-T) );
	       }
	   }
    }
	public static double Pearson(int h,int k) {
	   return InnerProd(h,k)/(Norma(h)*Norma(k));
	}
	//------------------------------------------------
	public static double Norma(int j) {
	double S;
	   S = 0;
	   for(int i=1;i<=N;i++)  {
	       S = S + Math.pow(DATASET[i][j],2);
	   }
	   return Math.sqrt(S);
	}
	//------------------------------------------------
	public static double InnerProd(int h,int k) {
	double S;
	   S = 0;
	   for(int i=1;i<=N;i++)  {
	       S = S + DATASET[i][h]*DATASET[i][k];
	   }
	   return S;
	}
	//------------------------------------------------
	public static void VisualizarDatos() {
	   for(int i=1;i<=N;i++)  {
	       for(int j=1;j<=n;j++)  {
		   	   System.out.print(DATASET[i][j] + "\t");
   	   	   }
	   	   System.out.println(" | " + DATASET[i][n+m]);
   	   }
   	   System.out.println();
    }
	//------------------------------------------------
	public static void CargarDatos() {
	   for(int j=1;j<=n;j++)  {
		   for(int i=1;i<=N;i++)  {
		   	   switch(j) {
		   	   	  case 1: DATASET[i][j] = 500*(1 + (int)(Math.random()*18)); break; //SUELDO
		   	   	  case 2: DATASET[i][j] = 500*(1 + (int)(Math.random()*18)); break; //GASTO
		   	   	  case 3: DATASET[i][j] =  1 + (int)(Math.random()*9); break; //TOTAL PERSONAS
		   	   	  case 4: DATASET[i][j] =  (int)(Math.random()*2); break; //ESTADO 1
		   	   	  case 5: DATASET[i][j] =  (int)(Math.random()*2); break; //ESTADO 2
	   	   	     default: break;
	   	   	   }
   	   	   }
   	   }
	   for(int i=1;i<=N;i++)  {
   	   	  DATASET[i][n+m] = 1 + (int)(Math.random()*3); break; //CLASE
   	   }
    }
	//------------------------------------------------
	public static void main(String args[]) {
	   CargarDatos();
	   VisualizarDatos();
	   DependenciaLineal();
	}
	//------------------------------------------------
} //class