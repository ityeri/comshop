# comshop

> [kommand](https://github.com/monun/kommand) 에 영감을 받아 만들어진 
> [Brigadier](https://github.com/mojang/Brigadier) 의 DSL 래퍼이자 명령어 라이브러리

[Brigadier](https://github.com/mojang/Brigadier) 는 모장에서 명령어 파싱을 위해 사용하는 라이브러리이며,
[kommand](https://github.com/monun/kommand) 는 내부적으로 Brigadier 를 사용합니다.
때문에 Brigadier 와 kommand 의 명령어 정의는 구조적으로 유사합니다.

Brigadier
```java
literal("test")
    .then(argument("number", IntegerArgumentType.integer())
        .then(argument("word", StringArgumentType.word())
            .then(argument("flag", BoolArgumentType.bool())
                .executes(context -> {
                    int number = IntegerArgumentType.getInteger(context, "number");
                    String word = StringArgumentType.getString(context, "word");
                    boolean flag = BoolArgumentType.getBool(context, "flag");
                    return 0;
                })
            )
        )
    );
```

kommand
```kotlin
kommand {
    register("test") {
        then("number" to intArgument) {
            then("word" to word) {
                then("flag" to bool) {
                    executes { context ->
                        val number: Int by context
                        val word: String by context
                        val flag: Boolean by context
                        0
                    }
                }
            }
        }
    }
}
```

kommand 는 Brigadier 와 비슷하면서도 코틀린 친화적인 형태로 명령어를 정의할수 있습니다.
하지만 Brigadier 와 비슷하게, 인자가 많아지면서
코드의 들여쓰기와 길이가 크게 늘어납니다. 
또한 재귀적인 체인 형태로 명령어 노드가 이어지고 코드가 쓰여지는 kommand 와 Brigadier 의 특성상,
인자 정의부분, 실행 블록, 하위 명령어 블록을 명확하게 구분하기 어렵습니다

아래는 같은 구조의 명령어를 comshop 을 활용해 작성한 코드입니다
```kotlin
command("test") {
    arguments {
        "number" { IntegerArgumentType.integer() }
        "word" { StringArgumentType.word() }
        "flag" { BoolArgumentType.bool() }
    }

    executes {
        val number = getArg("number", Int::class)
        val word = getArg("word", String::class)
        val flag = getArg("flag", Boolean::class)
        0
    }
}
```

코드의 들여쓰기가 줄며, 인자를 정의하는 부분과 실행블록이 명확히 분리되고 구분됩니다.
하위 명령어는 then() 을 활용해 같은 형태로 손쉽게 정의할수 있습니다

---

* comshop 은 kotlin dsl 을 통해 paper api 에서 동작하는 
  플러그인 명령어를 정의하기 위한 DSL 을 제공합니다

* 다량의 하위 명령어에서 여전히 발생하는 들여쓰기와 코드 길이 문제를 해결하기 위해
  명령어를 개별 클래스로 분리하기에 용이한 추상 클래스를 제공합니다

* 명령어 파싱을 위해 모장에서 사용하는 라이브러리인 `Brigadier` 를 기반으로 동작합니다

---

* Supported Paper api versions
  * 1.21.10 - java 21

향후 구버전에 대한 지원이 추가될수 있습니다

# dependency

comshop 은 JitPack 을 통해 배포됩니다

kotlin
```kotlin
repositories {
    maven("https://jitpack.io") {
        name = "jitpack"
    }
}

dependencies {
    implementation("com.github.ityeri:comshop:v1.0.0-beta.2")
}
```

groovy
```groovy
repositories {
    maven {
        url "https://jitpack.io"
        name = "jitpack"
    }
}

dependencies {
    implementation 'com.github.ityeri:comshop:v1.0.0-beta.2'
}
```

# usage

## DSL 로 명령어 만들어 보기

```kotlin
val greetingCommand = command("greeting") {
    // 이 명령어를 누가 사용할수 있는지를 정의합니다. Boolean 을 반환하는 블록이 와야 합니다
    requires { source -> source.sender.isOp }
    // source.sender.isOp 값을 리턴하도록 하여 op 가 있어야만 사용할수 있도록 설정합니다

    // 명령어의 인수 지정
    arguments {
        // Brigadier 의 ArgumentType 구현체
        // StringArgumentType.word() 는 Brigadier 의 내장 인수 타입니다
        "message" { StringArgumentType.word() }
        // "word" 는 나중에 executes 블록에서 인수값을 가져오기 위해 쓰는 인수의 이름입니다
    }

    // 이 명령어가 어떻게 실행될지 지정
    executes { source -> // source: CommandSourceStack
        // 위 arguments 부분에서 지정한 이름을 이용해 인수를 가져옵니다
        // 인수 타입의 클래스(KClass) 를 같이 넘겨 주어야 합니다
        val message = getArg("message", String::class)

        // CommandSourceStack 은 CommandSender 를 래핑하는 인터페이스입니다
        // CommandSourceStack.sender 를 통해 전송자를 CommandSender 로 가져올수 있습니다
        source.sender.sendMessage(message)
        // /!\ CommandSourceStack 은 comshop 의 기능이 아닙니다.
        // paper 에서 Brigadier 를 지원하기 위해 존재하는 paper api 의 일부입니다

        0 // 명령어 실행이 성공시엔 0을, 실패시엔 1을 반환합니다
    }

    // 하위 명령어를 정의합니다
    then("player") {
        arguments {
            // 여러개의 인수를 지정할수도 있습니다
            // 실제 서버에서 명령어를 입력할땐 코드에 쓰여진 순서대로 인수를 입력하면 됩니다
            "player" { ArgumentTypes.player() }
            "message" { StringArgumentType.word() }
            // 플레이어 인수를 받을땐 paper api 에서 Brigadier 를 위해 제공하는
            // ArgumentTypes.player() 를 사용할수 있습니다
        }

        executes { source ->
            // ArgumentTypes.player() 는 Player 를 바로 뱉어주는게 아니라
            // 플레이어를 담고 있는 PlayerSelectorArgumentResolver 를 뱉습니다 (왜?)
            val playerResolver = getArg("player", PlayerSelectorArgumentResolver::class)
            val player = playerResolver.resolve(source).first()

            val message = getArg("message", String::class)

            player.sendMessage(message)

            0
        }
    }
}
```

인게임에서:
```
/greeting 안녕!
안녕!
```
```
/greeting some_player 와샍으!
와샍으! (지정된 플레이어에게)
```

## 클래스 단위로 명령어 만들어보기

DSL 을 활용한 명령어 정의는 직관적이고 간결하지만,
하나의 DSL 문 안에 여러개의 하위 명령어나 기능이 모이게 되면
코드가 길어지는 현상은 필연적으로 발생합니다.
이런 상황을 위해, 명령어의 정의를 클래스로 분리하여 작성하기에 용이하도록
`LiteralCommandBuilder` 라는 추상 클래스를 제공합니다

```kotlin
class ExampleCommand : LiteralCommandBuilder() {
    override val name: String = "example"

    init {
        arguments {
            "unsigned int" { IntegerArgumentType.integer(0) }
            "flag" { BoolArgumentType.bool() }
        }

        executes { source ->
            val integer = getArg("unsigned int", Int::class)
            val flag = getArg("flag", Boolean::class)
            source.sender.sendMessage("Unsinged integer: $integer, flag: $flag")
            1
        }
    }
}
```

init 블록 내부는 상술한 DSL 과 똑같이 작성할수 있습니다.
차이점은 한 명령어의 코드가 하나의 클래스라는 덩어리로 분리된다는 것입니다

인게임에서:
```
example 120 true
Unsinged integer: 120, flag: true
```

## DSL 과 CLASS 명령어를 같이 활용해 보기

DSL 을 활용해 만든 명령어와 클래스로 만든 명령어는
`then` 매서드로 서로 혼용이 가능합니다

```kotlin
class ExampleCommand : LiteralCommandBuilder() {
    override val name: String = "example"
    init { ... }
}

val greetingCommand = command("greeting") {
    ...
    then(ExampleCommand())
}
```

```kotlin
class ExampleCommand : LiteralCommandBuilder() {
    override val name: String = "example"

    init {
        ...
        then(command("wasans")) {
            executes { source ->
                source.sender.sendMessage("This is wasans command")
                0
            }
        }
    }
}
```

TODO 제네릭을 추가, 값 자체를 받기 + apply 블럭은 선택
이러면??? 리터럴 커내드 고유 어쩌구가 필요가 없 + 혼용 자체를 생각할 필요가 없듬

## 명령어 등록하기

명렁어 등록을 위해선 `CommandRegistrar` 를 사용합니다.
실제로 명령어를 등록할땐, 페이퍼의 라이프사이클 API 를 사용합니다

---

comshop 이 서버의 명렁어 등록 시점에 개입할수 있도록
서버의 라이프사이클 매니저에 등록합니다.

```kotlin
CommandRegistrar.lifecycleRegister(plugin) // plugin: JavaPlugin
```

---

명령어를 추가합니다. 
실제로 명령어가 서버에 추가되는 시점은
`LifecycleEvents.COMMANDS` 가 트리거 될때입니다.

```kotlin
CommandRegistrar.register(command) // command: CommandBuilder
```

DSL 을 활용해 작성한 명령어를 등록하는것과,
상속을 통해 만들어진 클래스 형태의 명령어를 등록하는 방법은 같습니다

```kotlin
val greetingCommand = command("greeting") { ... }
CommandRegistrar.register(greetingCommand)
```

```kotlin
class ExampleCommand : LiteralCommandBuilder() { ... }
CommandRegistrar.register(ExampleCommand())
```

> `lifecycleRegister(plugin)` 와 `register(command)` 중에 뭘 먼저 호출하는지는 중요하지 않습니다.
> 다만 둘다 `LifecycleEvents.COMMANDS` 이전 시점엔 완료 되어 있어야 합니다

## 사용할수 있는 인수 타입들

comshop 은 Brigadier 를 래핑하며, 때문에 `ArgumentType` 의 구현체는 모두 가능합니다. 

### Brigadier 내장

`com.mojang.brigadier.arguments` 패키지 내부.
기초적인 타입들을 파싱하기 위한 인수 타입들이 있습니다

| 이름 | 리턴 타입 | 사용처 |
|---|---|---|
| `IntegerArgumentType.integer` | `Int` | 64비트 부호있는 정수 파싱 |
| `LongArgumentType.longArg` | `Ling` | 64비트 부호있는 정수 파싱 |
| `FloatArgumentType.floatArg` | `Float` | 32비트 부동소수점 실수 파싱 |
| `DoubleArgumentType.doubleArg` | `Double` | 64비트 부동소수점 실수 파싱 |
| `StringArgumentType.word` | `String` | 띄어쓰기 없는 한 단어 파싱 |
| `StringArgumentType.string` | `String` | 띄어쓰기 가능한 끝따옴표로 묶인 문자열 파싱 |
| `StringArgumentType.greedyString` | `String` | 띄어쓰기 가능한 큰따옴표로 묶이지 않은 문자열 파싱 (보통 명령어 맨 끝 인수로 옴)
| `BoolArgumentType.bool` | `Boolean` | `true`, `false` 불리언 파싱 |

### Paper api 측 Brigadier 지원

`io.papermc.paper.command.brigadier.argument` 패키지 내부.
마인크래프트 고유 타입 파싱을 위한 인수 타입들이 다수 있습니다

페이퍼에서 제공하는 대부분의 인수 타입은 보통 그 타입을 그대로 반환하지 않고,
`...SelectorArgumentResolver` 라 하는 래퍼에 의해 래핑되어 반환됩니다.
(예컨대, `ArgumentType.player` 는 `Player` 가 아닌 
`PlayerSelectorArgumentResolver` 를 반환합니다)

paper api 에서 Brigadier 를 위해 지원하는 타입들은 
[Paper docs - Development / API / Command API / Arguments](
https://docs.papermc.io/paper/dev/command-api/arguments/minecraft/
) 에 정리되어 있습니다

# examples

전체 예제코드 내지 테스트 코드는 
라이브러리 소스코드의 comshop-plugin 모듈 내에 포함되어 있습니다

## full example - DSL

```kotlin
import com.github.ityeri.comshop.CommandDSL.Companion.command
import com.github.ityeri.comshop.CommandRegistrar
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.plugin.java.JavaPlugin


class ComshopPlugin : JavaPlugin() {

    override fun onEnable() {
        CommandRegistrar.lifecycleRegister(this)

        val greetingCommand = command("greeting") {
            requires { source -> source.sender.isOp }

            arguments {
                "message" { StringArgumentType.word() }
            }

            executes { source -> 
                val message = getArg("message", String::class)
                source.sender.sendMessage(message)

                0
            }

            then("player") {
                arguments {
                    "player" { ArgumentTypes.player() }
                    "message" { StringArgumentType.word() }
                }

                executes { source ->
                    val playerResolver = getArg("player", PlayerSelectorArgumentResolver::class)
                    val player = playerResolver.resolve(source).first()
                    val message = getArg("message", String::class)

                    player.sendMessage(message)

                    0
                }
            }
        }
        
        CommandRegistrar.register(greetingCommand)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
```

## full example - CLASS

```kotlin
import com.github.ityeri.comshop.builder.LiteralCommandBuilder
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.github.ityeri.comshop.CommandRegistrar
import org.bukkit.plugin.java.JavaPlugin

class ExampleCommand : LiteralCommandBuilder() {
    override val name: String = "example"

    init {
        arguments {
            "unsigned int" { IntegerArgumentType.integer(0) }
            "flag" { BoolArgumentType.bool() }
        }

        executes { source ->
            val integer = getArg("unsigned int", Int::class)
            val flag = getArg("flag", Boolean::class)
            source.sender.sendMessage("Unsinged integer: $integer, flag: $flag")
            0
        }
    }
}

class ComshopPlugin : JavaPlugin() {

    override fun onEnable() {
        CommandRegistrar.lifecycleRegister(this)
        CommandRegistrar.register(ExampleCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
```

# TODO - 앞으로 추가될수도 있는것

* 인수 분기   
    comshop 은 아직 인수 분기를 지원하지 않습니다.

* 동적 인수   
    제대로 테스트 되지 않았습니다. 타 동적 인수 구현이 존재할순 있습니다.
    길이가 특정되지 않는 명령어의 구현 또한 불문명합니다 
    (예로, as 와 at 등을 여럿 연결할수 있는 execute 같은 명령어)

* 인수 타입 커스텀 시스템   
    현재 브리가디어는 `CustomArgumentType` 을 통해 인수 타입 커스텀을 지원하지만,
    추후 comshop 의 목적에 맞춰 자체 커스텀 인수가 추가될수 있습니다