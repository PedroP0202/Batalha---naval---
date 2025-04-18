fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int): Boolean {
    return (numLinhas == numColunas) && (numLinhas in setOf(4, 5, 7, 8, 10))
}

fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 1
    var letras = ""

    while (count <= numColunas) {
        if (numColunas <= 26) {
            val letra = obterLetra(count)
            letras += letra
        }

        if (count < numColunas) {
            letras += " | "
        }
        count++
    }

    return letras
}

fun obterLetra(numero: Int): Char {
    return (numero + 64).toChar()  // A = 65 na tabela ASCII
}

fun criaTerreno(numLinhas: Int, numColunas: Int): String {
    var countLinhas = 1
    var espacos = "\n"

    espacos += "| ${criaLegendaHorizontal(numColunas)} |\n"

    while (countLinhas <= numLinhas) {
        var countColunas = 0

        while (countColunas < numColunas) {
            espacos += "| ~ "
            countColunas++
        }

        espacos += "| $countLinhas\n"
        countLinhas++
    }

    return espacos
}






var numLinhas = -1
var numColunas = -1
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    // Esta função recebe uma string de coordenadas, o número de linhas e o número de colunas.
    // Divide a string de coordenadas pelos caracteres ","
    val partes = coordenadas.split(",")

    // Verifica se existem exatamente duas partes após a divisão
    if (partes.size != 2) {
        return null
    }

    // Extrai as partes correspondentes à linha e à coluna
    val linhaStr = partes[0].trim()
    val colunaStr = partes[1].trim()

    // Converte a parte da linha para um número inteiro ou retorna null se não for um número válido
    val linha = linhaStr.toIntOrNull()

    // Extrai o primeiro caractere da parte da coluna e converte-o para maiúsculo
    if (colunaStr.isEmpty()) return null
    val colunaChar = colunaStr[0].uppercaseChar()

    // Se a linha não for válida ou o caractere da coluna não estiver entre 'A' e 'Z', retorna null
    if (linha == null || colunaChar !in 'A'..'Z') {
        return null
    }

    // Calcula o número da coluna (sendo A = 1, B = 2, etc.)
    val coluna = colunaChar - 'A' + 1

    // Verifica se as coordenadas estão dentro dos limites da grade especificada
    if (linha in 1..numLinhas && coluna in 1..numColunas) {
        // Se as coordenadas forem válidas, retorna-as como um Par de inteiros
        return Pair(linha, coluna)
    }

    // Se as coordenadas estiverem fora dos limites, retorna null
    return null
}
fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int?> {
    val tamanhoTabuleiro = numLinhas * numColunas
    return when (tamanhoTabuleiro) {
        16 -> arrayOf(2, 0, 0, 0) // Tabuleiro 4x4: 2 submarinos, 0 contra-torpedeiros, 0 navios-tanque, 0 porta-aviões
        25 -> arrayOf(1, 1, 1, 0) // Tabuleiro 5x5: 1 submarino, 1 contra-torpedeiro, 1 navio-tanque, 0 porta-aviões
        49 -> arrayOf(2, 1, 1, 1) // Tabuleiro 7x7: 2 submarinos, 1 contra-torpedeiro, 1 navio-tanque, 1 porta-aviões
        64 -> arrayOf(2, 2, 1, 1) // Tabuleiro 8x8: 2 submarinos, 2 contra-torpedeiros, 1 navio-tanque, 1 porta-aviões
        100 -> arrayOf(3, 2, 1, 1) // Tabuleiro 10x10: 3 submarinos, 2 contra-torpedeiros, 1 navio-tanque, 1 porta-aviões
        else -> arrayOfNulls(4) // Tamanho não especificado: array nulo com 4 elementos
    }
}

