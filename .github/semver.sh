#!/bin/bash

GIT_REV_LIST=`git rev-list --tags --max-count=1`
GIT_REV_LAST=`git rev-parse HEAD`
VERSION='0.0.0'
if [[ -n $GIT_REV_LIST ]]; then
    VERSION=`git describe --tags $GIT_REV_LIST`
fi
echo "Latest rev-list: $GIT_REV_LIST"
echo "Latest version tag: $VERSION"
# split into array
VERSION_BITS=(${VERSION//./ })
#get number parts and increase last one by 1
VNUM1=${VERSION_BITS[0]}
VNUM2=${VERSION_BITS[1]}
VNUM3=${VERSION_BITS[2]}

#compute major,minor,patch bump
MAJOR=`git log $GIT_REV_LIST..$GIT_REV_LAST --pretty=%B | egrep -c '^(breaking|major|BREAKING CHANGE)(\(.*\))?:'`
MINOR=`git log $GIT_REV_LIST..$GIT_REV_LAST --pretty=%B | egrep -c '^(feature|minor|feat)(\(.*\))?:'`
PATCH=`git log $GIT_REV_LIST..$GIT_REV_LAST --pretty=%B | egrep -c '^(fix|patch|docs|style|refactor|perf|test|chore)(\(.*\))?:'`

# PATCH 0.0.1
if [ $PATCH -gt 0 ]; then
    VNUM3=$((VNUM3+1))
fi
# MINOR 0.1.0
if [ $MINOR -gt 0 ]; then
    VNUM2=$((VNUM2+1))
    VNUM3=0
fi
# MAJOR 1.0.0
if [ $MAJOR -gt 0 ]; then
    VNUM1=$((VNUM1+1))
    VNUM2=0
    VNUM3=0
fi
# count all commits for a branch
GIT_COMMIT_COUNT=`git rev-list --count HEAD`
echo "GIT_COMMIT_COUNT: $GIT_COMMIT_COUNT"
#create new tag
NEW_TAG="$VNUM1.$VNUM2.$VNUM3"
echo "NEW_TAG: $NEW_TAG"
echo "VERSION=$NEW_TAG" >> $GITHUB_ENV
echo "PREVIOUS_VERSION=$VERSION" >> $GITHUB_ENV



