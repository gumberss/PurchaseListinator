from uuid import UUID

class Interaction:
    def __init__(self, id: UUID, prompt_name:str, variables:dict, images:dict, message:str = None):
        self.id = id
        self.prompt_name = prompt_name
        self.variables = variables
        self.images = images
        self.message = message

class InteractionRequest:
    def __init__(self, id, interaction, response, request_date, status, timeout, details):
        self.id = id
        self.interaction = interaction
        self.response = response
        self.request_date = request_date
        self.status = status
        self.timeout = timeout
        self.details = details

    def failed(self, details: str):
        self.status = 'failed'
        self.details = details
        return self
        