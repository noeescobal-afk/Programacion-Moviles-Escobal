fun main() {

    print("Ingrese su nombre completo: ")
    val nombreEstudiante = readLine() ?: ""

    print("Ingrese su turno (M=Manana, T=Tarde, N=Noche): ")
    val turno = readLine()!!.uppercase()

    print("Ingrese su categoria (O=Ordinario, B=Becado): ")
    val categoria = readLine()!!.uppercase()

    print("Ingrese el valor de cada credito (S/): ")
    val valorCredito = readLine()!!.toDouble()

    var precioMatricula = 0.0

    if (categoria == "O") {
        print("Ingrese el precio de la matricula (S/): ")
        precioMatricula = readLine()!!.toDouble()
    }

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


    var porcentajeTurno = 0.0

    if (turno == "M") {
        porcentajeTurno = 0.10
    } else if (turno == "T") {
        porcentajeTurno = 0.15
    } else if (turno == "N") {
        porcentajeTurno = 0.20
    }

    val montoTurno = totalPagar * porcentajeTurno

    val totalConTurno = totalPagar + montoTurno

    val matricula = precioMatricula

    val totalConMatricula = totalConTurno + matricula

    val igv = totalConMatricula * 0.18

    val totalFinal = totalConMatricula + igv

    val numeroCuotas: Int

    if (totalFinal > 2500) {
        numeroCuotas = 3
    } else {
        numeroCuotas = 2
    }

    val montoCuota = totalFinal / numeroCuotas

    println()
    println("Estudiante: $nombreEstudiante")
    println()

    println("+-----------------------------+------------+------------+")
    println(
        String.format(
            "| %-27s | %-10s | %-10s |",
            "Curso",
            "Creditos",
            "Costo"
        )
    )
    println("+-----------------------------+------------+------------+")

    print(tablaCursos)

    println("+-----------------------------+------------+------------+")

    println()
    println("Cursos Matriculados: $cantidadCursos")
    println("Total Creditos: $totalCreditos")
    println("Total cursos: ${totalPagar.toInt()}")
    println("Turno: $turno")
    println("Recargo por turno: ${montoTurno.toInt()}")
    println("Categoria: $categoria")
    println("Matricula: ${matricula.toInt()}")
    println("IGV 18%: ${igv.toInt()}")
    println("Total a pagar: ${totalFinal.toInt()}")
    println("Carga academica: $cargaAcademica")
    println(
        "Forma de pago: $numeroCuotas cuotas de ${montoCuota.toInt()} Soles"
    )
}