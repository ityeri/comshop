# Version Compatibility

comshop separates the command definition (version-independent) from the registration implementation (version-specific).
Your command code stays the same; only the `impl` artifact changes per Paper version.

## Supported Paper versions

| Paper version | Implementation module | Compatibility  |
|---------------|-----------------------|----------------|
| 1.21.3        | `impl-1.21.10`        | ❌ Incompatible |
| 1.21.4 - 26.3 | `impl-1.21.10`        | ✅ Compatible   |

Notes:

* The `comshop-interface` module is compiled against the Paper 1.21.4 API.
* The only implementation module currently available is `impl-1.21.10`, built against Paper 1.21.10.
* The `example-plugin` test server runs Paper 26.3.

## Choosing an implementation module

Add the `impl-<version>` artifact that matches your server's Paper version. For example, on Paper 1.21.10:

```kotlin
dependencies {
    implementation("com.github.ityeri.comshop:front:v2.1.0")
    implementation("com.github.ityeri.comshop:impl-1.21.10:v2.1.0")
}
```

## Future versions

* Support for Paper versions below 1.21.4 is planned.
* New implementation modules will be published as separate artifacts (`impl-<mc version>`).
