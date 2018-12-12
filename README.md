# Datadog APM + Clojure

Here's a simple Clojure web application that fails to work with the Datadog APM agent.

Environment: AWS EC2 t2.small, openjdk 8

Steps to reproduce:

1. Install Datadog agent:

```bash
API_KEY='your api key'
DD_API_KEY=${API_KEY} bash -c "$(curl -L https://raw.githubusercontent.com/DataDog/datadog-agent/master/cmd/agent/install_script.sh)"
```

2. Download Datadog Java agent jar:

```bash
wget -O dd-java-agent.jar 'https://search.maven.org/classic/remote_content?g=com.datadoghq&a=dd-java-agent&v=LATEST'
```

3. Upload `app.jar` included in the repo to your EC2 instance:

```bash
scp -i ~/compute_software/ssh-keys/kenny_test.pem app.jar ubuntu@${host}:~
```

4. SSH into your EC2 instance and start the web application:

```bash
java -javaagent:dd-java-agent.jar -server -cp app.jar clojure.main -m compute.fibonacci.core
```

After several minutes this will result in a `java.lang.OutOfMemoryError: Java heap space`.
Here is the full output of running the above command:

```
java -javaagent:dd-java-agent.jar -server -cp app.jar clojure.main -m compute.fibonacci.core
[main] INFO datadog.trace.agent.ot.DDTraceOTInfo - dd-trace - version: 0.19.0~81b33ef3
[main] INFO datadog.trace.agent.ot.DDTracer - New instance: DDTracer-73194df{ serviceName=unnamed-java-app, writer=DDAgentWriter { api=DDApi { tracesEndpoint=http://localhost:8126/v0.4/traces } }, sampler=AllSampler { sample=true }, runtimeId=4f6ddca2-f929-45c0-9a8d-35f6fac9ea73, defaultSpanTags={}}
[main] INFO datadog.trace.agent.tooling.VersionLogger - dd-trace-ot - version: 0.19.0~81b33ef3
[main] INFO datadog.trace.agent.tooling.VersionLogger - dd-trace-api - version: 0.19.0~81b33ef3
[main] INFO datadog.trace.agent.tooling.VersionLogger - dd-java-agent - version: 0.19.0~81b33ef3
[main] INFO datadog.trace.agent.jmxfetch.JMXFetch - JMXFetch is disabled
Exception in thread "main" java.lang.ExceptionInInitializerError
	at clojure.main.<clinit>(main.java:20)
Caused by: java.lang.OutOfMemoryError: Java heap space, compiling:(clojure/core.clj:7535:10)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:7010)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:6991)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyze(Compiler.java:6729)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:6998)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyze(Compiler.java:6729)
	at clojure.lang.Compiler$IfExpr$Parser.parse(Compiler.java:2817)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:7003)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyze(Compiler.java:6729)
	at clojure.lang.Compiler$BodyExpr$Parser.parse(Compiler.java:6100)
	at clojure.lang.Compiler$LetExpr$Parser.parse(Compiler.java:6420)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:7003)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:6991)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyze(Compiler.java:6729)
	at clojure.lang.Compiler$BodyExpr$Parser.parse(Compiler.java:6100)
	at clojure.lang.Compiler$FnMethod.parse(Compiler.java:5460)
	at clojure.lang.Compiler$FnExpr.parse(Compiler.java:4022)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:7001)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyze(Compiler.java:6729)
	at clojure.lang.Compiler$BodyExpr$Parser.parse(Compiler.java:6100)
	at clojure.lang.Compiler$FnMethod.parse(Compiler.java:5460)
	at clojure.lang.Compiler$FnExpr.parse(Compiler.java:4022)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:7001)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:6991)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.access$300(Compiler.java:38)
	at clojure.lang.Compiler$DefExpr$Parser.parse(Compiler.java:595)
	at clojure.lang.Compiler.analyzeSeq(Compiler.java:7003)
	at clojure.lang.Compiler.analyze(Compiler.java:6773)
	at clojure.lang.Compiler.analyze(Compiler.java:6729)
	at clojure.lang.Compiler.eval(Compiler.java:7066)
	at clojure.lang.Compiler.load(Compiler.java:7514)
	at clojure.lang.RT.loadResourceScript(RT.java:379)
	at clojure.lang.RT.loadResourceScript(RT.java:370)
	at clojure.lang.RT.load(RT.java:460)
	at clojure.lang.RT.load(RT.java:426)
	at clojure.lang.RT.doInit(RT.java:468)
	at clojure.lang.RT.<clinit>(RT.java:336)
	... 1 more
Caused by: java.lang.OutOfMemoryError: Java heap space
```