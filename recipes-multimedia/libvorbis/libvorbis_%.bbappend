# | clang-7: error: unknown argument: '-mfused-madd'
do_configure:prepend:toolchain-clang() {
	sed -i -e "s/-mfused-madd//g" ${S}/configure.ac
}
