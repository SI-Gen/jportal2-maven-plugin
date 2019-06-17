# JPortal2 Maven Plugin
Maven plugin for [JPortal2](https://github.com/SI-Gen/jportal2)

##Example Usage
```xml
<build>
    <plugins>
        <plugin>
            <groupId>za.co.bbd</groupId>
            <artifactId>jportal2-maven-plugin</artifactId>
            <configuration>
                <sourcePath>${basedir}/src/main/sql/</sourcePath>
                <generators>
                    <generator>JavaJCCode:${basedir}/target/generated-sources/java/za/co/anydb</generator>
                    <generator>PostgresDDL:${basedir}/target/generated-sources/scripts/sql</generator>
                </generators>
                <compilerFlags>
                    <compilerFlag>utilizeEnums</compilerFlag>
                </compilerFlags>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>za.co.bbd</groupId>
                    <artifactId>jportal2</artifactId>
                    <version>${jportal2.version}</version>
                </dependency>
            </dependencies>
            <executions>
                <execution>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>jportal</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
