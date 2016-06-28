
In order to display the hello world message, use: 
kvm -curses -kernel arch/x86_64/boot/bzImage -append 'root=/dev/hda1 ro print_me' -drive file=../local.qcow2 --redir tcp:2222::22
dmesg | grep "Hello World from Me"



