tasks.register("run") {
    doLast {
        exec {
            commandLine("bash", "-c", "python3 main.py")
        }
    }
}