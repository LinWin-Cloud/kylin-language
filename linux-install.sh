#!/bin/bash

sudo mkdir /usr/kylin
sudo cp -r ./* /usr/kylin
sudo echo '/usr/kylin/kylin $1 $2' > /bin/kylin
sudo chmod +x /bin/kylin
sudo chmod 777 /usr/kylin/* -R
