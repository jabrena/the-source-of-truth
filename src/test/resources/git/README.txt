# Analysis

- git rev-list --max-parents=0 HEAD
- git log
- git log --reverse
- git log --merges --author="Juan Antonio Brena Moral"
- git log --merges --first-parent master
- git shortlog HEAD -sn --no-merges --since="1 Jan, 2019" --before="1 Feb, 2019"
- git log HEAD --since="1 Jan, 2018" --before="1 Feb, 2019" --author="Juan Antonio Brena Moral" --pretty=tformat: --numstat"
