TODO 남은 타입 정리 + 클래스 상속형 명령어 빌더는 이름 뭘로할거임?

# comshop

> kommand 에 영감을 받아 만들어진 Brigadier 의 dsl 래퍼이자 명령어 라이브러리

comshop 은 kotlin dsl 을 통해 paper api 에서 동작하는 
플러그인 명령어를 정의하기 위한 DSL 을 제공합니다

명령어 파싱을 위해 모장에서 사용하는 라이브러리인 `Brigadier` 를 기반으로 동작합니다

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

## 상속을 활용한 명령어 만들어보기

TODO

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
CommandRegistrar.register(command)
```

> `lifecycleRegister(plugin)` 와 `register(command)` 중에 뭘 먼저 호출하는지는 중요하지 않습니다.
> 다만 둘다 `LifecycleEvents.COMMANDS` 이전 시점엔 완료 되어 있어야 합니다

인게임에서:

```
/greeting 안녕!
```

```
/greeting some_player 와샍으!
```

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

(예컨대, `ArgumentType.player` 는 `Player` 가 아닌 `PlayerSelectorArgumentResolver` 를 반환합니다)

paper api 에서 Brigadier 를 위해 지원하는 타입들은 
[Paper docs - Development / API / Command API / Arguments](https://docs.papermc.io/paper/dev/command-api/arguments/minecraft/) 부분에 정리되어 있습니다

# full example

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

# TODO 앞으로 추가될수도 있는것

* 인수 분기   
    comshop 은 아직 인수 분기를 지원하지 않습니다.

* 동적 인수   
    제대로 테스트 되지 않았습니다. 타 동적 인수 구현이 존재할순 있습니다.
    길이가 특정되지 않는 명령어의 구현 또한 불문명합니다 
    (예로, as 와 at 등을 여럿 연결할수 있는 execute 같은 명령어)

* 인수 타입 커스텀 시스템   
    현재 브리가디어는 `CustomArgumentType` 을 통해 인수 타입 커스텀을 지원하지만,
    추후 comshop 의 목적에 맞춰 자체 커스텀 인수가 추가될수 있습니다