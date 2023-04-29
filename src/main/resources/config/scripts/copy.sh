#!/bin/bash

# Check that two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 directory1 directory2" >&2
    exit 1
fi

# Get the directories to compare
dir1="$1"
dir2="$2"

# Create a temporary file for errors
error_file="$(mktemp)"

# Create directory 1 if it doesn't exist
if [ ! -d "$dir1" ]; then
    echo "Creating directory $dir1"
    if ! mkdir "$dir1" ; then
        echo "Error: Failed to create directory $dir1" >&2
        cat "$error_file" >&2
        rm -f "$error_file"
        exit 1
    fi
fi

# Copy contents of directory 2 to directory 1
echo "Copying contents of $dir2 to $dir1"
if ! cp -r "$dir2"/* "$dir1" 2>&1 | tee "$error_file" ; then
    echo "Error: Failed to copy contents of $dir2 to $dir1" >&2
    cat "$error_file" >&2
    rm -f "$error_file"
    exit 1
fi

rm -f "$error_file"

echo "Copying done!"