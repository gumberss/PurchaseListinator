from cassandra.cluster import Cluster

def run_migration(session):
    session.execute("""
    CREATE KEYSPACE IF NOT EXISTS execution
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
    """)
    session.execute("""
    CREATE TABLE IF NOT EXISTS execution.interactions (
        interaction_id UUID PRIMARY KEY,
        input text,
        response text,
        request_date date,
        status int,
        timeout int 
    );
    """)