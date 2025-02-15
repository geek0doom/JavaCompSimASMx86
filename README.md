This is a project from my operating systems class that i'm rather proud of. It may be a bit buggy and only takes .json files, but it was mostly coded by me with minor contributions from other team members, PS thanks Josh you helped a ton on the memory and other subsystems

Abstract:
The experimental operating system simulator contains a Kernel and Loader wrapped into a Simulator module, that instantiates the objects and pulls the program files onto the disk. Further, the operating system contains a simulated single-core Central Processing Unit (CPU) that can perform various rudimentary functions as instructed by assembly code. It also contains a simulated Memory (RAM), and Disk Storage that are 2048 bytes and 4096 bytes respectively. The operating system also contains both a Long-Term Scheduler for process optimization and wait queues. Additionally, the Central Processing Unit has several sub-parts including the Process Control Bus (PCB), the Direct Memory Access (DMA), and Registers.

Overview
The operating system simulation was designed to be split into seven different modules. Each module would function as a facsimile of its equivalent hardware component. This allowed each piece to be tested independently and for decreased coupling of the simulations. However, one of the key attributes of this design is that while the jobs are inputted as hex-based assembly code, the simulation has a translation layer that converts the code into integers, strings, and other types that are more readable and manipulatable within Java.

![image](https://github.com/user-attachments/assets/5095243b-4e37-4fa5-9206-53fe4c885291)

https://www.youtube.com/embed/aesEmOlsIQI?feature=oembed


ChangeLogs:
**Todo
