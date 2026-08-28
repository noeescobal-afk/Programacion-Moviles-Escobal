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

        sc.close();
    }
}