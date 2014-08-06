# tsapi.pro - Telephony Services API Properties File for JTAPI
#
# This file must be located in one of the directories found in CLASSPATH.

# ---------------------------------------------------------------------------
# This is a list of the servers offering Telephony Services via TCP/IP.
# Either domain name or IP address may be used; the default port number is 450.
# The form is: host_name=port_number   For example:
#
# tserver.mydomain.com=450
# 127.0.0.1=450
#
# (Remove the '#' when creating actual server entries.)
10.10.10.100=450
10.10.10.101=450

# ---------------------------------------------------------------------------
# When accessing Telephony Services via a secure, encrypted connection, the
# Application Enablement (AE) Services server sends its certificate to the
# JTAPI client, and the JTAPI client verifies that the certificate is signed
# by a trusted Certificate Authority (CA).
#
# If your organization has installed its own certificate on the AE Server,
# then the JTAPI client must have access to the trusted CA certificate(s)
# for the AE Services server certificate.  Provide the location of a Java
# Key Store containing the trusted CA certificate(s) and the password for
# the Java Key Store here.  For example:
#
#trustStoreLocation=
#trustStorePassword=
#
# (Note the use of "\\" instead of "\" when specifying Windows file locations.)

# ---------------------------------------------------------------------------
# This file may specify alternate TSAPI links (Tlinks) for a preferred Tlink.
# The format of each entry is:
#
# Alternates(preferred_Tlink)=alternate_Tlink_1:alternate_Tlink_2:...
#
# Each entry may specify between one and four alternate Tlinks for the
# preferred Tlink.  Up to 16 entries are allowed.
#
# For example:
#
# Alternates(AVAYA#CM1#CSTA#AES1)=AVAYA#CM1#CSTA#AES2:AVAYA#CM1#CSTA#AES3
tsDevicePerformanceOptimization=true
callCompletionTimeout=120
maxWaitForSocket=5
[Telephony Servers]
Alternates(AVAYA#AVAYA_ECS#CSTA#THOR)=AVAYA#AVAYA_ECS#CSTA#CICLOPE