fun coordenadaContida(tamanhoDoTabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    return linha in 1..tamanhoDoTabuleiro.size &&
            coluna in 1..tamanhoDoTabuleiro[0].size
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val coordenadasNaoZero = coordenadas.filter { it != Pair(0, 0) }
    return coordenadasNaoZero.toTypedArray()
}
fun preencheTabuleiroComputador(
    tabuleiroVazio: Array<Array<Char?>>,
    coordenadas: Array<Int>
): Array<Array<Char?>> {
    var submarinosInseridos = 0
    var contraTorpedeirosInseridos = 0
    val tamanhoTabuleiro = tabuleiroVazio.size

    // Função interna para inserir navios no tabuleiro
    fun insereNavio(linha: Int, coluna: Int, navio: Char, tamanho: Int) {
        for (i in linha until (linha + tamanho)) {
            if (i in tabuleiroVazio.indices && coluna in tabuleiroVazio[i].indices) {
                if (tabuleiroVazio[i][coluna] == null) {
                    tabuleiroVazio[i][coluna] = navio
                } else {
                    println("Coordenadas ocupadas. Não foi possível inserir o navio $navio.")
                    return
                }
            } else {
                println("Coordenadas fora do limite para o navio $navio.")
                return
            }
        }
    }

    // Itera sobre as coordenadas fornecidas para posicionar os navios
    for (count in 0 until coordenadas.size step 2) {
        val linha = coordenadas[count]
        val coluna = coordenadas[count + 1]

        if (linha in 0 until tamanhoTabuleiro && coluna in 0 until tamanhoTabuleiro) {
            when (tamanhoTabuleiro) {
                4 -> {
                    if (submarinosInseridos < 2) {
                        insereNavio(linha, coluna, '1', 1)
                        submarinosInseridos++
                    }
                }
                5 -> {
                    if (submarinosInseridos < 1 && contraTorpedeirosInseridos < 1) {
                        insereNavio(linha, coluna, '1', 1)
                        insereNavio(linha + 1, coluna, '2', 2)
                        insereNavio(linha + 3, coluna, '3', 3)
                        submarinosInseridos++
                        contraTorpedeirosInseridos++
                    }
                }
                // Outros tamanhos podem ser adicionados aqui
                else -> {
                    println("Tamanho de tabuleiro não tratado.")
                }
            }
        } else {
            println("Coordenadas inválidas para posicionar o navio.")
        }
    }

    return tabuleiroVazio
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    // Verifica se cada coordenada está dentro dos limites do tabuleiro
    for ((linha, coluna) in coordenadas) {
        if (!coordenadaContida(tabuleiro, linha, coluna)) {
            return false // Se alguma coordenada estiver fora do tabuleiro, retorna falso
        }
    }

    // Verifica se cada posição nas coordenadas fornecidas está livre (nula) no tabuleiro
    for ((linha, coluna) in coordenadas) {
        if (tabuleiro[linha - 1][coluna - 1] != null) {
            return false // Se alguma posição não estiver livre, retorna falso
        }
    }

    return true // Todas as coordenadas são válidas e livres
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    val caractereSubmarino = '1'        // Submarino
    val caractereContraTorpedeiro = '2' // Contra-torpedeiro
    val caractereNavioTanque = '3'      // Navio-tanque

    val linhaAjustada = linha - 1
    val colunaAjustada = coluna - 1

    // Verifica limites
    if (linhaAjustada !in tabuleiro.indices || colunaAjustada !in tabuleiro[0].indices) {
        return false // Fora dos limites do tabuleiro
    }

    // Verifica o caractere da posição
    val charNaPosicao = tabuleiro[linhaAjustada][colunaAjustada]

    // Se for submarino, está sempre completo
    if (charNaPosicao == caractereSubmarino) {
        return true
    }

    // Se não for um tipo válido de navio, retorna false
    if (charNaPosicao != caractereContraTorpedeiro && charNaPosicao != caractereNavioTanque) {
        return false
    }

    // Verifica se algum vizinho tem o mesmo caractere (parte do mesmo navio)
    val vizinhos = arrayOf(
        Pair(linhaAjustada + 1, colunaAjustada),
        Pair(linhaAjustada - 1, colunaAjustada),
        Pair(linhaAjustada, colunaAjustada + 1),
        Pair(linhaAjustada, colunaAjustada - 1)
    )

    for ((vizLinha, vizColuna) in vizinhos) {
        if (
            vizLinha in tabuleiro.indices &&
            vizColuna in tabuleiro[0].indices &&
            tabuleiro[vizLinha][vizColuna] == charNaPosicao
        ) {
            return true // Encontrou parte adjacente do mesmo navio
        }
    }

    return false // Não há partes adjacentes: navio incompleto
}

