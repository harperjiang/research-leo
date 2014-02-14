#!/usr/bin/python

import fileinput
import re
from types import *

dic = {}
for line in fileinput.input():
	# Replace _z with _b
	line = line.strip()	
	m = re.search('((_[a-z])?.jpg)',line)
	if type(m) != NoneType:
		line = line.replace(m.group(0), '_b.jpg')
	parts = line.rpartition('/');
	dic[parts[2].strip()] = line

for key in dic:
	print '{}'.format(dic[key])
