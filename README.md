# lc3-vm-kotlin

A Kotlin implementation of the [LC-3 Virtual Machine](https://en.wikipedia.org/wiki/Little_Computer_3), inspired by ["Write your Own Virtual Machine"](https://justinmeiners.github.io/lc3-vm/) article by [Justin Meiners](https://github.com/justinmeiners) and [Ryan Pendleton](https://github.com/rpendleton).

### Reference links

- [Write your Own Virtual Machine](https://justinmeiners.github.io/lc3-vm/)
- [LC-3 Instruction Set](https://justinmeiners.github.io/lc3-vm/supplies/lc3-isa.pdf)
- The [RawConsoleInput.java](https://www.source-code.biz/snippets/java/RawConsoleInput/RawConsoleInput.java) class by [Christian d'Heureuse](https://www.source-code.biz/) (originally found in [this StackOverflow thread](https://stackoverflow.com/a/30008252/4353763))

### Screenshots

#### Hangman

![lc3-vm-kotlin running Hangman](https://i.imgur.com/u8Lmu88.gif "lc3-vm-kotlin running Hangman")

#### Rogue

![lc3-vm-kotlin running Rogue](https://i.imgur.com/TqtJrRb.gif "lc3-vm-kotlin running Rogue")

### Installation and Setup Instructions

You will need [Maven](https://maven.apache.org/install.html) installed and configured onto your machine.

Cloning the repository:

1. Run `git clone https://github.com/CianciuStyles/lc3-vm-kotlin.git` in the terminal

Installation:

1. Run `mvn clean install` in the terminal

To run the test suite:

1. Run `mvn test` in the terminal

To run `lc3-vm-kotlin`:

1. Run `mvn exec:java@2048` in the terminal to run 2048.
1. Run `mvn exec:java@hangman` in the terminal to run Hangman.
1. Run `mvn exec:java@rogue` in the terminal to run Rogue.
