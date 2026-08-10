# Suggestions

## Custom suggestions on arguments

Attach a `suggests` block to any argument to control what the client shows while typing:

```kotlin
register("somecommand") {
    requires { sender.isOp }

    arguments {
        "color" named word()
            .suggests {
                suggest("red")
                suggest("green")
                suggest("blue")
                suggest("white")
            }
            .asArg
    }
}
```

## The suggestion builder

Inside `suggests { }` you have:

* `suggest(text: String)` / `suggest(text: String, tooltipMessage: Component?)` — add a suggestion, optionally with a tooltip
* `sender`, `entity`, `player` — the sender-related values (from `SourceContext`)
* `context: CommandWritingContext` — the current input state:

| Property | Meaning |
|----------|---------|
| `fullInput` | The full input string |
| `start` | Where the current argument starts |
| `remaining` | The remaining (unparsed) input — `fullInput.substring(start)` |
| `remainingLower` | `remaining` in lowercase |

A tooltip example:

```kotlin
arguments {
    "color" named word()
        .suggests {
            suggest("red", Component.text("A warm color"))
            suggest("green", Component.text("A calm color"))
        }
        .asArg
}
```

## Suggestions for custom argument types

Custom argument types define their own suggestions through the `suggests` block of `customArgument` (or via `simpleSuggests`). See [Custom Arguments](custom-arguments.md).

## QUOTED strings

When the underlying string type is `quotedString()`, suggestions usually need quotes around them. The `simpleSuggests` helper handles this automatically: it wraps suggestions in quotes and strips quotes when matching against the current input.
