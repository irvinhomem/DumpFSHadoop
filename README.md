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
- That 'busybox' is installed on the Android device/embedded Linux device ('busybox' contains 'dd' if the OS doesn't have it)
- That there is an SSH server on the device allowing the HDFS Storage node to initiate an SSH connection to the device
- That the devices are on the same local network (Although the testing we did was over the Internet over a VPN connection)
- That a HDFS cluster is present to store the incoming dumped 'dd' file

Testing
-------------
Testing has been done using the following devices (... in reverse order):
- Google Nexus 5 - Android 5.1.1
- Samsung Galaxy S4-I9506-LTE - Android 5.1.1
- Samsung Galaxy Tab 2 - Android 4.0.3
- HTC Incredible S - Android 2.3.3
- HTC MyTouch 4G Slide (MT4GS) - CyanogenMod
- Chumby (Legacy embedded Linux device)


Attribution
-------------
This work was done by Irvin Homem as part of his Master's Thesis at KTH - The Royal Insitute of Technology (Stockholm, Sweden), and expanded on in some of his doctoral publications at Stockholm University.
- http://irvinhomem.com/pubs/IrvinHomem-MscThesis-LEIA.pdf
- http://kth.diva-portal.org/smash/record.jsf?pid=diva2%3A766909&dswid=-6661
- http://ojs.jdfsl.org/index.php/jdfsl/article/view/340



