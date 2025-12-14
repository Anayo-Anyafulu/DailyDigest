#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: ./push_to_github.sh <github_repo_url>"
    echo "Example: ./push_to_github.sh https://github.com/Anayo-Anyafulu/DailyDigest.git"
    exit 1
fi

REPO_URL=$1

echo "Adding remote origin..."
git remote add origin $REPO_URL

echo "Renaming branch to main..."
git branch -m main

echo "Pushing to GitHub..."
git push -u origin main

echo "Done! 🚀"
