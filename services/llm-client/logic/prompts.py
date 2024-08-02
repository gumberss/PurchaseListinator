from models.prompts import Prompt
from typing import List, Dict, Any
import re

def find_variables(prompt : Prompt):
    pattern = r'\{\{(\w+)\}\}'
    return re.findall(pattern, prompt.prompt)

def find_missing_variables(prompt_variables: List[str], received_variables: Dict[str, Any]) -> List[str]:
    missing_vars = []
    for var in prompt_variables:
        if var not in received_variables or received_variables[var] is None:
            missing_vars.append(var)
    return missing_vars

def replace_variables(prompt: Prompt, variables: dict[str, Any]) -> str:
    pattern = re.compile(r"\{\{(.*?)\}\}")
    return pattern.sub(lambda match: str(variables.get(match.group(1), match.group(0))), prompt.prompt)
