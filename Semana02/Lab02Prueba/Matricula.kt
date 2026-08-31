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
    }
}