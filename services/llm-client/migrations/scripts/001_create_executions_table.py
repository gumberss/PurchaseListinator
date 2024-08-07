from cassandra.cluster import Cluster

def run_migration(session):
    session.execute("""
    CREATE KEYSPACE IF NOT EXISTS execution
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
    """)
    session.execute("""
    CREATE TABLE IF NOT EXISTS execution.interaction_requests (
        id UUID PRIMARY KEY,
        interaction text,
        response text,
        request_date bigint,
        status text,
        timeout int,
        details text             
    );
    """)