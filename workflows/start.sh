#!/bin/bash

mkdir -p results/{data,plots,tables}
echo "Computing the pairwise comparisons"
java -jar target/workflows.jar
echo "Interpreting and plotting the results"
Rscript plots.R
chmod -R 777 results/
