# IDDog
Android APP em Kotlin usando androidx, volley e testes com espresso.

Consistindo de uma activity para login e outra para listagem dos dados recebidos da API, onde só é possível acessar a página de listagem estando autenticado.



## Libs
Utilizando libs padrões do android:

* androidx.appcompat:appcompat - Melhor compatibilidade com versões antigas
* androidx.core:core-ktx - Core androidx
* com.google.android.material:material - Utilização de material design
* androidx.constraintlayout:constraintlayout - ContraintLayout utilizado na página de listagem
* androidx.vectordrawable:vectordrawable - Adiciona suporte a VectorDrawable
* com.android.volley:volley - Requisições HTTP
* android.arch.navigation:navigation-fragment-ktx - Utilização de FragmentNavigator
* android.arch.navigation:navigation-ui-ktx - Utilização de NavigationUI
* junit:junit - Testes
* androidx.test:core-ktx - Core para testes
* androidx.test:runner - Execução dos testes
* androidx.test:rules - Gerenciar a activity sendo testada
* androidx.test.ext:junit-ktx - Execução dos testes com junit
* androidx.test.espresso:espresso-core - Core do Espresso
* androidx.test.espresso:espresso-contrib - Funções úteis do Espresso
* androidx.test.espresso:espresso-intents - Teste sobre intents recebidos pela activity
* androidx.test.espresso:espresso-idling-resource - Para implementação de IdlingResource para requisições HTTP



## Executar
* Android Studio 3.4+
* Kotlin 1.3.30
* Gradle 3.40
* Android SDK 16 - 28
* Java SDK version 1.8
