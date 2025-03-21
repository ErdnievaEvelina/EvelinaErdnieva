package com.example.eveerdnieva

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.eveerdnieva.data.HomePageScreen
import com.example.eveerdnieva.data.NewsArticleScreen
import com.example.eveerdnieva.data.model.Article
import com.example.eveerdnieva.data.repository.RepositoryImpl
import com.example.eveerdnieva.presentation.DetailNew
import com.example.eveerdnieva.presentation.NewsViewModel
import com.example.eveerdnieva.ui.theme.EveErdnievaTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<NewsViewModel>(factoryProducer = {
        object: ViewModelProvider.Factory{
            override fun <T: ViewModel> create(modelClass:Class<T>):T{
                return NewsViewModel(RepositoryImpl(RetrofitInstance.api)) as T
            }
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EveErdnievaTheme {
                Surface(
                    modifier= Modifier.fillMaxSize(),
                    color=MaterialTheme.colorScheme.background
                ) {
                    val context= LocalContext.current
                    LaunchedEffect(key1=viewModel.showError) {
                        viewModel.showError.collectLatest { show->
                            if(show){
                                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Column(modifier=Modifier.fillMaxSize()) {
                        Text(text="NEWS US",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier=Modifier.padding(10.dp).align(Alignment.CenterHorizontally))

                        ChangeCategory(viewModel)
                        val navController= rememberNavController()
                        NavHost(navController=navController, startDestination = HomePageScreen) {
                            composable<HomePageScreen>{
                                ListNews(viewModel,navController)
                            }
                            composable<NewsArticleScreen>{
                                val args=it.toRoute<NewsArticleScreen>()
                                DetailNew(args.url)

                            }
                        }

                    }


                }

            }
        }
    }
}
@Composable
fun ListNews(viewModel: NewsViewModel,navController: NavController){
    val newsList by viewModel.news.collectAsState()
    if (newsList.isEmpty()){
        Box (
            modifier=Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
           CircularProgressIndicator()
        }
    }else{
        LazyColumn(
            modifier=Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(newsList.size){index->
                NewItem(newsList[index],navController)

            }

        }
    }


}
@Composable
fun NewItem(article: Article,navController:NavController){
    Card(
        modifier=Modifier.padding(8.dp),
        elevation=CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick={
            navController.navigate(NewsArticleScreen(article.url))
        }
    ) {
        Row(
            modifier=Modifier.fillMaxSize().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(model=article.urlToImage?:
            "https://avatars.mds.yandex.net/i?id=7f6bab199a03b89e490f52b68dd3bb098af7393f-5248093-images-thumbs&n=13", contentDescription = null,
                modifier=Modifier.size(80.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop)
            Column(
                modifier=Modifier.padding(8.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text=article.title,
                maxLines=3)

                Text(text=article.source.name,
                    fontSize = 16.sp,
                    maxLines = 1)

            }

        }

    }

}
@Composable
fun ChangeCategory(viewModel: NewsViewModel){
    val categoryList=listOf(
        "GENERAL",
        "SPORTS",
        "BUSINESS",
        "HEALTH",
        "SCIENCE",
        "ENTERTAINMENT",
        "TECHNOLOGY"
    )
    Row (
        modifier=Modifier.fillMaxWidth().padding(5.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ){
        categoryList.forEach { category->
            Button(onClick={
                viewModel.changeCategory(category)
            }) {
               Text(
                   text=category)
            }
        }

    }

}

