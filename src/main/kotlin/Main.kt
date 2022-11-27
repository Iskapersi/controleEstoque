const val menuInicial = """
1 - ADICIONAR ITEM
2 - EDITAR ITEM
3 - EXIBIR ITENS EM ESTOQUE
4 - EXIBIR TODOS OS ITENS
0 - FECHAR SISTEMA
Digite a opção desejada:"""

const val menuEstoque = """
========ESTOQUE==========
ID=======ITEM=======QUANT"""

private const val ADICIONARITEM = 1
private const val EDITARITEM = 2
private const val EXIBIRESTOQUE = 3
private const val EXIBIRTUDO = 4

class LimiteEstoqueMaxException(message:String): Exception(message)


var idItens = 1
val estoque = mutableListOf<Triple<Int, String, Int>>()

fun main() {

    programa()
    println("Fim do Programa")


}

fun programa () {

    do {
        println(menuInicial)
        val opcao = readln().toInt()
        try {
            when (opcao) {
                ADICIONARITEM -> {
                    estoque.add(adicionarItem(idItens))
                    idItens++
                }

                EDITARITEM -> editarItens(estoque)
                EXIBIRESTOQUE -> exibirEstoque(estoque, false)
                EXIBIRTUDO -> exibirEstoque(estoque, true)
            }
        } catch (e: LimiteEstoqueMaxException) {
            println(e.message)
        } catch (e: Exception) {
            println("Erro, tente novamente")
        }
    } while (opcao != 0)

}

fun adicionarItem(idItens: Int): Triple<Int, String, Int> {

        println("Digite o nome do item a ser adicionado:")
        val itemNovo = readln().uppercase()
        println("Digite a quantidade do item:")
        val quant = readln().toInt()
        if (quant > 999) {
            throw LimiteEstoqueMaxException("A quantidade de produtos não pode ultrapassar 999.")
        }
    return Triple(idItens, itemNovo, quant)
}


fun exibirEstoque(estoque: MutableList<Triple<Int, String, Int>>, tudo: Boolean) {
    var condicao = false
    if (estoque.isEmpty()){
        println("Estoque Vazio")
    } else {
        println(menuEstoque)
        estoque.forEach { item ->
            if (tudo || item.third > 0) {
                println("#%04d....${item.second}.......${item.third}".format(item.first))
                condicao = true
            }
        }
        if (!condicao && !tudo) {
            println("Sem estoque a exibir")
        }
    }
}

fun editarItens (estoque: MutableList<Triple<Int, String, Int>>) {

    do{
        println("Digite o id do produto para alterar ou 0 para sair:")
        val procuraId = readln().toIntOrNull()
        if(procuraId == null){
            println("Número inválido. Tente novamente.")
        } else {
            if (procuraId != 0) {
                val item = estoque[procuraId - 1]

                //Let vai permitir pesquisar o indice para alterar
                item.let {
                    println("Digite o novo nome do produto ou ENTER para cancelar:")
                    var nomeNovo = readlnOrNull() ?: it.second
                    if(nomeNovo.isEmpty()){
                        nomeNovo = it.second
                    }

                    println("Digite a nova quantidade do produto ou ENTER para cancelar:")
                    val quantNova = readlnOrNull()?.toIntOrNull() ?: it.third
                    if(quantNova > 999) {
                        throw LimiteEstoqueMaxException("A quantidade de produtos não pode ultrapassar 999.")
                    }

                    //Salvar as alterações no indice desejado
                    val itemEditado = Triple(it.first, nomeNovo.uppercase(), quantNova)
                    estoque[it.first - 1] = itemEditado
                }
            }
        }
    } while (procuraId != null && procuraId != 0)
}