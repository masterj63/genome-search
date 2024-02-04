### Misc

To run them all, `./gradlew run`.

To attach the data, `ln -s "$HOME/Downloads/genomas"`.

For the right Java version, consult `.java-version` which is meant to be picked up by `jenv`.

### Implementations

| Name          | Description                  |
|---------------|------------------------------|
| bash-app      | a 'grep -r' onliner          |
| clj-app       | reads every file into memory |
| cpp-app-mm    | memory-mapped                |
| cpp-app-slurp | reads every file into memory |
| kt-app-cr     | slurp + coroutines           |
| kt-app-mm     | memory-mapped                |
| kt-app-mt     | slurp + classic threads      |
| kt-app-slurp  | reads every file into memory |
| py-app        | simplest loops               |