import json
import requests
api_url = "http://localhost:58000/api/v1/network-device"

headers={"X-Auth-Token": "NC-148-85a4f0ce92814a5e8466-nbi"}

resp = requests.get(api_url, headers=headers, verify=False)

print("Request status: ", resp.status_code)

response_json = resp.json()
networkDevices = response_json["response"]
print(networkDevices)
for networkDevice in networkDevices:
    print(networkDevice["hostname"], "\t", networkDevice["platformId"], "\t", networkDevice["managementIpAddress"])