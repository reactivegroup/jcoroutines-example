# java coroutines

[![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://groups.google.com/g/reactive-group)

提供Java语言协程example的示例程序库.

## Contributing

[How to contribute](./CONTRIBUTING.md)

## Quasar

Quasar fiber 依赖 java instrumentation 修改你的代码，可以在运行时通过 java Agent 实现，也可以在编译时使用 ant task实现。

通过 java agent 很简单，在程序启动的时候将下面的指令加入到命令行，注意把 path-to-quasar-jar.jar 替换成你实际的 quasar java 的地址：

    -javaagent:path-to-quasar-jar.jar
    -javaagent:C:\Users\...\jcoroutines-example\agent\0.7.4\quasar-core-0.7.4-jdk8.jar
    