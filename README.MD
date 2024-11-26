WIMD
===
Where is my dart ?

API de l'application WIMD, app collaborative pour référencer les emplacements
des jeux d'arcade, fléchettes... dans sa ville. 


Génération du keystore à utiliser dans l'application (profil prod) : 

```
openssl pkcs12 -export \
    -in ${KEYSTORE_PATH}fullchain.pem \
    -inkey ${KEYSTORE_PATH}privkey.pem \
    -out ${KEYSTORE_PATH}keystore.p12 \
    -name tomcat \
    -CAfile ${KEYSTORE_PATH}chain.pem \
    -caname root
```