fun lancarTiro(
    tabuleiroRealComputador: Array<Array<Char?>>,
    tabuleiroPalpitesHumano: Array<Array<Char?>>,
    coordenadaDoTiro: Pair<Int, Int>
): String {
    val linha = coordenadaDoTiro.first
    val coluna = coordenadaDoTiro.second

    // Converte para índices (0-based)
    val linhaC = linha - 1
    val colunaC = coluna - 1

    // Verifica se a coordenada está dentro dos limites
    if (
        linhaC !in tabuleiroRealComputador.indices ||
        colunaC !in tabuleiroRealComputador[0].indices
    ) {
        return "Coordenadas inválidas!"
    }

    // Obtém o conteúdo da posição do tabuleiro real
    val resultadoDoTiro = tabuleiroRealComputador[linhaC][colunaC]

    // Atualiza o tabuleiro de palpites com base no resultado
    return when (resultadoDoTiro) {
        null -> {
            tabuleiroPalpitesHumano[linhaC][colunaC] = 'X' // Água
            "Água."
        }
        '1' -> {
            tabuleiroPalpitesHumano[linhaC][colunaC] = '1' // Submarino
            "Tiro num submarino."
        }
        '2' -> {
            tabuleiroPalpitesHumano[linhaC][colunaC] = '2' // Contra-torpedeiro
            "Tiro num contra-torpedeiro."
        }
        '3' -> {
            tabuleiroPalpitesHumano[linhaC][colunaC] = '3' // Navio-tanque
            "Tiro num navio-tanque."
        }
        '4' -> {
            tabuleiroPalpitesHumano[linhaC][colunaC] = '4' // Porta-aviões
            "Tiro num porta-aviões."
        }
        else -> {
            tabuleiroPalpitesHumano[linhaC][colunaC] = 'X' // Valor inesperado tratado como água
            "Água."
        }
    }
}

fun insereNavioSimples(
    tabuleiro: Array<Array<Char?>>,
    linha: Int,
    coluna: Int,
    dimensao: Int
): Boolean {
    val numLinhas = tabuleiro.size
    val numColunas = tabuleiro[0].size

    // Verifica se a posição inicial está dentro dos limites do tabuleiro
    if (linha <= 0 || coluna <= 0 || linha > numLinhas || coluna > numColunas) {
        return false // Posição inicial fora dos limites
    }

    val navioChar = when (dimensao) {
        1 -> '1' // Submarino
        2 -> '2' // Contra-torpedeiro
        3 -> '3' // Navio-tanque
        4 -> '4' // Porta-aviões
        else -> return false // Dimensão inválida
    }

    // Verifica se o navio vai ultrapassar os limites do tabuleiro na horizontal
    if (coluna + dimensao - 1 > numColunas) {
        return false // Navio ultrapassa os limites do tabuleiro na horizontal
    }

    // Insere o navio na posição correta
    for (count in 0 until dimensao) {
        val linhaCorrigida = linha - 1
        val colunaCorrigida = coluna - 1
        tabuleiro[linhaCorrigida][colunaCorrigida + count] = navioChar // Insere o navio
    }
    return true // Navio inserido com sucesso
}

fun juntarCoordenadas(
    coordenadas: Array<Pair<Int, Int>>,
    coordenadas1: Array<Pair<Int, Int>>
): Array<Pair<Int, Int>> {
    // Calcula o tamanho do novo array combinado
    val tamanhoNovoArray = coordenadas.size + coordenadas1.size

    // Cria um novo array para armazenar as coordenadas combinadas
    val coordenadasCombinadas = Array<Pair<Int, Int>>(tamanhoNovoArray) { Pair(-1, -1) }

    var count = 0 // Inicializa o contador para preencher o novo array

    // Copia as coordenadas do primeiro array para o novo array combinado
    for (coord in coordenadas) {
        coordenadasCombinadas[count] = coord
        count++
    }

    // Copia as coordenadas do segundo array para o novo array combinado
    for (coord in coordenadas1) {
        coordenadasCombinadas[count] = coord
        count++
    }

    return coordenadasCombinadas // Retorna o novo array combinado
}

//fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
//   // Obtém o número de linhas e colunas do tabuleiro
//    val linhas = tabuleiro.size
//    val colunas = if (linhas > 0) tabuleiro[0].size else 0
//    var totalTiros = 0 // Variável para contar o total de tiros no tabuleiro
//
//    // Percorre o tabuleiro verificando as células
//    for (linha in 0 until linhas) {
//        for (coluna in 0 until colunas) {
//            val celula = tabuleiro[linha][coluna] // Obtém o conteúdo da célula atual
//
//            // Se a célula não for nula, significa que foi atingido um navio
//            if (celula != null) {
//                totalTiros++
//            }
//        }
//    }

//    // Retorna verdadeiro se houver pelo menos um tiro no tabuleiro
//    return totalTiros >= 1
//}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
// Obtém o número de linhas e colunas do tabuleiro
    val linhas = tabuleiro.size
    val colunas = if (linhas > 0) tabuleiro[0].size else 0
    var totalTiros = 0
    var linha = 0
    while (linha < linhas) {
        var coluna = 0
        while (coluna < colunas) {
            val celula = tabuleiro[linha][coluna]
            val dimensao = celula?.toString()?.toIntOrNull()
            if (dimensao != null) {

                totalTiros++
            }
            coluna++ // Move para a próxima coluna
        }
        linha++ // Move para a próxima linha
    }
