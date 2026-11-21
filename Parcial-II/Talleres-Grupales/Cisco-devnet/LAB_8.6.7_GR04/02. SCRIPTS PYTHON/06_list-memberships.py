import json
import requests
from config import ACCESS_TOKEN, ROOM_ID

url = 'https://webexapis.com/v1/memberships'
headers = {
    'Authorization': f'Bearer {ACCESS_TOKEN}',
    'Content-Type': 'application/json'
}
params = {
    'roomId': ROOM_ID
}

res = requests.get(url, headers=headers, params=params)
print(f'Status: {res.status_code}')
print(json.dumps(res.json(), indent=4))
