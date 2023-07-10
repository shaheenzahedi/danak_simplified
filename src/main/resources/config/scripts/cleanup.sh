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

# Delete identical files in directory 1
echo "Deleting identical files in $dir1"
if ! find "$dir1" -type f -exec sh -c 'cmp -s "$1" "$2/${1#$3}" && rm "$1" && ln -s "$2${1#$3}" "$1"' sh {} "$dir2" "$dir1" \; 2>&1 | tee "$error_file" ; then
    echo "Error: Failed to delete identical files in $dir1" >&2
    cat "$error_file" >&2
    rm -f "$error_file"
    exit 1
fi

# Delete empty directories in directory 1
echo "Deleting empty directories in $dir1"
if ! find "$dir1" -type d -exec rmdir -p {} \; 2> /dev/null ; then
    echo "Error: Failed to delete empty directories in $dir1" >&2
    exit 1
fi

echo "Clean up done!"