// Retorna verdadeiro se houver pelo menos dois tiros no tabuleiro
    return totalTiros >= 1
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    var countNavios = 0 // Variável para armazenar a contagem de navios encontrados
    var linha = 0 // Variável para controlar a linha atual no tabuleiro
    var coluna = 0 // Variável para controlar a coluna atual no tabuleiro
    var dimensao1 = 0 // Variável para contar o número de células consecutivas com a dimensão específica

    // O loop percorre o tabuleiro linha por linha
    while (linha < tabuleiro.size) {
        // Verifica se a célula é do tipo que estamos buscando (dimensão de navio)
        val celula = tabuleiro[linha][coluna]?.toString()?.toIntOrNull()

        // Se a célula tem a dimensão procurada, incrementa o contador
        if (celula == dimensao) {
            dimensao1++

            // Se encontramos o número suficiente de células para formar o navio, conta o navio
            if (dimensao1 == dimensao) {
                countNavios++
                dimensao1 = 0 // Reinicia a contagem para a próxima sequência
            }
        }

        // Move para a próxima coluna
        coluna++

        // Se a coluna atingir o tamanho máximo, reinicia para a próxima linha
        if (coluna == tabuleiro[linha].size) {
            coluna = 0
            linha++
        }
    }

    return countNavios // Retorna o total de navios encontrados
}

fun gerarCoordenadasNavio(
    tabuleiro: Array<Array<Char?>>,
    linha: Int,
    coluna: Int,
    orientacao: String,
    dimensao: Int
): Array<Pair<Int, Int>> {

    val arrayFinal = Array(dimensao) { Pair(0, 0) }

    // Função auxiliar para verificar se a coordenada está dentro dos limites do tabuleiro
    fun coordenadaValida(linha: Int, coluna: Int): Boolean {
        return linha in 0 until tabuleiro.size && coluna in 0 until tabuleiro[0].size
    }

    // Loop para gerar as coordenadas conforme a orientação
    for (i in 0 until dimensao) {
        val (novaLinha, novaColuna) = when (orientacao) {
            "E" -> Pair(linha, coluna + i)  // Leste
            "N" -> Pair(linha - i, coluna)  // Norte
            "S" -> Pair(linha + i, coluna)  // Sul
            "O" -> Pair(linha, coluna - i)  // Oeste
            else -> return emptyArray() // Orientação inválida
        }

        // Verifica se a nova coordenada está válida
        if (!coordenadaValida(novaLinha, novaColuna)) {
            return emptyArray() // Retorna array vazio se coordenada fora dos limites
        }

        // Atribui a coordenada válida ao array
        arrayFinal[i] = Pair(novaLinha, novaColuna)
    }

    return arrayFinal // Retorna o array de coordenadas
}

fun estaValida(tabuleiro: Array<Array<Char?>>, coordenada: Pair<Int, Int>): Pair<Int, Int> {
    // Verifica se a coordenada está dentro dos limites do tabuleiro
    return if (coordenadaContida(tabuleiro, coordenada.first, coordenada.second)) {
        // Se a coordenada estiver dentro do tabuleiro, retorna a própria coordenada
        coordenada
    } else {
        // Se a coordenada estiver fora do tabuleiro, retorna (0, 0)
        Pair(0, 0)
    }
}

