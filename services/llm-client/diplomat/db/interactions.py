from components.scylla_connection import ScyllaConnection
from datetime import datetime, timezone

def get_prompt(prompt_name, scylla: ScyllaConnection):
    query = "select * from configuration.prompts where prompt_name = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (prompt_name,))
    return rows[0] if rows else None

def get_interaction(interaction_id, scylla: ScyllaConnection):
    query = "select * from execution.interactions where id = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (interaction_id,))
    return rows[0] if rows else None

def insert(interaction, scylla: ScyllaConnection):
    statement = """INSERT INTO execution.interactions (id, input, response, request_date, status, timeout) 
                   VALUES (?, ?, ?, ?, ?, ?)"""
    interaction_id = interaction.id
    input_value = interaction.message
    response = None 
    request_date = int(datetime.now(timezone.utc).timestamp() * 1000)
    status = 'pending'  
    timeout = 30000 
    prepared_statement = scylla.session.prepare(statement)
    scylla.session.execute(prepared_statement, (interaction_id, input_value, response, request_date, status, timeout))


def update_interaction(interaction_result, scylla: ScyllaConnection):
    statement = """UPDATE execution.interactions
                   SET response = %s, status = %s
                   WHERE id = %s"""
    interaction_id = interaction_result.id
    response = interaction_result.response
    status = interaction_result.status 
    scylla.session.execute(statement, (interaction_id, response, status))