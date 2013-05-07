#MongoDB driver for Scriptella ETL

## Building from source
Type `mvn clean package` to build the driver. The result jar will be in the `target' folder. 

##Usage example:

Download MongoDB driver from http://central.maven.org/maven2/org/mongodb/mongo-java-driver/ and make sure the required JARs are on classpath:

    <connection id="out" url="mongodb://localhost/test"  classpath="../lib/scriptella-mongodb-driver.jar:../lib/mongo-java-driver-2.10.1.jar" />
    <query connection-id="in">
        SELECT * FROM USERS
        <script connection-id="out">
            {
                operation: 'db.collection.save',
                collection: 'users',
                data: {
                    user_id: '?user_id',
                    name: '?name'
                }
            }
        </script>
    </query>



For additional details see [Reference documentation](https://github.com/scriptella/scriptella-mongodb/wiki/Reference).
