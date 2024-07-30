from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider

# If your ScyllaDB is running without authentication, you can omit the auth_provider part
# auth_provider = PlainTextAuthProvider(username='your_username', password='your_password')

cluster = Cluster(['127.0.0.1'], port=9042)  # Change to your Docker host's IP if necessary
session = cluster.connect()

# Optional: Set the keyspace if you have one
# session.set_keyspace('your_keyspace')

# Execute a simple query
rows = session.execute('SELECT key FROM system.local')
for row in rows:
    print(row.key)

# Close the connection
cluster.shutdown()