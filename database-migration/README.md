# database-migration

This project stores all of the Liquibase changesets representing shema updates for the relational database used by the various
screen scraper components.  Anytime a new schema update is required, a sequentially numbered Liquibase-formatted changeset file must be created within
the 'migrations' folder.  The gradle build script allows you to execute Liquibase commands against whatever database is defined within your
```~/.gradle/gradle.properties``` file.  Certain properties must therefore be added to your ```~/.gradle/gradle.properties``` file, as
shown in the example below:

```
local_db_url=jdbc:postgresql://localhost:5432/cdevoto
local_db_username=cdevoto
local_db_password=
```

To update your database so that it reflects the most recent schema updates, you can execute the following command:

```
./gradlew update
```

Other Liquibase commands are also accessible as gradle tasks.  For more information, see the **[liquibase-gradle-plugin](https://github.com/liquibase/liquibase-gradle-plugin)**.

The build script also includes custom tasks to bundle up the migrations.xml file and the migrations folder into a TAR file, and to publish
this TAR file to the nexus artifact repository.  This makes it possible for the changesets to be accessed by other build scripts.  Anytime
you add a new changeset, you should therefore execute the following command:

```
./gradlew clean build uploadArchives
```

If you wish to use this TAR file within another gradle build script, you can create a separate 'database' configuration, and declare a dependency on the database-migration artifact as follows:

```
configurations {
  database
}

dependencies {
   database 'com.doradosystems.mis.common:database-migration:1.0.+'
}
```
