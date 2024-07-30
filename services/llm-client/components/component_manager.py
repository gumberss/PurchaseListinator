from typing import List, Type
from components.scylla_connection import ScyllaConnection
from components.base_component import BaseComponent

components_to_initialize: List[Type[BaseComponent]] = [
    ScyllaConnection
]

class ComponentManager:
    components: dict[Type[BaseComponent], BaseComponent] = {}

    @classmethod
    def initialize_components(cls) -> None:
        for component_type in components_to_initialize:
            try:
                component_instance = component_type()
                component_instance.initialize()
                cls.components[component_type] = component_instance
            except Exception as e:
                print(f"Failed to initialize {component_type.__name__}: {e}")

    @classmethod
    def get_component(cls, component_type: Type[BaseComponent]) -> BaseComponent:
        return cls.components.get(component_type)

    @classmethod
    def close_components(cls) -> None:
        for component in cls.components.values():
            try:
                component.close()
            except Exception as e:
                print(f"Failed to close component: {e}")