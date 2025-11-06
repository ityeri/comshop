# comeshop

> kommand 에 영감을 받아 만들어진 Brigadier 의 dsl 래퍼이자 명령어 라이브러리

comshop 은 kotlin dsl 을 통해 paper api 에서 동작하는 
플러그인 명령어를 정의하기 위한 DSL 을 제공합니다

모장의 명령어 파싱 라이브러리인 Brigadier 를 래핑하기에, 
기본적인 인자 지정엔 Brigadier 내장 `ArgumentType` 들을 사용할수 있습니다.

# example

```kotlin
val command = command("comshop") {
    op = false // Not implemented yet

    arguments { // 명령어의 인수 정의
        "int" { IntegerArgumentType.integer() }
        "word" { StringArgumentType.word() }
        "bool" { BoolArgumentType.bool() }
    }

    executes { source ->
        // 명령어의 실행을 정의
        // 위에서 지정한 인수 이름과 타입을 기준으로 인자를 가져옴
        val int = getArg("int", Int::class)
        val word = getArg("word1", String::class)
        val bool = getArg("bool", Boolean::class)
        source.sender.sendMessage("int: $int, word: $word, bool: $bool")

        0 // 종료 코드 리턴
    }

    then("tell") { // 하위 명령어
        arguments {
            // paper api 가 브리가디어 용으로 제공하는
            // PlayerSelectorArgumentResolver 사용
            "player" { ArgumentTypes.player() }
        }

        executes { source ->
            val player = getArg("player", PlayerSelectorArgumentResolver::class)
                .resolve(source).first()

            player.sendMessage("Someone want to talking with you!")

            0
        }
    }

    then("op-only") {
        op = true // Not implemented yet

        executes { source ->
            source.sender.sendMessage("You are used op-only command! good...")
            0
        }

        then("wasans") {
            executes { source ->
                source.sender.sendMessage("wa papyrus")
                0
            }
        }
    }
}

register(command, this)
```

인게임에서: 
```shell
/cmoshop 123 wasans true

int: 123, word: wasans, bool: true
```

```shell
/cmoshop tell ityeri

Someone want to talking with you!
```


# TODO

op, CustomArgumentType wrapper, dynamic argument