fun coordenadasResto(
    tabuleiro: Array<Array<Char?>>,
    linha: Int,
    coluna: Int,
    orientacao: String,
    dimensao: Int
): Array<Pair<Int, Int>> {
    // Obtém as coordenadas do navio na posição inicial e na orientação especificada
    val coordenadasDoNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)

    // Se não houver coordenadas para o navio, retorna um array vazio
    if (coordenadasDoNavio.isEmpty()) {
        return emptyArray()
    }

    // Inicializa arrays para armazenar as coordenadas das casas adjacentes
    val casasAdjacente1 = Array<Pair<Int, Int>>(dimensao) { Pair(0, 0) }
    val casasAdjacente2 = Array<Pair<Int, Int>>(dimensao) { Pair(0, 0) }

    // Determina a casa inicial do navio dependendo da orientação
    var primeiraCasa = coordenadasDoNavio.first()
    if (orientacao == "N" || orientacao == "O") {
        primeiraCasa = coordenadasDoNavio.last()
    }

    // Gera as coordenadas das casas adjacentes baseadas na orientação
    if (orientacao == "E" || orientacao == "O") {
        val coordenadaAcima = Pair(primeiraCasa.first - 1, primeiraCasa.second)
        val coordenadaAbaixo = Pair(primeiraCasa.first + 1, primeiraCasa.second)

        // Preenche as casas adjacentes na direção vertical
        for (i in 0 until dimensao) {
            val acima = estaValida(tabuleiro, Pair(coordenadaAcima.first, coordenadaAcima.second + i))
            val abaixo = estaValida(tabuleiro, Pair(coordenadaAbaixo.first, coordenadaAbaixo.second + i))
            casasAdjacente1[i] = acima
            casasAdjacente2[i] = abaixo
        }
    }

    if (orientacao == "N" || orientacao == "S") {
        val coordenadaEsquerda = Pair(primeiraCasa.first, primeiraCasa.second - 1)
        val coordenadaDireita = Pair(primeiraCasa.first, primeiraCasa.second + 1)

        // Preenche as casas adjacentes na direção horizontal
        for (i in 0 until dimensao) {
            val esquerda = estaValida(tabuleiro, Pair(coordenadaEsquerda.first + i, coordenadaEsquerda.second))
            val direita = estaValida(tabuleiro, Pair(coordenadaDireita.first + i, coordenadaDireita.second))
            casasAdjacente1[i] = esquerda
            casasAdjacente2[i] = direita
        }
    }

    // Combina as coordenadas das casas adjacentes
    val coordenadaResto = juntarCoordenadas(casasAdjacente1, casasAdjacente2)

    // Limpa as coordenadas vazias e retorna o resultado final
    return limparCoordenadasVazias(coordenadaResto)
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    // Corrige o tamanho do tabuleiro para ter numColunas em cada linha
    return Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }
}

fun geraTiroComputador(tabuleiroPalpiteComputador: Array<Array<Char?>>): Pair<Int, Int> {
    var linha: Int
    var coluna: Int
    // Loop para gerar tiros aleatórios até encontrar uma célula não atingida
    do {
        // Gera um número aleatório para a linha dentro dos limites do tabuleiro
        linha = (0 until tabuleiroPalpiteComputador.size).random()
        // Gera um número aleatório para a coluna dentro dos limites do tabuleiro
        coluna = (0 until tabuleiroPalpiteComputador[0].size).random()
    } while (tabuleiroPalpiteComputador[linha][coluna] != null) // Repete se a célula já foi atingida

    // Retorna as coordenadas do tiro aleatório (incrementadas em 1 para corresponder à indexação humana)
    return Pair(linha + 1, coluna + 1)
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, linha: Int,
                              coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    val coordenadasCanto = Array<Pair<Int, Int>>(4) { Pair(0, 0) }
    val coordenadaExtremos = Array<Pair<Int, Int>>(2) { Pair(0, 0) }
    val coordenadasResto = coordenadasResto(tabuleiro, linha, coluna, orientacao, dimensao)
    val coordenadasDoNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)

    if (coordenadasDoNavio.isEmpty()) {
        return emptyArray() // Retorna um array vazio caso não haja coordenadas do navio
    }

    val primeiraCasa = coordenadasDoNavio.first()
    val ultimaCasa = coordenadasDoNavio.last()

    // Bloco de verificação para cada orientação
    when (orientacao) {
        "E" -> { // Orientação Leste
            coordenadasCanto[0] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second - 1))
            coordenadasCanto[1] = estaValida(tabuleiro, Pair(ultimaCasa.first - 1, ultimaCasa.second + 1))
            coordenadasCanto[2] = estaValida(tabuleiro, Pair(primeiraCasa.first + 1, primeiraCasa.second - 1))
            coordenadasCanto[3] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second + 1))
            coordenadaExtremos[0] = estaValida(tabuleiro, Pair(primeiraCasa.first, primeiraCasa.second - 1))
            coordenadaExtremos[1] = estaValida(tabuleiro, Pair(ultimaCasa.first, ultimaCasa.second + 1))
        }
        "O" -> { // Orientação Oeste
            coordenadasCanto[0] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second + 1))
            coordenadasCanto[1] = estaValida(tabuleiro, Pair(ultimaCasa.first - 1, ultimaCasa.second - 1))
            coordenadasCanto[2] = estaValida(tabuleiro, Pair(primeiraCasa.first + 1, primeiraCasa.second + 1))
            coordenadasCanto[3] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second - 1))
            coordenadaExtremos[0] = estaValida(tabuleiro, Pair(primeiraCasa.first, primeiraCasa.second + 1))
            coordenadaExtremos[1] = estaValida(tabuleiro, Pair(ultimaCasa.first, ultimaCasa.second - 1))
        }
        "N" -> { // Orientação Norte
            coordenadasCanto[0] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second - 1))
            coordenadasCanto[1] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second - 1))
            coordenadasCanto[2] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second + 1))
            coordenadasCanto[3] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second + 1))
            coordenadaExtremos[0] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second))
            coordenadaExtremos[1] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second))
        }
        "S" -> { // Orientação Sul
            coordenadasCanto[0] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second - 1))
            coordenadasCanto[1] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second - 1))
            coordenadasCanto[2] = estaValida(tabuleiro, Pair(primeiraCasa.first - 1, primeiraCasa.second + 1))
            coordenadasCanto[3] = estaValida(tabuleiro, Pair(ultimaCasa.first + 1, ultimaCasa.second + 1))
            coordenadaExtremos[0] = estaValida(tabuleiro, Pair(primeiraCasa.first + 1, primeiraCasa.second))
            coordenadaExtremos[1] = estaValida(tabuleiro, Pair(ultimaCasa.first - 1, ultimaCasa.second))
        }
    }

    // Combina as coordenadas das casas adjacentes e retorna a versão limpa
    val coordenadaFinal: Array<Pair<Int, Int>> = juntarCoordenadas(
        juntarCoordenadas(coordenadasCanto, coordenadasResto), coordenadaExtremos
    )

    return limparCoordenadasVazias(coordenadaFinal)
}

