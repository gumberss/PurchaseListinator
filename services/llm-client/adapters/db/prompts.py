from models.prompts import Prompt

def to_model(wire) -> Prompt:
    return Prompt(wire.prompt_name, wire.prompt)