#!/bin/bash

# Check that two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 directory1 directory2"
    exit 1
fi

# Get the directories to compare
dir1="$1"
dir2="$2"
csv_file="$dir2/files.csv"

# Create directory 2 and any missing parent directories if they don't exist
echo "Creating directory $dir2 and any missing parent directories"
mkdir -p "$dir2"

# Create CSV file header
echo "Creating CSV file $csv_file with file checksums and sizes"
echo "Name,Path,Checksum,Size" > "$csv_file"

# Generate checksum and size for each file in directory 1 and write to CSV file
find "$dir1" -type f -print0 | while IFS= read -r -d $'\0' file; do
    name="$(basename "$file")"
    path="$(dirname "$file")"
    rel_path="$(realpath --relative-to="$dir1" "$path")"
    checksum="$(md5sum "$file" | cut -d' ' -f1)"
    size="$(wc -c < "$file")"
    echo "$name,$rel_path,$checksum,$size" >> "$csv_file"
done

echo "Generate CSV done!"
