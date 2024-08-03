class Prompt:
    def __init__(self, prompt_name:str, prompt:str):
        self.prompt_name = prompt_name
        self.prompt = prompt

class RenderedPrompt:
    def __init__(self, prompt_name:str, prompt:str, images:dict[str, str]):
        self.prompt_name = prompt_name
        self.prompt = prompt
        self.images = images
        