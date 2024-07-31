class Interaction:
    def __init__(self, id, prompt_name, variables, images):
        self.id = id
        self.prompt_name = prompt_name
        self.variables = variables
        self.images = images
        self.message = None