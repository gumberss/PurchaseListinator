from components.scylla_connection import ScyllaConnection
from datetime import datetime, timezone
from adapters.db import interactions as interactions_db_adapter
from utils import json

def get_interaction(interaction_id, scylla: ScyllaConnection):
    query = "select * from execution.interaction_requests where id = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (interaction_id,))
    return interactions_db_adapter.request_to_model(rows[0]) if rows else None

def insert(interaction, scylla: ScyllaConnection):
    print(json.serialize(interaction))
    statement = """INSERT INTO execution.interaction_requests (id, interaction, response, request_date, status, timeout) 
                   VALUES (?, ?, ?, ?, ?, ?)"""
    interaction_id = interaction.id
    interaction_value = json.serialize(interaction)
    response = None 
    request_date = int(datetime.now(timezone.utc).timestamp() * 1000)
    status = 'pending'  
    timeout = 30000 
    prepared_statement = scylla.session.prepare(statement)
    scylla.session.execute(prepared_statement, (interaction_id, interaction_value, response, request_date, status, timeout))


def update_interaction(interaction_result, scylla: ScyllaConnection):
    statement = """UPDATE execution.interaction_requests
                   SET response = %s, status = %s
                   WHERE id = %s"""
    interaction_id = interaction_result.id
    response = interaction_result.response
    status = interaction_result.status 
    scylla.session.execute(statement, (response, status, interaction_id))