// Função para gerar o tipo de navio com base na sua dimensão
fun navioDimensao(dimensao: Int): Array<Char> {
    return Array(dimensao) {
        when (dimensao) {
            2 -> '2'
            3 -> '3'
            4 -> '4'
            else -> '1' // Caso de navio com dimensão 1
        }
    }
}



// Função principal para gerar o mapa (tabuleiro)
fun obtemMapa(tabuleiroRealOuPalpites: Array<Array<Char?>>, veracidade: Boolean): Array<String> {
    val tabuleiroTamanho = tabuleiroRealOuPalpites.size
    val mapa: Array<String> = Array(tabuleiroTamanho + 1) { "" }

    for (l in 0 until tabuleiroTamanho) {
        val espaco = StringBuilder()

        for (c in 0 until tabuleiroTamanho) {
            val entreColunas = when {
                tabuleiroRealOuPalpites[l][c] == null -> if (veracidade) '~' else '?' // Posição vazia
                veracidade -> tabuleiroRealOuPalpites[l][c] ?: '?' // Se for verdadeiro, mostra o valor
                tabuleiroRealOuPalpites[l][c] == 'x' -> 'x' // Tiro
                else -> {
                    val navioCompleto = navioCompleto(tabuleiroRealOuPalpites, l, c)
                    if (navioCompleto) {
                        tabuleiroRealOuPalpites[l][c] ?: '?' // Caso seja um navio completo
                    } else {
                        // Se o navio não for completo, exibe o número com subscrito
                        when (tabuleiroRealOuPalpites[l][c]) {
                            '2' -> "\u2082" // Subscrito 2
                            '3' -> "\u2083" // Subscrito 3
                            '4' -> "\u2084" // Subscrito 4
                            'X' -> 'X' // Marca de navio afundado
                            else -> tabuleiroRealOuPalpites[l][c] ?: '?'
                        }
                    }
                }
            }

            espaco.append("| $entreColunas ") // Adiciona a célula ao mapa
        }

        // Adiciona o número da linha
        espaco.append("| ${l + 1}")
        mapa[l + 1] = espaco.toString()
    }

    // Cria a legenda horizontal e adiciona como a primeira linha do mapa
    mapa[0] = "| ${criaLegendaHorizontal(tabuleiroTamanho)} |"
    return mapa // Retorna o mapa gerado
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int,
                orientacao: String, tipoNavio: Int): Boolean {
    val linhaAjustada = linha - 1
    val colunaAjustada = coluna - 1

    // Verifica se a posição inicial está dentro dos limites do tabuleiro
    if (linhaAjustada < 0 || colunaAjustada < 0 || linhaAjustada >= tabuleiro.size ||
        colunaAjustada >= tabuleiro[0].size) {
        return false // Posição inicial fora dos limites
    }

    // Verifica se a posição inicial está vazia
    if (tabuleiro[linhaAjustada][colunaAjustada] != null) {
        return false // Posição inicial ocupada
    }

    // Verifica se a dimensão do navio é válida
    if (tipoNavio < 1 || tipoNavio > 4) {
        return false // Dimensão inválida
    }

    // Define os caracteres para cada tipo de navio
    val navioChars = arrayOf('1', '2', '3', '4')

    // Verifica a direção do navio
    val direcao: Pair<Int, Int> = when (orientacao) {
        "N" -> Pair(-1, 0) // Norte (decrementa linha)
        "S" -> Pair(1, 0)  // Sul (incrementa linha)
        "E" -> Pair(0, 1)  // Leste (incrementa coluna)
        "O" -> Pair(0, -1) // Oeste (decrementa coluna)
        else -> return false // Orientação inválida
    }

    // Verifica se o navio ultrapassa os limites do tabuleiro
    for (count in 0 until tipoNavio) {
        val newLinha = linhaAjustada + count * direcao.first
        val newColuna = colunaAjustada + count * direcao.second

        // Verifica se as novas coordenadas estão dentro dos limites
        if (newLinha < 0 || newLinha >= tabuleiro.size || newColuna < 0 || newColuna >= tabuleiro[0].size) {
            return false // O navio ultrapassa os limites
        }

        // Verifica se há colisão com outro navio
        if (tabuleiro[newLinha][newColuna] != null) {
            return false // Colisão com outro navio
        }
    }

    // Insere o navio no tabuleiro
    for (count in 0 until tipoNavio) {
        val newLinha = linhaAjustada + count * direcao.first
        val newColuna = colunaAjustada + count * direcao.second
        tabuleiro[newLinha][newColuna] = navioChars[tipoNavio - 1]
    }

    return true // Navio inserido com sucesso
}
fun lerJogo(nomeDoFicheiro: String, tipoDeTabuleiro: Int): Array<Array<Char?
        >> {
    val tabuleiro = Array(numLinhas) { Array<Char?>(numColunas) { null } }
    return tabuleiro
}
fun gravarJogo(nomeDoFrquivo: String,
               tabuleiroRealHumano: Array<Array<Char?>>,
               tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>,
               tabuleiropalpitesComputador: Array<Array<Char?>>) {
}

