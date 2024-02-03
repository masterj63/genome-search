tasks.register("run") {
    doLast {
        exec {
            commandLine("bash", "main.sh")
        }
    }
}