cmd_drivers/pci/built-in.o :=  ld -m elf_x86_64  -r -o drivers/pci/built-in.o drivers/pci/access.o drivers/pci/bus.o drivers/pci/probe.o drivers/pci/remove.o drivers/pci/pci.o drivers/pci/quirks.o drivers/pci/pci-driver.o drivers/pci/search.o drivers/pci/pci-sysfs.o drivers/pci/rom.o drivers/pci/setup-res.o drivers/pci/proc.o drivers/pci/hotplug.o drivers/pci/htirq.o drivers/pci/setup-bus.o drivers/pci/pci-acpi.o