fun tabuleiro(tabuleiroHumano: Array<Array<Char?>>, prova: Boolean) {
    for (i in obtemMapa(tabuleiroHumano, prova)) {
        println(i)
    }
}
fun tiposDeTabuleiros(dimensao: Int) {
    tabuleiroHumano = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroComputador = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(dimensao, dimensao)
}
fun mensagem(
    jogador: String, alvoAtingido: String,
    tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int, Int>
) {
    print(">>> ${jogador} >>>${alvoAtingido}")
    println(if (navioCompleto(tabuleiroPalpites, coordenadas.first,
            coordenadas.second)) " Navio ao fundo!" else "")
}

const val MENU_PRINCIPAL = 1
const val MENU_DEFINIR_TABULEIRO = 2
const val MENU_DEFINIR_NAVIOS = 3
const val MENU_JOGAR = 4
const val MENU_LER_FICHEIRO = 5
const val MENU_GRAVAR_FICHEIRO = 6
const val SAIR = 7

fun menuPrincipal(): Int {
    var opcao: String?
    do {
        println("\n> > Batalha Naval < <\n")
        println("1 - Definir Tabuleiro e Navios")
        println("2 - Jogar")
        println("3 - Gravar")
        println("4 - Ler")
        println("0 - Sair")
        print("\n")

        opcao = readlnOrNull()
        if (opcao == null || opcao.toIntOrNull() == null || opcao !in setOf("0", "1", "2", "3", "4")) {
            do {
                println("!!! Opção inválida, tente novamente")
                opcao = readlnOrNull()
            } while (opcao == null || opcao.toIntOrNull() == null || opcao !in setOf("0", "1", "2", "3", "4"))
        }
    } while (opcao == null || opcao.toIntOrNull() == null)

    val opcaoInt = opcao.toInt()
    return when (opcaoInt) {
        0 -> {
            println("Vou sair")
            SAIR
        }
        1 -> MENU_DEFINIR_TABULEIRO
        2 -> MENU_JOGAR
        3, 4 -> {
            println("!!! POR IMPLEMENTAR, tente novamente")
            MENU_PRINCIPAL
        }
        else -> {
            println("!!! Opção inválida, tente novamente")
            MENU_PRINCIPAL
        }
    }
}

