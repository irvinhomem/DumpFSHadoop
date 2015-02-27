==================================================================================
DumpFSHadoop
==================================================================================

Introduction
-------------
This proof of concept Java application enables the dumping of Android partitions (or embedded Linux device partitions), compression and transfer over a network and onto a Hadoop HDFS cluster.
This application is part of the LEIA system.

Assumptions
-------------
- The Android device is rooted, or one has some sort of privileged access in order to initiate the 'dd' command.
- That 'busybox' is installed on the Android device/embedded Linux device
- That there is an SSH server on the device allowing the HDFS Storage node to initiate an SSH connection to the device
- That the devices are on the same local network (Although the testing we did was over the Internet over a VPN connection)
- That a HDFS cluster is present.

Testing
-------------
Testing has been done using the following devices:
- Chumby (Legacy embedded Linux device)
- HTC MyTouch 4G Slide (MT4GS) - CyanogenMod
- HTC Incredible S - Android 2.3.3
- Samsung Galaxy Tab 2 - Android 4.0.3


Attribution
-------------
This work was done by Irvin Homem as part of his Master's Thesis at KTH - THe Royal Insitute of Technology (Stockholm, Sweden)
- http://irvinhomem.com/pubs/IrvinHomem-MscThesis-LEIA.pdf
- http://kth.diva-portal.org/smash/record.jsf?pid=diva2%3A766909&dswid=-6661



