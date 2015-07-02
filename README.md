# WifiFingerPrint

This is a simple Android App for RSSI tracking and positioning.
The gui is very simple and not very user friendly, as it is only a proof-of-concept.

The App is able to learn locations, collect acces points and their rssi values.
This part is called RSSI-Fingerprinting.

The second part is the positioning algorithm.
Utilising the euclidian distance algorithm, indoor locations can be recognised.
The accuracy is about 2-4m indoors.

The "trained" data is saved as a simple csv-file.
