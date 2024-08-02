from components.scylla_connection import ScyllaConnection
from adapters.db import prompts as prompts_db_adapter

def get_prompt(prompt_name, scylla: ScyllaConnection):
    query = "select * from configuration.prompts where prompt_name = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (prompt_name,))
    return prompts_db_adapter.to_model(rows[0]) if rows else None