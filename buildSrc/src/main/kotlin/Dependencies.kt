object Dependencies {
    object AndroidX {
        object Core {
            private const val nameSpace = "androidx.core"
            const val coreKTX = "$nameSpace:core-ktx:${Versions.AndroidX.Core.coreKTX}"
        }
        object AppCompat {
            private const val nameSpace = "androidx.appcompat"
            const val appCompat = "$nameSpace:appcompat:${Versions.AndroidX.AppCompat.appCompat}"
        }
        object ConstraintLayout {
            private const val nameSpace = "androidx.constraintlayout"
            const val constraintlayout = "$nameSpace:constraintlayout:${Versions.AndroidX.ConstraintLayout.constraintLayout}"
        }
        object Test {
            object Ext {
                private const val nameSpace = "androidx.test.ext"
                const val jUnit = "$nameSpace:junit:${Versions.AndroidX.Test.Ext.jUnit}"
            }
            object Espresso {
                private const val nameSpace = "androidx.test.espresso"
                const val espressoCore = "$nameSpace:espresso-core:${Versions.AndroidX.Test.Espresso.espressoCore}"
            }
        }
        object RecyclerView {
            private const val nameSpace = "androidx.recyclerview"
            const val recyclerView = "$nameSpace:recyclerview:${Versions.AndroidX.RecyclerView.recyclerView}"
        }
        object LifeCycle {
            private const val nameSpace = "androidx.lifecycle"
            const val lifeCycleViewModel = "$nameSpace:lifecycle-viewmodel-ktx:${Versions.AndroidX.LifeCycle.lifeCycleViewModel}"
        }
        object Fragment {
            private const val nameSpace = "androidx.fragment"
            const val fragmentKTX = "$nameSpace:fragment-ktx:${Versions.AndroidX.Fragment.fragmentKTX}"
        }
        object Navigation {
            private const val nameSpace = "androidx.navigation"
            const val navigationFragmentKTX = "$nameSpace:navigation-fragment-ktx:${Versions.AndroidX.Navigation.navigationFragmentKTX}"
            const val navigationUiKTX = "$nameSpace:navigation-ui-ktx:${Versions.AndroidX.Navigation.navigationUiKTX}"
        }
        object Room {
            private const val nameSpace = "androidx.room"
            const val roomKTX = "$nameSpace:room-ktx:${Versions.AndroidX.Room.roomKTX}"
            const val roomRuntime = "$nameSpace:room-runtime:${Versions.AndroidX.Room.roomKTX}"
            const val roomCompiler = "$nameSpace:room-compiler:${Versions.AndroidX.Room.roomKTX}"
        }
    }
    object Com {
        object Google {
            object Android {
                object Material {
                    private const val nameSpace = "com.google.android.material"
                    const val material = "$nameSpace:material:${Versions.Com.Google.Android.Material.material}"
                }
            }
            object Code {
                object Gson {
                    private const val nameSpace = "com.google.code.gson"
                    const val gson = "$nameSpace:gson:${Versions.Com.Google.Code.Gson.gson}"

                }
            }
        }
        object Github {
            object Kirich1409 {
                private const val nameSpace = "com.github.kirich1409"
                const val viewBindingDelegateFull = "$nameSpace:viewbindingpropertydelegate-full:${Versions.Com.Github.Kirich1409.viewBindingDelegateFull}"
            }
            object BumpTech {
                object Glide {
                    private const val nameSpace = "com.github.bumptech.glide"
                    const val glide = "$nameSpace:glide:${Versions.Com.BumpTech.Glide.glide}"
                }
            }
        }
        object SquareUp {
            object Retrofit2 {
                private const val nameSpace = "com.squareup.retrofit2"
                const val retrofit = "$nameSpace:retrofit:${Versions.Com.SquareUp.Retrofit2.retrofit}"
                const val converterGson = "$nameSpace:converter-gson:${Versions.Com.SquareUp.Retrofit2.converterGson}"
            }
        }
        object Hannesdorfmann {
            private const val nameSpace = "com.hannesdorfmann"
            const val adapterDelegates =
                "$nameSpace:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.Com.Hannesdorfmann.adapterDelegates}"
        }
    }
    object JUnit {
        private const val nameSpace = "junit"
        const val jUnit = "$nameSpace:junit:${Versions.JUnit.jUnit}"
    }
    object Io {
        object InsertKoin {
            private const val nameSpace = "io.insert-koin"
            const val koinAndroid = "$nameSpace:koin-android:${Versions.Io.InsertKoin.koinAndroid}"
        }
    }
}