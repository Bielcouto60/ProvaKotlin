package com.example.prova

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prova.ui.theme.ProvaTheme
import androidx.navigation.compose.rememberNavController




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeyoutMain()
        }
    }
}

data class Produto(
    val nome: String,
    val categoria: String,
    val preco: Float,
    val quantidadeEmEstoque: Int
)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tela1(navController: NavController) {
    var nomeProduto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidadeEmEstoque by remember { mutableStateOf("") }
    val context = LocalContext.current
    val produtosCadastrados = mutableStateListOf<Produto>()

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Tela 1", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = nomeProduto,
            onValueChange = { nomeProduto = it },
            label = { Text(text = "Nome do produto") })

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text(text = "Categoria") })

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = preco, onValueChange = { preco = it }, label = { Text(text = "preço") })

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = quantidadeEmEstoque,
            onValueChange = { quantidadeEmEstoque = it },
            label = { Text(text = "Quantidade em estoque") })

        Button(onClick = {
            if (nomeProduto.isNotBlank() && categoria.isNotBlank() && preco.isNotBlank() && quantidadeEmEstoque.isNotBlank()) {
                navController.navigate("Tela2/$nomeProduto/$categoria/$preco/$quantidadeEmEstoque")
                val precoFloat = preco.toFloatOrNull()
                val quantidadeInt = quantidadeEmEstoque.toIntOrNull()

                if (precoFloat != null && quantidadeInt != null) {
                    // Adiciona o produto à lista
                    produtosCadastrados.add(
                        Produto(
                            nomeProduto,
                            categoria,
                            precoFloat,
                            quantidadeInt
                        )
                    )


                    navController.navigate("listaProdutos")
                }
            } else {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "cadastrar")
        }
    }





    @Composable
    fun ListaProdutos(navController: NavController) {
        Column {
            LazyColumn {
                items(produtosCadastrados) { produto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${produto.nome} (${produto.quantidadeEmEstoque} unidades)")
                        Button(onClick = {
                            navController.navigate("detalhes/${produto.nome}")
                        }) {
                            Text("Detalhes")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DetalhesProduto(nomeProduto: String) {
        val produto = produtosCadastrados.find { it.nome == nomeProduto }

        Column(modifier = Modifier.padding(16.dp)) {
            produto?.let {
                Text("Nome: ${it.nome}", fontSize = 24.sp)
                Text("Categoria: ${it.categoria}")
                Text("Preço: R$ ${it.preco}")
                Text("Quantidade em Estoque: ${it.quantidadeEmEstoque}")
            } ?: Text("Produto não encontrado")
        }
    }
}
@Composable
fun Tela2(navController: NavController, nomeProduto: String?, categoria: String?, preco: Float, quantidadeEmEstoque: Int) {
    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Bem-vindo, $nomeProduto", fontSize = 25.sp)
        Text(text = "Categoria: $categoria")
        Text(text = "Preço: R$ ${preco}")
        Text(text = "Quantidade em Estoque: $quantidadeEmEstoque")
        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Voltar para a Tela 1")
        }
    }
}

@Composable
fun LeyoutMain(){
    var navController = rememberNavController()

    Column (Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        NavHost(navController = navController, startDestination = "tela1"){
            //rotas:quais Leyout podemos navegar
            composable("tela1") { Tela1(navController = navController) }
            composable("tela2/{nomeProduto}/{categoria}/{preco}/{quantidadeEmEstoque}") {
                    backStackEntry ->

            val nomeProduto  = backStackEntry.arguments?.getString("nomeproduto")
            val   categoria = backStackEntry.arguments?.getString("categoria")
            val    preco = backStackEntry.arguments?.getString("preco")?.toFloat()
            val   quantidadeEmEstoque = backStackEntry.arguments?.getString("quantidade")?.toIntOrNull()
                if (preco != null && quantidadeEmEstoque != null) {
                    Tela2(
                        navController = navController,
                        nomeProduto = nomeProduto,
                        categoria = categoria,
                        preco = preco,
                        quantidadeEmEstoque = quantidadeEmEstoque
                    )
            }else{


                }
            }




    }
}



}



@Preview
@Composable
fun Preview() {
    LeyoutMain()

}
