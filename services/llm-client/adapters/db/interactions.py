from models.interactions import Interaction, InteractionRequest
from utils import json

def request_to_model(wire):
    return InteractionRequest(wire.id, json.deserialize(wire.interaction, Interaction) , wire.response, wire.request_date, wire.status, wire.timeout)