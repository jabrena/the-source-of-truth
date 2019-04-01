package org.jab.thesourceoftruth.service.maven;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.config.GlobalConfiguration;
import org.jab.thesourceoftruth.config.Repository;
import org.jab.thesourceoftruth.service.shell.Command2;
import org.jab.thesourceoftruth.service.shell.Proccess2;
import org.jab.thesourceoftruth.service.shell.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MavenAnalysis {


    @Autowired
    private GlobalConfiguration configuration;

    @Autowired
    private Proccess2 shellProcess;

    public void run(final Repository repository) {



        if (Files.exists(Paths.get(repository.getPath() + "/gradle.build"))) {
            LOGGER.info("Executing tests with Gradle for repository: {}", repository.getId());

            ProcessResult result = shellProcess.execute(new Command2.Builder()
                    .add("cd " + repository.getPath())
                    .add("./gradlew clean test -i")
                    .build());

            //result.getResults().forEach(LOGGER::info);
            result.getResults().stream()
                    .skip(Math.max(0, result.getResults().size() - 10))
                    .forEach(line -> {
                        if(line.contains("BUILD SUCCESSFUL")) {
                            LOGGER.info(line);
                        }
                    });

        } else if (Files.exists(Paths.get(repository.getPath() + "/pom.xml"))) {
            LOGGER.info("Executing tests with Maven for repository: {}", repository.getId());

            ProcessResult result = shellProcess.execute(new Command2.Builder()
                    .add("cd " + repository.getPath())
                    .add("./mvnw clean package")
                    .build());

            result.getResults().forEach(LOGGER::info);
        }

    }

}