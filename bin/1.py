import time

a = time.time()

for i in range(1000000):
    print("hello world")

b = time.time()
print(b-a)
