#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 directory1 directory2"
    exit 1
fi

dir1="$1"
dir2="$2"
csv_file="$dir2/files.csv"

# Create directory 2 and any missing parent directories if they don't exist
mkdir -p "$dir2"

# Create CSV file header
echo "Name,Path,Checksum" > "$csv_file"

# Generate checksum for each file in directory 1 and write to CSV file
find "$dir1" -type f -print0 | while IFS= read -r -d $'\0' file; do
    name="$(basename "$file")"
    path="$(dirname "$file")"
    rel_path="$(realpath --relative-to="$dir1" "$path")"
    checksum="$(md5sum "$file" | cut -d' ' -f1)"
    echo "$name,$rel_path,$checksum" >> "$csv_file"
done