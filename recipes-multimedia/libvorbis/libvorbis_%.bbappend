# On ppc32/spe targets (as of release 21) clang
# does not support this option
# | clang-21: error: unknown argument: '-mfused-madd'
do_configure:prepend:powerpc:toolchain-clang() {
	sed -i -e "s/-mfused-madd//g" ${S}/configure.ac
}
