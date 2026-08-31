fun main() {

    print("Ingrese su nombre completo: ")
    val nombreEstudiante = readLine() ?: ""

    print("Ingrese el valor de cada credito (S/): ")
    val valorCredito = readLine()!!.toDouble()

    print("Ingrese la cantidad de cursos a matricular: ")
    val cantidadCursos = readLine()!!.toInt()

    var totalCreditos = 0
    var totalPagar = 0.0
    var tablaCursos = ""

    for (i in 1..cantidadCursos) {

        print("Nombre del curso $i: ")
        val nombreCurso = readLine() ?: ""

        print("Creditos del curso $i: ")
        val creditosCurso = readLine()!!.toInt()

        val costoCurso = creditosCurso * valorCredito

        totalCreditos += creditosCurso
        totalPagar += costoCurso

        tablaCursos += String.format(
            "| %-27s | %-10d | %-10.0f |\n",
            nombreCurso,
            creditosCurso,
            costoCurso
        )
    }

    val cargaAcademica: String

    if (totalCreditos <= 12) {
        cargaAcademica = "Malla Regular"
    } else if (totalCreditos <= 18) {
        cargaAcademica = "Carga Completa"
    } else {
        cargaAcademica = "Permiso Autorizado"
    }

    val numeroCuotas: Int

    if (totalPagar > 2500) {
        numeroCuotas = 3
    } else {
        numeroCuotas = 2
    }

    val montoCuota = totalPagar / numeroCuotas
}