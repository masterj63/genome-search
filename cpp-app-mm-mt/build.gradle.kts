plugins {
    `cpp-application`
}

application {
    targetMachines.add(machines.macOS.x86_64)
}

tasks.withType(CppCompile::class).configureEach {
//    compilerArgs.addAll(listOf("-I/usr/include -lcrypto"))
    compilerArgs.add("-std=c++17")
}
tasks.withType<LinkExecutable>().configureEach {
    linkerArgs.addAll(
        listOf(
            "-L/usr/lib",
            "-lboost_system",
            "-lboost_iostreams",
        )
    )
}

tasks.register("run", Exec::class) {
    dependsOn("assemble")
    commandLine("./build/exe/main/debug/cpp-app-mm-mt")
}
