# Requirements 
- cassandra-driver
- pip install 'uvicorn[standard]'
- pip install fastapi
- pip install pytest

docker run -d --name some-scylla -p 9042:9042 scylladb/scylla
docker exec -it some-scylla cqlsh 
 python .\main.py

Needs:
    Get variables from the OS
    Make it works on doker


