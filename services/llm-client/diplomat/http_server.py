from fastapi import FastAPI
from wires.inputs.interactions import Interaction
from components.component_manager import ComponentManager
from components.scylla_connection import ScyllaConnection

app = FastAPI()

@app.on_event("startup")
async def startup_event():
    ComponentManager.initialize_components()
    
@app.on_event("shutdown")
async def shutdown_event():
    ComponentManager.close_components()

@app.post("/api/llm/interactions")
def greet(interaction: Interaction):
    a = ComponentManager.get_component(ScyllaConnection)
    query = "select * from configuration.prompts where prompt_name = 'tick_shopping_items' ;"
    prepared = a.session.prepare(query)
    #bound = prepared.bind(('value',))  # Replace 'value' with the actual value you want to query
    rows = a.session.execute(query)
    for row in rows:
        print(row)
