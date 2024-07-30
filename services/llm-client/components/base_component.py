from abc import ABC, abstractmethod

class BaseComponent(ABC):
    @abstractmethod
    def initialize(self):
        pass

    @abstractmethod
    def close(self):
        pass
