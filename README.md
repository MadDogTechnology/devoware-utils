# devoware-utils
A collection of utility libraries, including the **homonculus** framework for building Java stand-alone services.
## homonculus libraries
The following libraries are defined within the devoware-utils project:

* **[homonculus-bootstrap](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-bootstrap)**: a small library which provides base functionality for creating Java stand-alone applications.

* **[homonculus-database](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-database)**: encapsulates functionality needed to automatically create a data source that manages a pool of connections to a relational database from a YAML configuration file.

* **[homonculus-core](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-core)**: builds on the foundation established by the bootstrap library, providing a generalized framework for building java stand-alone services and microservices.

* **[homonculus-messaging](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-messaging)**: provides a higher-order API for interfacing to a RabbitMQ messaging system.

* **[mis-agent-model](https://github.com/cpdevoto/devoware-utils/tree/master/mis-agent-model)**: encapsulates a set of common model objects shared by multiple applications.

* **[homonculus-validators](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-validators)**: includes a collection of custom validation annotations together with their corresponding validation classes as
defined by [JSR 303 - Bean Validation](http://beanvalidation.org/1.0/spec/).

* **[homonculus-config](https://github.com/cpdevoto/devoware-utils/tree/master/homonculus-config)**: encapsulates logic used to automatically bind a YAML configuration file to a corresponding
[Jackson](https://github.com/FasterXML/jackson) annotated configuation class of your own design;  also includes functionality
to allow for configuration classes with validation annotations to be validated in accordance with the 
[JSR 303 - Bean Validation](http://beanvalidation.org/1.0/spec/) specification.

 