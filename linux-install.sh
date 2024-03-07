#!/bin/bash

sudo mkdir /usr/kylin
sudo cp -r ./* /usr/kylin
sudo echo '/usr/kylin/kylin $1 $2' > /bin/kylin
sudo chmod +x /bin/kylin
sudo chmod 777 /usr/kylin/* -R

sudo echo '/usr/kylin/bin/kpt $1 $2 $3 $4 > /bin/kpt'
sudo chmod +x /bin/kpt
