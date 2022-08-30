FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# systemd 251.4 started to cause boot issues see
# https://bugzilla.yoctoproject.org/show_bug.cgi?id=14906
# As a workaround disable O2 and use Os for now with clang
SELECTED_OPTIMIZATION:append:toolchain-clang = "-Os"
SELECTED_OPTIMIZATION:remove:toolchain-clang = "-O2"
