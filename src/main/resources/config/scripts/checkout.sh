#!/bin/bash

# Check that two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 git_repo_path tag_name" >&2
    exit 1
fi

# Get the Git repository path and tag name
git_repo_path="$1"
tag="$2"

# Change to the Git repository directory
cd "$git_repo_path" || exit

# Checkout the specified tag
if ! git checkout "$tag" ; then
    echo "Error: Failed to checkout tag $tag" >&2
    exit 1
fi

echo "Checkout to tag $tag done!"
