
from components.scylla_connection import ScyllaConnection
from uuid import uuid4
from datetime import datetime, timezone
from models.interactions import Interaction
from diplomat.db import interactions as interactions_db

class InteractionResponse:
    def __init__(self, id, status, response):
        self.id = id
        self.status = status
        self.response = response

def new_interaction(interaction: Interaction, scylla: ScyllaConnection):
    existent_interaction = interactions_db.get_interaction(interaction.id, scylla)
    if(existent_interaction is not None):
        if(existent_interaction.status == 'completed' or existent_interaction.status == 'failed'):
            return existent_interaction
        else:
            print(existent_interaction.request_date + existent_interaction.timeout)
            print(existent_interaction.request_date + existent_interaction.timeout -  int(datetime.now(timezone.utc).timestamp() * 1000)) 
            print( int(datetime.now(timezone.utc).timestamp() * 1000))
            if(existent_interaction.request_date + existent_interaction.timeout < int(datetime.now(timezone.utc).timestamp() * 1000)):
                print("failes")
                existent_interaction.status='failed'
                interactions_db.update_interaction(InteractionResponse(existent_interaction.id, 'failed', None), scylla)
                return existent_interaction
            else:
                return existent_interaction


    prompt = interactions_db.get_prompt(interaction.prompt_name, scylla)
    if(prompt is None): 
        return None
    
    # fill prompt with variables
    insert(interaction, scylla)
    # request Open IA
    # update the database
    # return
    return interaction


    
