from cassandra.cluster import Cluster
from components.base_component import BaseComponent

class ScyllaConnection(BaseComponent):
    def __init__(self):
        self.cluster = None
        self.session = None

    def initialize(self):
        self.cluster = Cluster(['127.0.0.1'], port=9042)
        self.session = self.cluster.connect()

    def get_session(self):
        return self.session

    def close(self):
        if self.cluster:
            self.cluster.shutdown()