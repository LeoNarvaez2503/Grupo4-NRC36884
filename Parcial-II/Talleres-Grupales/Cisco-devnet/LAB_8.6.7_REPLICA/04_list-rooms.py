import json
import requests
from config import ACCESS_TOKEN

url = 'https://webexapis.com/v1/rooms'
headers = {
    'Authorization': f'Bearer {ACCESS_TOKEN}',
    'Content-Type': 'application/json'
}
params = {
    'max': '100'
}

res = requests.get(url, headers=headers, params=params)
print(f'Status: {res.status_code}')
print(json.dumps(res.json(), indent=4))
