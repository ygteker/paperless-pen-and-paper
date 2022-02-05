#!/bin/bash
mv ./backend.jar ./backend_in_use.jar
sudo chown springboot:springboot ./backend_in_use.jar
sudo chmod 500 ./backend_in_use.jar
sudo chmod 444 ./certificate.p12
sudo systemctl restart springboot-backend.service