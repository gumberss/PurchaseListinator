from uuid import UUID

class Interaction:
    def __init__(self, id: UUID, prompt_name:str, variables:dict, images:dict, message:str = None):
        self.id = id
        self.prompt_name = prompt_name
        self.variables = variables
        self.images = images
        self.message = message

class InteractionRequest:
    def __init__(self, id, interaction, response, request_date, status, timeout):
        self.id = id
        self.interaction = interaction
        self.response = response
        self.request_date = request_date
        self.status = status
        self.timeout = timeout

    def failed(self):
        self.status = 'failed'
        return self
        

class InteractionResult:
    def __init__(self, id, status, response):
        self.id = id
        self.status = status
        self.response = response