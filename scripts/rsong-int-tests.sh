#!/usr/bin/env python
##
##  int test for rsong-proxy
##
import sys
import requests

host = sys.argv[1]
URL = "http://" + sys.argv[1]

def _curl(name):
  for p in ["_Immersive.izr", "_Stereo.izr"]:
         start_time = time.time()
         r = requests.get(url=URL + '/v1/song/music/' + name + p)
         print(r.url)
         pring(r.header)
         print(r.status_code)
         elapsed_time=time.time() - start_time
         print("elapsed time: " + elapsed_time)

         if (name == 'Tiny_Human'):
            name='Tiny Human' ## i feel dirty :(

   start_time = time.time()
   r = requests.get(url=URL + '/v1/art/' + name + '.jpg')
   print(r.url)
   print(r.header)
   print(r.status_code)
   print("elapsed time: " + elapsed_time)
   return

for name in ['Broke', 'Euphoria', 'Tiny_Human']: i
               _curl(name)
