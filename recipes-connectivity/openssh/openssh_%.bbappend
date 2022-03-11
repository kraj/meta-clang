do_configure:prepend() {
	sed -i -e '/-ftrapv/d' ${S}/configure.ac
}
