/*
 * Copyright (C) 2020 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.hilt.android.processor.internal.viewmodelinject

import com.google.testing.compile.CompilationSubject.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ViewModelGeneratorTest {

  @Test
  fun verifyModule_noArg() {
    val myViewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;

        class MyViewModel extends ViewModel {
            @ViewModelInject
            MyViewModel() { }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel")

    val expected = """
        package dagger.hilt.android.test;

        import androidx.lifecycle.ViewModel;
        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ViewModelComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoMap;
        import dagger.multibindings.StringKey;
        import $GENERATED_TYPE;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ViewModelComponent.class)
        @OriginatingElement(
            topLevelClass = MyViewModel.class
        )
        public final class MyViewModel_HiltModule {
            private MyViewModel_HiltModule() {
            }

            @Provides
            @IntoMap
            @StringKey("dagger.hilt.android.test.MyViewModel")
            @ViewModelInjectMap
            public static ViewModel provide() {
              return new MyViewModel();
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel_HiltModule")

    val compilation = compiler().compile(myViewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile("dagger.hilt.android.test.MyViewModel_HiltModule")
        .hasSourceEquivalentTo(expected)
    }
  }

  @Test
  fun verifyModule_savedStateOnlyArg() {
    val myViewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;
        import androidx.lifecycle.SavedStateHandle;

        class MyViewModel extends ViewModel {
            @ViewModelInject
            MyViewModel(SavedStateHandle savedState) { }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel")

    val expected = """
        package dagger.hilt.android.test;

        import androidx.lifecycle.SavedStateHandle;
        import androidx.lifecycle.ViewModel;
        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ViewModelComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoMap;
        import dagger.multibindings.StringKey;
        import $GENERATED_TYPE;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ViewModelComponent.class)
        @OriginatingElement(
            topLevelClass = MyViewModel.class
        )
        public final class MyViewModel_HiltModule {
            private MyViewModel_HiltModule() {
            }

            @Provides
            @IntoMap
            @StringKey("dagger.hilt.android.test.MyViewModel")
            @ViewModelInjectMap
            public static ViewModel provide(SavedStateHandle savedState) {
              return new MyViewModel(savedState);
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel_HiltModule")

    val compilation = compiler().compile(myViewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile("dagger.hilt.android.test.MyViewModel_HiltModule")
        .hasSourceEquivalentTo(expected)
    }
  }

  @Test
  fun verifyModule_mixedArgs() {
    val foo = """
        package dagger.hilt.android.test;

        public class Foo { }
        """.toJFO("dagger.hilt.android.test.Foo")

    val myViewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;
        import androidx.lifecycle.SavedStateHandle;
        import java.lang.String;

        class MyViewModel extends ViewModel {
            @ViewModelInject
            MyViewModel(String s, Foo f, SavedStateHandle savedState, long l) { }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel")

    val expected = """
        package dagger.hilt.android.test;

        import androidx.lifecycle.SavedStateHandle;
        import androidx.lifecycle.ViewModel;
        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ViewModelComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoMap;
        import dagger.multibindings.StringKey;
        import java.lang.String;
        import $GENERATED_TYPE;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ViewModelComponent.class)
        @OriginatingElement(
            topLevelClass = MyViewModel.class
        )
        public final class MyViewModel_HiltModule {
            private MyViewModel_HiltModule() {
            }

            @Provides
            @IntoMap
            @StringKey("dagger.hilt.android.test.MyViewModel")
            @ViewModelInjectMap
            public static ViewModel provide(String s, Foo f, SavedStateHandle savedState, long l) {
              return new MyViewModel(s, f, savedState, l);
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel_HiltModule")

    val compilation = compiler().compile(foo, myViewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile("dagger.hilt.android.test.MyViewModel_HiltModule")
        .hasSourceEquivalentTo(expected)
    }
  }

  @Test
  fun verifyModule_mixedAndProviderArgs() {
    val foo = """
        package dagger.hilt.android.test;

        public class Foo { }
        """.toJFO("dagger.hilt.android.test.Foo")

    val myViewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;
        import androidx.lifecycle.SavedStateHandle;
        import java.lang.String;
        import javax.inject.Provider;

        class MyViewModel extends ViewModel {
            @ViewModelInject
            MyViewModel(String s, Provider<Foo> f, SavedStateHandle savedState) { }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel")

    val expected = """
        package dagger.hilt.android.test;

        import androidx.lifecycle.SavedStateHandle;
        import androidx.lifecycle.ViewModel;
        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ViewModelComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoMap;
        import dagger.multibindings.StringKey;
        import java.lang.String;
        import $GENERATED_TYPE;
        import javax.inject.Provider;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ViewModelComponent.class)
        @OriginatingElement(
            topLevelClass = MyViewModel.class
        )
        public final class MyViewModel_HiltModule {
            private MyViewModel_HiltModule() {
            }

            @Provides
            @IntoMap
            @StringKey("dagger.hilt.android.test.MyViewModel")
            @ViewModelInjectMap
            public static ViewModel provide(String s, Provider<Foo> f, SavedStateHandle savedState) {
              return new MyViewModel(s, f, savedState);
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel_HiltModule")

    val compilation = compiler().compile(foo, myViewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile("dagger.hilt.android.test.MyViewModel_HiltModule")
        .hasSourceEquivalentTo(expected)
    }
  }

  @Test
  fun verifyModule_qualifiedArgs() {
    val myQualifier = """
        package dagger.hilt.android.test;

        import javax.inject.Qualifier;

        @Qualifier
        public @interface MyQualifier { }
        """.toJFO("dagger.hilt.android.test.MyQualifier")

    val myViewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;
        import androidx.lifecycle.SavedStateHandle;
        import java.lang.Long;
        import java.lang.String;
        import javax.inject.Named;
        import javax.inject.Provider;

        class MyViewModel extends ViewModel {
            @ViewModelInject
            MyViewModel(@Named("TheString") String s, @MyQualifier Provider<Long> l,
                    SavedStateHandle savedState) {
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel")

    val expected = """
        package dagger.hilt.android.test;

        import androidx.lifecycle.SavedStateHandle;
        import androidx.lifecycle.ViewModel;
        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ViewModelComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoMap;
        import dagger.multibindings.StringKey;
        import java.lang.Long;
        import java.lang.String;
        import $GENERATED_TYPE;
        import javax.inject.Named;
        import javax.inject.Provider;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ViewModelComponent.class)
        @OriginatingElement(
            topLevelClass = MyViewModel.class
        )
        public final class MyViewModel_HiltModule {
            private MyViewModel_HiltModule() {
            }

            @Provides
            @IntoMap
            @StringKey("dagger.hilt.android.test.MyViewModel")
            @ViewModelInjectMap
            public static ViewModel provide(@Named("TheString") String s,
                    @MyQualifier Provider<Long> l, SavedStateHandle savedState) {
              return new MyViewModel(s, l, savedState);
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel_HiltModule")

    val compilation = compiler().compile(myQualifier, myViewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile("dagger.hilt.android.test.MyViewModel_HiltModule")
        .hasSourceEquivalentTo(expected)
    }
  }

  @Test
  fun verifyInnerClass() {
    val viewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;

        class Outer {
            static class InnerViewModel extends ViewModel {
                @ViewModelInject
                InnerViewModel() { }
            }
        }
        """.toJFO("dagger.hilt.android.test.Outer")

    val expectedModule = """
        package dagger.hilt.android.test;

        import androidx.lifecycle.ViewModel;
        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ViewModelComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoMap;
        import dagger.multibindings.StringKey;
        import $GENERATED_TYPE;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ViewModelComponent.class)
        @OriginatingElement(topLevelClass = Outer.class)
        public final class Outer_InnerViewModel_HiltModule {
            private Outer_InnerViewModel_HiltModule() {
            }

            @Provides
            @IntoMap
            @StringKey("dagger.hilt.android.test.Outer${'$'}InnerViewModel")
            @ViewModelInjectMap
            public static ViewModel provide() {
              return new Outer.InnerViewModel();
            }
        }
        """.toJFO("dagger.hilt.android.test.Outer_InnerViewModel_HiltModule")

    val compilation = compiler().compile(viewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile(
        "dagger.hilt.android.test" +
          ".Outer_InnerViewModel_HiltModule"
      )
        .hasSourceEquivalentTo(expectedModule)
    }
  }

  @Test
  fun verifyKeyModule() {
    val myViewModel = """
        package dagger.hilt.android.test;

        import dagger.hilt.android.lifecycle.ViewModelInject;
        import androidx.lifecycle.ViewModel;

        class MyViewModel extends ViewModel {
            @ViewModelInject
            MyViewModel() { }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel")

    val expected = """
        package dagger.hilt.android.test;

        import dagger.Module;
        import dagger.Provides;
        import dagger.hilt.InstallIn;
        import dagger.hilt.android.components.ActivityRetainedComponent;
        import dagger.hilt.android.internal.lifecycle.ViewModelInjectMap;
        import dagger.hilt.codegen.OriginatingElement;
        import dagger.multibindings.IntoSet;
        import java.lang.String;
        import $GENERATED_TYPE;

        $GENERATED_ANNOTATION
        @Module
        @InstallIn(ActivityRetainedComponent.class)
        @OriginatingElement(
            topLevelClass = MyViewModel.class
        )
        public final class MyViewModel_Key_HiltModule {
            private MyViewModel_Key_HiltModule() {
            }

            @Provides
            @IntoSet
            @ViewModelInjectMap.KeySet
            public static String provide() {
              return "dagger.hilt.android.test.MyViewModel"
            }
        }
        """.toJFO("dagger.hilt.android.test.MyViewModel_Key_HiltModule")

    val compilation = compiler().compile(myViewModel)
    assertThat(compilation).apply {
      succeeded()
      generatedSourceFile("dagger.hilt.android.test.MyViewModel_Key_HiltModule")
        .hasSourceEquivalentTo(expected)
    }
  }
}
