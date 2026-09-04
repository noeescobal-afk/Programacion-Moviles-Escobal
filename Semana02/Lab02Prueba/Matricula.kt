fun main() {

    // AFORO
    var aforo = 0

    while (aforo <= 0) {
        print("Ingrese el aforo maximo: ")
        aforo = readLine()?.toIntOrNull() ?: 0

        if (aforo <= 0) {
            println("Error: el aforo debe ser mayor que 0.")
        }
    }

    var cantidadPersonas = 0
    var continuar = "S"

    // RESUMEN DE TODAS LAS PERSONAS
    var resumenPersonas = ""

    // REGISTRAR PERSONAS
    while (cantidadPersonas < aforo && continuar == "S") {

        cantidadPersonas++

        println()
        println("======================================")
        println("          PERSONA $cantidadPersonas")
        println("======================================")

        // NOMBRE
        var nombreEstudiante = ""

        while (nombreEstudiante.isBlank() || !nombreEstudiante.all { it.isLetter() || it.isWhitespace() }) {

            print("Ingrese su nombre completo: ")
            nombreEstudiante = readLine() ?: ""

            if (nombreEstudiante.isBlank()) {
                println("Error: el nombre no puede estar vacio.")
            } else if (!nombreEstudiante.all { it.isLetter() || it.isWhitespace() }) {
                println("Error: el nombre solo puede contener letras y espacios.")
            }
        }

        // TURNO
        var turno = ""

        while (turno != "M" && turno != "T" && turno != "N") {
            print("Ingrese su turno (M=Manana, T=Tarde, N=Noche): ")
            turno = readLine()?.uppercase() ?: ""

            if (turno != "M" && turno != "T" && turno != "N") {
                println("Error: ingrese M, T o N.")
            }
        }

        // CATEGORIA
        var categoria = ""

        while (categoria != "O" && categoria != "B") {
            print("Ingrese su categoria (O=Ordinario, B=Becado): ")
            categoria = readLine()?.uppercase() ?: ""

            if (categoria != "O" && categoria != "B") {
                println("Error: ingrese O o B.")
            }
        }

        // MATRICULA
        var precioMatricula = 0.0

        if (categoria == "O") {

            var matriculaValida = false

            while (!matriculaValida) {
                print("Ingrese el precio de la matricula (S/): ")
                val entrada = readLine()?.toDoubleOrNull()

                if (entrada != null && entrada > 0) {
                    precioMatricula = entrada
                    matriculaValida = true
                } else {
                    println("Error: la matricula debe ser mayor que 0.")
                }
            }
        }

        // VALOR DEL CREDITO
        var valorCredito = 0.0

        while (valorCredito <= 0) {
            print("Ingrese el valor de cada credito (S/): ")
            valorCredito = readLine()?.toDoubleOrNull() ?: 0.0

            if (valorCredito <= 0) {
                println("Error: el valor del credito debe ser mayor que 0.")
            }
        }

        // CANTIDAD DE CURSOS
        var cantidadCursos = 0

        while (cantidadCursos <= 0) {
            print("Ingrese la cantidad de cursos a matricular: ")
            cantidadCursos = readLine()?.toIntOrNull() ?: 0

            if (cantidadCursos <= 0) {
                println("Error: debe ingresar al menos 1 curso.")
            }
        }

        var totalCreditos = 0
        var totalPagar = 0.0
        var tablaCursos = ""

        // REGISTRO DE CURSOS
        for (i in 1..cantidadCursos) {

            var nombreCurso = ""

            while (nombreCurso.isBlank()) {
                print("Nombre del curso $i: ")
                nombreCurso = readLine() ?: ""

                if (nombreCurso.isBlank()) {
                    println("Error: el nombre del curso no puede estar vacio.")
                }
            }

            var creditosCurso = 0

            while (creditosCurso <= 0) {
                print("Creditos del curso $i: ")
                creditosCurso = readLine()?.toIntOrNull() ?: 0

                if (creditosCurso <= 0) {
                    println("Error: los creditos deben ser mayores que 0.")
                }
            }

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

        // CARGA ACADEMICA
        val cargaAcademica: String

        if (totalCreditos <= 12) {
            cargaAcademica = "Malla Regular"
        } else if (totalCreditos <= 18) {
            cargaAcademica = "Carga Completa"
        } else {
            cargaAcademica = "Permiso Autorizado"
        }

        // RECARGO POR TURNO
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

        // MATRICULA
        val totalConMatricula = totalConTurno + precioMatricula

        // IGV
        val igv = totalConMatricula * 0.18

        val totalFinal = totalConMatricula + igv

        // CUOTAS
        val numeroCuotas: Int

        if (totalFinal > 2500) {
            numeroCuotas = 3
        } else {
            numeroCuotas = 2
        }

        val montoCuota = totalFinal / numeroCuotas

        // GUARDAR RESUMEN DE LA PERSONA
        resumenPersonas += """
            
            Persona $cantidadPersonas
            Nombre: $nombreEstudiante
            Turno: $turno
            Categoria: $categoria
            Cursos: $cantidadCursos
            Creditos: $totalCreditos
            Total a pagar: S/ ${totalFinal.toInt()}
            --------------------------------------
            
        """.trimIndent()

        // MOSTRAR MATRICULA
        println()
        println("======================================")
        println("       RESUMEN DE MATRICULA")
        println("======================================")

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
        println("Matricula: ${precioMatricula.toInt()}")
        println("IGV 18%: ${igv.toInt()}")
        println("Total a pagar: ${totalFinal.toInt()}")
        println("Carga academica: $cargaAcademica")
        println("Forma de pago: $numeroCuotas cuotas de ${montoCuota.toInt()} Soles")

        // PREGUNTAR SI AGREGA OTRA PERSONA
        if (cantidadPersonas < aforo) {

            println()
            print("¿Desea agregar otra persona? (S/N): ")

            continuar = readLine()?.uppercase() ?: ""

            while (continuar != "S" && continuar != "N") {
                println("Error: solo ingrese S o N.")
                print("¿Desea agregar otra persona? (S/N): ")
                continuar = readLine()?.uppercase() ?: ""
            }

        } else {
            println()
            println("======================================")
            println("           AFORO COMPLETO")
            println("======================================")
            println("No se pueden registrar mas personas.")
        }
    }

    // RESUMEN FINAL DE PERSONAS
    println()
    println("======================================")
    println("       RESUMEN DE PERSONAS")
    println("======================================")

    print(resumenPersonas)

    println("Aforo maximo: $aforo")
    println("Personas registradas: $cantidadPersonas")
    println("Espacios disponibles: ${aforo - cantidadPersonas}")

    println("======================================")
}
