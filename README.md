# ByteHook
**A simple and user-friendly Java bytecode hook framework can obtain the ability to modify methods without knowing the bytecode knowledge.**

## Usage
[demo video](https://youtu.be/KRF6gmZ066E)

[bytehook-examples](https://github.com/xtherk/bytehook-examples)

```xml
<dependency>
    <groupId>io.github.xtherk</groupId>
    <artifactId>bytehook-sdk</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>

<repositories>
    <repository>
        <id>ossrh</id>
        <name>ossrh</name>
        <url>https://s01.oss.sonatype.org/content/repositories/releases</url>
        <releases>
            <enabled>true</enabled>
        </releases>
    </repository>
</repositories>
```
## Features

- [x] Replace the target method
- [ ] Similar to `AOP`(Aspect Oriented Programming)

## Credits
- [Recaf](https://github.com/Col-E/Recaf)
- [deencapsulation](https://github.com/xxDark/deencapsulation)