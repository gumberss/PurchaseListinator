from wires.inputs.interactions import Interaction as WireInteraction
from models.interactions import Interaction as ModelInteraction

def dto_to_model(dto: WireInteraction) -> ModelInteraction:
    return ModelInteraction(dto.requestId, dto.promptName, dto.variables, dto.images)
