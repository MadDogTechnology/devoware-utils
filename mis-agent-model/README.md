# mis-agent-model
The **mis-agent-model** library encapsulates a set of common model objects shared by multiple applications.  Each model object is
basically an immutable POJO created using a variation of the Builder pattern first articulated by Joshua Bloch in the second edition
of his seminal book [Effective Java](https://www.amazon.com/Effective-Java-2nd-Joshua-Bloch/dp/0321356683). Each model class is also
designed to support JSON serialization/deserialization via [Jackson](https://github.com/FasterXML/jackson) annotations.
