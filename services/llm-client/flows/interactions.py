
from components.scylla_connection import ScyllaConnection
from datetime import datetime, timezone
from models.interactions import Interaction, InteractionResult
from diplomat.db import interactions as interactions_db
from diplomat.db import prompts as prompts_db
from logic import interactions as interactions_logic

def new_interaction(interaction: Interaction, scylla: ScyllaConnection):
    existent_interaction = interactions_db.get_interaction(interaction.id, scylla)
    if(existent_interaction is not None):
        if(interactions_logic.is_final_state(existent_interaction)):
            return existent_interaction
        else:
            if(interactions_logic.is_timed_out(existent_interaction, int(datetime.now(timezone.utc).timestamp() * 1000))):
                existent_interaction.failed()
                interactions_db.update_interaction(InteractionResult(existent_interaction.id, 'failed', None), scylla)
                return existent_interaction
            else:
                return existent_interaction

    prompt = prompts_db.get_prompt(interaction.prompt_name, scylla)
    if(prompt is None): 
        return None
    
    # fill prompt with variables
    interactions_db.insert(interaction, scylla)
    # request Open IA
    # update the database
    # return
    return interaction


    
