#!/bin/bash
scalac -d _build $1 || exit 1
scala -cp _build $2
