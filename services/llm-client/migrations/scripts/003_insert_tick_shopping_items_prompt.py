from cassandra.cluster import Cluster

def run_migration(session):
    session.execute("""
    INSERT INTO configuration.prompts (prompt_name, prompt)
    VALUES('tick_shopping_items', 'Are you able to tick items on a cart if I send you a picture?')
    """)
    