import json
import requests
api_url = "http://localhost:58000/api/v1/host"

headers={"X-Auth-Token": "NC-148-85a4f0ce92814a5e8466-nbi"}

resp = requests.get(api_url, headers=headers, verify=False)

print("Request status: ", resp.status_code)

response_json = resp.json()
hosts = response_json["response"]
print(hosts)