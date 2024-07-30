from cassandra.cluster import Cluster

def run_migration(session):
    session.execute("""
    CREATE KEYSPACE IF NOT EXISTS configuration
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
    """)
    session.execute("""
    CREATE TABLE IF NOT EXISTS configuration.prompts (
        prompt_name text PRIMARY KEY,
        prompt text
    );
    """)