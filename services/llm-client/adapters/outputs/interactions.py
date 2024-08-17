from wires.outputs.interactions import Interaction as WireOutInteraction 
from models.interactions import InteractionRequest

def model_to_dto(interaction_request: InteractionRequest) -> WireOutInteraction:
    return WireOutInteraction(request_id=interaction_request.id, 
                              status=interaction_request.status, 
                              response=interaction_request.response,
                              details = interaction_request.details)
