from fastapi import FastAPI
from wires.inputs.interactions import Interaction
from flows.interactions import new_interaction
from adapters.inputs.interaction import dto_to_model
from components.component_manager import ComponentManager
from components.scylla_connection import ScyllaConnection
from adapters.outputs import interactions

app = FastAPI()

@app.on_event("startup")
async def startup_event():
    ComponentManager.initialize_components()
    
@app.on_event("shutdown")
async def shutdown_event():
    ComponentManager.close_components()

@app.post("/api/llm/interactions")
def greet(interaction: Interaction):
    return interactions.model_to_dto(new_interaction(dto_to_model(interaction), ComponentManager.get_component(ScyllaConnection)))

