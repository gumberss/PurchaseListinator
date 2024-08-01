class Interaction:
    def __init__(self, id, prompt_name, variables, images):
        self.id = id
        self.prompt_name = prompt_name
        self.variables = variables
        self.images = images
        self.message = None

    def failed(self):
        self.status = 'failed'
        return self

class InteractionResult:
    def __init__(self, id, status, response):
        self.id = id
        self.status = status
        self.response = response