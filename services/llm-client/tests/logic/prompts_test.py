import pytest
from logic import prompts 
from typing import List, Dict, Any
from models.prompts import Prompt

def test_find_variables_basic():
    prompt = Prompt("","Hello {{name}}, how are you?")
    assert prompts.find_variables(prompt) == ['name']

def test_find_multiple_variables():
    prompt = Prompt("", "{{name}} and {{age}} are variables.")
    assert prompts.find_variables(prompt) == ['name', 'age']

def test_no_variables():
    prompt = Prompt("", "There are no variables here.")
    assert prompts.find_variables(prompt) == []

def test_edge_case_empty_string():
    prompt = Prompt("", "")
    assert prompts.find_variables(prompt) == []

def test_edge_case_empty_braces():
    prompt = Prompt("", "{{}} should not be considered a variable.")
    assert prompts.find_variables(prompt) == []

def find_missing_variables(variables: List[str], values: Dict[str, Any]) -> List[str]:
    missing_vars = []
    for var in variables:
        if var not in values or values[var] is None:
            missing_vars.append(var)
    return missing_vars

def test_find_missing_variables_all_present():
    variables: List[str] = ["var1", "var2", "var3"]
    values: Dict[str, Any] = {
        "var1": "value1",
        "var2": "value2",
        "var3": "value3"
    }
    assert find_missing_variables(variables, values) == []

def test_find_missing_variables_some_missing():
    variables: List[str] = ["var1", "var2", "var3"]
    values: Dict[str, Any] = {
        "var1": "value1",
        "var2": "value2"
    }
    assert find_missing_variables(variables, values) == ["var3"]

def test_find_missing_variables_some_none():
    variables: List[str] = ["var1", "var2", "var3"]
    values: Dict[str, Any] = {
        "var1": "value1",
        "var2": None,
        "var3": "value3"
    }
    assert find_missing_variables(variables, values) == ["var2"]

def test_find_missing_variables_empty_variables():
    variables: List[str] = []
    values: Dict[str, Any] = {
        "var1": "value1",
        "var2": "value2",
        "var3": "value3"
    }
    assert find_missing_variables(variables, values) == []

def test_find_missing_variables_empty_values():
    variables: List[str] = ["var1", "var2", "var3"]
    values: Dict[str, Any] = {}
    assert find_missing_variables(variables, values) == ["var1", "var2", "var3"]

def test_replace_variables():
    template = "Hello, {{name}}. You have {{count}} new messages."
    values = {"name": "Alice", "count": 5}
    assert prompts.replace_variables(Prompt("",template), values) == "Hello, Alice. You have 5 new messages."
    
    template = "Hello, {{name}}. You have {{count}} new messages and your ID is {{id}}."
    values = {"name": "Bob", "count": 10, "id": 12345}
    assert prompts.replace_variables(Prompt("",template), values) == "Hello, Bob. You have 10 new messages and your ID is 12345."
    
    template = "Hello, {{name}}."
    values = {}
    assert prompts.replace_variables(Prompt("",template), values) == "Hello, {{name}}."