fun menuDefinirTabuleiro(): Int {
    println("\n> > Batalha Naval < <\n")
    println("Defina o tamanho do tabuleiro:")

    // Solicitar número de linhas
    print("Quantas linhas?\n")
    var numLinhas = readLine()?.toIntOrNull() ?: 0
    if (numLinhas == -1) {
        return MENU_PRINCIPAL
    }

    // Solicitar número de colunas
    print("Quantas colunas?\n")
    var numColunas = readLine()?.toIntOrNull() ?: 0
    if (numColunas == -1) {
        return MENU_PRINCIPAL
    }

    // Validar tamanho do tabuleiro
    while (!tamanhoTabuleiroValido(numLinhas, numColunas)) {
        println("!!! Tamanho do tabuleiro inválido, tente novamente")
        print("Quantas linhas?\n")
        numLinhas = readLine()?.toIntOrNull() ?: 0
        if (numLinhas == -1) {
            return MENU_PRINCIPAL
        }
        print("Quantas colunas?\n")
        numColunas = readLine()?.toIntOrNull() ?: 0
        if (numColunas == -1) {
            return MENU_PRINCIPAL
        }
    }

    // Configurar tabuleiro humano
    tiposDeTabuleiros(numLinhas)
    tabuleiro(tabuleiroHumano, true)

    // Inserir dois submarinos
    var count = 0
    while (count < 2) {
        println("Insira as coordenadas de um submarino:")
        var coordenadas: String
        do {
            print("Coordenadas? (ex: 6,G)\n")
            coordenadas = readLine().toString()
            if (coordenadas == "-1") {
                return MENU_PRINCIPAL
            }
            val coordenadasValidas = processaCoordenadas(coordenadas, numLinhas, numColunas)
            if (coordenadasValidas != null) {
                val (linha, coluna) = coordenadasValidas
                tabuleiroHumano[linha - 1][coluna - 1] = '1' // Atualiza tabuleiro com submarino
                tabuleiro(tabuleiroHumano, true)
                count++
            } else {
                println("!!! Coordenadas inválidas, tente novamente")
            }
        } while (coordenadas != "-1" && processaCoordenadas(coordenadas, numLinhas, numColunas) == null)
    }

    // Opção de visualizar tabuleiro do computador
    print("Pretende ver o mapa gerado para o Computador? (S/N)\n")
    val opcao = readln().toString()
    if (opcao.uppercase() == "S") {
        println(tabuleiroComputador)
    }

    return MENU_PRINCIPAL
}
fun menuDefinirNavios(): Int {
    println("MENU DEFINIR NAVIOS")
// Lógica para definir os navios
    return MENU_PRINCIPAL // Voltar para o menu principal
}

fun menuJogar(): Int {
    if (tabuleiroHumano.isEmpty() || tabuleiroComputador.isEmpty()) {
        println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
        return MENU_PRINCIPAL
    }

    while (true) {
        // Jogada do humano
        tabuleiro(tabuleiroPalpitesDoHumano, false)
        println("Indique a posição que pretende atingir")
        var coordenadas: String
        val dimensao = tabuleiroHumano.size
        var coordenadasProcessadas: Pair<Int, Int>? = null

        do {
            print("Coordenadas? (ex: 6,G)\n")
            coordenadas = readln().toString()
            if (coordenadas == "-1") {
                return MENU_PRINCIPAL
            }
            coordenadasProcessadas = processaCoordenadas(coordenadas, dimensao, dimensao)
            if (coordenadasProcessadas != null && coordenadaContida(tabuleiroComputador, dimensao, dimensao)) {
                break
            }
            println("!!! Coordenadas inválidas, tente novamente")
        } while (true)

        val acertouAlvo = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas!!)
        mensagem("HUMANO", acertouAlvo, tabuleiroPalpitesDoHumano, coordenadasProcessadas!!)

        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("PARABÉNS! Você venceu o jogo!")
            print("Prima enter para voltar ao menu principal\n")
            readln()
            return MENU_PRINCIPAL
        }

        // Jogada do computador
        val computadorAcertouAlvo = geraTiroComputador(tabuleiroPalpitesDoComputador)
        println("Computador lançou tiro para a posição (${computadorAcertouAlvo.first}, ${computadorAcertouAlvo.second})")
        val acertouAlvoComputador = lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, computadorAcertouAlvo)
        mensagem("COMPUTADOR", acertouAlvoComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas!!)

        if (venceu(tabuleiroPalpitesDoComputador)) {
            println("O COMPUTADOR VENCEU! Melhor sorte na próxima!")
            print("Prima enter para voltar ao menu principal\n")
            readln()
            return MENU_PRINCIPAL
        }

        print("Prima enter para continuar\n")
        readln()
    }
}
fun menuLerFicheiro(): Int {
    println("MENU LER FICHEIRO")
    // Lógica para ler um arquivo
    return MENU_PRINCIPAL // Voltar para o menu principal
}

fun menuGravarFicheiro(): Int {
    println("MENU GRAVAR FICHEIRO")
    // Lógica para gravar um arquivo
    return MENU_PRINCIPAL // Voltar para o menu principal
}

fun main() {
    var menuAtual = MENU_PRINCIPAL

    while (true) {
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}


