import os
import importlib.util
from cassandra.cluster import Cluster

def apply_migrations(session, migrations_dir='scripts'):
    migration_files = sorted(f for f in os.listdir(migrations_dir) if f.endswith('.py') and f != "__init__.py")
    for migration_file in migration_files:
        migration_path = os.path.join(migrations_dir, migration_file)
        spec = importlib.util.spec_from_file_location(migration_file, migration_path)
        migration_module = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(migration_module)
        migration_module.run_migration(session)
        print(f"Applied {migration_file}")

def get_session():
    cluster = Cluster(['127.0.0.1'], port=9042)
    return cluster.connect()

def close_session(cluster):
    cluster.shutdown()

if __name__ == "__main__":
    session = get_session()
    apply_migrations(session)
    session.cluster.shutdown()