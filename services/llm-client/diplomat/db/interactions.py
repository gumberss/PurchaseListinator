from components.scylla_connection import ScyllaConnection
from datetime import datetime, timezone
from adapters.db import interactions as interactions_db_adapter
from models.interactions import InteractionRequest
from utils import json

def get_interaction(interaction_id, scylla: ScyllaConnection):
    query = "select * from execution.interaction_requests where id = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (interaction_id,))
    return interactions_db_adapter.request_to_model(rows[0]) if rows else None

def insert(interaction, scylla: ScyllaConnection):
    statement = """INSERT INTO execution.interaction_requests (id, interaction, response, request_date, status, timeout, details) 
                   VALUES (?, ?, ?, ?, ?, ?, ?)"""
    interaction_id = interaction.id
    interaction_value = json.serialize(interaction)
    response = None 
    request_date = int(datetime.now(timezone.utc).timestamp() * 1000)
    status = 'pending'  
    timeout = 30000 
    details = None
    prepared_statement = scylla.session.prepare(statement)
    scylla.session.execute(prepared_statement, (interaction_id, interaction_value, response, request_date, status, timeout, details))
    return InteractionRequest(interaction_id,interaction, response, request_date, status, timeout, details)


def update_interaction(interaction_request: InteractionRequest, scylla: ScyllaConnection):
    statement = """UPDATE execution.interaction_requests
                   SET response = %s, status = %s, details = %s
                   WHERE id = %s"""
    interaction_id = interaction_request.id
    response = interaction_request.response
    status = interaction_request.status 
    details = interaction_request.details
    scylla.session.execute(statement, (response, status, details, interaction_id))