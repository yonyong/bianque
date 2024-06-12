
#!/bin/bash

# URL of the file to download
# replace your deploy ip here
url="http://{target_ip}/bianque/lib.tar.gz"

# Local IP
local_ip=$(hostname -I | awk '{print $1}')

#start
echo "Start to setup"

# Download the file
echo "Downloading file from $url"
wget --header="X-REQUEST-IP: $local_ip" "$url"
echo "Download completed"

# Extract the file
echo "Extracting file"
tar -zxvf lib.tar.gz
echo "Extract completed"

# Delete the file
echo "Deleting file"
rm -f lib.tar.gz
echo "Delete completed"

# Change the permissions of the file
echo "Changing permissions"
chmod -R 777 lib
echo "Change completed"

# remove the file which under /usr/local/bin match the pattern: bianque*
echo "Removing files under /usr/local/bin match the pattern: bianque"
rm -f /usr/local/bin/bianque*
echo "Remove completed"

# move the file which under lib/sh to the location: /usr/local/bin
echo "Moving files under lib/sh to /usr/local/bin"
mv lib/sh/* /usr/local/bin
echo "Move completed"

# if /usr/local/bin is not in the PATH, add it to the PATH
if [[ ":$PATH:" != *":/usr/local/bin:"* ]]; then
    echo "export PATH=\$PATH:/usr/local/bin" >> /etc/profile
    source /etc/profile
fi

# remove the file which under /etc/bash_completion.d/ match the pattern: bianque*
echo "Removing files under /etc/bash_completion.d/ match the pattern: bianque"
rm -f /etc/bash_completion.d/bianque*
echo "Remove completed"

# move the file which under lib/bash_completion.d/ to the location: /etc/bash_completion.d/
echo "Moving files under lib/bash_completion.d/ to /etc/bash_completion.d/"
mv lib/bash_completion/* /etc/bash_completion.d/
echo "Move completed"

# execute source /etc/bash_completion.d/xx under lib/bash_completion.d/ witch matches the pattern: bianque*
for file in /etc/bash_completion.d/bianque*; do
    source $file
done

# remove lib
echo "Removing lib"
rm -rf lib
echo "Remove completed"

# suc
echo "Setup completed successfully"
```