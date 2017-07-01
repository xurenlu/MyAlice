#!/bin/bash

param=$1

if [ -z "$param" ] 
then
  param="myalice"
fi

git add .
git commit -m "$param"
git push -u origin master
