try:
    from APMonitor.apm import *
except:
    # Automatically install APMonitor
    import pip
    pip.main(['install','APMonitor'])
    from APMonitor.apm import *
    
# Import APM package
from apm import *

# Solve optimization problem
sol = apm_solve('file',3)
file2 = open("variables.txt", 'r')
x= file2.read().splitlines()
file2.close()
print(x)
file = open("TestFile.txt", "w")
file.write('--- Results of the Optimization Problem --- \n')
y = 0
for y in range (0, len(x)):
	file.write(x[y] + ': '+str(sol[x[y]])+ "\n")
file.close()
