import java.util.Scanner;

public class Matricula {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ================== COMMIT 1: DATOS DE ENTRADA ==================
        System.out.print("Ingrese su nombre completo: ");
        String nombreEstudiante = sc.nextLine();

        System.out.print("Ingrese el valor de cada credito (S/): ");
        double valorCredito = sc.nextDouble();
        sc.nextLine();

        System.out.print("Ingrese la cantidad de cursos a matricular: ");
        int cantidadCursos = sc.nextInt();
        sc.nextLine();

        String[] nombreCursos = new String[cantidadCursos];
        int[] creditosCursos = new int[cantidadCursos];

        for (int i = 0; i < cantidadCursos; i++) {
            System.out.print("Nombre del curso " + (i + 1) + ": ");
            nombreCursos[i] = sc.nextLine();

            System.out.print("Creditos del curso " + (i + 1) + ": ");
            creditosCursos[i] = sc.nextInt();
            sc.nextLine();
        }
        // ================== COMMIT 2: CALCULOS ==================
        double[] costoCursos = new double[cantidadCursos];
        int totalCreditos = 0;
        double totalPagar = 0;

        for (int i = 0; i < cantidadCursos; i++) {
            costoCursos[i] = creditosCursos[i] * valorCredito;
            totalCreditos += creditosCursos[i];
            totalPagar += costoCursos[i];
        }

        String cargaAcademica;
        if (totalCreditos <= 12) {
            cargaAcademica = "Malla Regular";
        } else if (totalCreditos <= 18) {
            cargaAcademica = "Carga Completa";
        } else {
            cargaAcademica = "Renuncia Autorizada";
        }

        int numeroCuotas;
        if (totalPagar > 2500) {
            numeroCuotas = 3;
        } else {
            numeroCuotas = 2;
        }
        double montoCuota = totalPagar / numeroCuotas;
        sc.close();
    }
}