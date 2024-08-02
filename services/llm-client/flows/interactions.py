
from components.scylla_connection import ScyllaConnection
from datetime import datetime, timezone
from models.interactions import Interaction
from diplomat.db import interactions as interactions_db
from diplomat.db import prompts as prompts_db
from logic import interactions as interactions_logic
from logic import prompts as prompts_logic

def new_interaction(interaction: Interaction, scylla: ScyllaConnection):
    existent_interaction = interactions_db.get_interaction(interaction.id, scylla)
    if(existent_interaction is not None):
        if(interactions_logic.is_final_state(existent_interaction)):
            return existent_interaction
        else:
            if(interactions_logic.is_timed_out(existent_interaction, int(datetime.now(timezone.utc).timestamp() * 1000))):
                existent_interaction.failed("Timed out")
                interactions_db.update_interaction(existent_interaction, scylla)
                return existent_interaction
            else:
                return existent_interaction

    interaction_request = interactions_db.insert(interaction, scylla)

    prompt = prompts_db.get_prompt(interaction.prompt_name, scylla)
    if(prompt is None): 
        interaction_request.failed("Prompt not found")
        interactions_db.update_interaction(interaction_request, scylla)
        return interaction_request
    
    prompt_variables = prompts_logic.find_variables(prompt)
    missing_variables = prompts_logic.find_missing_variables(prompt_variables, interaction.variables)

    if(missing_variables):
        missing_variables = ", ".join(missing_variables)
        interaction_request.failed(f"Variables missing: {missing_variables}")
        interactions_db.update_interaction(interaction_request, scylla)
        return interaction_request

    rendered_prompt = prompts_logic.replace_variables(prompt, interaction.variables)
    

    # request Open IA
    # update the database
    # return
    return rendered_prompt


    
