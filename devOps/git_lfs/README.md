
 This script is for adding files to Git LFS and then pushing them to a repo.
 It assumes you have already initalized git lfs. 

 Why do you need this script?
 If you need to push a loooooooot of dependencies to Git LFS. 
 If you try to push all your files at once in a single commit, any sort of 
 interruption in network connectivity could cause the push to fail and then 
 you will have to start all over again. This happened to me several times as I # tried to push 24 GB of files via git lfs to a repository. I wrote this script
 to commit and then push each file individually so I could get past that hurdle
 and move on to other things. 

 Cons: this makes the commit history rather long, but after you do it once, 
 it should be smooth sailing from there. I've never had this con cause me any
 trouble.
