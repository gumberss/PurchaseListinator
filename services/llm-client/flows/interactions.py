
from components.scylla_connection import ScyllaConnection
from uuid import uuid4
from datetime import datetime
from models.interactions import Interaction



def insert(interaction, scylla):
    statement = """INSERT INTO interactions (id, input, response, request_date, status, timeout) 
                   VALUES (?, ?, ?, ?, ?, ?)"""
    interaction_id = uuid4()
    input_value = interaction.message
    response = None 
    request_date = int(datetime.now(datetime.UTC).timestamp() * 1000)
    status = 'pending'  
    timeout = 30000 
    scylla.session.execute(statement, (interaction_id, input_value, response, request_date, status, timeout))

def get_prompt(prompt_name, scylla):
    query = "select * from configuration.prompts where prompt_name = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (prompt_name,))
    return rows[0] if rows else None

def get_interaction(interaction_id, scylla):
    query = "select * from execution.interactions where id = ?;"
    prepared_statement = scylla.session.prepare(query)
    rows = scylla.session.execute(prepared_statement, (interaction_id,))
    return rows[0] if rows else None

def update_interaction(interaction_result, scylla):
    statement = """UPDATE execution.interactions
                   SET response = %s, status = %s
                   WHERE id = %s"""
    interaction_id = interaction_result.id
    response = interaction_result.response
    status = interaction_result.status 
    scylla.session.execute(statement, (interaction_id, response, status))

class InteractionResponse:
    def __init__(self, id, status, response):
        self.id = id
        self.status = status
        self.response = response

def new_interaction(interaction: Interaction, scylla: ScyllaConnection):
    existent_interaction = get_interaction(interaction.id, scylla)
    if(existent_interaction is not None):
        if(existent_interaction.status == 'completed' or existent_interaction.status == 'failed'):
            return existent_interaction
        else:
            if(existent_interaction.request_date + existent_interaction.timeout > int(datetime.now(datetime.UTC).timestamp() * 1000)):
                existent_interaction.status='failed'
                update_interaction(InteractionResponse(existent_interaction.id, 'failed', None), scylla)
                return existent_interaction
            else:
                return existent_interaction


    prompt = get_prompt(interaction.prompt_name, scylla)
    if(prompt is None): 
        return None
    
    # fill prompt with variables
    insert(interaction, scylla)
    # request Open IA
    # update the database
    # return
    return interaction


    
