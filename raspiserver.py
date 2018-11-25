#!/usr/bin/env python


try:
    import RPi.GPIO as GPIO
except RuntimeError:
    print("Error importing RPi.GPIO!  This is probably because you need superuser privileges.  You can achieve this by using 'sudo' to run your script")

import socket
import time

# setup servo
servoPIN0 = 17
servoPIN1 = 18
servoPIN2 = 21
servoPIN3 = 22
GPIO.setmode(GPIO.BCM)
GPIO.setup(servoPIN0, GPIO.OUT)
GPIO.setup(servoPIN1, GPIO.OUT)
GPIO.setup(servoPIN2, GPIO.OUT)
GPIO.setup(servoPIN3, GPIO.OUT)

# TODO jos ei toimi, testaa 50 Hz
hz = 50
servos = [GPIO.PWM(servoPIN0, hz),
		GPIO.PWM(servoPIN1, hz),
		GPIO.PWM(servoPIN2, hz),
		GPIO.PWM(servoPIN3, hz)] # PWM with 200Hz

# Initialization
servos[0].start(2.5)
servos[1].start(2.5)
servos[2].start(2.5)
servos[3].start(2.5)

TCP_IP = '127.0.0.1'
TCP_PORT = 5005

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.listen(1)

def setAngleDeg(axis, val):
	print "Setting servo", axis, "to", val
	realval = val / 18.0 + 2.5
	servos[axis].ChangeDutyCycle(realval)
	
print "Waiting for client at", socket.gethostbyname(socket.gethostname())
conn, addr = s.accept()
print 'Connection address:', addr

while 1:
	data = conn.recv(1)
	if not data: break

	axis = int(data)

	data = conn.recv(3)
	if not data: break
	
	val = int(data)
	setAngleDeg(axis, val)

	conn.recv(1)

    	time.sleep(0.001)


servos[0].stop()
servos[1].stop()
servos[2].stop()
servos[3].stop()
GPIO.cleanup()
conn.close()

