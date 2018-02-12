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
sol = apm_solve('hs71',3)


file = open("TestFile.txt", "w")
file.write('--- Results of the Optimization Problem --- \n')
file.write('x[1]: '+ str(sol['x[1]']) + "\n")
file.write('x[2]: '+ str(sol['x[2]'])+ "\n")
file.write('x[3]: '+ str(sol['x[3]'])+ "\n")
file.write('x[4]: '+ str(sol['x[4]'])+ "\n")
file.close()