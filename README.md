#MongoDB driver for Scriptella ETL

##Usage example:

    <connection id="out" url="mongodb://localhost/test"/>
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
