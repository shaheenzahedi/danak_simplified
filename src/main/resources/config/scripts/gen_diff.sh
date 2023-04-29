#!/bin/bash

# Check if two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <path-to-lists-folder> <path-to-diffs-folder>"
    exit 1
fi

# Get the path to the lists folder and the diffs folder
LISTS_FOLDER="$1"
DIFFS_FOLDER="$2"

# Create the diffs folder if it doesn't exist
mkdir -p "$DIFFS_FOLDER"

# Loop through all subdirectories of the lists folder
for dir in "$LISTS_FOLDER"/*/; do
    # Get the name of the subdirectory
    dir_name="$(basename "$dir")"

    # Check if the subdirectory name is a number
    if [[ "$dir_name" =~ ^[0-9]+$ ]]; then
        # Get the path to the files.csv in the subdirectory
        csv_path="$dir/files.csv"

        # Check if the files.csv exists
        if [ -f "$csv_path" ]; then
            # Loop through all other subdirectories of the lists folder
            for other_dir in "$LISTS_FOLDER"/*/; do
                # Get the name of the other subdirectory
                other_dir_name="$(basename "$other_dir")"

                # Check if the other subdirectory name is a number and is different from the current subdirectory
                if [[ "$other_dir_name" =~ ^[0-9]+$ ]] && [ "$dir_name" != "$other_dir_name" ] && [ "$dir_name" -lt "$other_dir_name" ]; then
                    # Get the path to the other files.csv in the other subdirectory
                    other_csv_path="$other_dir/files.csv"

                    # Check if the other files.csv exists
                    if [ -f "$other_csv_path" ]; then
                        # Generate the diff file name
                        diff_file_name="${dir_name}_${other_dir_name}_diff.txt"

                        # Get the path to the diff file in the diffs folder
                        diff_file_path="$DIFFS_FOLDER/$diff_file_name"

                        # Check if the diff file already exists
                        if [ -f "$diff_file_path" ]; then
                            echo "Skipping ${dir_name} vs ${other_dir_name}; diff already exists"
                            continue
                        fi

                        # Generate the minimal diff between the files.csv and the other files.csv
                        diff_output="$(diff -d "$csv_path" "$other_csv_path")"

                        # Print only the lines that differ between the two files
                        if [ -n "$diff_output" ]; then
                            echo "$diff_output" > "$diff_file_path"
                        fi
                    fi
                fi
            done
        fi
    fi
done