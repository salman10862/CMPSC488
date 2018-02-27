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


file = open("TestFile.txt", "w")
file.write('--- Results of the Optimization Problem --- \n')
file.write('x[0]: '+ str(sol['int_x[0]']) + "\n")
file.write('x[0]: '+ str(sol['int_x[1]'])+ "\n")
file.write('x[0]: '+ str(sol['int_x[2]'])+ "\n")
file.write('x[0]: '+ str(sol['int_x[3]'])+ "\n")
file.close()