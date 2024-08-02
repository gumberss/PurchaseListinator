import pytest
from logic import prompts 
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