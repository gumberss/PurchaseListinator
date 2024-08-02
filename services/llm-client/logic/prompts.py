from models.prompts import Prompt
import re

def find_variables(prompt : Prompt):
    pattern = r'\{\{(\w+)\}\}'
    return re.findall(pattern, prompt.prompt)