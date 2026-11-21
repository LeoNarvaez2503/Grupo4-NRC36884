import requests
import json


access_token = 'ZmIwOWNhMTItYjhjNy00OThjLWE4MjUtYzc2ZDNhNDliMDU3MGNhMTA2MWEtNzA2_P0A1_4324cf20-3dc3-4497-af07-1b996464c625' 
url = 'https://webexapis.com/v1/people'
headers = {
    'Authorization': 'Bearer {}'.format(access_token),
    'Content-Type': 'application/json'
}
params = {
    'email': 'anthonybastidas48@gmail.com'
}
res = requests.get(url, headers=headers, params=params)
print(json.dumps(res.json(), indent=4)) 